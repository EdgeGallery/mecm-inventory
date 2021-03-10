/*
 *  Copyright 2021 Huawei Technologies Co., Ltd.
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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.edgegallery.mecm.inventory.apihandler.dto.AppdRuleConfigDto;
import org.edgegallery.mecm.inventory.apihandler.dto.AppdRuleDeletedDto;
import org.edgegallery.mecm.inventory.apihandler.dto.SyncDeletedRulesDto;
import org.edgegallery.mecm.inventory.apihandler.dto.SyncUpdatedRulesDto;
import org.edgegallery.mecm.inventory.model.AppRuleManager;
import org.edgegallery.mecm.inventory.service.InventoryServiceImpl;
import org.edgegallery.mecm.inventory.service.SyncServiceImpl;
import org.edgegallery.mecm.inventory.service.repository.AppDRuleRepository;
import org.edgegallery.mecm.inventory.service.repository.AppRuleManagerRepository;
import org.edgegallery.mecm.inventory.utils.Constants;
import org.edgegallery.mecm.inventory.utils.InventoryUtilities;
import org.edgegallery.mecm.inventory.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MEPM synchronization API handler.
 */
@Api(value = "MEPM synchronization API system")
@Validated
@RequestMapping("/inventory/v1")
@RestController
public class MepmSyncHandler {

    @Autowired
    private SyncServiceImpl syncService;

    @Autowired
    private InventoryServiceImpl service;

    @Autowired
    private AppDRuleRepository appDRuleRepository;

    @Autowired
    private AppRuleManagerRepository ruleManagerRepository;

    @Value("${server.ssl.enabled:false}")
    private String isSslEnabled;

    /**
     * Synchronizes application rules from a given edge to center.
     *
     * @param tenantId    tenant identifier
     * @param accessToken access token
     * @param mepmIp      mepm IP
     * @return success if ok, partial failure is something failed
     */
    @ApiOperation(value = "Synchronize application rule record", response = String.class)
    @GetMapping(path = "/tenants/{tenant_id}/mepms/{mepm_ip}/apprule/sync", produces =
            MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT') || hasRole('MECM_ADMIN')")
    public ResponseEntity<Status> syncAppdRecords(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.TENANT_ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = "access token") @RequestHeader("access_token") String accessToken,
            @ApiParam(value = "mepm ip") @PathVariable("mepm_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String mepmIp) {
        Status finalStatus = new Status("Success");
        // Synchronize added & updated records
        // Get dto records
        ResponseEntity<SyncUpdatedRulesDto> updateResponse =
                syncService.syncRecords(getAppRuleMgrSyncUrl(mepmIp, tenantId) + "/sync_updated",
                        SyncUpdatedRulesDto.class, accessToken);
        SyncUpdatedRulesDto syncUpdatedRulesDto = updateResponse.getBody();
        // Update table
        if (syncUpdatedRulesDto != null) {
            for (AppdRuleConfigDto updatedRecord : syncUpdatedRulesDto.getAppdRuleUpdatedRecs()) {
                Status updateStatus = service.addRecord(
                        InventoryUtilities.getAppdRule(tenantId, updatedRecord.getAppInstanceId(), updatedRecord),
                        appDRuleRepository);
                if (!updateStatus.getResponse().equals("Saved")) {
                    finalStatus.setResponse("Partial Failure");
                }
            }
        }

        // Synchronize deleted records
        // Get dto records
        ResponseEntity<SyncDeletedRulesDto> deleteResponse =
                syncService.syncRecords(getAppRuleMgrSyncUrl(mepmIp, tenantId) + "/sync_deleted",
                        SyncDeletedRulesDto.class, accessToken);
        SyncDeletedRulesDto syncDeletedRulesDto = deleteResponse.getBody();
        // Update table
        if (syncDeletedRulesDto != null) {
            for (AppdRuleDeletedDto deletedRecord : syncDeletedRulesDto.getAppdRuleDeletedRecs()) {
                Status deleteStatus = service.deleteRecord(tenantId + deletedRecord.getAppInstanceId(),
                        appDRuleRepository);
                if (!deleteStatus.getResponse().equals("Deleted")) {
                    finalStatus.setResponse("Partial Failure");
                }
            }
        }
        return new ResponseEntity<>(finalStatus, HttpStatus.OK);
    }

    private String getAppRuleMgrSyncUrl(String mepmIp, String tenantId) {
        AppRuleManager ruleManager = service.getRecord(mepmIp, ruleManagerRepository);
        String rulePort = ruleManager.getAppRulePort();
        String url;
        if (Boolean.parseBoolean(isSslEnabled)) {
            url = "https://" + mepmIp + ":" + rulePort + "/apprulemgr/v1/configuration/tenants/" + tenantId
                    + "/app_instances/appd_configuration";
        } else {
            url = "http://" + mepmIp + ":" + rulePort + "/apprulemgr/v1/configuration/tenants/" + tenantId
                    + "/app_instances/appd_configuration";
        }
        return url;
    }
}
