package com.qc.skin.deployer;

import android.view.View;
import android.widget.TextView;

import com.qc.skin.SkinResDeployerFactory;
import com.qc.skin.bean.SkinAttr;
import com.qc.skin.bean.SkinConfig;
import com.qc.skin.skinInterface.ISkinResDeployer;
import com.qc.skin.skinInterface.ISkinResourceManager;


/**
 * author : Don
 * date : 2020/12/3 10:23
 * description :
 */
public class TextDrawableTopResDeployer implements ISkinResDeployer {
  @Override
  public void deploy(View view, SkinAttr skinAttr, ISkinResourceManager resource) {
    if (view instanceof TextView
        && SkinResDeployerFactory.TEXT_DRAWABLE_TOP.equals(skinAttr.attrName)) {
      TextView tv = (TextView) view;
      if (SkinConfig.RES_TYPE_DRAWABLE.equals(skinAttr.attrType)) {
        tv.setCompoundDrawablesWithIntrinsicBounds(null, resource.getDrawable(skinAttr.attrRefId),
            null, null);
      } else if (SkinConfig.RES_TYPE_MIPMAP.equals(skinAttr.attrType)) {
        tv.setCompoundDrawablesWithIntrinsicBounds(null,
            resource.getDrawableForMipmap(skinAttr.attrRefId), null, null);
      }
    }
  }
}
