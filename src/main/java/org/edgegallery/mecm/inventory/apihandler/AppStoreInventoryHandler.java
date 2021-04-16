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
import java.util.LinkedList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.edgegallery.mecm.inventory.apihandler.dto.AppStoreDto;
import org.edgegallery.mecm.inventory.model.AppStore;
import org.edgegallery.mecm.inventory.service.InventoryServiceImpl;
import org.edgegallery.mecm.inventory.service.repository.AppStoreRepository;
import org.edgegallery.mecm.inventory.utils.Constants;
import org.edgegallery.mecm.inventory.utils.InventoryUtilities;
import org.edgegallery.mecm.inventory.utils.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Application store Inventory API handler.
 */
@RestSchema(schemaId = "inventory-appstore")
@Api(value = "Inventory MEC Application store Inventory api system")
@Validated
@RequestMapping("/inventory/v1")
@Controller
public class AppStoreInventoryHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppStoreInventoryHandler.class);
    @Autowired
    private InventoryServiceImpl service;
    @Autowired
    private AppStoreRepository repository;

    /**
     * Adds a new application store record entry into the Inventory.
     *
     * @param appStoreDto application store record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Adds new application store record", response = String.class)
    @PostMapping(path = "/appstores", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_ADMIN')")
    public ResponseEntity<Status> addAppStoreRecord(
            @Valid @ApiParam(value = "appstore inventory information") @RequestBody AppStoreDto appStoreDto) {
        AppStore store = InventoryUtilities.getModelMapper().map(appStoreDto, AppStore.class);
        store.setAppstoreId(appStoreDto.getAppstoreIp());
        Status status = service.addRecord(store, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Updates an exiting application store record in the Inventory matching the given tenant ID & application store
     * IP.
     *
     * @param appStoreIp  application store IP
     * @param appStoreDto application store record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Updates existing application store record", response = String.class)
    @PutMapping(path = "/appstores/{appstore_ip}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_ADMIN')")
    public ResponseEntity<Status> updateAppStoreRecord(
            @ApiParam(value = "appstore IP") @PathVariable("appstore_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String appStoreIp,
            @Valid @ApiParam(value = "appstore inventory information") @RequestBody AppStoreDto appStoreDto) {
        if (!appStoreIp.equals(appStoreDto.getAppstoreIp())) {
            LOGGER.error("Input validation failed for appstore IP, value in body {}, value in url {}",
                    appStoreDto.getAppstoreIp(), appStoreIp);
            throw new IllegalArgumentException("appstore IP in body and url is different");
        }
        AppStore store = InventoryUtilities.getModelMapper().map(appStoreDto, AppStore.class);
        store.setAppstoreId(appStoreIp);
        Status status = service.updateRecord(store, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Retrieves all application store records.
     *
     * @return application store records & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves all application store records", response = List.class)
    @GetMapping(path = "/appstores", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT') || hasRole('MECM_ADMIN') || hasRole('MECM_GUEST')")
    public ResponseEntity<List<AppStoreDto>> getAllAppStoreRecords() {
        List<AppStore> appStores = service.getTenantRecords(null, repository);
        List<AppStoreDto> appStoreDtos = new LinkedList<>();
        for (AppStore store : appStores) {
            AppStoreDto appStoreDto = InventoryUtilities.getModelMapper().map(store, AppStoreDto.class);
            appStoreDtos.add(appStoreDto);
        }
        return new ResponseEntity<>(appStoreDtos, HttpStatus.OK);
    }

    /**
     * Retrieves a specific application store record in the Inventory matching the given tenant ID & application store
     * IP.
     *
     * @param appStoreIp application store IP
     * @return application store record & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves application store record", response = AppStoreDto.class)
    @GetMapping(path = "/appstores/{appstore_ip}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT') || hasRole('MECM_ADMIN') || hasRole('MECM_GUEST')")
    public ResponseEntity<AppStoreDto> getAppStoreRecord(
            @ApiParam(value = "appstore IP") @PathVariable("appstore_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String appStoreIp) {
        AppStore store = service.getRecord(appStoreIp, repository);
        AppStoreDto appStoreDto = InventoryUtilities.getModelMapper().map(store, AppStoreDto.class);
        return new ResponseEntity<>(appStoreDto, HttpStatus.OK);
    }

    /**
     * Deletes all application store records for a given tenant.
     *
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes all application store records", response = String.class)
    @DeleteMapping(path = "/appstores", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_ADMIN')")
    public ResponseEntity<Status> deleteAllAppStoreRecords() {
        Status status = service.deleteTenantRecords(null, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Deletes a specific application store record in the Inventory matching the given tenant ID & application store
     * IP.
     *
     * @param appStoreIp application store IP
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes application store record", response = String.class)
    @DeleteMapping(path = "/appstores/{appstore_ip}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_ADMIN')")
    public ResponseEntity<Status> deleteAppStoreRecord(
            @ApiParam(value = "appstore IP") @PathVariable("appstore_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String appStoreIp) {
        Status status = service.deleteRecord(appStoreIp, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
