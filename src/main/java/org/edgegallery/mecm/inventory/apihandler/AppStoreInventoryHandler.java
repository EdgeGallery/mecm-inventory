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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.validation.Valid;
import org.edgegallery.mecm.inventory.apihandler.dto.AppStoreDto;
import org.edgegallery.mecm.inventory.apihandler.dto.MecHostDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Application store Inventory API handler.
 */
@Api(value = "Inventory MEC Application store Inventory api system")
@Validated
@RequestMapping("/inventory/v1")
@RestController
public class AppStoreInventoryHandler {

    /**
     * Adds a new application store record entry into the Inventory.
     *
     * @param tenantId    tenant ID
     * @param appStoreDto application store record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Adds new application store record", response = String.class)
    @PostMapping(path = "/tenants/{tenant_id}/appstores", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> addAppStoreRecord(
            @PathVariable("tenant_id") String tenantId,
            @Valid @ApiParam(value = "appstore inventory information") @RequestBody AppStoreDto appStoreDto) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Updates an exiting application store record in the Inventory matching the given tenant ID & application store
     * IP.
     *
     * @param tenantId    tenant ID
     * @param appStoreIp  application store IP
     * @param appStoreDto application store record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Updates existing application store record", response = String.class)
    @PutMapping(path = "/tenants/{tenant_id}/appstores/{appstore_ip}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> updateAppStoreRecord(
            @PathVariable("tenant_id") String tenantId,
            @PathVariable("appstore_ip") String appStoreIp,
            @Valid @ApiParam(value = "appstore inventory information") @RequestBody AppStoreDto appStoreDto) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Retrieves all application store records.
     *
     * @param tenantId tenant ID
     * @return application store records & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves all application store records", response = List.class)
    @GetMapping(path = "/tenants/{tenant_id}/appstores", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppStoreDto>> getAllAppStoreRecords(@PathVariable("tenant_id") String tenantId) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Retrieves a specific application store record in the Inventory matching the given tenant ID & application store
     * IP.
     *
     * @param tenantId   tenant ID
     * @param appStoreIp application store IP
     * @return application store record & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves application store record", response = MecHostDto.class)
    @GetMapping(path = "/tenants/{tenant_id}/appstores/{appstore_ip}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppStoreDto> getAppStoreRecord(@PathVariable("tenant_id") String tenantId,
                                                         @PathVariable("appstore_ip") String appStoreIp) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Deletes all application store records for a given tenant.
     *
     * @param tenantId tenant ID
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes all application store records", response = String.class)
    @DeleteMapping(path = "/tenant/{tenant_id}/appstores", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteAllAppStoreRecords(@PathVariable("tenant_id") String tenantId) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Deletes a specific application store record in the Inventory matching the given tenant ID & application store
     * IP.
     *
     * @param tenantId   tenant ID
     * @param appStoreIp application store IP
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes application store record", response = String.class)
    @DeleteMapping(path = "/tenant/{tenant_id}/appstores/{appstore_ip}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteAppStoreRecord(@PathVariable("tenant_id") String tenantId,
                                                       @PathVariable("appstore_ip") String appStoreIp) {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
