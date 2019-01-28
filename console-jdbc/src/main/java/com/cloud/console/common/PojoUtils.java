package com.cloud.console.common;

import java.lang.reflect.Field;
import java.util.Properties;

/** Created by Frank on 2019-01-12. */
public class PojoUtils {
  public static Properties pojo2Prop(Object object) {
    Properties properties = new Properties();
    Field[] fields = object.getClass().getDeclaredFields();
    for (int i = 0, len = fields.length; i < len; i++) {
      String varName = fields[i].getName();
      try {
        boolean accessFlag = fields[i].isAccessible();
        fields[i].setAccessible(true);
        Object obj = fields[i].get(object);
        if (obj != null) properties.put(varName, obj.toString());
        fields[i].setAccessible(accessFlag);
      } catch (IllegalArgumentException ex) {
        ex.printStackTrace();
      } catch (IllegalAccessException ex) {
        ex.printStackTrace();
      }
    }
    return properties;
  }
}
