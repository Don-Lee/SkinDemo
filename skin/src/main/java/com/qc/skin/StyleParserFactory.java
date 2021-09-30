package com.qc.skin;

import android.util.AttributeSet;
import android.view.View;

import com.qc.skin.bean.SkinAttr;
import com.qc.skin.parser.TextViewTextColorStyleParser;
import com.qc.skin.parser.ViewBackgroundStyleParser;
import com.qc.skin.skinInterface.ISkinStyleParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author : Don
 * date : 2020/12/1 13:14
 * description : 控件的style的属性管理
 * 目前只支持background以及textcolor属性，用户可自己扩展然后调用addStyleParser()添加进来
 */
public class StyleParserFactory {
  private static List<ISkinStyleParser> sStyleParserArray = new ArrayList<>();

  static {
    addStyleParser(new TextViewTextColorStyleParser());
    addStyleParser(new ViewBackgroundStyleParser());
  }

  public static void addStyleParser(ISkinStyleParser deployer) {
    if (!sStyleParserArray.contains(deployer)) {
      sStyleParserArray.add(deployer);
    }
  }

  public static void parseStyle(View view, AttributeSet attrs, Map<String, SkinAttr> viewAttrs) {
    for (ISkinStyleParser parser : sStyleParserArray) {
      parser.parseXmlStyle(view, attrs, viewAttrs);
    }
  }

}
