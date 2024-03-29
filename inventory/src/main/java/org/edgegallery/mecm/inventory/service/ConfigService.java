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

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * Configuration service to configure configs for a host.
 */
public interface ConfigService {

    /**
     * Uploads configuration file to host's infra manager plugin.
     *
     * @param tenantId tenant ID
     * @param hostIp edge host IP
     * @param file   configuration file
     * @param token  access token
     * @return status
     */
    ResponseEntity<String> uploadConfig(String tenantId, String hostIp, MultipartFile file, String token);

    /**
     * Deletes configuration file for host's specific infra manager plugin.
     *
     * @param tenantId tenant ID
     * @param hostIp edge host IP
     * @param token  access token
     * @return status
     */
    ResponseEntity<String> deleteConfig(String tenantId, String hostIp, String token);
}
