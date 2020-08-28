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
import java.util.Optional;
import javax.validation.Valid;
import org.edgegallery.mecm.inventory.apihandler.dto.AppStoreDto;
import org.edgegallery.mecm.inventory.apihandler.dto.MecHostDto;
import org.edgegallery.mecm.inventory.model.AppStore;
import org.edgegallery.mecm.inventory.service.InventoryServiceImpl;
import org.edgegallery.mecm.inventory.service.repository.AppStoreRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private InventoryServiceImpl service;

    @Autowired
    private AppStoreRepository repository;

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
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id") String tenantId,
            @Valid @ApiParam(value = "appstore inventory information") @RequestBody AppStoreDto appStoreDto) {
        ModelMapper mapper = new ModelMapper();
        AppStore store = mapper.map(appStoreDto, AppStore.class);
        store.setTenantId(tenantId);
        store.setAppstoreId(appStoreDto.getAppstoreIp() + "_" + tenantId);
        String status = service.addRecord(store, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
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
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id") String tenantId,
            @ApiParam(value = "appstore IP") @PathVariable("appstore_ip") String appStoreIp,
            @Valid @ApiParam(value = "appstore inventory information") @RequestBody AppStoreDto appStoreDto) {
        ModelMapper mapper = new ModelMapper();
        AppStore store = mapper.map(appStoreDto, AppStore.class);
        store.setTenantId(tenantId);
        store.setAppstoreId(appStoreDto + "_" + tenantId);
        String status = service.updateRecord(store, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Retrieves all application store records.
     *
     * @param tenantId tenant ID
     * @return application store records & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves all application store records", response = List.class)
    @GetMapping(path = "/tenants/{tenant_id}/appstores", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppStoreDto>> getAllAppStoreRecords(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id") String tenantId) {
        List<AppStore> appStores = service.getTenantRecords(tenantId, repository);
        List<AppStoreDto> appStoreDtos = new LinkedList<>();
        for (AppStore store : appStores) {
            ModelMapper mapper = new ModelMapper();
            AppStoreDto appStoreDto = mapper.map(store, AppStoreDto.class);
            appStoreDtos.add(appStoreDto);
        }
        return new ResponseEntity<>(appStoreDtos, HttpStatus.OK);
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
    public ResponseEntity<AppStoreDto> getAppStoreRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id") String tenantId,
            @ApiParam(value = "appstore IP") @PathVariable("appstore_ip") String appStoreIp) {
        Optional<AppStore> record = service.getRecord(appStoreIp + "_" + tenantId, repository);
        if (record.isPresent()) {
            AppStore store = record.get();
            ModelMapper mapper = new ModelMapper();
            AppStoreDto appStoreDto = mapper.map(store, AppStoreDto.class);
            return new ResponseEntity<>(appStoreDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Deletes all application store records for a given tenant.
     *
     * @param tenantId tenant ID
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes all application store records", response = String.class)
    @DeleteMapping(path = "/tenant/{tenant_id}/appstores", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteAllAppStoreRecords(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id") String tenantId) {
        String status = service.deleteTenantRecords(tenantId, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
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
    public ResponseEntity<String> deleteAppStoreRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id") String tenantId,
            @ApiParam(value = "appstore IP") @PathVariable("appstore_ip") String appStoreIp) {
        String status = service.deleteRecord(appStoreIp + "_" + tenantId, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
