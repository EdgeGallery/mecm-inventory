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
import org.edgegallery.mecm.inventory.apihandler.dto.AppLcmDto;
import org.edgegallery.mecm.inventory.model.AppLcm;
import org.edgegallery.mecm.inventory.service.InventoryServiceImpl;
import org.edgegallery.mecm.inventory.service.repository.AppLcmRepository;
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
 * Application LCM Inventory API handler.
 */
@Api(value = "Inventory applcm Inventory api system")
@Validated
@RequestMapping("/inventory/v1")
@RestController
public class AppLcmInventoryHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppLcmInventoryHandler.class);
    @Autowired
    private InventoryServiceImpl service;
    @Autowired
    private AppLcmRepository repository;

    /**
     * Adds a new application LCM record entry into the Inventory.
     *
     * @param appLcmDto application lifecycle manager record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Adds new application LCM record", response = String.class)
    @PostMapping(path = "/applcms", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('MECM_ADMIN')")
    public ResponseEntity<Status> addAppLcmRecord(
            @Valid @ApiParam(value = "applcm inventory information") @RequestBody AppLcmDto appLcmDto) {
        AppLcm lcm = InventoryUtilities.getModelMapper().map(appLcmDto, AppLcm.class);
        lcm.setApplcmId(appLcmDto.getApplcmIp());
        Status status = service.addRecord(lcm, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Updates an exiting application LCM record in the Inventory matching the given tenant ID & appLCM IP.
     *
     * @param appLcmIp  application LCM IP
     * @param appLcmDto application lifecycle manager record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Updates existing application LCM record", response = String.class)
    @PutMapping(path = "/applcms/{applcm_ip}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('MECM_ADMIN')")
    public ResponseEntity<Status> updateAppLcmRecord(
            @ApiParam(value = "applcm IP") @PathVariable("applcm_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String appLcmIp,
            @Valid @ApiParam(value = "applcm inventory information") @RequestBody AppLcmDto appLcmDto) {
        if (!appLcmIp.equals(appLcmDto.getApplcmIp())) {
            LOGGER.error("Input validation failed for applcm IP, value in body {}, value in url {}",
                    appLcmDto.getApplcmIp(), appLcmIp);
            throw new IllegalArgumentException("applcm IP in body and url is different");
        }
        AppLcm lcm = InventoryUtilities.getModelMapper().map(appLcmDto, AppLcm.class);
        lcm.setApplcmId(appLcmIp);
        Status status = service.updateRecord(lcm, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Retrieves all application LCM records.
     *
     * @return application LCM records & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves all application LCM records", response = List.class)
    @GetMapping(path = "/applcms", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT') || hasRole('MECM_ADMIN') || hasRole('MECM_GUEST')")
    public ResponseEntity<List<AppLcmDto>> getAllAppLcmRecords() {
        List<AppLcm> appLcms = service.getTenantRecords(null, repository);
        List<AppLcmDto> appLcmDtos = new LinkedList<>();
        for (AppLcm lcm : appLcms) {
            AppLcmDto appLcmDto = InventoryUtilities.getModelMapper().map(lcm, AppLcmDto.class);
            appLcmDtos.add(appLcmDto);
        }
        return new ResponseEntity<>(appLcmDtos, HttpStatus.OK);
    }

    /**
     * Retrieves a specific application LCM record in the Inventory matching the given tenant ID & appLCM IP.
     *
     * @param appLcmIp application LCM IP
     * @return application LCM record & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves application LCM record", response = AppLcmDto.class)
    @GetMapping(path = "/applcms/{applcm_ip}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT') || hasRole('MECM_ADMIN') || hasRole('MECM_GUEST')")
    public ResponseEntity<AppLcmDto> getAppLcmRecord(
            @ApiParam(value = "applcm IP") @PathVariable("applcm_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String appLcmIp) {
        AppLcm lcm = service.getRecord(appLcmIp, repository);
        AppLcmDto appLcmDto = InventoryUtilities.getModelMapper().map(lcm, AppLcmDto.class);
        return new ResponseEntity<>(appLcmDto, HttpStatus.OK);
    }

    /**
     * Deletes all records for a given tenant.
     *
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes all application LCM records", response = String.class)
    @DeleteMapping(path = "/applcms", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('MECM_ADMIN')")
    public ResponseEntity<Status> deleteAllAppLcmRecords() {
        Status status = service.deleteTenantRecords(null, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Deletes a specific application LCM record in the Inventory matching the given tenant ID & appLCM IP.
     *
     * @param appLcmIp application LCM IP
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes application LCM record", response = String.class)
    @DeleteMapping(path = "/applcms/{applcm_ip}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('MECM_ADMIN')")
    public ResponseEntity<Status> deleteAppLcmRecord(
            @ApiParam(value = "applcm IP") @PathVariable("applcm_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String appLcmIp) {
        Status status = service.deleteRecord(appLcmIp, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
