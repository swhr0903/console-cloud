package com.cloud.console.tx;

import com.cloud.console.common.Response;
import com.cloud.console.mq.ResponseProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/** Created by Frank on 2019-01-16. */
@Component
public class TxMessageProcess implements ResponseProcess<TxMessage> {

  @Autowired RedisTemplate redisTemplate;

  @Autowired TxProcess txProcess;

  @Value("${spring.application.name}")
  private String appName;

  @Override
  public Response process(TxMessage txMessage) {
    try {
      if (txMessage.getTxConsumer().equals(appName)) {
        String txId = txMessage.getTxId();
        Transaction transaction = txMessage.getTransaction();
        Tx redisTx = (Tx) redisTemplate.opsForValue().get(txId);
        List<Transaction> transactions = redisTx.getTransactions();
        for (int i = 0; i < transactions.size(); i++) {
          Transaction trans = transactions.get(i);
          if (transaction.getTxId() == trans.getTxId()
              && transaction.getStatus() == 0
              && trans.getStatus() == 0) {
            Boolean result = txProcess.process(transaction.getStatements());
            if (result) {
              Transaction transCopy = (Transaction) trans.clone();
              transCopy.setStatus(1);
              transactions.set(i, transCopy);
              redisTx.setTransactions(transactions);
              if (i == transactions.size() - 1) {
                redisTx.setStatus(1);
              }
              redisTemplate.opsForValue().set(txId, redisTx);
            } else {
              throw new Exception("transaction exec fail,");
            }
          }
        }
      }
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      return new Response(false, "transaction exec exception" + throwable.getMessage());
    }
    return new Response(true, "");
  }
}
