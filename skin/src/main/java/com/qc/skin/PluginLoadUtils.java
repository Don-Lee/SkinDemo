package com.qc.skin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import com.qc.skin.plugin.PluginInfo;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * author : Don
 * date : 2020/12/1 8:48
 * description : 资源包加载类
 */
public class PluginLoadUtils {
  private static final String TAG = "PluginLoadUtils";

  private volatile Context mContext;
  private static PluginLoadUtils sInstance;
  private final Map<String, PluginInfo> mPluginInfoHolder = new HashMap<>();

  private PluginLoadUtils(Context context) {
    mContext = context;
  }

  public static PluginLoadUtils getInstance(Context context) {
    if (sInstance == null) {
      synchronized (PluginLoadUtils.class) {
        if (sInstance == null) {
          sInstance = new PluginLoadUtils(context);
        }
      }
    }
    return sInstance;
  }

  public PackageInfo getPackageInfo(String apkPath) {
    PluginInfo pluginInfo = mPluginInfoHolder.get(apkPath);
    if (pluginInfo != null) {
      PackageInfo info = pluginInfo.getPackageInfo();
      if (info != null) {
        return info;
      }
    }
    return createPackageInfo(apkPath);
  }

  public Resources getPluginResources(String apkPath) {
    PluginInfo pluginInfo = mPluginInfoHolder.get(apkPath);
    if (pluginInfo != null) {
      Resources res = pluginInfo.getResources();
      if (res != null) {
        return res;
      }
    }
    return createResources(apkPath);
  }

  private PackageInfo createPackageInfo(String apkFilePath) {
    PackageManager pm = mContext.getPackageManager();
    PackageInfo pkgInfo = null;
    try {
      // 检索程序外的一个安装包文件
      pkgInfo = pm.getPackageArchiveInfo(apkFilePath,
          PackageManager.GET_ACTIVITIES |
              PackageManager.GET_SERVICES |
              PackageManager.GET_META_DATA);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return pkgInfo;
  }

  private Resources createResources(String dexPath) {
    AssetManager assetManager = createAssetManager(dexPath);
    if (assetManager != null) {
      return createResources(assetManager);
    }
    return null;
  }

  private Resources createResources(AssetManager assetManager) {
    if (assetManager == null) {
      Log.e(TAG, " create Resources failed assetManager is NULL !! ");
      return null;
    }
    Resources superRes = mContext.getResources();
    // 创建一个Resources对象，该Resources对象包含了插件apk中的资源
    Resources resources =
        new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
    return resources;
  }

  // 构建换肤的AssetManager实例
  private AssetManager createAssetManager(String dexPath) {
    try {
      // 创建一个AssetManager资源管理器，通过反射调用addAssetPath()方法，可以加载插件apk中的资源。
      AssetManager assetManager = AssetManager.class.newInstance();
      Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
      addAssetPath.invoke(assetManager, dexPath);
      return assetManager;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}
