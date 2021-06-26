package com.hufe.frame.dataobject.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccessLogBO {

    private String ip;

    private String api;

    private String userId;

}
