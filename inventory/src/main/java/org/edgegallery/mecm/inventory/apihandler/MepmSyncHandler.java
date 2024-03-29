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
import java.util.HashSet;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.edgegallery.mecm.inventory.apihandler.dto.AppdRuleConfigDto;
import org.edgegallery.mecm.inventory.apihandler.dto.AppdRuleDeletedDto;
import org.edgegallery.mecm.inventory.apihandler.dto.MecHostDeletedDto;
import org.edgegallery.mecm.inventory.apihandler.dto.MecHostDto;
import org.edgegallery.mecm.inventory.apihandler.dto.SyncDeletedMecHostDto;
import org.edgegallery.mecm.inventory.apihandler.dto.SyncDeletedRulesDto;
import org.edgegallery.mecm.inventory.apihandler.dto.SyncUpdatedMecHostDto;
import org.edgegallery.mecm.inventory.apihandler.dto.SyncUpdatedRulesDto;
import org.edgegallery.mecm.inventory.model.MecHost;
import org.edgegallery.mecm.inventory.model.Mepm;
import org.edgegallery.mecm.inventory.service.InventoryServiceImpl;
import org.edgegallery.mecm.inventory.service.impl.RestServiceImpl;
import org.edgegallery.mecm.inventory.service.repository.AppDRuleRepository;
import org.edgegallery.mecm.inventory.service.repository.MecHostRepository;
import org.edgegallery.mecm.inventory.service.repository.MepmRepository;
import org.edgegallery.mecm.inventory.utils.Constants;
import org.edgegallery.mecm.inventory.utils.InventoryUtilities;
import org.edgegallery.mecm.inventory.utils.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RestSchema(schemaId = "inventory-mepmsync")
@Api(value = "MEPM synchronization API system")
@Validated
@RequestMapping("/inventory/v1")
@RestController
public class MepmSyncHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MepmSyncHandler.class);
    private static final String PARTIAL_FAILURE = "Partial Failure";
    private static final String APPRULE_TENANTS = "/apprulemgr/v1/tenants/";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String ACCESSTOKEN = "access token";

    @Autowired
    private RestServiceImpl syncService;
    @Autowired
    private InventoryServiceImpl service;
    @Autowired
    private AppDRuleRepository appDRuleRepository;
    @Autowired
    private MecHostRepository hostRepository;
    @Autowired
    private MepmRepository mepmRepository;
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
            @ApiParam(value = ACCESSTOKEN) @RequestHeader(ACCESS_TOKEN) String accessToken,
            @ApiParam(value = "mepm ip") @PathVariable("mepm_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String mepmIp) {
        Status finalStatus = new Status("Success");
        // Synchronize added & updated records
        // Get dto records
        ResponseEntity<SyncUpdatedRulesDto> updateResponse =
                syncService.syncRecords(getMepmAppRuleSyncUrl(mepmIp, tenantId) + "/sync_updated",
                        SyncUpdatedRulesDto.class, accessToken);
        SyncUpdatedRulesDto syncUpdatedRulesDto = updateResponse.getBody();
        updateAppdRuleRecord(syncUpdatedRulesDto, tenantId, finalStatus);
        // Synchronize deleted records
        // Get dto records
        ResponseEntity<SyncDeletedRulesDto> deleteResponse =
                syncService.syncRecords(getMepmAppRuleSyncUrl(mepmIp, tenantId) + "/sync_deleted",
                        SyncDeletedRulesDto.class, accessToken);
        SyncDeletedRulesDto syncDeletedRulesDto = deleteResponse.getBody();
        // Update table
        if (syncDeletedRulesDto != null && syncDeletedRulesDto.getAppdRuleDeletedRecs() != null) {
            for (AppdRuleDeletedDto deletedRecord : syncDeletedRulesDto.getAppdRuleDeletedRecs()) {
                Status deleteStatus = service.deleteRecord(tenantId + deletedRecord.getAppInstanceId(),
                        appDRuleRepository);
                if (!deleteStatus.getResponse().equals("Deleted")) {
                    finalStatus.setResponse(PARTIAL_FAILURE);
                }
            }
        }
        return new ResponseEntity<>(finalStatus, HttpStatus.OK);
    }

    private void updateAppdRuleRecord(SyncUpdatedRulesDto syncUpdatedRulesDto, String tenantId, Status finalStatus) {
        // Update table
        if (syncUpdatedRulesDto != null && syncUpdatedRulesDto.getAppdRuleUpdatedRecs() != null) {
            for (AppdRuleConfigDto updatedRecord : syncUpdatedRulesDto.getAppdRuleUpdatedRecs()) {
                Status addOrUpdateStatus = null;
                try {
                    addOrUpdateStatus = service.addRecord(
                            InventoryUtilities.getAppdRule(tenantId, updatedRecord.getAppInstanceId(), updatedRecord),
                            appDRuleRepository);
                    LOGGER.info("record added status {}", addOrUpdateStatus);
                } catch (IllegalArgumentException e) {
                    if (e.getMessage().equals("Record already exist")) {
                        addOrUpdateStatus = service.updateRecord(InventoryUtilities.getAppdRule(tenantId,
                                updatedRecord.getAppInstanceId(), updatedRecord), appDRuleRepository);
                        LOGGER.info("record updated status {}", addOrUpdateStatus);
                    }
                }
                if (addOrUpdateStatus != null && !addOrUpdateStatus.getResponse().equals("Saved") && !addOrUpdateStatus
                        .getResponse().equals("Updated")) {
                    finalStatus.setResponse(PARTIAL_FAILURE);
                }
            }
        }
    }

    /**
     * Synchronize MEC host from a given MEPM to center.
     *
     * @param accessToken access token
     * @param mepmIp      mepm IP
     * @return success if ok, partial failure is something failed
     */
    @ApiOperation(value = "Synchronize mec host record", response = String.class)
    @GetMapping(path = "/mepms/{mepm_ip}/mechost/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_ADMIN')")
    public ResponseEntity<Status> syncMecHostsRecords(
            @ApiParam(value = ACCESSTOKEN) @RequestHeader(ACCESS_TOKEN) String accessToken,
            @ApiParam(value = "mepm ip") @PathVariable("mepm_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String mepmIp) {
        Status finalStatus = new Status("Success");
        // Synchronize added & updated records
        // Get dto records
        ResponseEntity<SyncUpdatedMecHostDto> updateResponse =
                syncService.syncRecords(getMepmMecHostSyncUrl(mepmIp) + "/sync_updated",
                        SyncUpdatedMecHostDto.class, accessToken);
        SyncUpdatedMecHostDto syncUpdatedMecHostDto = updateResponse.getBody();
        // Update table
        updateMecHostRecord(syncUpdatedMecHostDto, finalStatus, mepmIp);

        // Synchronize deleted records
        // Get dto records
        ResponseEntity<SyncDeletedMecHostDto> deleteResponse =
                syncService.syncRecords(getMepmMecHostSyncUrl(mepmIp) + "/sync_deleted",
                        SyncDeletedMecHostDto.class, accessToken);
        SyncDeletedMecHostDto syncDeletedMecHostDto = deleteResponse.getBody();
        // Update table
        if (syncDeletedMecHostDto != null && syncDeletedMecHostDto.getMecHostStaleRecs() != null) {
            for (MecHostDeletedDto deletedRecord : syncDeletedMecHostDto.getMecHostStaleRecs()) {
                Status deleteStatus = service.deleteRecord(deletedRecord.getMechostIp(), hostRepository);
                LOGGER.info("Record deleted status {}", deleteStatus);
                if (!deleteStatus.getResponse().equals("Deleted")) {
                    finalStatus.setResponse(PARTIAL_FAILURE);
                    LOGGER.info("Record synced status {}", finalStatus);
                }
            }
        }
        LOGGER.info("Record synced status {}", finalStatus);
        return new ResponseEntity<>(finalStatus, HttpStatus.OK);
    }

    private void updateMecHostRecord(SyncUpdatedMecHostDto syncUpdatedMecHostDto, Status finalStatus, String mepmIp) {
        if (syncUpdatedMecHostDto != null && syncUpdatedMecHostDto.getMecHostUpdatedRecs() != null) {
            for (MecHostDto mecHostDto : syncUpdatedMecHostDto.getMecHostUpdatedRecs()) {

                MecHost host = InventoryUtilities.getMecHost(mecHostDto, mecHostDto.getMechostIp());
                host.setApplications(new HashSet<>());
                host.setMepmIp(mepmIp);
                Status addOrUpdateStatus = null;

                try {
                    addOrUpdateStatus = service.addRecord(host, hostRepository);
                    LOGGER.info("Record added status {}", addOrUpdateStatus);
                } catch (IllegalArgumentException e) {
                    if (e.getMessage().equals("Record already exist")) {
                        MecHost hostDb = service.getRecord(mecHostDto.getMechostIp(), hostRepository);
                        host.setApplications(hostDb.getApplications());
                        addOrUpdateStatus = service.updateRecord(host, hostRepository);
                        LOGGER.info("Record updated status {}", addOrUpdateStatus);
                    }
                }

                if (addOrUpdateStatus != null && !addOrUpdateStatus.getResponse().equals("Saved") && !addOrUpdateStatus
                        .getResponse().equals("Updated")) {
                    finalStatus.setResponse(PARTIAL_FAILURE);
                }
            }
        }
    }

    private String getMepmAppRuleSyncUrl(String mepmIp, String tenantId) {
        Mepm mepm = service.getRecord(mepmIp, mepmRepository);
        String mepmPort = mepm.getMepmPort();
        String url;
        if (Boolean.parseBoolean(isSslEnabled)) {
            url = new StringBuilder(Constants.HTTPS_PROTO).append(mepmIp).append(":")
                    .append(mepmPort).append(APPRULE_TENANTS).append(tenantId)
                    .append("/app_instances/appd_configuration").toString();
        } else {
            url = new StringBuilder(Constants.HTTP_PROTO).append(mepmIp).append(":")
                    .append(mepmPort).append(APPRULE_TENANTS).append(tenantId)
                    .append("/app_instances/appd_configuration").toString();
        }
        return url;
    }

    private String getMepmMecHostSyncUrl(String mepmIp) {
        Mepm mepm = service.getRecord(mepmIp, mepmRepository);
        String mepmPort = mepm.getMepmPort();
        String url;
        if (Boolean.parseBoolean(isSslEnabled)) {
            url = new StringBuilder(Constants.HTTPS_PROTO).append(mepmIp).append(":")
                    .append(mepmPort).append("/lcmcontroller/v1/hosts").toString();
        } else {
            url = new StringBuilder(Constants.HTTP_PROTO).append(mepmIp).append(":")
                    .append(mepmPort).append("/lcmcontroller/v1/hosts").toString();
        }
        return url;
    }

}
