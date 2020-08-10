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

package org.edgegallery.mecm.esr.apihandler;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.edgegallery.mecm.esr.model.MecHost;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * MEC host registry API handler.
 */
@Api(value = "ESR MEC host registry api system")
@Validated
@RequestMapping("/esr/v1")
@RestController
public class MecHostRegistryHandler {

    // TODO pre authorization & parameter validations

    /**
     * Adds a new MEC host record entry into the registry.
     *
     * @param tenantId tenant ID
     * @param mecHost mec host record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Adds new MEC host record", response = String.class)
    @RequestMapping(path = "/tenants/{tenant_id}/mechost",
            method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> addMecHostRecord(@PathVariable("tenant_id") String tenantId,
                                                   @RequestBody MecHost mecHost) {
        // TODO: implementation
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Updates an exiting MEC host record in the registry matching the given tenant ID & mec host IP.
     *
     * @param tenantId tenant ID
     * @param mecHostIp mec host IP
     * @param mecHost mec host record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Updates existing MEC host record", response = String.class)
    @RequestMapping(path = "/tenants/{tenant_id}/mechosts/{mechost_ip}",
            method = RequestMethod.PUT, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> updateMecHostRecord(@PathVariable("tenant_id") String tenantId,
                                     @PathVariable("mechost_ip") String mecHostIp, @RequestBody MecHost mecHost) {
        // TODO: implementation
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Retrieves all MEC host records
     *
     * @param tenantId tenant ID
     * @return MEC host records & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves all MEC host records", response = List.class)
    @RequestMapping(path = "/tenants/{tenant_id}/mechosts",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MecHost>> getAllMecHostRecords(@PathVariable("tenant_id") String tenantId) {
        // TODO: implementation
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Retrieves a specific MEC host record in the registry matching the given tenant ID & mec host IP.
     *
     * @param tenantId tenant ID
     * @param mecHostIp MEC host IP
     * @return MEC host record & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves MEC host record", response = MecHost.class)
    @RequestMapping(path = "/tenants/{tenant_id}/mechosts/{mechost_ip}",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MecHost> getMecHostRecord(@PathVariable("tenant_id") String tenantId,
                                  @PathVariable("mechost_ip") String mecHostIp) {
        // TODO: implementation
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Deletes all records for a given tenant.
     *
     * @param tenantId tenant ID
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes all MEC host records", response = String.class)
    @RequestMapping(path = "/tenant/{tenant_id}/mechosts",
            method = RequestMethod.DELETE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteAllMecHostRecords(@PathVariable("tenant_id") String tenantId) {
        // TODO: implementation
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Deletes a specific MEC host record in the registry matching the given tenant ID & mec host IP.
     *
     * @param tenantId tenant ID
     * @param mecHostIp MEC host IP
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes MEC host record", response = String.class)
    @RequestMapping(path = "/tenant/{tenant_id}/mechosts/{mechost_ip}",
            method = RequestMethod.DELETE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteMecHostRecord(@PathVariable("tenant_id") String tenantId,
                                     @PathVariable("mechost_ip") String mecHostIp) {
        // TODO: implementation
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
