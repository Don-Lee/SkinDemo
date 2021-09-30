package com.qc.skin.deployer;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.qc.skin.bean.SkinAttr;
import com.qc.skin.bean.SkinConfig;
import com.qc.skin.skinInterface.ISkinResDeployer;
import com.qc.skin.skinInterface.ISkinResourceManager;


/**
 * author : Don
 * date : 2020/12/1 13:02
 * description :
 */
public class ImageDrawableResDeployer implements ISkinResDeployer {
  @Override
  public void deploy(View view, SkinAttr skinAttr, ISkinResourceManager resource) {
    if (!(view instanceof ImageView)) {
      return;
    }
    Drawable drawable = null;
    if (SkinConfig.RES_TYPE_COLOR.equals(skinAttr.attrType)) {
      drawable = new ColorDrawable(resource.getColor(skinAttr.attrRefId));
    } else if (SkinConfig.RES_TYPE_DRAWABLE.equals(skinAttr.attrType)) {
      drawable = resource.getDrawable(skinAttr.attrRefId);
    } else if (SkinConfig.RES_TYPE_MIPMAP.equals(skinAttr.attrType)) {
      drawable = resource.getDrawableForMipmap(skinAttr.attrRefId);
    }

    if (drawable != null) {
      ((ImageView) view).setImageDrawable(drawable);
    }
  }
}
