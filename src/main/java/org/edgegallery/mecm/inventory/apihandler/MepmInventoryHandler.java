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
import org.edgegallery.mecm.inventory.apihandler.dto.MepmDto;
import org.edgegallery.mecm.inventory.model.Mepm;
import org.edgegallery.mecm.inventory.service.InventoryServiceImpl;
import org.edgegallery.mecm.inventory.service.repository.MepmRepository;
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
@RestSchema(schemaId = "inventory-mepm")
@Api(value = "Inventory mepm Inventory api system")
@Validated
@RequestMapping("/inventory/v1")
@RestController
public class MepmInventoryHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MepmInventoryHandler.class);
    @Autowired
    private InventoryServiceImpl service;
    @Autowired
    private MepmRepository mepmRepository;

    /**
     * Adds a new Mec Platform Manager record entry into the Inventory.
     *
     * @param mepmDto Mec Platform Manager record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Adds new mepm record", response = String.class)
    @PostMapping(path = "/mepms", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('MECM_ADMIN')")
    public ResponseEntity<Status> addMepmRecord(
            @Valid @ApiParam(value = "mepm inventory information") @RequestBody MepmDto mepmDto) {
        Mepm mepm = InventoryUtilities.getModelMapper().map(mepmDto, Mepm.class);
        mepm.setMepmId(mepmDto.getMepmIp());
        LOGGER.info("mepm object------------------{}", mepm.toString());
        Status status = service.addRecord(mepm, mepmRepository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Updates an exiting Mec Platform Manager record in the Inventory matching the given tenant ID & Mepm IP.
     *
     * @param mepmIp  Mec Platform Manager IP
     * @param mepmDto Mec Platform Manager record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Updates existing Mepm record", response = String.class)
    @PutMapping(path = "/mepms/{mepm_ip}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('MECM_ADMIN')")
    public ResponseEntity<Status> updateMepmRecord(
            @ApiParam(value = "Mepm IP") @PathVariable("mepm_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String mepmIp,
            @Valid @ApiParam(value = "mepm inventory information") @RequestBody MepmDto mepmDto) {
        if (!mepmIp.equals(mepmDto.getMepmIp())) {
            LOGGER.error("Input validation failed for mepm IP, value in body {}, value in url {}",
                    mepmDto.getMepmIp(), mepmIp);
            throw new IllegalArgumentException("mepm IP in body and url is different");
        }
        Mepm mepm = InventoryUtilities.getModelMapper().map(mepmDto, Mepm.class);
        mepm.setMepmId(mepmIp);
        Status status = service.updateRecord(mepm, mepmRepository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Retrieves all Mec Platform Manager records.
     *
     * @return Mec Platform Manager records & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves all mepm records", response = List.class)
    @GetMapping(path = "/mepms", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT') || hasRole('MECM_ADMIN') || hasRole('MECM_GUEST')")
    public ResponseEntity<List<MepmDto>> getAllMepmRecords() {
        List<Mepm> mepms = service.getTenantRecords(null, mepmRepository);
        List<MepmDto> mepmDtos = new LinkedList<>();
        for (Mepm mepm : mepms) {
            MepmDto mepmDto = InventoryUtilities.getModelMapper().map(mepm, MepmDto.class);
            mepmDtos.add(mepmDto);
        }
        return new ResponseEntity<>(mepmDtos, HttpStatus.OK);
    }

    /**
     * Retrieves a specific Mec Platform Manager record in the Inventory matching the given tenant ID & mepm IP.
     *
     * @param mepmIp Mec Platform Manager IP
     * @return mepm record & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves mepm record", response = MepmDto.class)
    @GetMapping(path = "/mepms/{mepm_ip}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT') || hasRole('MECM_ADMIN') || hasRole('MECM_GUEST')")
    public ResponseEntity<MepmDto> getMepmRecord(
            @ApiParam(value = "mepm IP") @PathVariable("mepm_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String mepmIp) {
        Mepm mepm = service.getRecord(mepmIp, mepmRepository);
        MepmDto mepmDto = InventoryUtilities.getModelMapper().map(mepm, MepmDto.class);
        return new ResponseEntity<>(mepmDto, HttpStatus.OK);
    }

    /**
     * Deletes all records for a given tenant.
     *
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes all mepm records", response = String.class)
    @DeleteMapping(path = "/mepms", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('MECM_ADMIN')")
    public ResponseEntity<Status> deleteAllMepmRecords() {
        Status status = service.deleteTenantRecords(null, mepmRepository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Deletes a specific mepm record in the Inventory matching the given tenant ID & mepm IP.
     *
     * @param mepmIp Mec Platform Manager IP
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes mepm record", response = String.class)
    @DeleteMapping(path = "/mepms/{mepm_ip}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('MECM_ADMIN')")
    public ResponseEntity<Status> deleteMepmRecord(
            @ApiParam(value = "mepm IP") @PathVariable("mepm_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String mepmIp) {
        Status status = service.deleteRecord(mepmIp, mepmRepository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
