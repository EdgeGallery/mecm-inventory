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
import org.edgegallery.mecm.inventory.apihandler.dto.MecHostDto;
import org.edgegallery.mecm.inventory.model.MecHost;
import org.edgegallery.mecm.inventory.service.ConfigServiceImpl;
import org.edgegallery.mecm.inventory.service.InventoryServiceImpl;
import org.edgegallery.mecm.inventory.service.repository.MecHostRepository;
import org.edgegallery.mecm.inventory.utils.Constants;
import org.edgegallery.mecm.inventory.utils.InventoryUtilities;
import org.edgegallery.mecm.inventory.utils.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * MEC host Inventory API handler.
 */
@Api(value = "Inventory MEC host Inventory api system")
@Validated
@RequestMapping("/inventory/v1")
@RestController
public class MecHostInventoryHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MecHostInventoryHandler.class);
    @Autowired
    private InventoryServiceImpl service;
    @Autowired
    private MecHostRepository repository;
    @Autowired
    private ConfigServiceImpl configService;

    /**
     * Adds a new MEC host record entry into the Inventory.
     *
     * @param tenantId   tenant ID
     * @param mecHostDto mec host record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Adds new MEC host record", response = String.class)
    @PostMapping(path = "/tenants/{tenant_id}/mechosts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Status> addMecHostRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.ID_REGEX) @Size(max = 64) String tenantId,
            @Valid @ApiParam(value = "mechost inventory information") @RequestBody MecHostDto mecHostDto) {
        MecHost host = InventoryUtilities.getModelMapper().map(mecHostDto, MecHost.class);
        host.setTenantId(tenantId);
        host.setMechostId(mecHostDto.getMechostIp() + "_" + tenantId);
        Status status = service.addRecord(host, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Updates an exiting MEC host record in the Inventory matching the given tenant ID & mec host IP.
     *
     * @param tenantId   tenant ID
     * @param mecHostIp  mec host IP
     * @param mecHostDto mec host record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Updates existing MEC host record", response = String.class)
    @PutMapping(path = "/tenants/{tenant_id}/mechosts/{mechost_ip}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Status> updateMecHostRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = "mechost IP") @PathVariable("mechost_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String mecHostIp,
            @Valid @ApiParam(value = "mechost inventory information") @RequestBody MecHostDto mecHostDto) {
        if (!mecHostIp.equals(mecHostDto.getMechostIp())) {
            LOGGER.error("Input validation failed for mechost IP, value in body {}, value in url {}",
                    mecHostDto.getMechostIp(), mecHostIp);
            throw new IllegalArgumentException("mechost IP in body and url is different");
        }
        MecHost host = InventoryUtilities.getModelMapper().map(mecHostDto, MecHost.class);
        host.setTenantId(tenantId);
        host.setMechostId(mecHostIp + "_" + tenantId);
        Status status = service.updateRecord(host, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Retrieves all MEC host records.
     *
     * @param tenantId tenant ID
     * @return MEC host records & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves all MEC host records", response = List.class)
    @GetMapping(path = "/tenants/{tenant_id}/mechosts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MecHostDto>> getAllMecHostRecords(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.ID_REGEX) @Size(max = 64) String tenantId) {
        List<MecHost> mecHosts = service.getTenantRecords(tenantId, repository);
        List<MecHostDto> mecHostDtos = new LinkedList<>();
        for (MecHost host : mecHosts) {
            MecHostDto mecHostDto = InventoryUtilities.getModelMapper().map(host, MecHostDto.class);
            mecHostDtos.add(mecHostDto);
        }
        return new ResponseEntity<>(mecHostDtos, HttpStatus.OK);
    }

    /**
     * Retrieves a specific MEC host record in the Inventory matching the given tenant ID & mec host IP.
     *
     * @param tenantId  tenant ID
     * @param mecHostIp MEC host IP
     * @return MEC host record & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves MEC host record", response = MecHostDto.class)
    @GetMapping(path = "/tenants/{tenant_id}/mechosts/{mechost_ip}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MecHostDto> getMecHostRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = "mechost IP") @PathVariable("mechost_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String mecHostIp) {
        MecHost host = service.getRecord(mecHostIp + "_" + tenantId, repository);
        MecHostDto mecHostDto = InventoryUtilities.getModelMapper().map(host, MecHostDto.class);
        return new ResponseEntity<>(mecHostDto, HttpStatus.OK);
    }

    /**
     * Deletes all records for a given tenant.
     *
     * @param tenantId tenant ID
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes all MEC host records", response = String.class)
    @DeleteMapping(path = "/tenant/{tenant_id}/mechosts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Status> deleteAllMecHostRecords(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.ID_REGEX) @Size(max = 64) String tenantId) {
        Status status = service.deleteTenantRecords(tenantId, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Deletes a specific MEC host record in the Inventory matching the given tenant ID & mec host IP.
     *
     * @param tenantId  tenant ID
     * @param mecHostIp MEC host IP
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes MEC host record", response = String.class)
    @DeleteMapping(path = "/tenant/{tenant_id}/mechosts/{mechost_ip}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Status> deleteMecHostRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = "mechost IP") @PathVariable("mechost_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String mecHostIp) {
        Status status = service.deleteRecord(mecHostIp + "_" + tenantId, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Uploads K8s configuration file to host's infra manager plugin.
     *
     * @param tenantId  tenant ID
     * @param mecHostIp edge host IP
     * @param file      configuration file
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Upload K8s configuration file to applcm", response = String.class)
    @PostMapping(path = "/tenants/{tenant_id}/mechosts/{mechost_ip}/k8sconfig",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadConfigFile(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = "mechost IP") @PathVariable("mechost_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String mecHostIp,
            @ApiParam(value = "config file") @RequestParam("file") MultipartFile file) {
        String status = configService.uploadConfig(tenantId, mecHostIp, file);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Deletes K8s configuration file for host's specific infra manager plugin.
     *
     * @param tenantId  tenant ID
     * @param mecHostIp edge host IP
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes K8s configuration file from applcm", response = String.class)
    @DeleteMapping(path = "/tenants/{tenant_id}/mechosts/{mechost_ip}/k8sconfig",
            produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> deleteConfigFile(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = "mechost IP") @PathVariable("mechost_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String mecHostIp) {
        String status = configService.deleteConfig(tenantId, mecHostIp);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
