package com.vichen.config;

import com.alibaba.fastjson.JSONObject;
import com.vichen.http.HttpApiService;
import com.vichen.http.HttpResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenyu
 * @Date: 2018/10/22 17:20
 */
public class BaiduApiConfig {
  private static final int appId = 14508497;
  private static final String apiKey = "zhporhLW7nTlcyagBly9X0fj";
  private static final String secretKey = "uXsPRRHk87mnnAeVzIyggvgDMdViZWqA";
  private static final String oauth = "https://aip.baidubce.com/oauth/2.0/token";
  public static final String general_basic =
    "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";
  public static final String accurate_basic =
    "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic";

  private static String token = null;
  private static LocalDateTime expires_in = null;
  private static Logger logger = LoggerFactory.getLogger(BaiduApiConfig.class);

  public static String getToken() {
    if (null != token && LocalDateTime.now().isBefore(expires_in)) {
      return token;
    }

    Map<String, Object> params = new HashMap<>();
    params.put("grant_type", "client_credentials");
    params.put("client_id", apiKey);
    params.put("client_secret", secretKey);
    HttpResult result;
    try {
      result = HttpApiService.INSTANCE.doPost(oauth, params);
      if (HttpApiService.ok == result.getCode()) {
        token = JSONObject.parseObject(result.getBody()).getString("access_token");
        expires_in = LocalDateTime.now()
          .plusSeconds(JSONObject.parseObject(result.getBody()).getInteger("expires_in"));

        logger.info("token获取成功,数据为:{}", result.getBody());
      } else {
        logger.error("token获取失败,返回结果：{}", result.getBody());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return token;

  }
}
