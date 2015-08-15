/**
 *
 * @author Wei-Ming Wu
 *
 *
 * Copyright 2015 Wei-Ming Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package com.github.wnameless.spring.batchapi;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class BatchApiTest {

  RestTemplate template = new RestTemplate();

  @Test
  public void testBatch()
      throws URISyntaxException, ParseException, IOException {
    HttpClient client = HttpClientBuilder.create().build();

    HttpPost post = new HttpPost("http://localhost:8080/batch");
    post.setHeader("Content-Type", "application/json");
    String json =
        "[{\"method\":\"GET\",\"url\":\"/home\",\"headers\":{\"Authorization\":\"Basic "
            + Base64Utils.encodeToString("user:password".getBytes()) + "\"}}]";
    HttpEntity entity = new ByteArrayEntity(json.getBytes("UTF-8"));
    post.setEntity(entity);
    HttpResponse response = client.execute(post);
    String result = EntityUtils.toString(response.getEntity());

    List<Map<String, ?>> res = new Gson()
        .getAdapter(new TypeToken<List<Map<String, ?>>>() {}).fromJson(result);

    assertTrue(200 == Double.valueOf(res.get(0).get("status").toString()));
  }

}
