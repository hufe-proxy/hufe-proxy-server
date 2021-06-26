package com.hufe.frame.service.impl;

import com.hufe.frame.constant.user.UserRoleTypeEnum;
import com.hufe.frame.dataobject.ao.common.LoginAO;
import com.hufe.frame.dataobject.bo.AccessLogBO;
import com.hufe.frame.dataobject.po.exception.FrameMessageException;
import com.hufe.frame.dataobject.vo.common.LoginShowVO;
import com.hufe.frame.model.AccessLogEntity;
import com.hufe.frame.model.UserEntity;
import com.hufe.frame.repository.AccessLogRepository;
import com.hufe.frame.repository.UserRepository;
import com.hufe.frame.service.CommonService;
import com.hufe.frame.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

  @Autowired
  JedisPool jedisPool;

  @Autowired
  AccessLogRepository accessLogRepository;

  @Autowired
  UserRepository userRepository;

  @Override
  public void createAccessLog(AccessLogBO params) {
    AccessLogEntity accessLog = AccessLogEntity
      .builder()
      .api(params.getApi())
      .ip(params.getIp())
      .userId(params.getUserId())
      .build();
    accessLogRepository.save(accessLog);
  }

  @Override
  public LoginShowVO login(LoginAO params) {
    UserEntity user = userRepository.findOne(params.getAccount(), params.getPassword(), UserRoleTypeEnum.SuperAdmin.getValue());
    if (user == null) {
      throw new FrameMessageException("账号或密码错误");
    }
    String id = Long.toString(user.getId());
    String token = CommonUtil.getMD5(id);
    try (Jedis jedis = jedisPool.getResource()) {
      long exTime = 60 * 60 * 10;
      SetParams setParams = new SetParams();
      setParams.ex(exTime);
      jedis.set(token, id, setParams);
    }
    return LoginShowVO.builder()
      .token(token)
      .roleType(user.getRoleType())
      .build();
  }

}
