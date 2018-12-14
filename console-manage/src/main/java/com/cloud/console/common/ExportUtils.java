package com.cloud.console.common;

import org.apache.commons.lang.time.DateFormatUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/** Created by Frank on 2018-12-14. */
public class ExportUtils {
  public static String export(String fileName, Map<String, String> header, List dataList)
      throws Exception {
    List<String[]> datas = new ArrayList<>();
    String[] row = new String[header.size()];
    int index = 0;
    for (Map.Entry<String, String> entry : header.entrySet()) {
      row[index] = entry.getValue();
      index++;
    }
    datas.add(row);
    for (int i = 0; i < dataList.size(); i++) {
      row = new String[header.size()];
      Object data = dataList.get(i);
      index = 0;
      for (Map.Entry<String, String> entry : header.entrySet()) {
        row[index] = ReflectionUtils.getFieldByClasss(entry.getKey(), data);
        index++;
      }
      datas.add(row);
    }
    fileName += DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + ".csv";
    File file = new File(Constant.CSV_DIR_NAME + File.separatorChar + fileName);
    CSVUtils.write(file, datas);
    String fileUrl = File.separatorChar + Constant.CSV_DIR_NAME + File.separatorChar + fileName;
    return fileUrl;
  }
}
