package com.vichen.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

/**
 * HTTP配置文件
 *
 * @author chenyu
 */
public class HttpClientConfig {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  public static HttpClientConfig INSTANCE = new HttpClientConfig();

  private HttpClientConfig() {
  }

  /**
   * 最大连接数
   */
  private Integer maxTotal = 100;
  /**
   * 并发数
   */
  private Integer defaultMaxPerRoute = 20;
  /**
   * 创建连接的最长时间
   */
  private Integer connectTimeout = 5000;
  /**
   * 从连接池中获取到连接的最长时间
   */
  private Integer connectionRequestTimeout = 200;
  /**
   * 数据传输的最长时间
   */
  private Integer socketTimeout = 10000;


  /**
   * 首先实例化一个连接池管理器，设置最大连接数、并发连接数
   *
   * @return PoolingHttpClientConnectionManager
   */
  public PoolingHttpClientConnectionManager getHttpClientConnectionManager() {
    PoolingHttpClientConnectionManager httpClientConnectionManager =
      new PoolingHttpClientConnectionManager();
    //最大连接数
    httpClientConnectionManager.setMaxTotal(maxTotal);
    //并发数
    httpClientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);

    return httpClientConnectionManager;
  }

  /**
   * 实例化连接池，设置连接池管理器。
   * 这里需要以参数形式注入上面实例化的连接池管理器
   *
   * @return HttpClientBuilder
   */
  public HttpClientBuilder getHttpClientBuilder() {
    PoolingHttpClientConnectionManager httpClientConnectionManager =
      getHttpClientConnectionManager();

    //HttpClientBuilder中的构造方法被protected修饰，所以这里不能直接使用new来实例化一个HttpClientBuilder，可以使用HttpClientBuilder提供的静态方法create()来获取HttpClientBuilder对象
    HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

    httpClientBuilder.setConnectionManager(httpClientConnectionManager);

    return httpClientBuilder;
  }

  /**
   * 注入连接池，用于获取httpClient
   *
   * @return CloseableHttpClient
   */
  public CloseableHttpClient getCloseableHttpClient() {
    HttpClientBuilder httpClientBuilder = getHttpClientBuilder();
    return httpClientBuilder.build();
  }

  /**
   * Builder是RequestConfig的一个内部类
   * 通过RequestConfig的custom方法来获取到一个Builder对象
   * 设置builder的连接信息
   *
   * @return RequestConfig
   */
  public RequestConfig.Builder getBuilder() {
    RequestConfig.Builder builder = RequestConfig.custom();
    return builder.setConnectTimeout(connectTimeout)
      .setConnectionRequestTimeout(connectionRequestTimeout).setSocketTimeout(socketTimeout);
  }

  /**
   * 使用builder构建一个RequestConfig对象
   *
   * @return RequestConfig
   */
  @Bean public RequestConfig getRequestConfig() {
    RequestConfig.Builder builder = getBuilder();
    return builder.build();
  }

}
