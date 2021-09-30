package com.qc.skin.bean;

/**
 * author : Don
 * date : 2020/11/30 15:06
 * description :
 */
public class SkinConfig {
  /***
   * 支持的命名空间
   */
  // public static final String SKIN_XML_NAMESPACE = "http://schemas.android.com/android/skin";
  public static final String SKIN_XML_NAMESPACE = "http://schemas.android.com/android/skin";

  /**
   * 界面元素支持换肤的属性
   */
  public static final String ATTR_SKIN_ENABLE = "skinEnable";
  public static final String SUPPORTED_ATTR_SKIN_LIST = "attrs";


  /**
   * 控件设置了style
   */
  public static final String RES_TYPE_STYLE = "style";

  /**
   * 属性值对应的类型是color
   */
  public static final String RES_TYPE_COLOR = "color";
  /**
   * 属性值对应的类型是drawable
   */
  public static final String RES_TYPE_DRAWABLE = "drawable";
  /**
   * 属性值对应的类型是mipmap
   */
  public static final String RES_TYPE_MIPMAP = "mipmap";
}
