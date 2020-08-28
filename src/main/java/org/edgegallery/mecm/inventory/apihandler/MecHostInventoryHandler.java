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
import org.edgegallery.mecm.inventory.apihandler.dto.MecHostDto;
import org.edgegallery.mecm.inventory.model.MecHost;
import org.edgegallery.mecm.inventory.service.InventoryServiceImpl;
import org.edgegallery.mecm.inventory.service.repository.MecHostRepository;
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
 * MEC host Inventory API handler.
 */
@Api(value = "Inventory MEC host Inventory api system")
@Validated
@RequestMapping("/inventory/v1")
@RestController
public class MecHostInventoryHandler {

    @Autowired
    private InventoryServiceImpl service;

    @Autowired
    private MecHostRepository repository;

    /**
     * Adds a new MEC host record entry into the Inventory.
     *
     * @param tenantId   tenant ID
     * @param mecHostDto mec host record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Adds new MEC host record", response = String.class)
    @PostMapping(path = "/tenants/{tenant_id}/mechosts", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> addMecHostRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id") String tenantId,
            @Valid @ApiParam(value = "mechost inventory information") @RequestBody MecHostDto mecHostDto) {
        ModelMapper mapper = new ModelMapper();
        MecHost host = mapper.map(mecHostDto, MecHost.class);
        host.setTenantId(tenantId);
        host.setMechostId(mecHostDto.getMecHostIp() + "_" + tenantId);
        String status = service.addRecord(host, repository);
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
    @PutMapping(path = "/tenants/{tenant_id}/mechosts/{mechost_ip}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> updateMecHostRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id") String tenantId,
            @ApiParam(value = "mechost IP") @PathVariable("mechost_ip") String mecHostIp,
            @Valid @ApiParam(value = "mechost inventory information") @RequestBody MecHostDto mecHostDto) {
        ModelMapper mapper = new ModelMapper();
        MecHost host = mapper.map(mecHostDto, MecHost.class);
        host.setTenantId(tenantId);
        host.setMechostId(mecHostIp + "_" + tenantId);
        String status = service.updateRecord(host, repository);
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
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id") String tenantId) {
        List<MecHost> mecHosts = service.getTenantRecords(tenantId, repository);
        List<MecHostDto> mecHostDtos = new LinkedList<>();
        for (MecHost host : mecHosts) {
            ModelMapper mapper = new ModelMapper();
            MecHostDto mecHostDto = mapper.map(host, MecHostDto.class);
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
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id") String tenantId,
            @ApiParam(value = "mechost IP") @PathVariable("mechost_ip") String mecHostIp) {
        Optional<MecHost> record = service.getRecord(mecHostIp + "_" + tenantId, repository);
        if (record.isPresent()) {
            MecHost host = record.get();
            ModelMapper mapper = new ModelMapper();
            MecHostDto mecHostDto = mapper.map(host, MecHostDto.class);
            return new ResponseEntity<>(mecHostDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Deletes all records for a given tenant.
     *
     * @param tenantId tenant ID
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes all MEC host records", response = String.class)
    @DeleteMapping(path = "/tenant/{tenant_id}/mechosts", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteAllMecHostRecords(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id") String tenantId) {
        String status = service.deleteTenantRecords(tenantId, repository);
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
    @DeleteMapping(path = "/tenant/{tenant_id}/mechosts/{mechost_ip}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteMecHostRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id") String tenantId,
            @ApiParam(value = "mechost IP") @PathVariable("mechost_ip") String mecHostIp) {
        String status = service.deleteRecord(mecHostIp + "_" + tenantId, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
