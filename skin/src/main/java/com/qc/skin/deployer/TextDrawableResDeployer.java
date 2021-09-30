package com.qc.skin.deployer;

import android.view.View;
import android.widget.TextView;

import com.qc.skin.bean.SkinAttr;
import com.qc.skin.bean.SkinConfig;
import com.qc.skin.skinInterface.ISkinResDeployer;
import com.qc.skin.skinInterface.ISkinResourceManager;


/**
 * author : Don
 * date : 2020/12/3 10:23
 * description :
 */
public class TextDrawableResDeployer implements ISkinResDeployer {
  @Override
  public void deploy(View view, SkinAttr skinAttr, ISkinResourceManager resource) {
    if (view instanceof TextView) {
      TextView tv = (TextView) view;
      if (skinAttr.note > 0) {// 目前就IconTabTextView类使用了这个字段
        if (SkinConfig.RES_TYPE_MIPMAP.equals(skinAttr.attrType)) {
          tv.setCompoundDrawablesWithIntrinsicBounds(
              resource.getDrawableForMipmap(skinAttr.attrRefId),
              null, resource.getDrawableForMipmap(skinAttr.note), null);
        } else if (SkinConfig.RES_TYPE_DRAWABLE.equals(skinAttr.attrType)) {
          tv.setCompoundDrawablesWithIntrinsicBounds(resource.getDrawable(skinAttr.attrRefId), null,
              null, null);
        }
      } else {
        if (SkinConfig.RES_TYPE_DRAWABLE.equals(skinAttr.attrType)) {
          tv.setCompoundDrawablesWithIntrinsicBounds(resource.getDrawable(skinAttr.attrRefId), null,
              null, null);
        } else if (SkinConfig.RES_TYPE_MIPMAP.equals(skinAttr.attrType)) {
          tv.setCompoundDrawablesWithIntrinsicBounds(
              resource.getDrawableForMipmap(skinAttr.attrRefId),
              null, null, null);
        }
      }


    }
  }
}
