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
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.edgegallery.mecm.inventory.apihandler.dto.AppdRuleConfigDto;
import org.edgegallery.mecm.inventory.model.AppdRule;
import org.edgegallery.mecm.inventory.service.InventoryServiceImpl;
import org.edgegallery.mecm.inventory.service.repository.AppDRuleRepository;
import org.edgegallery.mecm.inventory.utils.Constants;
import org.edgegallery.mecm.inventory.utils.InventoryUtilities;
import org.edgegallery.mecm.inventory.utils.Status;
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
 * AppD Rule Inventory API handler.
 */
@RestSchema(schemaId = "inventory-apprule")
@Api(value = "Inventory AppD Rule Inventory api system")
@Validated
@RequestMapping("/inventory/v1")
@RestController
public class AppdRuleInventoryHandler {

    private static final String APPD_INV_INFO = "appD rule inventory information";
    private static final String APP_INST_IDENTIFIER = "app instance identifier";
    private static final String APP_INSTANCE_ID = "app_instance_id";
    private static final String TENANT_IDENTIFIER = "tenant identifier";
    private static final String TENANT_ID = "tenant_id";


    @Autowired
    private InventoryServiceImpl service;

    @Autowired
    private AppDRuleRepository repository;

    /**
     * Adds a new APPDRule record entry into the Inventory.
     *
     * @param tenantId          tenant ID
     * @param appInstanceId     appInstance ID
     * @param appDRuleConfigDto appDRule record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Adds AppD rule record", response = String.class)
    @PostMapping(path = "/tenants/{tenant_id}/app_instances/{app_instance_id}/appd_configuration", produces =
            MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT') || hasRole('MECM_ADMIN')")
    public ResponseEntity<Status> addAppdRuleRecord(
            @ApiParam(value = TENANT_IDENTIFIER) @PathVariable(TENANT_ID)
            @Pattern(regexp = Constants.TENANT_ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = APP_INST_IDENTIFIER) @PathVariable(APP_INSTANCE_ID)
            @Pattern(regexp = Constants.APP_INST_ID_REGX) @Size(max = 64) String appInstanceId,
            @Valid @ApiParam(value = APPD_INV_INFO)
            @RequestBody AppdRuleConfigDto appDRuleConfigDto) {
        Status status = service.addRecord(InventoryUtilities.getAppdRule(tenantId, appInstanceId, appDRuleConfigDto),
                repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Updates an exiting APPDRule record in the Inventory matching the given tenant ID & app instance ID.
     *
     * @param tenantId          tenant ID
     * @param appInstanceId     app instance Id
     * @param appDRuleConfigDto appDRule record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Updates existing appDRule record", response = String.class)
    @PutMapping(path = "/tenants/{tenant_id}/app_instances/{app_instance_id}/appd_configuration",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT') || hasRole('MECM_ADMIN')")
    public ResponseEntity<Status> updateAppdRuleRecord(
            @ApiParam(value = TENANT_IDENTIFIER) @PathVariable(TENANT_ID)
            @Pattern(regexp = Constants.TENANT_ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = APP_INST_IDENTIFIER) @PathVariable(APP_INSTANCE_ID)
            @Pattern(regexp = Constants.APP_INST_ID_REGX) @Size(max = 64) String appInstanceId,
            @Valid @ApiParam(value = APPD_INV_INFO)
            @RequestBody AppdRuleConfigDto appDRuleConfigDto) {
        Status status = service.updateRecord(InventoryUtilities.getAppdRule(tenantId, appInstanceId,
                appDRuleConfigDto), repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Retrieves appDRule records for given tenant ID and app instance ID.
     *
     * @param tenantId      tenant ID
     * @param appInstanceId app instance Id
     * @return appDRule records & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves appDRule records for given tenent ID and app instance ID", response =
            List.class)
    @GetMapping(path = "/tenants/{tenant_id}/app_instances/{app_instance_id}/appd_configuration",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT') || hasRole('MECM_GUEST') || hasRole('MECM_ADMIN')")
    public ResponseEntity<AppdRuleConfigDto> getAppdRuleRecord(
            @ApiParam(value = TENANT_IDENTIFIER) @PathVariable(TENANT_ID)
            @Pattern(regexp = Constants.TENANT_ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = APP_INST_IDENTIFIER) @PathVariable(APP_INSTANCE_ID)
            @Pattern(regexp = Constants.APP_INST_ID_REGX) @Size(max = 64) String appInstanceId) {
        AppdRule appDRule = service.getRecord(tenantId + appInstanceId, repository);
        AppdRuleConfigDto appDRuleConfigDto = InventoryUtilities.getModelMapper()
                .map(appDRule, AppdRuleConfigDto.class);
        return new ResponseEntity<>(appDRuleConfigDto, HttpStatus.OK);
    }

    /**
     * Deletes appDRule records for a given tenant and app instance.
     *
     * @param tenantId      tenant ID
     * @param appInstanceId app instance Id
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes appDRule records", response = String.class)
    @DeleteMapping(path = "/tenants/{tenant_id}/app_instances/{app_instance_id}/appd_configuration",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT') || hasRole('MECM_ADMIN')")
    public ResponseEntity<Status> deleteAllAppdRecords(
            @ApiParam(value = TENANT_IDENTIFIER) @PathVariable(TENANT_ID)
            @Pattern(regexp = Constants.TENANT_ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = APP_INST_IDENTIFIER) @PathVariable(APP_INSTANCE_ID)
            @Pattern(regexp = Constants.APP_INST_ID_REGX) @Size(max = 64) String appInstanceId) {
        Status status = service.deleteRecord(tenantId + appInstanceId, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
