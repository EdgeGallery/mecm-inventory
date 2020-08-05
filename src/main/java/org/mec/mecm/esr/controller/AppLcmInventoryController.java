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
import org.mec.mecm.esr.model.AppLCM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Application LCM inventory controller.
 */
@Api(value = "ESR applcm inventory api system")
@Validated
@RestController
public class AppLcmInventoryController {
    private final static Logger logger = LoggerFactory.getLogger(AppLcmInventoryController.class);

    // TODO pre authorization & parameter validations

    /**
     * Adds a new application LCM record entry into the inventory.
     *
     * @param tenantId tenant ID
     * @param appLcm application lifecycle manager record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Adds new application LCM record", response = String.class)
    @RequestMapping(path = "/esr/v1/tenant/{tenant_id}/applcm",
            method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public String addAppLcmRecord(@PathVariable("tenant_id") String tenantId, @RequestBody AppLCM appLcm) {
        // TODO: implementation
        return null;
    }

    /**
     * Updates an exiting application LCM record in the inventory matching the given tenant ID & appLCM IP.
     *
     * @param tenantId tenant ID
     * @param appLcmIp application LCM IP
     * @param appLcm application lifecycle manager record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Updates existing application LCM record", response = String.class)
    @RequestMapping(path = "/esr/v1/tenant/{tenant_id}/applcms/{applcm_ip}",
            method = RequestMethod.PUT,produces = MediaType.APPLICATION_JSON_VALUE)
    public String updateAppLCMRecord(@PathVariable("tenant_id") String tenantId,
                                     @PathVariable("applcm_ip") String appLcmIp, @RequestBody AppLCM appLcm) {
        // TODO: implementation
        return null;
    }

    /**
     * Retrieves all application LCM records
     *
     * @param tenantId tenant ID
     * @return application LCM records & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves all application LCM records", response = List.class)
    @RequestMapping(path = "/tenant/{tenant_id}/applcms",
            method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AppLCM> getAllAppLcmRecords(@PathVariable("tenant_id") String tenantId) {
        // TODO: implementation
        return null;
    }

    /**
     * Retrieves a specific application LCM record in the inventory matching the given tenant ID & appLCM IP.
     *
     * @param tenantId tenant ID
     * @param appLcmIp application LCM IP
     * @return application LCM record & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves application LCM record", response = AppLCM.class)
    @RequestMapping(path = "/tenant/{tenant_id}/applcms/{applcm_ip}",
            method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public AppLCM getAppLcmRecord(@PathVariable("tenant_id") String tenantId,
                                  @PathVariable("applcm_ip") String appLcmIp) {
        // TODO: implementation
        return null;
    }

    /**
     * Deletes all records for a given tenant.
     *
     * @param tenantId tenant ID
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes all application LCM records", response = String.class)
    @RequestMapping(path = "/tenant/{tenant_id}/applcms",
            method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteAllAppLcmRecords(@PathVariable("tenant_id") String tenantId) {
        // TODO: implementation
        return null;
    }

    /**
     * Deletes a specific application LCM record in the inventory matching the given tenant ID & appLCM IP.
     *
     * @param tenantId tenant ID
     * @param appLcmIp application LCM IP
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes application LCM record", response = String.class)
    @RequestMapping(path = "/tenant/{tenant_id}/applcms/{applcm_ip}",
            method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteAppLcmRecord(@PathVariable("tenant_id") String tenantId,
                                     @PathVariable("applcm_ip") String appLcmIp) {
        // TODO: implementation
        return null;
    }
}
