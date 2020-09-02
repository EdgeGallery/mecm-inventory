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

package org.edgegallery.mecm.inventory.service;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestClientService {

    @Value("${ssl.enabled:false}")
    private String isSslEnabled;

    @Value("${ssl.trust-store:}")
    private String trustStorePath;

    @Value("${ssl.trust-store-password:}")
    private String trustStorePasswd;

    RestTemplate getRestTemplate() {
        RestClientHelper builder =
                new RestClientHelper(Boolean.parseBoolean(isSslEnabled), trustStorePath, trustStorePasswd);
        CloseableHttpClient client = builder.buildHttpClient();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(client);
        factory.setBufferRequestBody(false);
        return new RestTemplate(factory);
    }
}
