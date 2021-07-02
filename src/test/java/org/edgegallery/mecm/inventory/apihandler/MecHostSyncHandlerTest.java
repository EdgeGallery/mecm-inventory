/*
 *  Copyright 2020 Huawei Technologies Co., Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.edgegallery.mecm.inventory.apihandler;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InventoryApplicationTest.class)
@AutoConfigureMockMvc
public class MecHostSyncHandlerTest {

    private static final String ACCESS_TOKEN = "access_token";
    private static final String SAMPLE_TOKEN = "SampleToken";
    @Autowired
    MockMvc mvc;
    @Autowired
    private RestTemplate restTemplate;
    private MockRestServiceServer server;

    @Before
    public void setUp() {
        server = MockRestServiceServer.createServer(restTemplate);
    }

    private MockRestServiceServer resetServer(MockRestServiceServer server) {
        server.reset();
        server = MockRestServiceServer.createServer(restTemplate);
        return server;
    }

    @After
    public void clear() {
        server.reset();
    }

    private void syncMecHost(MockRestServiceServer server)  throws Exception {

        // Mocking get updated mechost API
        String url = "http://1.1.1.1:10000/lcmcontroller/v1/hosts/sync_updated";
        server.expect(requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess());

        // Mocking get deleted mechost API
        url = "http://1.1.1.1:10000/lcmcontroller/v1/hosts/sync_deleted";
        server.expect(requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess());
    }

    @Test
    @WithMockUser(roles = {"MECM_TENANT", "MECM_ADMIN", "MECM_GUEST"})
    public void syncMecHostTest() throws Exception {
        syncMecHost(server);

       // Test mepm record post
        MvcResult result =
                mvc.perform(MockMvcRequestBuilders.post("/inventory/v1/mepms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .content("{ \"mepmIp\": \"1.1.1.1\", \"mepmPort\": \"10000\", \"userName\": \"Test\", "
                                + "\"mepmName\": \"mepm123\" }")).andDo(MockMvcResultHandlers.print()).andReturn();

        String postResponse = result.getResponse().getContentAsString();
        Assert.assertEquals("{\"response\":\"Saved\"}", postResponse);


        // Sync app instance info
        ResultActions postResult =
                mvc.perform(MockMvcRequestBuilders.get("/inventory/v1/mepms/" + "1.1.1.1" + "/mechost/sync")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).with(csrf())
                        .header(ACCESS_TOKEN, SAMPLE_TOKEN));
        MvcResult postMvcResult = postResult.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
    }
}
