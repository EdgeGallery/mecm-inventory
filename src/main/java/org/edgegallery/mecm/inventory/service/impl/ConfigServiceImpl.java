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

package org.edgegallery.mecm.inventory.service.impl;

import static org.edgegallery.mecm.inventory.utils.Constants.APPLCM_URI;

import org.edgegallery.mecm.inventory.exception.InventoryException;
import org.edgegallery.mecm.inventory.model.MecHost;
import org.edgegallery.mecm.inventory.model.Mepm;
import org.edgegallery.mecm.inventory.service.ConfigService;
import org.edgegallery.mecm.inventory.service.InventoryServiceImpl;
import org.edgegallery.mecm.inventory.service.repository.MecHostRepository;
import org.edgegallery.mecm.inventory.service.repository.MepmRepository;
import org.edgegallery.mecm.inventory.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


/**
 * Implementation of configuration service.
 */
@Service("ConfigServiceImpl")
public class ConfigServiceImpl implements ConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigServiceImpl.class);

    @Autowired
    private InventoryServiceImpl service;

    @Autowired
    private MecHostRepository hostRepository;

    @Autowired
    private MepmRepository mepmRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${server.ssl.enabled:false}")
    private String isSslEnabled;

    @Override
    public ResponseEntity<String> uploadConfig(String hostIp, MultipartFile file, String token) {

        // Preparing request parts.
        Resource resource = file.getResource();
        LinkedMultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("configFile", resource);
        parts.add("hostIp", hostIp);

        // Preparing HTTP header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        httpHeaders.set("access_token", token);

        // Creating HTTP entity with header and parts
        HttpEntity<LinkedMultiValueMap<String, Object>> httpEntity = new HttpEntity<>(parts, httpHeaders);

        // Preparing URL
        MecHost host = service.getRecord(hostIp, hostRepository);
        String mepmIp = host.getMepmIp();
        Mepm mepm = service.getRecord(mepmIp, mepmRepository);
        String mepmPort = mepm.getMepmPort();
        String url;
        String msg;
        if (Boolean.parseBoolean(isSslEnabled)) {
            url = new StringBuilder(Constants.HTTPS_PROTO).append(mepmIp).append(":")
                    .append(mepmPort).append(APPLCM_URI).toString();
        } else {
            url = new StringBuilder(Constants.HTTP_PROTO).append(mepmIp).append(":")
                    .append(mepmPort).append(APPLCM_URI).toString();
        }

        ResponseEntity<String> response;
        // Sending request
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            JSONObject json = new JSONObject(response.getBody());
            msg = json.getString("message");
            LOGGER.info("msg is : {}", msg);
        } catch (RestClientException | JSONException e) {
            throw new InventoryException("Failure while uploading file to mepm with error message: "
                    + e.getLocalizedMessage());
        }

        // Updated status to uploaded
        host.setConfigUploadStatus("Uploaded");
        service.updateRecord(host, hostRepository);

        LOGGER.info("Upload status code {}, value {} ", response.getStatusCodeValue(), response.getBody());
        return new ResponseEntity<>(msg, HttpStatus.valueOf(response.getStatusCodeValue()));
    }

    @Override
    public ResponseEntity<String> deleteConfig(String hostIp, String token) {
        // Preparing URL
        MecHost host = service.getRecord(hostIp, hostRepository);
        String mepmIp = host.getMepmIp();
        Mepm mepm = service.getRecord(mepmIp, mepmRepository);
        String mecmPort = mepm.getMepmPort();
        String url;
        if (Boolean.parseBoolean(isSslEnabled)) {
            url = new StringBuilder(Constants.HTTPS_PROTO).append(mepmIp).append(":")
                    .append(mecmPort).append(APPLCM_URI).toString();
        } else {
            url = new StringBuilder(Constants.HTTP_PROTO).append(mepmIp).append(":")
                    .append(mecmPort).append(APPLCM_URI).toString();
        }

        // Preparing request parts.
        MultiValueMap<String, String> parts = new LinkedMultiValueMap<>();
        parts.add("hostIp", hostIp);

        // Preparing HTTP header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("access_token", token);

        // Creating HTTP entity with header and parts
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(parts, httpHeaders);

        ResponseEntity<String> response;
        // Sending request
        try {
            response = restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class);
        } catch (RestClientException e) {
            throw new InventoryException("Failure while removing file from mepm with error message: "
                    + e.getLocalizedMessage());
        }

        LOGGER.info("Delete status code {}, value {} ", response.getStatusCodeValue(), response.getBody());

        // Update the DB
        host.setConfigUploadStatus("Deleted");
        service.updateRecord(host, hostRepository);
        return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getStatusCodeValue()));
    }
}
