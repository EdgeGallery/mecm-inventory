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
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.edgegallery.mecm.inventory.apihandler.dto.AppdRuleDto;
import org.edgegallery.mecm.inventory.utils.Constants;
import org.edgegallery.mecm.inventory.utils.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * AppD Rule Inventory API handler.
 */
@Api(value = "Inventory AppD Rule Inventory api system")
@Validated
@RequestMapping("/inventory/v1")
@RestController
public class AppdRuleInventoryHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppdRuleInventoryHandler.class);

    /**
     * Adds a new APPDRule record entry into the Inventory.
     *
     * @param tenantId   tenant ID
     * @param appInstanceId  appInstance ID
     * @param appDRuleDto appDRule record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Adds AppD rule record", response = String.class)
    @PostMapping(path = "/tenants/{tenant_id}/app_instances/{app_instance_id}/appd_configuration", produces =
            MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT')")
    public ResponseEntity<Status> addAppdRuleRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.TENANT_ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = "app instance identifier") @PathVariable("app_instance_id")
            @Pattern(regexp = Constants.APP_INST_ID_REGX) @Size(max = 64) String appInstanceId,
            @Valid @ApiParam(value = "appD rule inventory information") @RequestBody AppdRuleDto appDRuleDto) {
        // TODO: to be implemented
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Updates an exiting APPDRule record in the Inventory matching the given tenant ID & app instance ID.
     *
     * @param tenantId   tenant ID
     * @param appInstanceId  app instance Id
     * @param appDRuleDto appDRule record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Updates existing appDRule record", response = String.class)
    @PutMapping(path = "/tenants/{tenant_id}/app_instances/{app_instance_id}/appd_configuration",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT')")
    public ResponseEntity<Status> updateAppdRuleRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.TENANT_ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = "app instance identifier") @PathVariable("app_instance_id")
            @Pattern(regexp = Constants.APP_INST_ID_REGX) @Size(max = 64) String appInstanceId,
            @Valid @ApiParam(value = "appD rule inventory information") @RequestBody AppdRuleDto appDRuleDto) {
        // TODO: to be implemented
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Retrieves appDRule records for given tenant ID and app instance ID.
     *
     * @param tenantId tenant ID
     * @param appInstanceId  app instance Id
     * @return appDRule records & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves appDRule records for given tenent ID and app instance ID", response =
            List.class)
    @GetMapping(path = "/tenants/{tenant_id}/app_instances/{app_instance_id}/appd_configuration",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT')")
    public ResponseEntity<AppdRuleDto> getAppdRuleRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.TENANT_ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = "app instance identifier") @PathVariable("app_instance_id")
            @Pattern(regexp = Constants.APP_INST_ID_REGX) @Size(max = 64) String appInstanceId) {
        // TODO: to be implemented
        AppdRuleDto appDRuleDto = new AppdRuleDto();
        return new ResponseEntity<>(appDRuleDto, HttpStatus.OK);
    }

    /**
     * Deletes appDRule records for a given tenant and app instance.
     *
     * @param tenantId   tenant ID
     * @param appInstanceId  app instance Id
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes appDRule records", response = String.class)
    @DeleteMapping(path = "/tenants/{tenant_id}/app_instances/{app_instance_id}/appd_configuration",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT')")
    public ResponseEntity<Status> deleteAllAppdRecords(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.TENANT_ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = "app instance identifier") @PathVariable("app_instance_id")
            @Pattern(regexp = Constants.APP_INST_ID_REGX) @Size(max = 64) String appInstanceId) {
        // TODO: to be implemented
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
