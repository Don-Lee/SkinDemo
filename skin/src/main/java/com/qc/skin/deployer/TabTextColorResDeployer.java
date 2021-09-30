package com.qc.skin.deployer;

import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.qc.skin.bean.SkinAttr;
import com.qc.skin.bean.SkinConfig;
import com.qc.skin.skinInterface.ISkinResDeployer;
import com.qc.skin.skinInterface.ISkinResourceManager;

/**
 * author : Don
 * date : 2020/12/2 15:28
 * description :
 */
public class TabTextColorResDeployer implements ISkinResDeployer {
  @Override
  public void deploy(View view, SkinAttr skinAttr, ISkinResourceManager resource) {
    if (view instanceof TabLayout && skinAttr.attrType.equals(SkinConfig.RES_TYPE_COLOR)) {
      TabLayout tb = (TabLayout) view;
      tb.setTabTextColors(resource.getColorStateList(skinAttr.attrRefId));
    } else if (view instanceof TextView && skinAttr.attrType.equals(SkinConfig.RES_TYPE_COLOR)) {
      TextView tv = (TextView) view;
      tv.setTextColor(resource.getColorStateList(skinAttr.attrRefId));
    }
  }
}
