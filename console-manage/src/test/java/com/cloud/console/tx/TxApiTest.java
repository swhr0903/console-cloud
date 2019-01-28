package com.cloud.console.tx;

import com.cloud.console.App;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/** Created by Frank on 2019-01-24. */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class TxApiTest {

  @Autowired RestTemplate restTemplate;

  @Test
  public void testTxConsumer() {
    /*String user = "frank";
    String password = "aa000000";
    String userMsg = user + ":" + password;
    String base64UserMsg = Base64.getEncoder().encodeToString(userMsg.getBytes());

    HttpHeaders headers = new HttpHeaders();
    headers.add("User-Agent", "curl/7.58.0");
    headers.add("Authorization ", base64UserMsg);
    HttpEntity<String> httpEntity = new HttpEntity<>(headers);*/

    ResponseEntity<Response> responseEntity =
        restTemplate.postForEntity("http://127.0.0.1:8012/tx/consume", null, Response.class);
    Assert.assertNotEquals(responseEntity.getBody().getIsSuccess(), true);
  }
}
