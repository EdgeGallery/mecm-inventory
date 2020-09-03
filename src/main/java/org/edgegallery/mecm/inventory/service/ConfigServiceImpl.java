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

import static org.edgegallery.mecm.inventory.utils.Constants.APPLCM_URI;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.edgegallery.mecm.inventory.exception.InventoryException;
import org.edgegallery.mecm.inventory.model.AppLcm;
import org.edgegallery.mecm.inventory.model.MecHost;
import org.edgegallery.mecm.inventory.service.repository.AppLcmRepository;
import org.edgegallery.mecm.inventory.service.repository.MecHostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
    private AppLcmRepository lcmRepository;

    @Autowired
    private RestClientService restClientService;

    @Value("${ssl.enabled:false}")
    private String isSslEnabled;

    @Override
    public String uploadConfig(String tenantId, String hostIp, MultipartFile file, String token) {

        // Preparing request parts.
        Resource resource = file.getResource();
        LinkedMultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("configFile", resource);
        parts.add("hostIp", hostIp);

        // Preparing HTTP header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        try {
            httpHeaders.set("X-Real-IP", InetAddress.getLocalHost().getHostAddress());
            httpHeaders.set("access_token", token);
        } catch (UnknownHostException e) {
            throw new InventoryException(e.getLocalizedMessage());
        }

        // Creating HTTP entity with header and parts
        HttpEntity<LinkedMultiValueMap<String, Object>> httpEntity = new HttpEntity<>(parts, httpHeaders);

        // Preparing URL
        MecHost host = service.getRecord(hostIp + "_" + tenantId, hostRepository);
        String lcmIp = host.getApplcmIp();
        AppLcm lcm = service.getRecord(lcmIp + "_" + tenantId, lcmRepository);
        String lcmPort = lcm.getApplcmPort();
        String url;
        if (Boolean.parseBoolean(isSslEnabled)) {
            url = "https://" + lcmIp + ":" + lcmPort + APPLCM_URI;
        } else {
            url = "http://" + lcmIp + ":" + lcmPort + APPLCM_URI;
        }

        // Get Rest Template
        RestTemplate template = restClientService.getRestTemplate();

        // Sending request
        ResponseEntity<String> response = template.exchange(url, HttpMethod.POST, httpEntity, String.class);

        // Updated status to uploaded
        host.setConfigUploadStatus("Uploaded");
        service.updateRecord(host, hostRepository);

        LOGGER.info("Upload status code {}, value {} ", response.getStatusCodeValue(), response.getBody());
        return response.getBody();
    }

    @Override
    public String deleteConfig(String tenantId, String hostIp, String token) {
        // Preparing URL
        MecHost host = service.getRecord(hostIp + "_" + tenantId, hostRepository);
        String lcmIp = host.getApplcmIp();
        AppLcm lcm = service.getRecord(lcmIp + "_" + tenantId, lcmRepository);
        String lcmPort = lcm.getApplcmPort();
        String url;
        if (Boolean.parseBoolean(isSslEnabled)) {
            url = "https://" + lcmIp + ":" + lcmPort + APPLCM_URI;
        } else {
            url = "http://" + lcmIp + ":" + lcmPort + APPLCM_URI;
        }

        // Preparing request parts.
        MultiValueMap<String, String> parts = new LinkedMultiValueMap<>();
        parts.add("hostIp", hostIp);

        // Preparing HTTP header
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            httpHeaders.set("X-Real-IP", InetAddress.getLocalHost().getHostAddress());
            httpHeaders.set("access_token", token);
        } catch (UnknownHostException e) {
            throw new InventoryException(e.getLocalizedMessage());
        }

        // Creating HTTP entity with header and parts
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(parts, httpHeaders);

        // Get Rest Template
        RestTemplate template = restClientService.getRestTemplate();

        // Sending request
        ResponseEntity<String> response = template.exchange(url, HttpMethod.DELETE, httpEntity, String.class);

        LOGGER.info("Delete status code {}, value {} ", response.getStatusCodeValue(), response.getBody());

        // Update the DB
        host.setConfigUploadStatus("Deleted");
        service.updateRecord(host, hostRepository);

        return response.getBody();
    }
}
