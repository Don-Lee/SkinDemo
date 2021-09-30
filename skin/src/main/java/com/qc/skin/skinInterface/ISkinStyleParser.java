package com.qc.skin.skinInterface;

import android.util.AttributeSet;
import android.view.View;

import com.qc.skin.bean.SkinAttr;

import java.util.Map;

/**
 * author : Don
 * date : 2020/12/1 13:16
 * description : 和ISkinResDeployer一样，便于扩展
 */
public interface ISkinStyleParser {
  /**
   * 解析xml中设置的style属性
   * 
   * @param view 当前需要解析的view
   * @param attrs xml中参数集合
   * @param viewAttrs 解析出来的属性需要保存的集合
   */
  void parseXmlStyle(View view, AttributeSet attrs, Map<String, SkinAttr> viewAttrs);
}

