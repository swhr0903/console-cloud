package com.cloud.console.tx;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.Map;

/** Created by Frank on 2019-01-15. */
@Component
public class TxProcess {

  @Autowired RedisTemplate redisTemplate;

  @Autowired DataSourceTransactionManager dataSourceTransactionManager;

  @Autowired SqlSessionTemplate sqlSessionTemplate;

  public Boolean process(List<Statement> statements) {
    DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
    defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    defaultTransactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
    TransactionStatus transactionStatus =
        dataSourceTransactionManager.getTransaction(defaultTransactionDefinition);
    for (Statement statement : statements) {
      try {
        Map<String, Object> params = (Map) statement.getParams();
        if (params.get("id") != null) {
          Long idLongValue = ((Integer) params.get("id")).longValue();
          params.put("id", idLongValue);
        }
        sqlSessionTemplate.update(statement.getStatement(), params);
      } catch (Exception e) {
        e.printStackTrace();
        dataSourceTransactionManager.rollback(transactionStatus);
        return false;
      }
    }
    dataSourceTransactionManager.commit(transactionStatus);
    return true;
  }
}
