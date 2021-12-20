/*
 * Copyright 2021 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.edgegallery.mecm.north.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.edgegallery.mecm.north.controller.advice.RequestCheckBody;
import org.edgegallery.mecm.north.controller.advice.RequestPkgBody;
import org.edgegallery.mecm.north.controller.advice.ResponseOfStatus;
import org.edgegallery.mecm.north.controller.advice.ResponsePkgPost;
import org.edgegallery.mecm.north.facade.MecmPackageServiceFacade;
import org.edgegallery.mecm.north.service.MecmService;
import org.edgegallery.mecm.north.utils.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RestSchema(schemaId = "mecmPackage")
@RequestMapping("/north/v1")
@Api(tags = {"MecM Package Controller"})
@Validated
public class MecmPackageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MecHostController.class);

    @Autowired
    private MecmService mecmService;

    @Autowired
    private MecmPackageServiceFacade mecmPackageServiceFacade;

    /**
     * Upload and Instantiate package to MecM.
     *
     * @param pkgName  package name from appstore
     * @param pkgVersion package version from appstore
     * @param appClass package class from appstore
     * @param parameters package parameters from appstore
     * @param file package from appstore
     * @param hostList host list to upload and instantiate
     * @param tenantId path variable tenantId
     */

    @PostMapping(value = "/tenants/{tenantId}/package", produces = MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Upload and Instantiate package", response = ResponsePkgPost.class)
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "microservice not found", response = String.class),
        @ApiResponse(code = 500, message = "resource grant error", response = String.class)
    })
    @PreAuthorize("hasRole('APPSTORE_TENANT') || hasRole('APPSTORE_ADMIN')")
    public ResponseEntity<ResponsePkgPost> uploadAndInsPackage(
        @ApiParam(value = "file", required = true) @RequestPart("file") MultipartFile file,
        @ApiParam(value = "appPkgName", required = true) @RequestParam("appPkgName") String pkgName,
        @ApiParam(value = "appPkgVersion", required = true) @RequestParam("appPkgVersion") String pkgVersion,
        @ApiParam(value = "appClass", required = true) @RequestParam("appClass") String appClass,
        @ApiParam(value = "parameters", required = true) @RequestParam("parameters") String parameters,
        @ApiParam(value = "hostList", required = true) @RequestParam("hostList") String hostList,
        @ApiParam(value = "tenantId") @PathVariable("tenantId") String tenantId, HttpServletRequest request) {
        LOGGER.info("begin to upload and instantiate package to MecM");
        RequestPkgBody body = RequestPkgBody.builder().appPkgName(pkgName)
            .appPkgVersion(pkgVersion).appClass(appClass).file(file).hostList(hostList)
            .parameters(parameters).tenantId(tenantId).build();
        return mecmPackageServiceFacade.uploadAndInstantiatePkg(body, request.getHeader(Constant.ACCESS_TOKEN));
    }

    /**
     * get package Upload and Instantiate status from MecM.
     *
     * @param mecmPackageId mecmPackageId
     * @param tenantId path variable tenantId
     */

    @GetMapping(value = "/tenants/{tenantId}/packages/{mecmPackageId}", produces = MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get package Upload and Instantiate status", response = ResponseOfStatus.class)
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "microservice not found", response = String.class),
        @ApiResponse(code = 500, message = "resource grant error", response = String.class)
    })
    @PreAuthorize("hasRole('APPSTORE_TENANT') || hasRole('APPSTORE_ADMIN')")
    public ResponseEntity<ResponseOfStatus> getPackageDisAndInsStatus(
        @ApiParam(value = "tenantId") @PathVariable("tenantId") String tenantId,
        @ApiParam(value = "mecmPackageId") @PathVariable("mecmPackageId") String mecmPackageId,
        HttpServletRequest request) {
        LOGGER.info("begin to get package Upload and Instantiate status");
        RequestCheckBody body = RequestCheckBody.builder().tenantId(tenantId).mecmPackageId(mecmPackageId).build();
        return mecmPackageServiceFacade.getPkgDisAndInsStatus(body);
    }

    /**
     * delete package from MecM.
     *
     * @param mecmPackageId mecmPackageId
     * @param tenantId path variable tenantId
     */

    @DeleteMapping(value = "/tenants/{tenantId}/packages/{mecmPackageId}", produces = MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get package Upload and Instantiate status", response = ResponseOfStatus.class)
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "microservice not found", response = String.class),
        @ApiResponse(code = 500, message = "resource grant error", response = String.class)
    })
    @PreAuthorize("hasRole('APPSTORE_TENANT') || hasRole('APPSTORE_ADMIN')")
    public ResponseEntity<ResponseOfStatus> deletePackageDisAndInsStatus(
        @ApiParam(value = "tenantId") @PathVariable("tenantId") String tenantId,
        @ApiParam(value = "mecmPackageId") @PathVariable("mecmPackageId") String mecmPackageId,
        HttpServletRequest request) {
        LOGGER.info("begin to delete package Upload and Instantiate status");
        RequestCheckBody body = RequestCheckBody.builder().tenantId(tenantId).mecmPackageId(mecmPackageId).build();
        return mecmPackageServiceFacade.deletePackageDisAndInsStatus(body, request.getHeader(Constant.ACCESS_TOKEN));
    }

}
