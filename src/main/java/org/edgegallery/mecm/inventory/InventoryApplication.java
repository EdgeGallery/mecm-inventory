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

package org.edgegallery.mecm.inventory;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.edgegallery.mecm.inventory.service.RestClientHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * External system Inventory application.
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class})
public class InventoryApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryApplication.class);

    @Value("${ssl.enabled:false}")
    private String isSslEnabled;

    @Value("${ssl.trust-store:}")
    private String trustStorePath;

    @Value("${ssl.trust-store-password:}")
    private String trustStorePasswd;

    /**
     * Returns new instance of restTemplate with required configuration.
     *
     * @return restTemplate with required configuration
     */
    @Bean
    public RestTemplate restTemplate() {
        RestClientHelper builder =
                new RestClientHelper(Boolean.parseBoolean(isSslEnabled), trustStorePath, trustStorePasswd);
        CloseableHttpClient client = builder.buildHttpClient();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(client);
        factory.setBufferRequestBody(false);
        return new RestTemplate(factory);
    }


    /**
     * External system Inventory entry function.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        LOGGER.info("Inventory application starting----");
        // do not check host name
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                LOGGER.info("checks client trusted");
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                LOGGER.info("checks server trusted");
            }
        }
        };

        SSLContext sc;
        try {
            sc = SSLContext.getInstance("TLSv1.2");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(NoopHostnameVerifier.INSTANCE);

            SpringApplication.run(InventoryApplication.class, args);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            LOGGER.info("SSL context init error... exiting system {}", e.getMessage());
        }
    }

}
