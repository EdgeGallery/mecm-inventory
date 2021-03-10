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

import org.edgegallery.mecm.inventory.apihandler.dto.BaseDto;
import org.springframework.http.ResponseEntity;

/**
 * Sync service to synchronize records with MEPM.
 */
public interface SyncService {

    /**
     * Synchronizes updated or inserted records.
     *
     * @param url           url of MEPM component
     * @param responseClass class to which response needs to be mapped
     * @param token         access token
     * @param <T>           type of body
     * @return response entity with body of type T
     */
    <T extends BaseDto> ResponseEntity<T> syncUpdatedRecords(String url, Class<T> responseClass, String token);
}
