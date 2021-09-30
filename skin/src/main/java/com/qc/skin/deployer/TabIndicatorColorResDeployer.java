package com.qc.skin.deployer;

import android.view.View;

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
public class TabIndicatorColorResDeployer implements ISkinResDeployer {
  @Override
  public void deploy(View view, SkinAttr skinAttr, ISkinResourceManager resource) {
    if (view instanceof TabLayout && skinAttr.attrType.equals(SkinConfig.RES_TYPE_COLOR)) {
      TabLayout tb = (TabLayout) view;
      tb.setSelectedTabIndicatorColor(resource.getColor(skinAttr.attrRefId));
    }
  }
}
