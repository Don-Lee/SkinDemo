package com.qc.skin.deployer;

import com.qc.skin.bean.SkinAttr;
import com.qc.skin.bean.SkinConfig;
import com.qc.skin.skinInterface.ISkinResDeployer;
import com.qc.skin.skinInterface.ISkinResourceManager;

import android.view.View;
import android.widget.CheckBox;


/**
 * author : Don
 * date : 2020/11/30 16:43
 * description :
 */
public class CheckBoxDrawableDeployer implements ISkinResDeployer {
  @Override
  public void deploy(View view, SkinAttr skinAttr, ISkinResourceManager resource) {
    if (!(view instanceof CheckBox)) {
      return;
    }
    if (SkinConfig.RES_TYPE_DRAWABLE.equals(skinAttr.attrType)) {
      CheckBox cb = (CheckBox) view;
      cb.setButtonDrawable(resource.getDrawable(skinAttr.attrRefId));
    }
  }
}
