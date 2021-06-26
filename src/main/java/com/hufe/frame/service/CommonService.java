package com.hufe.frame.service;

import com.hufe.frame.dataobject.ao.common.LoginAO;
import com.hufe.frame.dataobject.bo.AccessLogBO;
import com.hufe.frame.dataobject.vo.common.LoginShowVO;

public interface CommonService {

    void createAccessLog(AccessLogBO params);

    LoginShowVO login(LoginAO params);

}
