package com.cloud.console.mq;

import com.cloud.console.common.Constants;
import com.cloud.console.common.MessageWithTime;
import com.cloud.console.common.Response;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/** Created by Frank on 2019-01-14. */
@Slf4j
@Component
public class MQBuilder {

  @Autowired ConnectionFactory connectionFactory;

  public Producer buildMessageProducer(
      final String exchange, final String routingKey, final String queue, final String type)
      throws IOException {
    Connection connection = connectionFactory.createConnection();
    this.buildQueue(connection, exchange, routingKey, queue, type);
    final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMandatory(true);
    rabbitTemplate.setExchange(exchange);
    rabbitTemplate.setRoutingKey(routingKey);
    rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    RetryCache retryCache = new RetryCache();
    rabbitTemplate.setConfirmCallback(
        (correlationData, ack, cause) -> {
          if (!ack) {
            log.info("send message failed: " + cause + correlationData.toString());
          } else {
            retryCache.del(Long.valueOf(correlationData.getId()));
          }
        });

    rabbitTemplate.setReturnCallback(
        (message, replyCode, replyText, tmpExchange, tmpRoutingKey) -> {
          try {
            Thread.sleep(Constants.ONE_SECOND);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          log.info("send message failed: " + replyCode + " " + replyText);
          rabbitTemplate.send(message);
        });

    return new Producer() {
      {
        retryCache.setSender(this);
      }

      @Override
      public Response send(Object message) {
        long id = retryCache.generateId();
        long time = System.currentTimeMillis();
        return send(new MessageWithTime(id, time, message));
      }

      @Override
      public Response send(MessageWithTime messageWithTime) {
        try {
          retryCache.add(messageWithTime);
          rabbitTemplate.correlationConvertAndSend(
              messageWithTime.getMessage(),
              new CorrelationData(String.valueOf(messageWithTime.getId())));
        } catch (Exception e) {
          return new Response(false, "");
        }
        return new Response(true, "");
      }
    };
  }

  // 1 创建连接和channel
  // 2 设置message序列化方法
  // 3 consume
  public <T> Consumer buildMessageConsumer(
      String exchange,
      String routingKey,
      final String queue,
      final ResponseProcess<T> responseProcess,
      String type)
      throws IOException {
    final Connection connection = connectionFactory.createConnection();
    this.buildQueue(connection, exchange, routingKey, queue, type);
    final MessagePropertiesConverter messagePropertiesConverter =
        new DefaultMessagePropertiesConverter();
    final MessageConverter messageConverter = new Jackson2JsonMessageConverter();
    return new Consumer() {
      Channel channel;

      {
        channel = connection.createChannel(false);
      }
      // 1 通过basicGet获取原始数据
      // 2 将原始数据转换为特定类型的包
      // 3 处理数据
      // 4 手动发送ack确认
      @Override
      public Response consume() {
        try {
          GetResponse getResponse = channel.basicGet(queue, false);
          while (getResponse == null) {
            getResponse = channel.basicGet(queue, false);
            Thread.sleep(Constants.ONE_SECOND);
          }
          Message message =
              new Message(
                  getResponse.getBody(),
                  messagePropertiesConverter.toMessageProperties(
                      getResponse.getProps(), getResponse.getEnvelope(), "UTF-8"));
          T messageBean = (T) messageConverter.fromMessage(message);
          Response response;
          try {
            response = responseProcess.process(messageBean);
          } catch (Exception e) {
            log.error("exception", e);
            response = new Response(false, "process exception: " + e);
          }
          if (response.getIsSuccess()) {
            channel.basicAck(getResponse.getEnvelope().getDeliveryTag(), false);
          } else {
            // 避免过多失败log
            Thread.sleep(Constants.ONE_SECOND);
            log.info("process message failed: " + response.getErrMsg());
            channel.basicNack(getResponse.getEnvelope().getDeliveryTag(), false, true);
          }
          return response;
        } catch (InterruptedException e) {
          log.error("exception", e);
          return new Response(false, "interrupted exception " + e.toString());
        } catch (ShutdownSignalException | ConsumerCancelledException | IOException e) {
          log.error("exception", e);
          try {
            channel.close();
          } catch (IOException | TimeoutException ex) {
            log.error("exception", ex);
          }
          channel = connection.createChannel(false);
          return new Response(false, "shutdown or cancelled exception " + e.toString());
        } catch (Exception e) {
          log.info("exception : ", e);
          try {
            channel.close();
          } catch (IOException | TimeoutException ex) {
            ex.printStackTrace();
          }
          channel = connection.createChannel(false);
          return new Response(false, "exception " + e.toString());
        }
      }
    };
  }

  private void buildQueue(
      Connection connection, String exchange, String routingKey, final String queue, String type)
      throws IOException {
    Channel channel = connection.createChannel(false);
    channel.exchangeDeclare(exchange, type, true, true, null);
    channel.queueDeclare(queue, true, false, false, null);
    channel.queueBind(queue, exchange, routingKey);
    try {
      channel.close();
    } catch (TimeoutException e) {
      log.info("close channel time out ", e);
    }
  }

  public void delQueue(Connection connection, final String queue) throws IOException {
    Channel channel = connection.createChannel(false);
    channel.queueDelete(queue);
    try {
      channel.close();
    } catch (TimeoutException e) {
      log.info("close channel time out ", e);
    }
  }

  // for test
  public int getMessageCount(final String queue) throws IOException {
    Connection connection = connectionFactory.createConnection();
    final Channel channel = connection.createChannel(false);
    final AMQP.Queue.DeclareOk declareOk = channel.queueDeclarePassive(queue);
    return declareOk.getMessageCount();
  }
}
