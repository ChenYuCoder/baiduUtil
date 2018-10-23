package com.vichen.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chenyu
 */
public class HttpApiService {

  public static final int ok = 200;

  public static HttpApiService INSTANCE = new HttpApiService();

  private HttpApiService() {
  }

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * 不带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
   *
   * @param url
   * @return
   * @throws Exception
   */
  public JSONObject doGet(String url) throws Exception {
    // 声明 http get 请求
    HttpGet httpGet = new HttpGet(url);

    // 装载配置信息
    httpGet.setConfig(HttpClientConfig.INSTANCE.getRequestConfig());

    // 发起请求
    CloseableHttpResponse response =
      HttpClientConfig.INSTANCE.getCloseableHttpClient().execute(httpGet);

    JSONObject result = null;
    // 判断状态码是否为200
    int httpCode = 200;
    if (response.getStatusLine().getStatusCode() == httpCode) {
      result = JSONObject.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
    } else {
      logger.error("http request failed :code:" + response.getStatusLine().getStatusCode());
    }

    response.close();

    return result;
  }

  /**
   * 带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
   *
   * @param url
   * @return
   * @throws Exception
   */
  public JSONObject doGet(String url, Map<String, Object> map) throws Exception {
    URIBuilder uriBuilder = new URIBuilder(url);

    if (map != null) {
      // 遍历map,拼接请求参数
      for (Map.Entry<String, Object> entry : map.entrySet()) {
        uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
      }
    }

    // 调用不带参数的get请求
    return this.doGet(uriBuilder.build().toString());

  }

  /**
   * 带参数的post请求
   *
   * @param url
   * @param map
   * @return
   * @throws Exception
   */
  public HttpResult doPost(String url, Map<String, Object> map) throws Exception {
    // 声明httpPost请求
    HttpPost httpPost = new HttpPost(url);
    // 加入配置信息
    httpPost.setConfig(HttpClientConfig.INSTANCE.getRequestConfig());

    // 判断map是否为空，不为空则进行遍历，封装from表单对象
    if (map != null) {
      List<NameValuePair> list = new ArrayList<NameValuePair>();
      for (Map.Entry<String, Object> entry : map.entrySet()) {
        list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
      }
      // 构造from表单对象
      UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "UTF-8");

      // 把表单放到post里
      httpPost.setEntity(urlEncodedFormEntity);
    }

    // 发起请求
    CloseableHttpResponse response =
      HttpClientConfig.INSTANCE.getCloseableHttpClient().execute(httpPost);

    HttpResult result = new HttpResult(response.getStatusLine().getStatusCode(),
      EntityUtils.toString(response.getEntity(), "UTF-8"));

    response.close();

    return result;
  }

  /**
   * 不带参数post请求
   *
   * @param url
   * @return
   * @throws Exception
   */
  public HttpResult doPost(String url) throws Exception {
    return this.doPost(url, null);
  }

}
