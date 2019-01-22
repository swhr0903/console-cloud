package com.cloud.console.tx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/** Created by Frank on 2019-01-22. */
@Component
@Slf4j(topic = "tx")
public class TxMonitor {

  @Autowired RedisTemplate redisTemplate;

  @Autowired TxProcess txProcess;

  public void process() {
    Cursor<Map.Entry<Object, Object>> curosr =
        redisTemplate.opsForHash().scan("TX", ScanOptions.NONE);
    Boolean result;
    while (curosr.hasNext()) {
      Map.Entry<Object, Object> entry = curosr.next();
      Tx redisTx = (Tx) entry.getValue();
      List<Transaction> transactions = redisTx.getTransactions();
      for (Transaction transaction : transactions) {
        if (redisTx.getStatus() == 1 && transaction.getStatus() == 0) {
          result = txProcess.process(transaction.getStatements());
          if (!result) {
            log.info("tx monitor process fail...");
            break;
          }
        }
      }
    }
  }
}
