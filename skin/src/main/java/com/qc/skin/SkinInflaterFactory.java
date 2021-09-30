package com.qc.skin;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.qc.skin.bean.SkinAttr;
import com.qc.skin.bean.SkinConfig;
import com.qc.skin.parser.SkinAttributeParser;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * author : Don
 * date : 2020/11/23 16:57
 * description : 拦截view的创建，解析控件属性
 */
public class SkinInflaterFactory implements LayoutInflater.Factory2 {
  private AppCompatActivity activity;

  private Map<View, SkinAttr> mSkinMap = new HashMap<>();

  // 所有原生控件都是这3个包下面的
  private static final String[] sClassPrefixList = {
      "android.widget.",
      "android.view.",
      "android.webkit."
  };

  public SkinInflaterFactory() {

  }

  public SkinInflaterFactory(AppCompatActivity activity) {
    this.activity = activity;
  }


  @Nullable
  @Override
  public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context,
      @NonNull AttributeSet attrs) {
    // 只有在xml中设置了View的属性app:skinEnable，才支持xml属性换肤
    boolean isNeedSkin = attrs.getAttributeBooleanValue(SkinConfig.SKIN_XML_NAMESPACE,
        SkinConfig.ATTR_SKIN_ENABLE, false);
    // 下面这两句必须写，否者会报错，可看https://blog.csdn.net/lmj623565791/article/details/51503977
    AppCompatDelegate delegate = activity.getDelegate();
    View view = delegate.createView(parent, name, context, attrs);
    if (isNeedSkin) {
      // 如果是自定义view的话此处的name是全限定名，因此会含有点
      if (name.contains(".")) {
        // 实例化view
        view = onCreateView(name, context, attrs);
      } else {// 如果是系统view，name表示类名
        for (String pre : sClassPrefixList) {
          view = onCreateView(pre + name, context, attrs);
          if (view != null) {
            break;
          }
        }
      }

      if (view != null) {
        parseAndSaveSkinAttr(attrs, view);
      }

      // 如果以接口的方式返回颜色值可以以此方法直接替换颜色，不需要以加载资源包的方式
      /*
       * if (view != null) {
       * if (view instanceof TextView) {
       * ((TextView) view).setTextColor(context.getResources().getColor(R.color.test));
       * } else {
       * view.setBackground(context.getDrawable(R.drawable.shape_white_bg));
       * }
       * Log.e("TAG", "换肤");
       * }
       */
    }

    return view;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull String name, @NonNull Context context,
      @NonNull AttributeSet attrs) {
    View view = null;
    try {
      // 反射得到控件的类对象
      Class clazz = context.getClassLoader().loadClass(name);
      // 获取到控件的构造方法
      Constructor<? extends View> constructor =
          clazz.getConstructor(Context.class, AttributeSet.class);
      // 通过构造方法实例化控件
      view = constructor.newInstance(context, attrs);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return view;
  }

  private void parseAndSaveSkinAttr(AttributeSet attrs, View view) {
    HashMap<String, SkinAttr> viewAttrs = SkinAttributeParser.parseSkinAttr(attrs, view);
    if (viewAttrs == null || viewAttrs.size() == 0) {
      return;
    }
    // 设置view的皮肤属性
    SkinManager.getInstance().deployViewSkinAttrs(view, viewAttrs);
    // save view attribute
    SkinManager.getInstance().saveSkinView(view, viewAttrs);
  }
}
