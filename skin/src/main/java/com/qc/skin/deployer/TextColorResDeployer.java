package com.qc.skin.deployer;

import com.qc.skin.bean.SkinAttr;
import com.qc.skin.bean.SkinConfig;
import com.qc.skin.skinInterface.ISkinResDeployer;
import com.qc.skin.skinInterface.ISkinResourceManager;

import android.view.View;
import android.widget.TextView;


/**
 * author : Don
 * date : 2020/11/30 16:43
 * description :
 */
public class TextColorResDeployer implements ISkinResDeployer {
  @Override
  public void deploy(View view, SkinAttr skinAttr, ISkinResourceManager resource) {
    if (view instanceof TextView && SkinConfig.RES_TYPE_COLOR.equals(skinAttr.attrType)) {
      TextView tv = (TextView) view;
      tv.setTextColor(resource.getColor(skinAttr.attrRefId));
    } else if (view instanceof TextView && skinAttr.attrType.equals(SkinConfig.RES_TYPE_COLOR)) {
      TextView tv = (TextView) view;
      tv.setTextColor(resource.getColorStateList(skinAttr.attrRefId));
    }
  }
}
