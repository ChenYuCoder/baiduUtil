package com.vichen.image;

/**
 * @author chenyu
 * @Date: 2018/10/22 16:50
 */
public interface IImageService {
  /**
   * 图片转文字
   *
   * @param highPrecision
   * @param image
   * @param url
   * @return
   */
  public String imageToWord(boolean highPrecision, String image, String url);

}
