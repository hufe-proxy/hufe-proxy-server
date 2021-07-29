package com.hufe.frame.util;

import com.hufe.frame.constant.project.ProjectSourceEnum;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommonUtil {

  public static String getMD5(String id) {
    long now = System.currentTimeMillis();
    double random = Math.random();
    String base = id + now + random;
    return DigestUtils.md5DigestAsHex(base.getBytes());
  }

  public static String getDateUUID() {
    LocalDateTime nowDateTime = LocalDateTime.now();
    return nowDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-ms"));
  }

  public static String getPublishLogProxyScript(String endpoint, String bucketName, String projectName, String uuid) {
    String uri = endpoint + "/" + bucketName + "/" + projectName + "/" + uuid;
    String script = "/" + projectName + "\\/?$/" + " " + uri + "/index.html\n";
    script += "/" + projectName + "/" + "filelist.js.*/" + " " + uri + "/filelist.js\n";
    script += "/" + projectName + "/(.+)/" + " " + uri + "/$1";
    return script;
  }

  public static String getMockLogProxyScript(String endpoint, String name, String address) {
    return address + " " + endpoint + "/" + name + " method://get";
  }

}
