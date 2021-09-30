package com.qc.skin.parser;

import java.util.Map;

import com.qc.skin.SkinResDeployerFactory;
import com.qc.skin.bean.SkinAttr;
import com.qc.skin.skinInterface.ISkinStyleParser;
import com.qc.skin.util.ReflectUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

/**
 * 解析Xml中的style属性，使支持style中定义的View的background支持换肤
 * Created by Windy on 2018/1/23.
 */
public class ViewBackgroundStyleParser implements ISkinStyleParser {

  private static int[] sViewStyleList;
  private static int sViewBackgroundStyleIndex;

  @Override
  public void parseXmlStyle(View view, AttributeSet attrs, Map<String, SkinAttr> viewAttrs) {
    Context context = view.getContext();
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
      int[] attr = new int[] {android.R.attr.background};
      TypedArray array = context.obtainStyledAttributes(attrs, attr, 0, 0);
      if (array != null && array.getIndexCount() > 0) {
        // Log.e("DON", "parseXmlStyle: " + array.getResourceId(0, -1));
        int colorResId = array.getResourceId(0, -1);
        SkinAttr skinAttr = SkinAttributeParser.parseSkinAttr(context,
            SkinResDeployerFactory.BACKGROUND, colorResId);
        if (skinAttr != null) {
          viewAttrs.put(skinAttr.attrName, skinAttr);
        }
      }
      array.recycle();
    } else {
      int[] viewStyleable = getTextViewStyleableList();
      int viewStyleableBackground = getTextViewTextColorStyleableIndex();

      TypedArray a = context.obtainStyledAttributes(attrs, viewStyleable, 0, 0);
      if (a != null) {
        int n = a.getIndexCount();
        for (int j = 0; j < n; j++) {
          int attr = a.getIndex(j);
          if (attr == viewStyleableBackground) {
            int drawableResId = a.getResourceId(attr, -1);
            SkinAttr skinAttr = SkinAttributeParser.parseSkinAttr(context,
                SkinResDeployerFactory.BACKGROUND, drawableResId);
            if (skinAttr != null) {
              viewAttrs.put(skinAttr.attrName, skinAttr);
            }
          }
        }
        a.recycle();
      }
    }
  }

  private static int[] getTextViewStyleableList() {
    if (sViewStyleList == null || sViewStyleList.length == 0) {
      sViewStyleList = (int[]) ReflectUtils.getField("com.android.internal.R$styleable", "View");
    }
    return sViewStyleList;
  }

  private static int getTextViewTextColorStyleableIndex() {
    if (sViewBackgroundStyleIndex == 0) {
      Object o = ReflectUtils.getField("com.android.internal.R$styleable", "View_background");
      if (o != null) {
        sViewBackgroundStyleIndex = (int) o;
      }
    }
    return sViewBackgroundStyleIndex;
  }
}
