/*
 *  Copyright 2021 Huawei Technologies Co., Ltd.
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

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import org.edgegallery.mecm.inventory.apihandler.dto.SyncBaseDto;
import org.edgegallery.mecm.inventory.exception.InventoryException;
import org.edgegallery.mecm.inventory.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Implementation of rest service.
 */
@Service("SyncServiceImpl")
public class RestServiceImpl implements RestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public <T extends SyncBaseDto> ResponseEntity<T> syncRecords(String url, Class<T> responseClass, String token) {
        // Preparing HTTP header
        HttpHeaders httpHeaders = getHttpHeader(token);

        // Creating HTTP entity with headers
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity responseEntity;
        // Sending request
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, responseClass);
        } catch (RestClientException e) {
            throw new InventoryException("Failure while sync file to MEPM with error message: "
                    + e.getLocalizedMessage());
        }
        LOGGER.info("Sync status code {}, value {} ", responseEntity.getStatusCodeValue(), responseEntity.getBody());

        HttpStatus statusCode = responseEntity.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new InventoryException("Failure while sync file to MEPM with not successful status code: "
                    + statusCode);
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> sendRequest(String url, HttpMethod method, String token, String data) {
        // Preparing HTTP header
        HttpHeaders httpHeaders = getHttpHeader(token);

        // Creating HTTP entity with headers
        HttpEntity<String> httpEntity = null;
        if (method == HttpMethod.POST || method == HttpMethod.PUT) {
            httpEntity = new HttpEntity<>(data, httpHeaders);
        } else if (method == HttpMethod.DELETE) {
            httpEntity = new HttpEntity<>(httpHeaders);
        }

        ResponseEntity<String> responseEntity;
        // Sending request
        try {
            responseEntity = restTemplate.exchange(url, method, httpEntity, String.class);
        } catch (RestClientException e) {
            throw new InventoryException("Failure while sending request with error message: "
                    + e.getLocalizedMessage());
        }
        LOGGER.info("Send request status code {}, value {} ", responseEntity.getStatusCodeValue(),
                responseEntity.getBody());

        HttpStatus statusCode = responseEntity.getStatusCode();
        if (statusCode.toString().equals("404")) {
            throw new NoSuchElementException(Constants.RECORD_NOT_FOUND_ERROR);
        }
        if (!statusCode.is2xxSuccessful()) {
            throw new InventoryException("Failure while sending request status code: " + statusCode);
        }
        return responseEntity;
    }

    private HttpHeaders getHttpHeader(String token) {
        List<MediaType> list = new LinkedList<>();
        list.add(MediaType.ALL);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(list);

        httpHeaders.set("access_token", token);
        return httpHeaders;
    }
}
