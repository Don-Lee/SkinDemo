package com.qc.skin;

import java.util.HashMap;
import java.util.Map;

import com.qc.skin.bean.SkinAttr;
import com.qc.skin.deployer.ActivityStatusBarColorResDeployer;
import com.qc.skin.deployer.BackgroundResDeployer;
import com.qc.skin.deployer.CheckBoxDrawableDeployer;
import com.qc.skin.deployer.ImageDrawableResDeployer;
import com.qc.skin.deployer.TabIndicatorColorResDeployer;
import com.qc.skin.deployer.TabTextColorResDeployer;
import com.qc.skin.deployer.TextColorResDeployer;
import com.qc.skin.deployer.TextDrawableResDeployer;
import com.qc.skin.deployer.TextDrawableRightResDeployer;
import com.qc.skin.deployer.TextDrawableTopResDeployer;
import com.qc.skin.skinInterface.ISkinResDeployer;

import android.text.TextUtils;

/**
 * author : Don
 * date : 2020/11/30 16:12
 * description : 需要换肤的属性类
 */
public class SkinResDeployerFactory {
  public static final String BACKGROUND = "background";
  public static final String IMAGE_SRC = "src";
  public static final String TEXT_COLOR = "textColor";
  public static final String TEXT_DRAWABLE = "drawableLeft";
  public static final String TEXT_DRAWABLE_RIGHT = "drawableRight";
  public static final String TEXT_DRAWABLE_TOP = "drawableTop";
  public static final String TAB_TEXT_COLOR = "tabTextColor";
  public static final String TAB_INDICATOR_COLOR = "tabIndicatorColor";
  public static final String TEXT_COLOR_HINT = "textColorHint";
  public static final String LIST_SELECTOR = "listSelector";
  public static final String DIVIDER = "divider";
  public static final String CHECK_BOX_DRAWABLE = "checkBoxDrawable";

  public static final String ACTIVITY_STATUS_BAR_COLOR = "statusBarColor";

  // 存放支持的换肤属性和对应的处理器
  private static Map<String, ISkinResDeployer> sSupportedSkinDeployerMap = new HashMap<>();
  // 静态注册支持换肤的属性和处理器
  static {
    registerDeployer(BACKGROUND, new BackgroundResDeployer());
    registerDeployer(IMAGE_SRC, new ImageDrawableResDeployer());
    registerDeployer(TEXT_COLOR, new TextColorResDeployer());
    registerDeployer(TAB_TEXT_COLOR, new TabTextColorResDeployer());
    registerDeployer(TAB_INDICATOR_COLOR, new TabIndicatorColorResDeployer());
    registerDeployer(TEXT_DRAWABLE, new TextDrawableResDeployer());
    registerDeployer(TEXT_DRAWABLE_RIGHT, new TextDrawableRightResDeployer());
    registerDeployer(TEXT_DRAWABLE_TOP, new TextDrawableTopResDeployer());
    // registerDeployer(TEXT_COLOR_HINT, new TextColorHintResDeployer());
    // registerDeployer(LIST_SELECTOR, new ListViewSelectorResDeployer());
    // registerDeployer(DIVIDER, new ListViewDividerResDeployer());
    registerDeployer(ACTIVITY_STATUS_BAR_COLOR, new ActivityStatusBarColorResDeployer());
    registerDeployer(CHECK_BOX_DRAWABLE, new CheckBoxDrawableDeployer());
    // registerDeployer(PROGRESSBAR_INDETERMINATE_DRAWABLE, new
    // ProgressBarIndeterminateDrawableDeployer());
  }

  public static void registerDeployer(String attrName, ISkinResDeployer skinResDeployer) {
    if (TextUtils.isEmpty(attrName) || null == skinResDeployer) {
      return;
    }

    if (!sSupportedSkinDeployerMap.containsKey(attrName)) {
      sSupportedSkinDeployerMap.put(attrName, skinResDeployer);
    }
  }

  public static ISkinResDeployer of(String attrName) {
    if (TextUtils.isEmpty(attrName)) {
      return null;
    }
    return sSupportedSkinDeployerMap.get(attrName);
  }

  public static ISkinResDeployer of(SkinAttr attr) {
    if (attr == null) {
      return null;
    }
    return of(attr.attrName);
  }

  // 判断是否支持该属性换肤 true:支持 false:不支持
  public static boolean isSupportedAttr(String attrName) {
    return of(attrName) != null;
  }

  // 判断是否支持该属性换肤 true:支持 false:不支持
  public static boolean isSupportedAttr(SkinAttr attr) {
    return of(attr) != null;
  }
}
