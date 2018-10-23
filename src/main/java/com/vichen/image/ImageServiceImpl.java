package com.vichen.image;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vichen.config.BaiduApiConfig;
import com.vichen.http.HttpApiService;
import com.vichen.http.HttpResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenyu
 * @Date: 2018/10/22 16:25
 */
@Service("imageService") public class ImageServiceImpl implements IImageService {

  private Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);


  @Override public String imageToWord(boolean highPrecision, String image, String url) {

    Map<String, Object> params = new HashMap<>();

    if (!StringUtils.isEmpty(image)) {
      params.put("image", image);
    } else if (!StringUtils.isEmpty(url)) {
      params.put("url", url);
    }

    if (CollectionUtils.isEmpty(params)) {
      return "image/url必须给一个";
    }

    params.put("access_token", BaiduApiConfig.getToken());

    StringBuffer resultBuffer = new StringBuffer();
    String baiduUrl = highPrecision ? BaiduApiConfig.accurate_basic : BaiduApiConfig.general_basic;

    try {

      HttpResult httpResult = HttpApiService.INSTANCE.doPost(baiduUrl, params);

      if (HttpApiService.ok == httpResult.getCode()) {
        JSONObject body = JSONObject.parseObject(httpResult.getBody());
        JSONArray wordsResult = body.getJSONArray("words_result");
        wordsResult.forEach(word -> {
          JSONObject jsonObject = (JSONObject) word;
          resultBuffer.append(jsonObject.getString("words"));
          resultBuffer.append("\n");
        });

      } else {
        logger.error("token获取失败,返回结果：{}", httpResult.getBody());
      }

    } catch (Exception e) {
      e.printStackTrace();
    }


    return resultBuffer.toString();
  }

}
