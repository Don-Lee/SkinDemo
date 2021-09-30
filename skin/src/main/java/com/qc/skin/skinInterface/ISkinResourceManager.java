package com.qc.skin.skinInterface;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

/**
 * author : Don
 * date : 2020/11/30 16:37
 * description :替换资源管理接口
 */
public interface ISkinResourceManager {
  String getPkgName();

  Resources getPluginResource();

  void setPluginResourcesAndPkgName(Resources resources, String pkgName);

  int getColor(@ColorRes int resId) throws Resources.NotFoundException;

  ColorStateList getColorStateList(@ColorRes int resId) throws Resources.NotFoundException;

  Drawable getDrawable(@DrawableRes int resId) throws Resources.NotFoundException;

  Drawable getDrawableForMipmap(int attrValueRefId) throws Resources.NotFoundException;
}
