package com.cloud.console.controller;

/** Created by Frank on 2019-01-24. */
import com.cloud.console.vo.tx.Tx;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "console-tx", path = "tx")
public interface TxClient {

  @PostMapping(
      value = "/coord",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
  Boolean coord(@RequestBody Tx tx);

  @PostMapping("/consume")
  Map<String, Object> consume();
}
