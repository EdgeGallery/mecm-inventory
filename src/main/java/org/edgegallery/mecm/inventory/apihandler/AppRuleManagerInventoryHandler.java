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
import org.edgegallery.mecm.inventory.apihandler.dto.AppRuleDto;
import org.edgegallery.mecm.inventory.model.AppRuleManager;
import org.edgegallery.mecm.inventory.service.InventoryServiceImpl;
import org.edgegallery.mecm.inventory.service.repository.AppRuleManagerRepository;
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
 * Application Rule Manager Inventory API handler.
 */
@Api(value = "app rule manager inventory api system")
@Validated
@RequestMapping("/inventory/v1")
@RestController
public class AppRuleManagerInventoryHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppRuleManagerInventoryHandler.class);
    @Autowired
    private InventoryServiceImpl service;

    @Autowired
    private AppRuleManagerRepository repository;

    /**
     * Adds a new application rule manager record entry into the Inventory.
     *
     * @param appRuleDto application rule manager record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Adds new application LCM record", response = String.class)
    @PostMapping(path = "/apprulemanagers", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('MECM_ADMIN')")
    public ResponseEntity<Status> addAppRuleManagerRecord(
            @Valid @ApiParam(value = "app rule manager inventory information") @RequestBody AppRuleDto appRuleDto) {
        AppRuleManager appRuleManager = InventoryUtilities.getModelMapper().map(appRuleDto, AppRuleManager.class);

        appRuleManager.setAppRuleManagerId(appRuleDto.getAppRuleIp());
        Status status = service.addRecord(appRuleManager, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Updates an exiting application rule manager record in the Inventory matching the given tenant ID &
     * appRuleManager IP.
     *
     * @param appRuleManagerIp  application rule manager IP
     * @param appRuleDto application rule manager record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Updates existing application rule manager record", response = String.class)
    @PutMapping(path = "/apprulemanagers/{app_rule_manager_ip}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('MECM_ADMIN')")
    public ResponseEntity<Status> updateAppRuleManagerRecord(
            @ApiParam(value = "app rule manager IP") @PathVariable("app_rule_manager_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String appRuleManagerIp,
            @Valid @ApiParam(value = "app rule manager information") @RequestBody AppRuleDto appRuleDto) {
        if (!appRuleManagerIp.equals(appRuleDto.getAppRuleIp())) {
            LOGGER.error("Input validation failed for app rule manager IP, value in body {}, value in url {}",
                    appRuleDto.getAppRuleIp(), appRuleManagerIp);
            throw new IllegalArgumentException("app rule manager IP in body and url is different");
        }
        AppRuleManager appRuleManager = InventoryUtilities.getModelMapper().map(appRuleDto, AppRuleManager.class);
        appRuleManager.setAppRuleManagerId(appRuleManagerIp);
        Status status = service.updateRecord(appRuleManager, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Retrieves all application rule manager records.
     *
     * @return application rule manager records & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves all application rule manager records", response = List.class)
    @GetMapping(path = "/apprulemanagers", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT') || hasRole('MECM_ADMIN') || hasRole('MECM_GUEST')")
    public ResponseEntity<List<AppRuleDto>> getAllAppRuleManagerRecords() {
        List<AppRuleManager> appRuleManagers = service.getTenantRecords(null, repository);
        List<AppRuleDto> appRuleDtos = new LinkedList<>();
        for (AppRuleManager appRuleManager : appRuleManagers) {
            AppRuleDto appRuleDto = InventoryUtilities.getModelMapper().map(appRuleManager, AppRuleDto.class);
            appRuleDtos.add(appRuleDto);
        }
        return new ResponseEntity<>(appRuleDtos, HttpStatus.OK);
    }

    /**
     * Retrieves a specific application rule manager record in the Inventory matching the given tenant ID &
     * app rule manager IP.
     *
     * @param appRuleManagerIp application rule manager IP
     * @return application rule manager record & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves application rule manager record", response = AppRuleDto.class)
    @GetMapping(path = "/apprulemanagers/{app_rule_manager_ip}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT') || hasRole('MECM_ADMIN') || hasRole('MECM_GUEST')")
    public ResponseEntity<AppRuleDto> getAppRuleManagerRecord(
            @ApiParam(value = "app rule manager IP") @PathVariable("app_rule_manager_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String appRuleManagerIp) {
        AppRuleManager appRuleManager = service.getRecord(appRuleManagerIp, repository);
        AppRuleDto appRuleDto = InventoryUtilities.getModelMapper().map(appRuleManager, AppRuleDto.class);
        return new ResponseEntity<>(appRuleDto, HttpStatus.OK);
    }

    /**
     * Deletes all records for a given tenant.
     *
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes all application LCM records", response = String.class)
    @DeleteMapping(path = "/apprulemanagers", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('MECM_ADMIN')")
    public ResponseEntity<Status> deleteAllAppRuleManagerRecords() {
        Status status = service.deleteTenantRecords(null, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Deletes a specific application rule manager record in the Inventory matching the given tenant ID
     * & app rule manager IP.
     *
     * @param appRuleManagerIp application rule manager IP
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes application LCM record", response = String.class)
    @DeleteMapping(path = "/apprulemanagers/{app_rule_manager_ip}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('MECM_ADMIN')")
    public ResponseEntity<Status> deleteAppRuleManagerRecord(
            @ApiParam(value = "app rule manager IP") @PathVariable("app_rule_manager_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String appRuleManagerIp) {
        Status status = service.deleteRecord(appRuleManagerIp, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
