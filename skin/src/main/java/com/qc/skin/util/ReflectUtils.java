package com.qc.skin.util;

import android.text.TextUtils;

import java.lang.reflect.Field;

/**
 * author : Don
 * date : 2020/12/1 13:34
 * description :
 */
public class ReflectUtils {
  // 获取类的实例的变量的值
  public static Object getField(Object reveiver, String filedName) {
    return getField(null, reveiver, filedName);
  }

  // 获取类的静态变量的值
  public static Object getField(String className, String fieldName) {
    return getField(className, null, fieldName);
  }

  // 获取内部类实例持有的外部类对象
  public static <T> T getExternalField(Object innerObj) {
    return getExternalField(innerObj, null);
  }

  private static Object getField(String className, Object receiver, String fieldName) {
    Class<?> clazz = null;
    Field field;
    if (!TextUtils.isEmpty(className)) {
      try {
        clazz = Class.forName(className);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    } else {
      if (receiver != null) {
        clazz = receiver.getClass();
      }
    }
    if (clazz == null) {
      return null;
    }

    try {
      field = findField(clazz, fieldName);
      if (field == null) {
        return null;
      }
      field.setAccessible(true);
      return field.get(receiver);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static Field findField(Class<?> clazz, String name) {
    try {
      return clazz.getDeclaredField(name);
    } catch (NoSuchFieldException e) {
      if (clazz.equals(Object.class)) {
        e.printStackTrace();
        return null;
      }
      Class<?> base = clazz.getSuperclass();
      return findField(base, name);
    }
  }

  /**
   * 内部类持有的外部类对象的形式为：
   * final Outer this$0;
   * flags: ACC_FINAL, ACC_SYNTHETIC
   * 参考：https://www.jianshu.com/p/9335c15c43cf
   * And：https://www.2cto.com/kf/201402/281879.html
   * 
   * @param innerObj 内部类对象
   * @param name 内部类持有的外部类名称，默认是"this$0"
   * @return 内部类持有的外部类对象
   */
  private static <T> T getExternalField(Object innerObj, String name) {
    Class clazz = innerObj.getClass();
    if (name == null || name.isEmpty()) {
      name = "this$0";
    }
    Field field;
    try {
      field = clazz.getDeclaredField(name);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
      return null;
    }
    field.setAccessible(true);
    if (checkModifier(field.getModifiers())) {
      try {
        return (T) field.get(innerObj);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
    return getExternalField(innerObj, name + "$");
  }

  // 表示Field或者Class是编译器自动生成的
  private static final int SYNTHETIC = 0x00001000;
  // 表示Field是final的
  private static final int FINAL = 0x00000010;
  // 内部类持有的外部类对象一定有这两个属性
  private static final int SYNTHETIC_AND_FINAL = SYNTHETIC | FINAL;

  private static boolean checkModifier(int mod) {
    return (mod & SYNTHETIC_AND_FINAL) == SYNTHETIC_AND_FINAL;
  }
}
