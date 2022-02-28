/*
 * Copyright 2021 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.edgegallery.mecm.north.service;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.edgegallery.mecm.north.ApplicationTest;
import org.edgegallery.mecm.north.controller.advice.RspHealthCheck;
import org.edgegallery.mecm.north.facade.MecmHostServiceFacade;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
@AutoConfigureMockMvc
public class MecmServiceTest {

    @Autowired
    private MockMvc mvc;

    private static Gson gson = new Gson();

    private HttpServer httpServer;

    @Before
    public void before() throws IOException {
        System.out.println("start to test");
        httpServer = HttpServer.create(new InetSocketAddress("localhost", 32759), 0);
        httpServer.createContext("/health-check/v1/edge/health", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String method = exchange.getRequestMethod();
                if (method.equals("GET")) {
                    String testData = "ok.";
                    byte[] response = testData.getBytes(StandardCharsets.UTF_8);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
                    exchange.getResponseBody().write(response);
                }
                exchange.close();
            }
        });
        httpServer.start();
    }

    @After
    public void after() {
        httpServer.stop(1);
    }

    @Test
    @WithMockUser(roles = "APPSTORE_ADMIN")
    public void health_check_interface_should_success() throws Exception {
        String hostIp = "localhost";
        String url = String.format("/north/v1/mechosts/%s/health", hostIp);
        MvcResult result = mvc.perform(
            MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON_VALUE).with(csrf())
                .accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        RspHealthCheck rspHealthCheck =  gson.fromJson(result.getResponse().getContentAsString(), RspHealthCheck.class);
        Assert.assertEquals("Healthy", rspHealthCheck.getMessage());
    }

}
