package com.qc.skin.skinInterface;

import android.view.View;

import com.qc.skin.bean.SkinAttr;

/**
 * author : Don
 * date : 2020/11/30 16:34
 * description : 使用工厂方法模式生产不同的换肤控件，方便扩展
 */
public interface ISkinResDeployer {
  /**
   * 将属性skinAttr通过resource设置到当前view上
   *
   * @param view 当前view
   * @param skinAttr 属性
   * @param resource 设置的资源工具
   */
  void deploy(View view, SkinAttr skinAttr, ISkinResourceManager resource);
}
