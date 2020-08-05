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

package org.mec.mecm.esr.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mec.mecm.esr.model.MecHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MEC host inventory controller.
 */
@Api(value = "MEC host inventory api system")
@Validated
@RestController
public class MecHostInventoryController {
    private final static Logger logger = LoggerFactory.getLogger(MecHostInventoryController.class);

    // TODO pre authorization & parameter validations

    /**
     * Adds a new MEC host record entry into the inventory.
     *
     * @param tenantId tenant ID
     * @param mecHost mec host record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Adds new MEC host record", response = String.class)
    @RequestMapping(path = "/esr/v1/tenant/{tenant_id}/mechost",
            method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public String addMecHostRecord(@PathVariable("tenant_id") String tenantId, @RequestBody MecHost mecHost) {
        // TODO: implementation
        return null;
    }

    /**
     * Updates an exiting MEC host record in the inventory matching the given tenant ID & mec host IP.
     *
     * @param tenantId tenant ID
     * @param mecHostIp mec host IP
     * @param mecHost mec host record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Updates existing MEC host record", response = String.class)
    @RequestMapping(path = "/esr/v1/tenant/{tenant_id}/mechosts/{mechost_ip}",
            method = RequestMethod.PUT,produces = MediaType.APPLICATION_JSON_VALUE)
    public String updateMecHostRecord(@PathVariable("tenant_id") String tenantId,
                                     @PathVariable("mechost_ip") String mecHostIp, @RequestBody MecHost mecHost) {
        // TODO: implementation
        return null;
    }

    /**
     * Retrieves all MEC host records
     *
     * @param tenantId tenant ID
     * @return MEC host records & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves all MEC host records", response = List.class)
    @RequestMapping(path = "/tenant/{tenant_id}/mechosts",
            method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MecHost> getAllMecHostRecords(@PathVariable("tenant_id") String tenantId) {
        // TODO: implementation
        return null;
    }

    /**
     * Retrieves a specific MEC host record in the inventory matching the given tenant ID & mec host IP.
     *
     * @param tenantId tenant ID
     * @param mecHostIp MEC host IP
     * @return MEC host record & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves MEC host record", response = MecHost.class)
    @RequestMapping(path = "/tenant/{tenant_id}/mechosts/{mechost_ip}",
            method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public MecHost getMecHostRecord(@PathVariable("tenant_id") String tenantId,
                                  @PathVariable("mechost_ip") String mecHostIp) {
        // TODO: implementation
        return null;
    }

    /**
     * Deletes all records for a given tenant.
     *
     * @param tenantId tenant ID
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes all MEC host records", response = String.class)
    @RequestMapping(path = "/tenant/{tenant_id}/mechosts",
            method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteAllMecHostRecords(@PathVariable("tenant_id") String tenantId) {
        // TODO: implementation
        return null;
    }

    /**
     * Deletes a specific MEC host record in the inventory matching the given tenant ID & mec host IP.
     *
     * @param tenantId tenant ID
     * @param mecHostIp MEC host IP
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes MEC host record", response = String.class)
    @RequestMapping(path = "/tenant/{tenant_id}/mechosts/{mechost_ip}",
            method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteMecHostRecord(@PathVariable("tenant_id") String tenantId,
                                     @PathVariable("mechost_ip") String mecHostIp) {
        // TODO: implementation
        return null;
    }
}
