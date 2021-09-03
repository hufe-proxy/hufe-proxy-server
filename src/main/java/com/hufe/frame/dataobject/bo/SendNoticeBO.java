package com.hufe.frame.dataobject.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendNoticeBO {

  String msgtype;

  SendNoticeTextBO text;

  SendNoticeAtBO at;

}
