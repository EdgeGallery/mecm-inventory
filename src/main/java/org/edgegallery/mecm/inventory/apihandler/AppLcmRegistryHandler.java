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
import java.util.List;
import org.edgegallery.mecm.inventory.model.AppLcm;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Application LCM registry API handler.
 */
@Api(value = "Inventory applcm registry api system")
@Validated
@RequestMapping("/inventory/v1")
@RestController
public class AppLcmRegistryHandler {

    // TODO pre authorization & parameter validations

    /**
     * Adds a new application LCM record entry into the registry.
     *
     * @param tenantId tenant ID
     * @param appLcm   application lifecycle manager record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Adds new application LCM record", response = String.class)
    @RequestMapping(path = "/tenants/{tenant_id}/applcms",
            method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> addAppLcmRecord(@PathVariable("tenant_id") String tenantId,
                                                  @RequestBody AppLcm appLcm) {
        // TODO: implementation
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Updates an exiting application LCM record in the registry matching the given tenant ID & appLCM IP.
     *
     * @param tenantId tenant ID
     * @param appLcmIp application LCM IP
     * @param appLcm   application lifecycle manager record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Updates existing application LCM record", response = String.class)
    @RequestMapping(path = "/tenants/{tenant_id}/applcms/{applcm_ip}",
            method = RequestMethod.PUT, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> updateAppLcmRecord(@PathVariable("tenant_id") String tenantId,
                                                     @PathVariable("applcm_ip") String appLcmIp,
                                                     @RequestBody AppLcm appLcm) {
        // TODO: implementation
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Retrieves all application LCM records.
     *
     * @param tenantId tenant ID
     * @return application LCM records & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves all application LCM records", response = List.class)
    @RequestMapping(path = "/tenants/{tenant_id}/applcms",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppLcm>> getAllAppLcmRecords(@PathVariable("tenant_id") String tenantId) {
        // TODO: implementation
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Retrieves a specific application LCM record in the registry matching the given tenant ID & appLCM IP.
     *
     * @param tenantId tenant ID
     * @param appLcmIp application LCM IP
     * @return application LCM record & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves application LCM record", response = AppLcm.class)
    @RequestMapping(path = "/tenants/{tenant_id}/applcms/{applcm_ip}",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppLcm> getAppLcmRecord(@PathVariable("tenant_id") String tenantId,
                                                  @PathVariable("applcm_ip") String appLcmIp) {
        // TODO: implementation
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Deletes all records for a given tenant.
     *
     * @param tenantId tenant ID
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes all application LCM records", response = String.class)
    @RequestMapping(path = "/tenants/{tenant_id}/applcms",
            method = RequestMethod.DELETE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteAllAppLcmRecords(@PathVariable("tenant_id") String tenantId) {
        // TODO: implementation
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Deletes a specific application LCM record in the registry matching the given tenant ID & appLCM IP.
     *
     * @param tenantId tenant ID
     * @param appLcmIp application LCM IP
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes application LCM record", response = String.class)
    @RequestMapping(path = "/tenants/{tenant_id}/applcms/{applcm_ip}",
            method = RequestMethod.DELETE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteAppLcmRecord(@PathVariable("tenant_id") String tenantId,
                                                     @PathVariable("applcm_ip") String appLcmIp) {
        // TODO: implementation
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Uploads K8s configuration file to applcm.
     * @param tenantId tenant ID
     * @param appLcmIp applcm IP
     * @param hostIp edge host IP
     * @param file configuration file
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Upload K8s configuration file to applcm", response = String.class)
    @RequestMapping(path = "/tenants/{tenant_id}/applcms/{applcm_ip}/host/{hostIp}/k8sconfig",
            method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String>  uploadConfigFile(@PathVariable("tenant_id") String tenantId,
                                                    @PathVariable("applcm_ip") String appLcmIp,
                                                    @PathVariable("hostIp") String hostIp,
                                                    @RequestParam("file") MultipartFile file) {

        //TODO: implementation
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Deletes K8s configuration file from applcm.
     * @param tenantId tenant ID
     * @param appLcmIp applcm IP
     * @param hostIp edge host IP
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes K8s configuration file from applcm", response = String.class)
    @RequestMapping(path = "/tenants/{tenant_id}/applcms/{applcm_ip}/host/{hostIp}/k8sconfig",
            method = RequestMethod.DELETE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String>  deleteConfigFile(@PathVariable("tenant_id") String tenantId,
                                                    @PathVariable("applcm_ip") String appLcmIp,
                                                    @PathVariable("hostIp") String hostIp) {

        //TODO: implementation
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
