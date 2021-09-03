package com.hufe.frame.dataobject.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendNoticeAtBO {

  String[] atMobiles;

  String[] atUserIds;

  Boolean isAtAll;

}
