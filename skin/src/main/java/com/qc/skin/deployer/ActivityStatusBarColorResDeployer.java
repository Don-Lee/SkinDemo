package com.qc.skin.deployer;

import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.qc.skin.bean.SkinAttr;
import com.qc.skin.bean.SkinConfig;
import com.qc.skin.skinInterface.ISkinResDeployer;
import com.qc.skin.skinInterface.ISkinResourceManager;
import com.qc.skin.util.ReflectUtils;

/**
 * Created by Windy on 2018/1/10.
 */

public class ActivityStatusBarColorResDeployer implements ISkinResDeployer {
  @Override
  public void deploy(View view, SkinAttr skinAttr, ISkinResourceManager resource) {
    // the view is the window's DecorView
    Window window;
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
      // API23以上，DecorView独立成一个类，并持有mWindow对象
      window = (Window) ReflectUtils.getField(view, "mWindow");
    } else {
      // API23以下，DecorView是PhoneWindow的内部类，隐式持有PhoneWindow对象
      window = ReflectUtils.getExternalField(view);
    }
    if (window == null) {
      throw new IllegalArgumentException("view is not a DecorView, cannot get the window");
    }
    if (SkinConfig.RES_TYPE_COLOR.equals(skinAttr.attrType)) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.setStatusBarColor(resource.getColor(skinAttr.attrRefId));
      } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        // 4.4及其以上
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      }
    }
  }
}
