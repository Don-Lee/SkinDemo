package com.qc.skin.parser;

import java.util.HashMap;

import com.qc.skin.SkinResDeployerFactory;
import com.qc.skin.StyleParserFactory;
import com.qc.skin.bean.SkinAttr;
import com.qc.skin.bean.SkinConfig;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * author : Don
 * date : 2020/11/30 15:21
 * description : 解析xml中的换肤属性
 */
public class SkinAttributeParser {

  private static final String TAG = "SkinAttributeParser";

  public static HashMap<String, SkinAttr> parseSkinAttr(AttributeSet attrs, View view) {
    if (view == null) {
      return null;
    }
    // 使用hashmap避免属性重复添加
    HashMap<String, SkinAttr> viewAttrs = new HashMap<>();
    Context context = view.getContext();

    // 先处理style类型, 避免布局中定义的属性被style中定义的属性覆盖
    for (int i = 0; i < attrs.getAttributeCount(); i++) {
      String attrName = attrs.getAttributeName(i);
      String attrValue = attrs.getAttributeValue(i);
      // 处理控件中设置的style属性
      if (SkinConfig.RES_TYPE_STYLE.equals(attrName)) {
        StyleParserFactory.parseStyle(view, attrs, viewAttrs);
      }
    }

    for (int i = 0; i < attrs.getAttributeCount(); i++) {
      String attrName = attrs.getAttributeName(i);
      String attrValue = attrs.getAttributeValue(i);


      if (!SkinResDeployerFactory.isSupportedAttr(attrName)) {
        continue;
      }

      // attrName=textColor attrValue=@2131492918此值就是R文件中的16进制值 view=TextView
      if (!attrValue.startsWith("@")) {
        continue;
      }

      SkinAttr skinAttr = null;
      try {
        skinAttr = getSkinAttrFromId(context, attrName, attrValue);
      } catch (NumberFormatException ex) {
        Log.e(TAG, "parseSkinAttr() error happened", ex);
        skinAttr = getSkinAttrBySplit(context, attrName, attrValue);
      } catch (Resources.NotFoundException ex) {
        Log.e(TAG, "parseSkinAttr() error happened", ex);
      }

      if (skinAttr != null) {
        viewAttrs.put(skinAttr.attrName, skinAttr);
      }
    }
    return viewAttrs;
  }

  public static SkinAttr parseSkinAttr(Context context, String attrName, int resId) {
    if (context == null) {
      return null;
    }
    SkinAttr skinAttr = null;
    try {
      String attrValueName = context.getResources().getResourceEntryName(resId);
      String attrValueType = context.getResources().getResourceTypeName(resId);
      skinAttr = new SkinAttr(attrName, resId, attrValueName, attrValueType);
    } catch (Exception e) {
      Log.e(TAG, " parseSkinAttr--- error happened ", e);
    }

    return skinAttr;
  }

  // 目前就IconTabTextView类使用了这个方法
  public static SkinAttr parseSkinAttr(Context context, String attrName, int resId, int resId2) {

    if (context == null) {
      return null;
    }
    SkinAttr skinAttr = null;
    try {
      String attrValueName = context.getResources().getResourceEntryName(resId);
      String attrValueType = context.getResources().getResourceTypeName(resId);
      skinAttr = new SkinAttr(attrName, resId, attrValueName, attrValueType, resId2);
    } catch (Exception e) {
      Log.e(TAG, " parseSkinAttr--- error happened ", e);
    }

    return skinAttr;
  }

  private static SkinAttr getSkinAttrFromId(Context context, String attrName, String attrValue) {
    String aValue = attrValue.substring(1);// 截取@符号后面的内容
    int id = Integer.parseInt(aValue);
    if (id == 0) {
      return null;
    }
    return parseSkinAttr(context, attrName, id);
  }

  private static SkinAttr getSkinAttrBySplit(Context context, String attrName, String attrValue) {
    try {
      int dividerIndex = attrValue.indexOf("/");
      String entryName = attrValue.substring(dividerIndex + 1);
      String typeName = attrValue.substring(1, dividerIndex);
      int id = context.getResources().getIdentifier(entryName, typeName, context.getPackageName());
      return new SkinAttr(attrName, id, entryName, typeName);
    } catch (Resources.NotFoundException e) {
      Log.e(TAG, "getSkinAttrBySplit error happened", e);
    }
    return null;
  }
}
