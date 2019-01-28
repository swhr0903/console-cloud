package com.cloud.console.mq;

import com.cloud.console.common.Constants;
import com.cloud.console.common.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Created by Frank on 2019-01-14. */
@Slf4j(topic = "mq")
public class ThreadPoolConsumer<T> {
  private ExecutorService executor;
  private volatile boolean stop = false;
  private final ThreadPoolConsumerBuilder<T> infoHolder;

  public static class ThreadPoolConsumerBuilder<T> {
    int threadCount;
    long intervalMils;
    MQBuilder mqBuilder;
    String exchange;
    String routingKey;
    String queue;
    String type = "direct";
    ResponseProcess<T> responseProcess;

    public ThreadPoolConsumerBuilder<T> setThreadCount(int threadCount) {
      this.threadCount = threadCount;
      return this;
    }

    public ThreadPoolConsumerBuilder<T> setIntervalMils(long intervalMils) {
      this.intervalMils = intervalMils;

      return this;
    }

    public ThreadPoolConsumerBuilder<T> setMQAccessBuilder(MQBuilder mqBuilder) {
      this.mqBuilder = mqBuilder;

      return this;
    }

    public ThreadPoolConsumerBuilder<T> setExchange(String exchange) {
      this.exchange = exchange;

      return this;
    }

    public ThreadPoolConsumerBuilder<T> setRoutingKey(String routingKey) {
      this.routingKey = routingKey;

      return this;
    }

    public ThreadPoolConsumerBuilder<T> setQueue(String queue) {
      this.queue = queue;

      return this;
    }

    public ThreadPoolConsumerBuilder<T> setType(String type) {
      this.type = type;

      return this;
    }

    public ThreadPoolConsumerBuilder<T> setResponseProcess(ResponseProcess<T> responseProcess) {
      this.responseProcess = responseProcess;

      return this;
    }

    public ThreadPoolConsumer<T> build() {
      return new ThreadPoolConsumer<T>(this);
    }
  }

  private ThreadPoolConsumer(ThreadPoolConsumerBuilder<T> threadPoolConsumerBuilder) {
    this.infoHolder = threadPoolConsumerBuilder;
    executor = Executors.newFixedThreadPool(threadPoolConsumerBuilder.threadCount);
  }

  // 1 构造messageConsumer
  // 2 执行consume
  public void start() throws IOException {
    for (int i = 0; i < infoHolder.threadCount; i++) {
      // 1
      final Consumer consumer =
          infoHolder.mqBuilder.buildMessageConsumer(
              infoHolder.exchange,
              infoHolder.routingKey,
              infoHolder.queue,
              infoHolder.responseProcess,
              infoHolder.type);

      executor.execute(
          new Runnable() {
            @Override
            public void run() {
              while (!stop) {
                try {
                  Response response = consumer.consume();

                  if (infoHolder.intervalMils > 0) {
                    try {
                      Thread.sleep(infoHolder.intervalMils);
                    } catch (InterruptedException e) {
                      e.printStackTrace();
                      log.info("interrupt ", e);
                    }
                  }

                  if (!response.getIsSuccess()) {
                    log.info("run error " + response.getErrMsg());
                  }
                } catch (Exception e) {
                  e.printStackTrace();
                  log.info("run exception ", e);
                }
              }
            }
          });
    }
    Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
  }

  public void stop() {
    this.stop = true;

    try {
      Thread.sleep(Constants.ONE_SECOND);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
