package com.cloud.console.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/** Created by Frank on 2019-01-16. */
@Component
public class TxCoord {

  @Autowired RedisTemplate redisTemplate;

  @Autowired TxPoducer txPoducer;

  public Boolean coord(Tx tx) {
    Boolean result = false;
    try {
      String txId = tx.getTxId();
      redisTemplate.opsForValue().set(tx.getTxId(), tx);
      List<Transaction> transactions = tx.getTransactions();
      for (int i = 0; i < transactions.size(); i++) {
        Transaction transaction = transactions.get(i);
        if ("0".equals(transaction.getStatus())) {
          TxMessage txMessage = new TxMessage();
          txMessage.setTxId(txId);
          txMessage.setTransaction(transaction);
          result = txPoducer.process(txMessage);
          if (!result) {
            break;
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return result;
  }
}
