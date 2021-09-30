package com.qc.skin.parser;

import java.util.Map;

import com.qc.skin.SkinResDeployerFactory;
import com.qc.skin.bean.SkinAttr;
import com.qc.skin.skinInterface.ISkinStyleParser;
import com.qc.skin.util.ReflectUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * author : Don
 * date : 2020/12/1 13:26
 * description : 解析Xml中的style属性，使支持style中定义的TextView的textColor支持换肤
 */
public class TextViewTextColorStyleParser implements ISkinStyleParser {

  private static int[] sTextViewStyleList;
  private static int sTextViewTextColorStyleIndex;

  @Override
  public void parseXmlStyle(View view, AttributeSet attrs, Map<String, SkinAttr> viewAttrs) {
    if (!TextView.class.isAssignableFrom(view.getClass())) {
      return;
    }
    Context context = view.getContext();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      int[] attr = new int[] {android.R.attr.textColor};
      TypedArray array = context.obtainStyledAttributes(attrs, attr, 0, 0);
      if (array != null && array.getIndexCount() > 0) {
        // Log.e("DON", "parseXmlStyle: " + array.getResourceId(0, -1));
        int colorResId = array.getResourceId(0, -1);
        SkinAttr skinAttr = SkinAttributeParser.parseSkinAttr(context,
            SkinResDeployerFactory.TEXT_COLOR, colorResId);
        if (skinAttr != null) {
          viewAttrs.put(skinAttr.attrName, skinAttr);
        }
      }
      array.recycle();
      // Log.e("DON", "parseXmlStyle: " + array.getIndexCount());

    } else {
      int[] textViewStyleable = getTextViewStyleableList();
      int textViewStyleableTextColor = getTextViewTextColorStyleableIndex();

      TypedArray a = context.obtainStyledAttributes(attrs, textViewStyleable, 0, 0);
      if (a != null) {
        int n = a.getIndexCount();
        for (int j = 0; j < n; j++) {
          int attr = a.getIndex(j);
          if (attr == textViewStyleableTextColor) {
            int colorResId = a.getResourceId(attr, -1);
            SkinAttr skinAttr = SkinAttributeParser.parseSkinAttr(context,
                SkinResDeployerFactory.TEXT_COLOR, colorResId);
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
    if (sTextViewStyleList == null || sTextViewStyleList.length == 0) {
      sTextViewStyleList =
          (int[]) ReflectUtils.getField("com.android.internal.R$styleable", "TextView");
    }
    return sTextViewStyleList;
  }

  private static int getTextViewTextColorStyleableIndex() {
    if (sTextViewTextColorStyleIndex == 0) {
      Object o = ReflectUtils.getField("com.android.internal.R$styleable", "TextView_textColor");
      if (o != null) {
        sTextViewTextColorStyleIndex = (int) o;
      }
    }
    return sTextViewTextColorStyleIndex;
  }
}
