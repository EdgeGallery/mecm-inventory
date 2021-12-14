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

package org.edgegallery.mecmNorth.controller;

import io.swagger.annotations.*;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.edgegallery.mecmNorth.controller.advice.*;
import org.edgegallery.mecmNorth.facade.MecmPackageServiceFacade;
import org.edgegallery.mecmNorth.service.MecmService;
import org.edgegallery.mecmNorth.utils.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Controller
@RestSchema(schemaId = "mecmPackage")
@RequestMapping("/mecm-north/v1")
@Api(tags = {"MecM Package Controller"})
@Validated
public class MecMPackageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MecHostController.class);

    @Autowired
    private MecmService mecmService;

    @Autowired
    private MecmPackageServiceFacade mecmPackageServiceFacade;

    /**
     * Upload and Instantiate package to MecM
     *
     * @param appPkgName    appPkgName
     * @param appPkgVersion appPkgVersion
     * @param file          package from appstore
     * @param hostList      host list to upload and instantiate
     * @param paramsMap     paramsMap
     * @param tenantId      path variable tenantId
     */

    @PostMapping(value = "/tenants/{tenantId}/package", produces = MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Upload and Instantiate package", response = ResponsePkgPost.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "microservice not found", response = String.class),
            @ApiResponse(code = 500, message = "resource grant error", response = String.class)
    })
    @PreAuthorize("hasRole('APPSTORE_TENANT') || hasRole('APPSTORE_ADMIN')")
    public ResponseEntity<ResponsePkgPost> uploadAndInsPackge(
            @ApiParam(value = "appPkgName", required = true) @RequestParam("appPkgName") String appPkgName,
            @ApiParam(value = "appPkgVersion", required = true) @RequestParam("appPkgVersion") String appPkgVersion,
            @ApiParam(value = "appClass", required = true) @RequestParam("appClass") String appClass,
            @ApiParam(value = "file", required = true) @RequestPart("file") MultipartFile file,
            @ApiParam(value = "hostList", required = true) @RequestBody String[] hostList,
            @ApiParam(value = "params", required = true) @RequestBody Map<String, Object> paramsMap,
            @ApiParam(value = "tenantId") @PathVariable("tenantId") String tenantId, HttpServletRequest request) {
        LOGGER.info("begin to upload and instantiate package to MecM");
        RequestPkgBody body = RequestPkgBody.builder().appPkgName(appPkgName).appPkgVersion(appPkgVersion).
                appClass(appClass).file(file).hostList(hostList).paramsMap(paramsMap).tenantId(tenantId).build();
        return mecmPackageServiceFacade.uploadAndInstantiatePkg(body, request.getHeader(Constant.ACCESS_TOKEN));
    }

    /**
     * get package Upload and Instantiate status from MecM
     *
     * @param mecmPackageId     mecmPackageId
     * @param tenantId      path variable tenantId
     */

    @PostMapping(value = "/tenants/{tenantId}/packages/{mecmPackageId}", produces = MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get package Upload and Instantiate status", response = ResponseOfStatus.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "microservice not found", response = String.class),
            @ApiResponse(code = 500, message = "resource grant error", response = String.class)
    })
    @PreAuthorize("hasRole('APPSTORE_TENANT') || hasRole('APPSTORE_ADMIN')")
    public ResponseEntity<ResponseOfStatus> getPackageDisAndInsStatus(
            @ApiParam(value = "tenantId") @PathVariable("tenantId") String tenantId,
            @ApiParam(value = "mecmPackageId") @PathVariable("mecmPackageId") String mecmPackageId, HttpServletRequest request) {
        LOGGER.info("begin to get package Upload and Instantiate status");
        RequestCheckBody body = RequestCheckBody.builder().tenantId(tenantId).mecmPackageId(mecmPackageId).build();
        return mecmPackageServiceFacade.getPkgDisAndInsStatus(body,request.getHeader(Constant.ACCESS_TOKEN));
    }

    /**
     *delete package from MecM
     *
     * @param mecmPackageId     mecmPackageId
     * @param tenantId      path variable tenantId
     */

    @PostMapping(value = "/tenants/{tenantId}/packages/{mecmPackageId}", produces = MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get package Upload and Instantiate status", response = ResponseOfStatus.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "microservice not found", response = String.class),
            @ApiResponse(code = 500, message = "resource grant error", response = String.class)
    })
    @PreAuthorize("hasRole('APPSTORE_TENANT') || hasRole('APPSTORE_ADMIN')")
    public ResponseEntity<ResponseOfStatus> deletePackageDisAndInsStatus(
            @ApiParam(value = "tenantId") @PathVariable("tenantId") String tenantId,
            @ApiParam(value = "mecmPackageId") @PathVariable("mecmPackageId") String mecmPackageId, HttpServletRequest request) {
        LOGGER.info("begin to delete package Upload and Instantiate status");
        RequestCheckBody body = RequestCheckBody.builder().tenantId(tenantId).mecmPackageId(mecmPackageId).build();
        return mecmPackageServiceFacade.deletePackageDisAndInsStatus(body,request.getHeader(Constant.ACCESS_TOKEN));
    }





}
