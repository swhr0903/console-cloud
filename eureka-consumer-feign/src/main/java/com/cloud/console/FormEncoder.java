package com.cloud.console;

import com.alibaba.fastjson.JSONObject;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.URLEncoder;

/** Created by Frank on 2018-12-06. */
public class FormEncoder implements Encoder {
  @Override
  public void encode(Object o, Type type, RequestTemplate rt) throws EncodeException {
    StringBuffer sb = new StringBuffer();
    try {
      Class clazz = Thread.currentThread().getContextClassLoader().loadClass(type.getTypeName());
      Field[] fields = clazz.getDeclaredFields();
      String oStr = JSONObject.toJSONString(o);
      for (Field field : fields) {
        if (sb.length() > 0) {
          sb.append("&");
        }
        field.setAccessible(true);
        Object fieldValue = field.get(JSONObject.parseObject(oStr, clazz));
        if (fieldValue != null) {
          sb.append(URLEncoder.encode(field.getName(), "UTF-8"))
              .append("=")
              .append(
                  URLEncoder.encode(
                      field.get(JSONObject.parseObject(oStr, clazz)).toString(), "UTF-8"));
        }
      }
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    rt.header("Content-Type", "application/x-www-form-urlencoded");
    rt.body(sb.toString());
  }
}
