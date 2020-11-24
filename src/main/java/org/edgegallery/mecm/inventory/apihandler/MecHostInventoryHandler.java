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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.edgegallery.mecm.inventory.apihandler.dto.MecApplicationDto;
import org.edgegallery.mecm.inventory.apihandler.dto.MecHostDto;
import org.edgegallery.mecm.inventory.apihandler.dto.MecHwCapabilityDto;
import org.edgegallery.mecm.inventory.model.MecApplication;
import org.edgegallery.mecm.inventory.model.MecHost;
import org.edgegallery.mecm.inventory.model.MecHwCapability;
import org.edgegallery.mecm.inventory.service.ConfigServiceImpl;
import org.edgegallery.mecm.inventory.service.InventoryServiceImpl;
import org.edgegallery.mecm.inventory.service.repository.MecApplicationRepository;
import org.edgegallery.mecm.inventory.service.repository.MecHostRepository;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * MEC host Inventory API handler.
 */
@Api(value = "Inventory MEC host Inventory api system")
@Validated
@RequestMapping("/inventory/v1")
@RestController
public class MecHostInventoryHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MecHostInventoryHandler.class);
    @Autowired
    private InventoryServiceImpl service;
    @Autowired
    private MecHostRepository repository;
    @Autowired
    private MecApplicationRepository appRepository;
    @Autowired
    private ConfigServiceImpl configService;

    /**
     * Adds a new MEC host record entry into the Inventory.
     *
     * @param tenantId   tenant ID
     * @param mecHostDto mec host record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Adds new MEC host record", response = String.class)
    @PostMapping(path = "/tenants/{tenant_id}/mechosts", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT')")
    public ResponseEntity<Status> addMecHostRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.TENANT_ID_REGEX) @Size(max = 64) String tenantId,
            @Valid @ApiParam(value = "mechost inventory information") @RequestBody MecHostDto mecHostDto) {
        MecHost host = InventoryUtilities.getModelMapper().map(mecHostDto, MecHost.class);
        host.setTenantId(tenantId);
        host.setMechostId(mecHostDto.getMechostIp() + "_" + tenantId);

        Set<MecHwCapability> capabilities = new HashSet<>();
        for (MecHwCapabilityDto v : mecHostDto.getHwcapabilities()) {
            MecHwCapability capability = InventoryUtilities.getModelMapper().map(v, MecHwCapability.class);

            capability.setMecCapabilityId(v.getHwType() + host.getMechostId());
            capability.setMecHost(host);
            capability.setTenantId(tenantId);

            capabilities.add(capability);
        }
        host.setHwcapabilities(capabilities);
        host.setApplications(new HashSet<>());

        Status status = service.addRecord(host, repository);
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
    @PutMapping(path = "/tenants/{tenant_id}/mechosts/{mechost_ip}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT')")
    public ResponseEntity<Status> updateMecHostRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.TENANT_ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = "mechost IP") @PathVariable("mechost_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String mecHostIp,
            @Valid @ApiParam(value = "mechost inventory information") @RequestBody MecHostDto mecHostDto) {
        if (!mecHostIp.equals(mecHostDto.getMechostIp())) {
            LOGGER.error("Input validation failed for mechost IP, value in body {}, value in url {}",
                    mecHostDto.getMechostIp(), mecHostIp);
            throw new IllegalArgumentException("mechost IP in body and url is different");
        }
        MecHost host = InventoryUtilities.getModelMapper().map(mecHostDto, MecHost.class);
        host.setTenantId(tenantId);
        host.setMechostId(mecHostIp + "_" + tenantId);

        Set<MecHwCapability> capabilities = new HashSet<>();
        for (MecHwCapabilityDto v : mecHostDto.getHwcapabilities()) {
            MecHwCapability capability = InventoryUtilities.getModelMapper().map(v, MecHwCapability.class);
            capability.setMecCapabilityId(v.getHwType() + host.getMechostId());
            capability.setMecHost(host);
            capability.setTenantId(tenantId);

            capabilities.add(capability);
        }
        host.setHwcapabilities(capabilities);

        MecHost hostDb = service.getRecord(mecHostIp + "_" + tenantId, repository);
        host.setApplications(hostDb.getApplications());
        Status status = service.updateRecord(host, repository);
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
    @PreAuthorize("hasRole('MECM_TENANT')")
    public ResponseEntity<List<MecHostDto>> getAllMecHostRecords(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.TENANT_ID_REGEX) @Size(max = 64) String tenantId) {
        List<MecHost> mecHosts = service.getTenantRecords(tenantId, repository);
        List<MecHostDto> mecHostDtos = new LinkedList<>();
        for (MecHost host : mecHosts) {
            MecHostDto mecHostDto = InventoryUtilities.getModelMapper().map(host, MecHostDto.class);
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
    @PreAuthorize("hasRole('MECM_TENANT')")
    public ResponseEntity<MecHostDto> getMecHostRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.TENANT_ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = "mechost IP") @PathVariable("mechost_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String mecHostIp) {
        MecHost host = service.getRecord(mecHostIp + "_" + tenantId, repository);
        MecHostDto mecHostDto = InventoryUtilities.getModelMapper().map(host, MecHostDto.class);
        return new ResponseEntity<>(mecHostDto, HttpStatus.OK);
    }


    /**
     * Retrieves MEC host specific capabilities records in the Inventory matching the given tenant ID & mec host IP.
     *
     * @param tenantId  tenant ID
     * @param mecHostIp MEC host IP
     * @return capabilities record & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves MEC host record", response = Map.class)
    @GetMapping(path = "/tenants/{tenant_id}/mechosts/{mechost_ip}/capabilities",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT')")
    public ResponseEntity<Map<String, List<MecHwCapabilityDto>>> getMecHostCapabilities(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.TENANT_ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = "mechost IP") @PathVariable("mechost_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String mecHostIp) {
        MecHost host = service.getRecord(mecHostIp + "_" + tenantId, repository);
        List<MecHwCapabilityDto> mecCapabilityDtos = new LinkedList<>();
        for (MecHwCapability hostCap : host.getHwcapabilities()) {
            MecHwCapabilityDto cap = InventoryUtilities.getModelMapper().map(hostCap, MecHwCapabilityDto.class);
            mecCapabilityDtos.add(cap);
        }

        if (mecCapabilityDtos.isEmpty()) {
            LOGGER.error("capabilities does not exist");
            throw new NoSuchElementException(Constants.RECORD_NOT_FOUND_ERROR);
        }

        Map<String, List<MecHwCapabilityDto>> hwCap = new HashMap<>();
        hwCap.put("hwcapabilities", mecCapabilityDtos);
        return new ResponseEntity<>(hwCap, HttpStatus.OK);
    }

    /**
     * Retrieves applications matching specific capability in MEC host record in the Inventory.
     *
     * @param tenantId       tenant ID
     * @param mecHostIp      MEC host IP
     * @param capabilityType MEC host capability type
     * @return application record & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves MEC application records", response = Map.class)
    @GetMapping(path = "/tenants/{tenant_id}/mechosts/{mechost_ip}/capabilities/{capability_type}/applications",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT')")
    public ResponseEntity<Map<String, List<MecApplicationDto>>> getMecApplications(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.TENANT_ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = "mechost IP") @PathVariable("mechost_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String mecHostIp,
            @ApiParam(value = "capability type") @PathVariable("capability_type")
            @Pattern(regexp = Constants.NAME_REGEX) @Size(max = 128) String capabilityType) {
        MecHost host = service.getRecord(mecHostIp + "_" + tenantId, repository);
        List<MecApplicationDto> applications = new LinkedList<>();

        for (MecHwCapability hostCap : host.getHwcapabilities()) {
            if (!hostCap.getHwType().equals(capabilityType)) {
                continue;
            }
            Set<MecApplication> apps = host.getApplications();
            for (MecApplication app : apps) {
                if (app.getCapabilities() != null && app.getCapabilities().contains(capabilityType)) {
                    MecApplicationDto cap = InventoryUtilities.getModelMapper().map(app, MecApplicationDto.class);
                    List<String> capList = Arrays.asList(app.getCapabilities().split(",", -1));
                    cap.setCapabilities(capList);
                    applications.add(cap);
                }
            }
        }

        if (applications.isEmpty()) {
            LOGGER.error("Application with capability type: {}, not found", capabilityType);
            throw new NoSuchElementException(Constants.RECORD_NOT_FOUND_ERROR);
        }

        Map<String, List<MecApplicationDto>> hwCap = new HashMap<>();
        hwCap.put("apps", applications);
        return new ResponseEntity<>(hwCap, HttpStatus.OK);
    }

    /**
     * Deletes all records for a given tenant.
     *
     * @param tenantId tenant ID
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes all MEC host records", response = String.class)
    @DeleteMapping(path = "/tenants/{tenant_id}/mechosts", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT')")
    public ResponseEntity<Status> deleteAllMecHostRecords(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.TENANT_ID_REGEX) @Size(max = 64) String tenantId) {
        Status status = service.deleteTenantRecords(tenantId, repository);
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
    @DeleteMapping(path = "/tenants/{tenant_id}/mechosts/{mechost_ip}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT')")
    public ResponseEntity<Status> deleteMecHostRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.TENANT_ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = "mechost IP") @PathVariable("mechost_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String mecHostIp) {
        Status status = service.deleteRecord(mecHostIp + "_" + tenantId, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Adds a new application record entry into the Inventory.
     *
     * @param tenantId  tenant ID
     * @param mecHostIp MEC host IP
     * @param mecAppDto mec application record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Adds new Application record", response = String.class)
    @PostMapping(path = "/tenants/{tenant_id}/mechosts/{mechost_ip}/apps", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT')")
    public ResponseEntity<Status> addApplicationRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.TENANT_ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = "mechost IP") @PathVariable("mechost_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String mecHostIp,
            @Valid @ApiParam(value = "application inventory information") @RequestBody MecApplicationDto mecAppDto) {

        MecApplication app = InventoryUtilities.getModelMapper().map(mecAppDto, MecApplication.class);
        app.setTenantId(tenantId);
        app.setCapabilities(null);

        String capabilities = mecAppDto.getCapabilities().stream().map(Object::toString)
                .collect(Collectors.joining(","));
        if (!capabilities.isEmpty()) {
            app.setCapabilities(capabilities);
        }

        MecHost host = service.getRecord(mecHostIp + "_" + tenantId, repository);
        app.setMecAppHost(host);

        Status status = service.addRecord(app, appRepository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Updates application record entry into the Inventory.
     *
     * @param tenantId  tenant ID
     * @param mecHostIp MEC host IP
     * @param mecAppDto mec application record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Updates Application record", response = String.class)
    @PutMapping(path = "/tenants/{tenant_id}/mechosts/{mechost_ip}/apps/{app_id}", produces =
            MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT')")
    public ResponseEntity<Status> updateApplicationRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.TENANT_ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = "mechost IP") @PathVariable("mechost_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String mecHostIp,
            @ApiParam(value = "application id")
            @PathVariable("app_id") @Pattern(regexp = Constants.APPLICATION_ID_REGEX)
            @Size(max = 64) String appId,
            @Valid @ApiParam(value = "application inventory information") @RequestBody MecApplicationDto mecAppDto) {

        MecApplication appdb = service.getRecord(appId, appRepository);

        MecApplication app = InventoryUtilities.getModelMapper().map(mecAppDto, MecApplication.class);
        app.setCapabilities(null);

        if (mecAppDto.getCapabilities().size() > 0) {
            String capabilities = mecAppDto.getCapabilities().stream().map(Object::toString)
                    .collect(Collectors.joining(","));
            if (!capabilities.isEmpty()) {
                appdb.setCapabilities(capabilities);
            }
        }

        appdb.setAppName(app.getAppName());
        appdb.setPackageId(app.getPackageId());
        appdb.setStatus(app.getStatus());

        Status status = service.updateRecord(appdb, appRepository);

        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Retrieves application record entry from the Inventory.
     *
     * @param tenantId  tenant ID
     * @param mecHostIp MEC host IP
     * @param appId     mec application ID
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves Application record", response = String.class)
    @GetMapping(path = "/tenants/{tenant_id}/mechosts/{mechost_ip}/apps/{app_id}", produces =
            MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT')")
    public ResponseEntity<MecApplicationDto> getApplicationRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.TENANT_ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = "mechost IP") @PathVariable("mechost_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String mecHostIp,
            @ApiParam(value = "application id")
            @PathVariable("app_id") @Pattern(regexp = Constants.APPLICATION_ID_REGEX)
            @Size(max = 64) String appId) {

        MecApplication application = service.getRecord(appId, appRepository);
        MecApplicationDto mecAppDto = InventoryUtilities.getModelMapper().map(application, MecApplicationDto.class);
        if (application.getCapabilities() != null) {
            List<String> capList = Arrays.asList(application.getCapabilities().split(",", -1));
            mecAppDto.setCapabilities(capList);
        }
        return new ResponseEntity<>(mecAppDto, HttpStatus.OK);
    }

    /**
     * Deletes application record entry into the Inventory.
     *
     * @param tenantId  tenant ID
     * @param mecHostIp MEC host IP
     * @param appId     mec application ID
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes Application record", response = String.class)
    @DeleteMapping(path = "/tenants/{tenant_id}/mechosts/{mechost_ip}/apps/{app_id}", produces =
            MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT')")
    public ResponseEntity<Status> deleteApplicationRecord(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.TENANT_ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = "mechost IP") @PathVariable("mechost_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String mecHostIp,
            @ApiParam(value = "application id")
            @PathVariable("app_id") @Pattern(regexp = Constants.APPLICATION_ID_REGEX)
            @Size(max = 64) String appId) {

        Status status = new Status("Deleted");

        MecHost hostDb = service.getRecord(mecHostIp + "_" + tenantId, repository);
        Set<MecApplication> apps = hostDb.getApplications();
        for (MecApplication app : apps) {
            if (app.getAppInstanceId().equals(appId)) {
                apps.remove(app);
                hostDb.setApplications(apps);
                Status updateStatus = service.updateRecord(hostDb, repository);
                LOGGER.info("Record update status {}", updateStatus);
                break;
            }
        }
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Uploads K8s configuration file to host's infra manager plugin.
     *
     * @param tenantId    tenant ID
     * @param mecHostIp   edge host IP
     * @param accessToken access token
     * @param file        configuration file
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Upload K8s configuration file to applcm", response = String.class)
    @PostMapping(path = "/tenants/{tenant_id}/mechosts/{mechost_ip}/k8sconfig",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT')")
    public ResponseEntity<String> uploadConfigFile(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.TENANT_ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = "mechost IP") @PathVariable("mechost_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String mecHostIp,
            @ApiParam(value = "access token") @RequestHeader("access_token") String accessToken,
            @ApiParam(value = "config file") @RequestParam("file") MultipartFile file) {
        return configService.uploadConfig(tenantId, mecHostIp, file, accessToken);
    }

    /**
     * Deletes K8s configuration file for host's specific infra manager plugin.
     *
     * @param tenantId    tenant ID
     * @param mecHostIp   edge host IP
     * @param accessToken access token
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes K8s configuration file from applcm", response = String.class)
    @DeleteMapping(path = "/tenants/{tenant_id}/mechosts/{mechost_ip}/k8sconfig",
            produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('MECM_TENANT')")
    public ResponseEntity<String> deleteConfigFile(
            @ApiParam(value = "tenant identifier") @PathVariable("tenant_id")
            @Pattern(regexp = Constants.TENANT_ID_REGEX) @Size(max = 64) String tenantId,
            @ApiParam(value = "mechost IP") @PathVariable("mechost_ip")
            @Pattern(regexp = Constants.IP_REGEX) @Size(max = 15) String mecHostIp,
            @ApiParam(value = "access token") @RequestHeader("access_token") String accessToken) {
        return configService.deleteConfig(tenantId, mecHostIp, accessToken);
    }
}
