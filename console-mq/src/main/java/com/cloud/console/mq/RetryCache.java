package com.cloud.console.mq;

import com.cloud.console.common.Constants;
import com.cloud.console.common.MessageWithTime;
import com.cloud.console.common.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

/** Created by Frank on 2019-01-14. */
@Slf4j(topic = "mq")
public class RetryCache {
  private Producer sender;
  private boolean stop = false;
  private Map<Long, MessageWithTime> map = new ConcurrentSkipListMap<>();
  private AtomicLong id = new AtomicLong();

  public void setSender(Producer sender) {
    this.sender = sender;
    startRetry();
  }

  public long generateId() {
    return id.incrementAndGet();
  }

  public void add(MessageWithTime messageWithTime) {
    map.putIfAbsent(messageWithTime.getId(), messageWithTime);
  }

  public void del(long id) {
    map.remove(id);
  }

  private void startRetry() {
    new Thread(
            () -> {
              while (!stop) {
                try {
                  Thread.sleep(Constants.RETRY_TIME_INTERVAL);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
                long now = System.currentTimeMillis();
                for (Map.Entry<Long, MessageWithTime> entry : map.entrySet()) {
                  MessageWithTime messageWithTime = entry.getValue();
                  if (null != messageWithTime) {
                    if (messageWithTime.getTime() + 3 * Constants.VALID_TIME < now) {
                      log.info("send message {} failed after 3 min ", messageWithTime);
                      del(entry.getKey());
                    } else if (messageWithTime.getTime() + Constants.VALID_TIME < now) {
                      Response res = sender.send(messageWithTime);
                      if (!res.getIsSuccess()) {
                        log.info(
                            "retry send message failed {} errMsg {}",
                            messageWithTime,
                            res.getErrMsg());
                      }
                    }
                  }
                }
              }
            })
        .start();
  }
}
