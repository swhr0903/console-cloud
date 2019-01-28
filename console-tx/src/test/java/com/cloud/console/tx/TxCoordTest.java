package com.cloud.console.tx;

import com.cloud.console.App;
import com.cloud.console.mapper.PayOrder;
import com.cloud.console.mapper.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** Created by Frank on 2019-01-16. */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class TxCoordTest {

  @Autowired TxCoord txCoord;

  @Test
  public void coord() {
    String txId = "TX" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    Tx tx = new Tx();
    tx.setTxId(txId);
    List<Transaction> transactions = new ArrayList<>();

    Transaction transaction = new Transaction();
    transaction.setTxId(1);
    List<Statement> statements = new ArrayList<>();
    Statement statement = new Statement();
    statement.setStatement("com.cloud.console.mapper.UserInfoMapper.insert");
    UserInfo userInfo = new UserInfo();
    userInfo.setId(Long.parseLong("1"));
    userInfo.setAccount("test1");
    statement.setParams(userInfo);
    statements.add(statement);

    statement = new Statement();
    statement.setStatement("com.cloud.console.mapper.UserInfoMapper.insert");
    userInfo = new UserInfo();
    userInfo.setId(Long.parseLong("2"));
    userInfo.setAccount("test2");
    statement.setParams(userInfo);
    statements.add(statement);
    transaction.setStatements(statements);
    transaction.setStatus(0);
    transactions.add(transaction);

    transaction = new Transaction();
    statements = new ArrayList<>();
    transaction.setTxId(2);
    statement.setStatement("com.cloud.console.mapper.PayOrderMapper.insert");
    PayOrder payOrder = new PayOrder();
    payOrder.setId(Long.parseLong("1"));
    payOrder.setPoid("E2017070517204434");
    String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    payOrder.setCreateDate(today);
    statement.setParams(payOrder);
    statements.add(statement);

    statement = new Statement();
    statement.setStatement("com.cloud.console.mapper.PayOrderMapper.insert");
    payOrder = new PayOrder();
    payOrder.setId(Long.parseLong("2"));
    payOrder.setPoid("E201707051720434");
    payOrder.setCreateDate(today);
    statement.setParams(payOrder);
    statements.add(statement);
    transaction.setStatements(statements);
    transaction.setStatus(0);
    transactions.add(transaction);

    tx.setTransactions(transactions);
    txCoord.coord(tx);
  }
}
