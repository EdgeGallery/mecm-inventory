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
import org.edgegallery.mecm.inventory.apihandler.dto.AppLcmDto;
import org.edgegallery.mecm.inventory.model.AppLcm;
import org.edgegallery.mecm.inventory.service.InventoryServiceImpl;
import org.edgegallery.mecm.inventory.service.repository.AppLcmRepository;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Application LCM Inventory API handler.
 */
@Api(value = "Inventory applcm Inventory api system")
@Validated
@RequestMapping("/inventory/v1")
@RestController
public class AppLcmInventoryHandler {

    @Autowired
    private InventoryServiceImpl service;

    @Autowired
    private AppLcmRepository repository;

    /**
     * Adds a new application LCM record entry into the Inventory.
     *
     * @param tenantId  tenant ID
     * @param appLcmDto application lifecycle manager record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Adds new application LCM record", response = String.class)
    @PostMapping(path = "/tenants/{tenant_id}/applcms")
    public ResponseEntity<String> addAppLcmRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id") String tenantId,
            @Valid @ApiParam(value = "applcm inventory information") @RequestBody AppLcmDto appLcmDto) {
        ModelMapper mapper = new ModelMapper();
        AppLcm lcm = mapper.map(appLcmDto, AppLcm.class);
        lcm.setTenantId(tenantId);
        lcm.setApplcmId(appLcmDto.getApplcmIp() + "_" + tenantId);
        String status = service.addRecord(lcm, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Updates an exiting application LCM record in the Inventory matching the given tenant ID & appLCM IP.
     *
     * @param tenantId  tenant ID
     * @param appLcmIp  application LCM IP
     * @param appLcmDto application lifecycle manager record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Updates existing application LCM record", response = String.class)
    @PutMapping(path = "/tenants/{tenant_id}/applcms/{applcm_ip}",
            produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> updateAppLcmRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id") String tenantId,
            @ApiParam(value = "applcm IP") @PathVariable("applcm_ip") String appLcmIp,
            @Valid @ApiParam(value = "applcm inventory information") @RequestBody AppLcmDto appLcmDto) {
        ModelMapper mapper = new ModelMapper();
        AppLcm lcm = mapper.map(appLcmDto, AppLcm.class);
        lcm.setTenantId(tenantId);
        lcm.setApplcmId(appLcmIp + "_" + tenantId);
        String status = service.updateRecord(lcm, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Retrieves all application LCM records.
     *
     * @param tenantId tenant ID
     * @return application LCM records & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves all application LCM records", response = List.class)
    @GetMapping(path = "/tenants/{tenant_id}/applcms", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppLcmDto>> getAllAppLcmRecords(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id") String tenantId) {
        List<AppLcm> appLcms = service.getTenantRecords(tenantId, repository);
        List<AppLcmDto> appLcmDtos = new LinkedList<>();
        for (AppLcm lcm : appLcms) {
            ModelMapper mapper = new ModelMapper();
            AppLcmDto appLcmDto = mapper.map(lcm, AppLcmDto.class);
            appLcmDtos.add(appLcmDto);
        }
        return new ResponseEntity<>(appLcmDtos, HttpStatus.OK);
    }

    /**
     * Retrieves a specific application LCM record in the Inventory matching the given tenant ID & appLCM IP.
     *
     * @param tenantId tenant ID
     * @param appLcmIp application LCM IP
     * @return application LCM record & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves application LCM record", response = AppLcmDto.class)
    @GetMapping(path = "/tenants/{tenant_id}/applcms/{applcm_ip}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppLcmDto> getAppLcmRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id") String tenantId,
            @ApiParam(value = "applcm IP") @PathVariable("applcm_ip") String appLcmIp) {
        Optional<AppLcm> record = service.getRecord(appLcmIp + "_" + tenantId, repository);
        if (record.isPresent()) {
            AppLcm lcm = record.get();
            ModelMapper mapper = new ModelMapper();
            AppLcmDto appLcmDto = mapper.map(lcm, AppLcmDto.class);
            return new ResponseEntity<>(appLcmDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Deletes all records for a given tenant.
     *
     * @param tenantId tenant ID
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes all application LCM records", response = String.class)
    @DeleteMapping(path = "/tenants/{tenant_id}/applcms",
            produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> deleteAllAppLcmRecords(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id") String tenantId) {
        String status = service.deleteTenantRecords(tenantId, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Deletes a specific application LCM record in the Inventory matching the given tenant ID & appLCM IP.
     *
     * @param tenantId tenant ID
     * @param appLcmIp application LCM IP
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes application LCM record", response = String.class)
    @DeleteMapping(path = "/tenants/{tenant_id}/applcms/{applcm_ip}",
            produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> deleteAppLcmRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id") String tenantId,
            @ApiParam(value = "applcm IP") @PathVariable("applcm_ip") String appLcmIp) {
        String status = service.deleteRecord(appLcmIp + "_" + tenantId, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Uploads K8s configuration file to applcm.
     *
     * @param tenantId tenant ID
     * @param appLcmIp applcm IP
     * @param hostIp   edge host IP
     * @param file     configuration file
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Upload K8s configuration file to applcm", response = String.class)
    @PostMapping(path = "/tenants/{tenant_id}/applcms/{applcm_ip}/host/{hostIp}/k8sconfig",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadConfigFile(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id") String tenantId,
            @ApiParam(value = "applcm IP") @PathVariable("applcm_ip") String appLcmIp,
            @ApiParam(value = "host IP") @PathVariable("hostIp") String hostIp,
            @RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Deletes K8s configuration file from applcm.
     *
     * @param tenantId tenant ID
     * @param appLcmIp applcm IP
     * @param hostIp   edge host IP
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes K8s configuration file from applcm", response = String.class)
    @DeleteMapping(path = "/tenants/{tenant_id}/applcms/{applcm_ip}/host/{hostIp}/k8sconfig",
            produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> deleteConfigFile(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id") String tenantId,
            @ApiParam(value = "applcm IP") @PathVariable("applcm_ip") String appLcmIp,
            @ApiParam(value = "host IP") @PathVariable("hostIp") String hostIp) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
