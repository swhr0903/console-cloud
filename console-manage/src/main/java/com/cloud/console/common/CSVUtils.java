package com.cloud.console.common;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.List;

/** Created by Frank on 2018-12-13. */
public class CSVUtils {

  public static List<String[]> read(String file) throws IOException {
    FileReader fReader = new FileReader(file);
    CSVReader csvReader = new CSVReader(fReader);
    return csvReader.readAll();
  }

  public static void write(File file,List<String[]> datas) throws Exception {
    File dir=new File(file.getPath().substring(0,file.getPath().lastIndexOf("/")));
    if(!dir.exists()){
      dir.mkdirs();
    }
    Writer writer = new FileWriter(file);
    CSVWriter csvWriter = new CSVWriter(writer);
    csvWriter.writeAll(datas);
    writer.close();
  }
}
