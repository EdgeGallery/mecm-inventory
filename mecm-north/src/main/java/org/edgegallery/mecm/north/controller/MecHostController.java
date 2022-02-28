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
import javax.validation.constraints.Pattern;
import javax.ws.rs.core.MediaType;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.edgegallery.mecm.north.controller.advice.RequestCheckBody;
import org.edgegallery.mecm.north.controller.advice.ResponseObject;
import org.edgegallery.mecm.north.controller.advice.ResponseOfStatus;
import org.edgegallery.mecm.north.controller.advice.RspHealthCheck;
import org.edgegallery.mecm.north.facade.MecmHostServiceFacade;
import org.edgegallery.mecm.north.utils.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RestSchema(schemaId = "mecmHost")
@RequestMapping("/north/v1")
@Api(tags = {"MecM Host Controller"})
@Validated
public class MecHostController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MecHostController.class);

    @Autowired
    private MecmHostServiceFacade mecmHostServiceFacade;

    /**
     * query mecm hosts.
     */
    @ApiOperation(value = "query mecm hosts", response = ResponseObject.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "query mecm hosts success", response = ResponseObject.class)
    })
    @GetMapping(value = "/mechosts", produces = MediaType.APPLICATION_JSON)
    @PreAuthorize("hasRole('APPSTORE_TENANT') || hasRole('APPSTORE_ADMIN')")
    public ResponseEntity<ResponseObject> queryMecmHosts(HttpServletRequest httpServletRequest) {
        LOGGER.info("enter query mecm hosts.");
        return mecmHostServiceFacade.getAllMecmHosts(httpServletRequest.getHeader(Constant.ACCESS_TOKEN));
    }

    /**
     * transfer the health check request
     *
     * @param hostIp mec host ip
     */
    @ApiOperation(value = "health check of mec hosts", response = ResponseObject.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "health check of mec host success", response = String.class)
    })
    @GetMapping(value = "/mechosts/{hostIp}/health", produces = MediaType.APPLICATION_JSON)
    @PreAuthorize("hasRole('APPSTORE_TENANT') || hasRole('APPSTORE_ADMIN')")
    public ResponseEntity<RspHealthCheck> healthCheck(
        @ApiParam(value = "hostIp") @PathVariable("hostIp") String hostIp,
        HttpServletRequest httpServletRequest) {
        LOGGER.info("begin to execute mec host health check");
        return mecmHostServiceFacade.healthCheck(hostIp, httpServletRequest.getHeader(Constant.ACCESS_TOKEN));
    }
}
