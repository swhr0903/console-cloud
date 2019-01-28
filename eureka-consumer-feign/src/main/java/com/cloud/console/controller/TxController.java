package com.cloud.console.controller;

import com.cloud.console.vo.tx.Tx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** Created by Frank on 2019-01-24. */
@RestController
@RequestMapping("/tx")
public class TxController {

  @Autowired TxClient txClient;

  @PostMapping(
      value = "/coord",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
  public Boolean coord(@RequestBody Tx tx) {
    return txClient.coord(tx);
  }

  @PostMapping("/consume")
  public Map<String, Object> consume() {
    return txClient.consume();
  }
}
