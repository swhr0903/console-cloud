package com.cloud.console.common;

import java.lang.reflect.Field;

/** Created by Frank on 2018-12-13. */
public class ReflectionUtils {

  /**
   * 根据属性名获取属性值，包括各种安全范围和所有父类
   *
   * @param fieldName
   * @param object
   * @return
   */
  public static String getFieldByClasss(String fieldName, Object object) {
    Field field;
    Class<?> clazz = object.getClass();
    for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
      try {
        field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return String.valueOf(field.get(object));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
