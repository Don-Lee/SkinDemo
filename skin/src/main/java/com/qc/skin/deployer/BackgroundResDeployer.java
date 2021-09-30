package com.qc.skin.deployer;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.qc.skin.bean.SkinAttr;
import com.qc.skin.bean.SkinConfig;
import com.qc.skin.skinInterface.ISkinResDeployer;
import com.qc.skin.skinInterface.ISkinResourceManager;


/**
 * author : Don
 * date : 2020/11/30 16:44
 * description : 背景资源换肤处理类
 */
public class BackgroundResDeployer implements ISkinResDeployer {
  @Override
  public void deploy(View view, SkinAttr skinAttr, ISkinResourceManager resource) {
    if (SkinConfig.RES_TYPE_COLOR.equals(skinAttr.attrType)) {
      view.setBackgroundColor(resource.getColor(skinAttr.attrRefId));
    } else if (SkinConfig.RES_TYPE_DRAWABLE.equals(skinAttr.attrType)) {
      Drawable bg = resource.getDrawable(skinAttr.attrRefId);
      view.setBackground(bg);
    } else if (SkinConfig.RES_TYPE_MIPMAP.equals(skinAttr.attrType)) {
      Drawable bg = resource.getDrawableForMipmap(skinAttr.attrRefId);
      view.setBackground(bg);
    }
  }
}
