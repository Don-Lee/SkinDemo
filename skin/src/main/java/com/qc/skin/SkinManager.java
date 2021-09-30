package com.qc.skin;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

import com.qc.skin.bean.SkinAttr;
import com.qc.skin.impl.SkinResourceManagerImpl;
import com.qc.skin.parser.SkinAttributeParser;
import com.qc.skin.skinInterface.ISkinResDeployer;
import com.qc.skin.skinInterface.ISkinResourceManager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.content.res.loader.ResourcesLoader;
import android.content.res.loader.ResourcesProvider;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * author : Don
 * date : 2020/11/30 18:12
 * description :
 */
public class SkinManager {
  private static final String TAG = "SkinManager";
  // 使用这个map保存所有需要换肤的view和其对应的换肤属性及资源
  // 使用WeakHashMap两个作用，1.避免内存泄漏，2.避免重复的view被添加
  // 使用HashMap存SkinAttr，为了避免同一个属性值存了两次
  private WeakHashMap<View, HashMap<String, SkinAttr>> mSkinAttrMap = new WeakHashMap<>();
  private Context mContext;
  private ISkinResourceManager mSkinResourceManager;
  private String mPluginSkinPath;

  private SkinManager() {

  }

  public static SkinManager getInstance() {
    return SkinManagerHolder.INSTANCE;
  }

  // todo provider自动初始化
  public void init(Context context) {
    mContext = context.getApplicationContext();
    mSkinResourceManager = new SkinResourceManagerImpl(mContext, null, null);
  }

  public void restoreToDefaultSkin() {
    mSkinResourceManager.setPluginResourcesAndPkgName(null, null);
    notifySkinChanged();
  }

  /**
   * 加载皮肤资源
   */
  public boolean loadSkin(String skinApkPath) {
    if (TextUtils.isEmpty(skinApkPath) || !(new File(skinApkPath)).exists()) {
      Log.e(TAG, " Try to load skin apk, but file is not exist, file path -->  " + skinApkPath +
          " So, restore to default skin.");
      restoreToDefaultSkin();
      return false;
    } else {
      return loadNewSkin(skinApkPath);
    }
  }

  /**
   * 设置可以换肤的view的属性
   *
   * @param view 设置的view
   * @param attrName 这个取值只能是 {@link SkinResDeployerFactory#BACKGROUND}
   *          {@link SkinResDeployerFactory#DIVIDER} {@link SkinResDeployerFactory#TEXT_COLOR}
   *          {@link SkinResDeployerFactory#LIST_SELECTOR} {@link SkinResDeployerFactory#IMAGE_SRC}
   *          等等
   * @param resId 资源id
   *
   */
  public void setSkinViewResource(View view, String attrName, int resId) {
    if (TextUtils.isEmpty(attrName)) {
      return;
    }

    SkinAttr attr = SkinAttributeParser.parseSkinAttr(view.getContext(), attrName, resId);
    if (attr != null) {
      doSkinAttrsDeploying(view, attr);
      saveSkinView(view, attr);
    }
  }

  public void setSkinViewResource(View view, String attrName, int resId, int resId2) {
    if (TextUtils.isEmpty(attrName)) {
      return;
    }

    SkinAttr attr = SkinAttributeParser.parseSkinAttr(view.getContext(), attrName, resId, resId2);
    if (attr != null) {
      doSkinAttrsDeploying(view, attr);
      saveSkinView(view, attr);
    }
  }

  protected void deployViewSkinAttrs(View view, HashMap<String, SkinAttr> viewAttrs) {
    if (view == null || viewAttrs == null || viewAttrs.size() == 0) {
      return;
    }
    Iterator iter = viewAttrs.entrySet().iterator();
    while (iter.hasNext()) {
      Map.Entry entry = (Map.Entry) iter.next();
      SkinAttr attr = (SkinAttr) entry.getValue();
      doSkinAttrsDeploying(view, attr);
    }
  }

  // 将View保存到被监听的view列表中,使得在换肤时能够及时被更新
  protected void saveSkinView(View view, HashMap<String, SkinAttr> viewAttrs) {
    if (view == null || viewAttrs == null || viewAttrs.size() == 0) {
      return;
    }
    HashMap<String, SkinAttr> originalSkinAttr = mSkinAttrMap.get(view);
    if (originalSkinAttr != null && originalSkinAttr.size() > 0) {
      originalSkinAttr.putAll(viewAttrs);
      mSkinAttrMap.put(view, originalSkinAttr);
    } else {
      mSkinAttrMap.put(view, viewAttrs);
    }
  }

  // 将新皮肤的属性部署到view上
  private void doSkinAttrsDeploying(@Nullable View view, @Nullable SkinAttr skinAttr) {
    ISkinResDeployer deployer = SkinResDeployerFactory.of(skinAttr);
    if (deployer != null) {
      deployer.deploy(view, skinAttr, mSkinResourceManager);
    }
  }

  /**
   * 加载新皮肤
   *
   * @param skinApkPath 新皮肤路径
   * @return true 加载新皮肤成功 false 加载失败
   */
  private boolean loadNewSkin(String skinApkPath) {
    // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    // return resourcesLoaderNewSkin(skinApkPath);
    // } else {
    return doNewSkinLoad(skinApkPath);
    // }
  }

  @RequiresApi(api = Build.VERSION_CODES.R)
  private boolean resourcesLoaderNewSkin(String skinApkPath) {
    File file = new File(skinApkPath);
    // 带资源的try语句（try-with-resource）,try块退出时，会自动调用res.close()方法，关闭资源.不用写一大堆finally来关闭资源。所有实现Closeable的类声明都可以写在里面
    try (ParcelFileDescriptor pfd =
        ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE)) {
      ResourcesProvider provider = ResourcesProvider.loadFromApk(pfd);
      ResourcesLoader loader = new ResourcesLoader();
      loader.addProvider(provider);
      Resources res = Resources.getSystem();
      res.addLoaders(loader);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  private boolean doNewSkinLoad(String skinApkPath) {
    if (TextUtils.isEmpty(skinApkPath)) {
      return false;
    }

    File file = new File(skinApkPath);
    if (!file.exists()) {
      return false;
    }

    PackageInfo packageInfo = PluginLoadUtils.getInstance(mContext).getPackageInfo(skinApkPath);
    Resources pluginResources =
        PluginLoadUtils.getInstance(mContext).getPluginResources(skinApkPath);
    if (packageInfo == null || pluginResources == null) {
      return false;
    }

    String skinPackageName = packageInfo.packageName;
    if (TextUtils.isEmpty(skinPackageName)) {
      return false;
    }

    mSkinResourceManager.setPluginResourcesAndPkgName(pluginResources, skinPackageName);

    mPluginSkinPath = skinApkPath;

    notifySkinChanged();
    return true;

  }

  // 更换皮肤时，通知view更换资源
  private void notifySkinChanged() {
    View view;
    HashMap<String, SkinAttr> viewAttrs;
    Iterator iterator = mSkinAttrMap.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry entry = (Map.Entry) iterator.next();
      view = (View) entry.getKey();
      viewAttrs = (HashMap<String, SkinAttr>) entry.getValue();
      if (view != null) {
        deployViewSkinAttrs(view, viewAttrs);
      }
    }
  }

  public void setWindowStatusBarColor(Window window, @ColorInt int resId) {
    View decorView = window.getDecorView();
    setSkinViewResource(decorView, SkinResDeployerFactory.ACTIVITY_STATUS_BAR_COLOR, resId);
  }


  public void setViewBackground(View view, int resId) {
    setSkinViewResource(view, SkinResDeployerFactory.BACKGROUND, resId);
  }

  public void setTextViewColor(View view, int resId) {
    if (resId == 0) {
      try {
        ((TextView) view).setTextColor(0);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      setSkinViewResource(view, SkinResDeployerFactory.TEXT_COLOR, resId);
    }
  }

  public void setTabTextColor(View view, int resId) {
    setSkinViewResource(view, SkinResDeployerFactory.TAB_TEXT_COLOR, resId);
  }

  public void setTabIndicatorColor(View view, int resId) {
    setSkinViewResource(view, SkinResDeployerFactory.TAB_INDICATOR_COLOR, resId);
  }

  public void setImageDrawable(View view, int resId) {
    if (resId == 0) {
      try {
        ((ImageView) view).setImageResource(0);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      setSkinViewResource(view, SkinResDeployerFactory.IMAGE_SRC, resId);
    }
  }

  public void setTextDrawableTop(View view, int resId) {
    if (resId == 0) {
      try {
        ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      setSkinViewResource(view, SkinResDeployerFactory.TEXT_DRAWABLE_TOP, resId);
    }
  }

  public void setTextDrawableRight(View view, int resId) {
    if (resId == 0) {
      try {
        ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      setSkinViewResource(view, SkinResDeployerFactory.TEXT_DRAWABLE_RIGHT, resId);
    }
  }

  public void setTextDrawables(View view, int resId) {
    if (resId == 0) {
      try {
        ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      setSkinViewResource(view, SkinResDeployerFactory.TEXT_DRAWABLE, resId);
    }
  }

  public void setTextDrawables(View view, int resId, int resId2) {
    setSkinViewResource(view, SkinResDeployerFactory.TEXT_DRAWABLE, resId, resId2);
  }

  public void setCheckButtonDrawable(View view, int resId) {
    setSkinViewResource(view, SkinResDeployerFactory.CHECK_BOX_DRAWABLE, resId);
  }

  private void saveSkinView(View view, SkinAttr viewAttr) {
    if (view == null || viewAttr == null) {
      return;
    }
    HashMap<String, SkinAttr> viewAttrs = new HashMap<>();
    viewAttrs.put(viewAttr.attrName, viewAttr);
    saveSkinView(view, viewAttrs);
  }

  private static class SkinManagerHolder {
    private static final SkinManager INSTANCE = new SkinManager();
  }

  public String getPluginSkinPath() {
    return mPluginSkinPath;
  }
}
