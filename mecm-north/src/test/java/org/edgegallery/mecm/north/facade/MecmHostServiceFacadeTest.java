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

package org.edgegallery.mecm.north.facade;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.edgegallery.mecm.north.ApplicationTest;
import org.edgegallery.mecm.north.controller.advice.RspHealthCheck;
import org.edgegallery.mecm.north.service.MecmService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
@AutoConfigureMockMvc
public class MecmHostServiceFacadeTest {

    @MockBean
    MecmService mecmService;

    @Autowired
    MecmHostServiceFacade mecmHostServiceFacade;

    private static Gson gson = new Gson();

    @Test
    @WithMockUser(roles = "APPSTORE_ADMIN")
    public void health_check_should_success() throws Exception{
        String token = "testToken";
        String hostIp = "localhost";
        String response = "Healthy";
        Mockito.when(mecmService.getHealthCheckResult(Mockito.any())).thenReturn(response);
        ResponseEntity <RspHealthCheck> responseEntity = mecmHostServiceFacade.healthCheck(hostIp, token);
        Assert.assertEquals(responseEntity.getBody().getMessage(), "Healthy");
    }
}