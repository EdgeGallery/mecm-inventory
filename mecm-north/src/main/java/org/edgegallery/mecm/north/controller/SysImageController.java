/*
 * Copyright 2022 Huawei Technologies Co., Ltd.
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
import javax.ws.rs.core.MediaType;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.edgegallery.mecm.north.controller.advice.ResponseObject;
import org.edgegallery.mecm.north.domain.ResponseConst;
import org.edgegallery.mecm.north.utils.exception.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Controller
@RestSchema(schemaId = "mecmSysImages")
@RequestMapping("/north/v1")
@Api(tags = {"MecM sysImages Controller"})
@Validated
public class SysImageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysImageController.class);
    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    @Value("${mecm-north.filesystem-address:}")
    private String fileSystemAddress;

    /**
     * get system images.
     */

    @GetMapping(value = "/system/images", produces = MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get system images", response = ResponseObject.class)
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "microservice not found", response = String.class),
        @ApiResponse(code = 500, message = "resource grant error", response = String.class)
    })
    public ResponseEntity<ResponseObject> getSysImages() {
        LOGGER.info("begin to get system images");
        ResponseEntity<String> response;
        try {
            String url = String.format("%s/image-management/v1/images", fileSystemAddress);
            response = REST_TEMPLATE.exchange(url, HttpMethod.GET, null, String.class);
        } catch (RestClientException e) {
            LOGGER.error("get sysImages exception {}", e.getMessage());
            return null;
        }
        ErrorMessage errMsg = new ErrorMessage(ResponseConst.RET_SUCCESS, null);
        return ResponseEntity.ok(new ResponseObject(response, errMsg, "query success."));
    }
}
