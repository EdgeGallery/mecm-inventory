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
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import org.edgegallery.mecm.inventory.apihandler.dto.AppRepoDto;
import org.edgegallery.mecm.inventory.model.AppRepo;
import org.edgegallery.mecm.inventory.service.InventoryServiceImpl;
import org.edgegallery.mecm.inventory.service.repository.AppRepoRepository;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Application store Inventory API handler.
 */
@Api(value = "Inventory MEC Application repo Inventory api system")
@Validated
@RequestMapping("/inventory/v1")
@RestController
public class AppRepoInventoryHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppRepoInventoryHandler.class);

    @Autowired
    private InventoryServiceImpl service;

    @Autowired
    private AppRepoRepository repository;

    @Value("${repository.appsource.repo:}")
    private String appSrcRepos;

    @PostConstruct
    void postConstruct() {
        configureAppSourceRepos();
    }

    private void configureAppSourceRepos() {
        LOGGER.info("Configure source repositories... {}", appSrcRepos);
        String[] reposInput = appSrcRepos.split("\\|");
        String[] srcRepo;
        AppRepo appRepo = null;

        if (reposInput.length > 20) {
            LOGGER.error("max source repo config limit exceeded");
            return;
        }
        for (String repo : reposInput) {
            try {
                srcRepo = repo.split("\\s+");
                if (srcRepo.length < 3) {
                    LOGGER.info("invalid source repo input format");
                    continue;
                }
                appRepo = new AppRepo();

                String[] repoId = srcRepo[0].split("=");
                if (repoId.length > 1) {
                    appRepo.setRepoId(repoId[1]);
                    appRepo.setRepoEndPoint(repoId[1]);
                }

                String[] userName = srcRepo[1].split("=");
                if (userName.length > 1) {
                    appRepo.setRepoUserName(userName[1]);
                }

                String[] password = srcRepo[2].split("=");
                if (password.length > 1) {
                    appRepo.setRepoPassword(password[1]);
                }

                appRepo.setTenantId(Constants.ADMIN_USER);

                LOGGER.info("Adding source repo info {}", appRepo.getRepoId());
                service.addRecord(appRepo, repository);
            } catch (IllegalArgumentException ex) {
                LOGGER.error("failed to add source repo {}", ex.getMessage());
            }
        }
    }

    /**
     * Adds a new application repo record entry into the Inventory.
     *
     * @param appRepoDto application store record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Adds new application repo record", response = String.class)
    @PostMapping(path = "/apprepos", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT')")
    public ResponseEntity<Status> addAppRepoRecord(
            @Valid @ApiParam(value = "app repo inventory information") @RequestBody AppRepoDto appRepoDto) {
        AppRepo repo = InventoryUtilities.getModelMapper().map(appRepoDto, AppRepo.class);
        repo.setRepoId(appRepoDto.getRepoEndPoint());
        repo.setTenantId(Constants.ADMIN_USER);
        Status status = service.addRecord(repo, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Updates an exiting application repo.
     *
     * @param appRepoEndPoint application repo
     * @param appRepoDto      application repo record details
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Updates existing application repo record", response = String.class)
    @PutMapping(path = "/apprepos/{apprepo_endpoint}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT')")
    public ResponseEntity<Status> updateAppRepoRecord(
            @ApiParam(value = "apprepo ID") @PathVariable("apprepo_endpoint")
            @Size(max = 255) String appRepoEndPoint,
            @Valid @ApiParam(value = "apprepo inventory information") @RequestBody AppRepoDto appRepoDto) {
        if (!appRepoEndPoint.equals(appRepoDto.getRepoEndPoint())) {
            LOGGER.error("Input validation failed for apprepo endpoint, value in body {}, value in url {}",
                    appRepoDto.getRepoEndPoint(), appRepoEndPoint);
            throw new IllegalArgumentException("apprepo endpoint in body and url is different");
        }
        AppRepo repo = InventoryUtilities.getModelMapper().map(appRepoDto, AppRepo.class);
        repo.setRepoId(repo.getRepoId());
        repo.setTenantId(Constants.ADMIN_USER);
        Status status = service.updateRecord(repo, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Retrieves all application repo records.
     *
     * @return application repo records & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves all application repo records", response = List.class)
    @GetMapping(path = "/apprepos", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT') || hasRole('MECM_GUEST')")
    public ResponseEntity<List<AppRepoDto>> getAllAppRepoRecords() {

        Iterable<AppRepo> appRepos = repository.findAll();
        List<AppRepoDto> appRepoDtos = new LinkedList<>();
        for (AppRepo appRepo : appRepos) {
            AppRepoDto appRepoDto = InventoryUtilities.getModelMapper().map(appRepo, AppRepoDto.class);
            appRepoDtos.add(appRepoDto);
        }
        return new ResponseEntity<>(appRepoDtos, HttpStatus.OK);
    }

    /**
     * Retrieves a specific application repo record in the Inventory.
     *
     * @param appRepoEndPoint endpoing application store IP
     * @return application repo record & status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Retrieves application repo record", response = AppRepoDto.class)
    @GetMapping(path = "/apprepos/{apprepo_endpoint}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT') || hasRole('MECM_GUEST')")
    public ResponseEntity<AppRepoDto> getAppStoreRecord(
            @ApiParam(value = "apprepo endpoint") @PathVariable("apprepo_endpoint")
            @Size(max = 255) String appRepoEndPoint) {
        AppRepo repo = service.getRecord(appRepoEndPoint, repository);
        AppRepoDto appRepoDto = InventoryUtilities.getModelMapper().map(repo, AppRepoDto.class);
        return new ResponseEntity<>(appRepoDto, HttpStatus.OK);
    }

    /**
     * Deletes all application store records for a given tenant.
     *
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes all application repo records", response = String.class)
    @DeleteMapping(path = "/apprepos", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT')")
    public ResponseEntity<Status> deleteAllAppRepoRecords() {
        Status status = service.deleteTenantRecords(Constants.ADMIN_USER, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Deletes a specific application store record in the Inventory matching the given tenant ID & application store
     * IP.
     *
     * @param appRepoEndPoint application repo endpoint
     * @return status code 200 on success, error code on failure
     */
    @ApiOperation(value = "Deletes application repo record", response = String.class)
    @DeleteMapping(path = "/apprepos/{apprepo_endpoint}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MECM_TENANT')")
    public ResponseEntity<Status> deleteAppStoreRecord(
            @ApiParam(value = "apprepo endpoint") @PathVariable("apprepo_endpoint")
            @Size(max = 255) String appRepoEndPoint) {
        Status status = service.deleteRecord(appRepoEndPoint, repository);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
