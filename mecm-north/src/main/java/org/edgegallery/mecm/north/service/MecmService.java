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

package org.edgegallery.mecm.north.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.edgegallery.mecm.north.domain.ResponseConst;
import org.edgegallery.mecm.north.utils.InitConfigUtil;
import org.edgegallery.mecm.north.utils.constant.Constant;
import org.edgegallery.mecm.north.utils.exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service("mecmService")
public class MecmService {

    public static final Logger LOGGER = LoggerFactory.getLogger(MecmService.class);

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    private static final String MECM_URL_GET_MECHOSTS = "/inventory/v1/tenants/%s/mechosts";

    private static final String APM_DELETE_EDGE_PACKAGE = "/apm/v1/tenants/%s/packages/%s/hosts/%s";

    private static final String APM_DELETE_APM_PACKAGE = "/apm/v1/tenants/%s/packages/%s";

    private static final String APPO_INSTANTIATE_APP = "/appo/v1/tenants/%s/app_instances/%s";

    private static final String MECM_UPLOAD_GET_APPINFO = "/apm/v1/tenants/%s/packages/upload";

    private static final String MECM_GET_DEPLOYMENT_STATUS = "/appo/v1/tenants/%s/apps/%s/packages/%s/status";

    private static final String RESPONSE_FROM_APM_FAILED = "upload csar file to apm failed, and the response code is: ";

    private static final String INSTANTIATE_APP_FAILED = "instantiate app from appo failed.";

    private static final String APPO_DELETE_APPLICATION_INSTANCE = "/appo/v1/tenants/%s/app_instances/%s";

    private static final String APP_NAME = "app_product_name";

    private static final String APP_VERSION = "app_package_version";

    private static final String ACCESS_TOKEN = "access_token";

    private static final String APP_CLASS = "app_class";

    private static final String APP_INSTANCE_ID = "appInstanceId";

    private static final String APP_ID = "appId";

    private static final String PACKAGE_ID = "packageId";

    private static final String SUCCESS = "success";

    private static final String APM_UPLOAD_PACKAGE = "/apm/v1/tenants/%s/packages/upload";

    private static final String APM_GET_PACKAGE = "/apm/v1/tenants/%s/packages/%s";

    private static final String INVENTORY_GET_MECHOSTS_URL = "/inventory/v1/tenants/%s/mechosts";

    private static final String TENANT_ID = "tenantId";

    private static final String CONTENT_TYPE = "Content-Type";

    private static final String APPO_CREATE_APPINSTANCE = "/appo/v1/tenants/%s/app_instances";

    private static final String APPLICATION_JSON = "application/json";

    private static final String CREATED = "Created";

    private static final String APPO_GET_INSTANCE = "/appo/v1/tenants/%s/app_instance_infos/%s";

    private static final String APPO_GET_KPI_INSTANCE = "/appo/v1/tenants/%s/hosts/%s/kpi";

    private static final String PROVIDER_ID = "app_provider_id";

    private static final String INSTANTIATED = "instantiated";

    private static final String ARCHITECTURE = "app_architecture";

    private static final String LOCAL_FILE_PATH = "/usr/mecm-north/";

    private static final String VM = "vm";

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${serveraddress.apm}")
    private String apmServerAddress;

    @Value("${serveraddress.appo}")
    private String appoUrl;

    @Value("${serveraddress.inventory}")
    private String inventoryUrl;

    /**
     * save file to local file path.
     *
     * @param uploadFile uploadFile
     * @param mecmPackageId mecmPackageId
     * @return save File Path
     */
    public String saveFileToLocal(MultipartFile uploadFile, String mecmPackageId) {
        File filePath = new File(InitConfigUtil.getWorkSpaceBaseDir() + LOCAL_FILE_PATH + mecmPackageId + "/");
        if (!filePath.isDirectory()) {
            boolean isSuccess = filePath.mkdirs();
            if (!isSuccess) {
                LOGGER.error("make file dir failed");
                return null;
            }
        }
        LOGGER.info("make file path to {}", LOCAL_FILE_PATH + mecmPackageId + "/");
        String fileName = uploadFile.getOriginalFilename();

        if (fileName == null || fileName.length() == 0) {
            LOGGER.error("the filename cannot be empty");
            return null;
        }

        try {
            uploadFile.transferTo(new File(filePath, fileName));
        } catch (IOException e) {
            LOGGER.error("Failed to save file.");
            return null;
        }
        LOGGER.info("upload file success {}", fileName);
        return InitConfigUtil.getWorkSpaceBaseDir() + LOCAL_FILE_PATH + mecmPackageId + "/" + fileName;
    }

    /**
     * upload file to apm service.
     *
     * @param filePath file path
     * @param context context info
     * @param hostIp host ip
     * @return response from atp
     */
    public ResponseEntity<String> uploadFileToApm(String filePath, Map<String, String> context, String hostIp,
        Map<String, String> packageInfo) {
        //context 需要ACCESS_TOKEN、apmServerAddress、TENANT_ID
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(filePath));
        body.add("hostList", hostIp);
        body.add("appPackageName", packageInfo.get(APP_NAME));
        body.add("appPackageVersion", packageInfo.get(APP_VERSION));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set(ACCESS_TOKEN, context.get(ACCESS_TOKEN));

        LOGGER.info("hostIp: " + hostIp);
        LOGGER.info("appPackageName: " + packageInfo.get(APP_NAME));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 走upload with package，body里放file
        String url = context.get("apmServerAddress").concat(String.format(APM_UPLOAD_PACKAGE, context.get(TENANT_ID)));
        try {
            return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        } catch (RestClientException e) {
            LOGGER.error("Failed to upload file to apm, exception {}", e.getMessage());
        }
        return null;
    }

    /**
     * get all mecm hosts.
     *
     * @param token access token
     * @return mecm host list
     */
    public List<Map<String, Object>> getAllMecmHosts(String token, String tenantId) {
        LOGGER.info("enter mec host service side");
        HttpHeaders headers = new HttpHeaders();
        headers.set(Constant.ACCESS_TOKEN, token);
        LOGGER.info("access token is: {}", token);
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url = inventoryUrl.concat(String.format(MECM_URL_GET_MECHOSTS, tenantId));
        LOGGER.info("mecm service side query token is : {}", token);
        try {
            ResponseEntity<String> response = REST_TEMPLATE.exchange(url, HttpMethod.GET, request, String.class);
            if (!HttpStatus.OK.equals(response.getStatusCode())) {
                LOGGER.error("Failed to get mechosts from mecm inventory, The status code is {}",
                    response.getStatusCode());
                throw new AppException("Failed to get mechosts from mecm inventory.",
                    ResponseConst.RET_GET_MECMHOST_FAILED);
            }

            return new Gson().fromJson(response.getBody(), List.class);
        } catch (RestClientException e) {
            LOGGER.error("Failed to get mechosts, RestClientException is {}", e.getMessage());
        }

        return Collections.emptyList();
    }

    /**
     * get package from apm.
     *
     * @param context context
     * @param packageId packageId
     * @param hostIp hostIp
     * @return
     */
    public boolean getApmPackage(Map<String, String> context, String packageId, String hostIp) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS_TOKEN, context.get(ACCESS_TOKEN));
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url = context.get("apmServerAddress")
            .concat(String.format(APM_GET_PACKAGE, context.get(TENANT_ID), packageId));
        LOGGER.warn("getApmPackage URL: " + url);

        long startTime = System.currentTimeMillis();
        while (true) {
            try {
                // time out limit
                if ((System.currentTimeMillis() - startTime) > 180000) {
                    LOGGER.error("get package {} from apm time out", packageId);
                    return false;
                }

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
                if (!HttpStatus.OK.equals(response.getStatusCode())) {
                    LOGGER.error("get package from apm reponse failed. The status code is {}",
                        response.getStatusCode());
                    return false;
                }

                JsonObject jsonObject = new JsonParser().parse(response.getBody()).getAsJsonObject();
                JsonArray mecHostInfo = jsonObject.get("mecHostInfo").getAsJsonArray();
                for (JsonElement mecHost : mecHostInfo) {
                    JsonObject mecHostObject = mecHost.getAsJsonObject();
                    String status = mecHostObject.get("status").getAsString();
                    String hostIpReq = mecHostObject.get("hostIp").getAsString();
                    if (hostIp.equals(hostIpReq)) {
                        LOGGER.info("status: {}", status);
                        if ("Distributed".equalsIgnoreCase(status) || "uploaded".equalsIgnoreCase(status)) {
                            return true;
                        } else {
                            if ("Error".equalsIgnoreCase(status)) {
                                return false;
                            } else {
                                break;
                            }
                        }
                    }
                }
                Thread.sleep(9000);
            } catch (RestClientException e) {
                LOGGER.error("Failed to get package from apm which packageId is {} exception {}", packageId,
                    e.getMessage());
                return false;
            } catch (InterruptedException e) {
                LOGGER.error("thead sleep exception.");
                return false;
            }
        }
    }

    /**
     * get package from apm.
     *
     * @param context context
     * @param packageId packageId
     * @param hostIp hostIp
     * @return
     */
    public String getApmPackageOnce(Map<String, String> context, String packageId, String hostIp) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS_TOKEN, context.get(ACCESS_TOKEN));
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url = context.get("apmServerAddress")
            .concat(String.format(APM_GET_PACKAGE, context.get(TENANT_ID), packageId));
        LOGGER.warn("getApmPackage URL: " + url);

        try {

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            if (!HttpStatus.OK.equals(response.getStatusCode())) {
                LOGGER.error("get package from apm reponse failed. The status code is {}", response.getStatusCode());
            }

            JsonObject jsonObject = new JsonParser().parse(response.getBody()).getAsJsonObject();
            JsonArray mecHostInfo = jsonObject.get("mecHostInfo").getAsJsonArray();
            for (JsonElement mecHost : mecHostInfo) {
                JsonObject mecHostObject = mecHost.getAsJsonObject();
                String status = mecHostObject.get("status").getAsString();
                String hostIpReq = mecHostObject.get("hostIp").getAsString();
                if (hostIp.equals(hostIpReq)) {
                    LOGGER.info("status: {}", status);
                    if ("Distributed".equalsIgnoreCase(status) || "uploaded".equalsIgnoreCase(status)) {
                        return "Distributed";
                    } else {
                        return status;
                    }
                }
            }
        } catch (RestClientException e) {
            LOGGER.error("Failed to get package from apm which packageId is {} exception {}", packageId,
                e.getMessage());
            return "Error";
        }
        return "";
    }

    /**
     * create app instance from appo.
     *
     * @param context context info
     * @param appName appName
     * @param hostIp mec host ip
     * @return create app instance sucess or not.s
     */
    public String createInstanceFromAppoOnce(Map<String, String> context, String appName, String hostIp) {
        Map<String, Object> body = new HashMap<>();
        body.put("appInstanceDescription", UUID.randomUUID().toString());
        body.put("appName", appName);
        body.put("appPackageId", context.get(PACKAGE_ID));
        body.put("appId", context.get(APP_ID));
        body.put("mecHost", hostIp);

        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS_TOKEN, context.get(ACCESS_TOKEN));
        headers.set(CONTENT_TYPE, APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String url = context.get("appoServerAddress")
            .concat(String.format(APPO_CREATE_APPINSTANCE, context.get(TENANT_ID)));

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            LOGGER.info("response is: {}", response.getStatusCode());
            if (HttpStatus.OK.equals(response.getStatusCode()) || HttpStatus.ACCEPTED.equals(
                response.getStatusCode())) {
                JsonObject jsonObject = new JsonParser().parse(response.getBody()).getAsJsonObject();
                JsonObject responseBody = jsonObject.get("response").getAsJsonObject();
                if (null != responseBody) {
                    String appInstanceId = responseBody.get("app_instance_id").getAsString();
                    LOGGER.info("new appInstanceId get from appo: {}", appInstanceId);
                    return appInstanceId;
                }
            }
        } catch (RestClientException e) {
            LOGGER.error("Failed to create app instance from appo which appId is {} exception {}", context.get(APP_ID),
                e.getMessage());
        }
        return null;
    }

    /**
     * get application instance from appo.
     *
     * @param context context
     * @param appInstanceId appInstanceId
     * @return
     */
    public String getApplicationInstanceOnce(Map<String, String> context, String appInstanceId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS_TOKEN, context.get(ACCESS_TOKEN));
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url = context.get("appoServerAddress")
            .concat(String.format(APPO_GET_INSTANCE, context.get(TENANT_ID), appInstanceId));
        LOGGER.warn("getApplicationInstance URL: " + url);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            if (!HttpStatus.OK.equals(response.getStatusCode())) {
                LOGGER.error("get application instance from appo reponse failed. The status code is {}",
                    response.getStatusCode());
                return "";
            }

            JsonObject jsonObject = new JsonParser().parse(response.getBody()).getAsJsonObject();
            JsonObject responseBody = jsonObject.get("response").getAsJsonObject();
            LOGGER.info("status: {}, operationalStatus: {}", responseBody.get("operationalStatus").getAsString());

            String responseStatus = responseBody.get("operationalStatus").getAsString();
            if ("Instantiation failed".equalsIgnoreCase(responseStatus) || "Create failed".equalsIgnoreCase(
                responseStatus)) {
                LOGGER.error("instantiate or create app failed. The status  is {}", responseStatus);
              return Constant.INSTANTIATE_ERROR_STATUS;
            }
            return responseStatus;
        } catch (RestClientException e) {
            LOGGER.error("Failed to get application instance from appo which app_instance_id is {} exception {}",
                appInstanceId, e.getMessage());
        }
        return "";
    }

    /**
     * create app instance from appo.
     *
     * @param context context info
     * @param appName appName
     * @param hostIp mec host ip
     * @return create app instance sucess or not.s
     */
    public String createInstanceFromAppo(Map<String, String> context, String appName, String hostIp,
        Map<String, Object> parameters) {
        Map<String, Object> body = new HashMap<>();
        body.put("appInstanceDescription", UUID.randomUUID().toString());
        body.put("appName", appName);
        body.put("appPackageId", context.get(PACKAGE_ID));
        body.put("appId", context.get(APP_ID));
        body.put("mecHost", hostIp);

        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS_TOKEN, context.get(ACCESS_TOKEN));
        headers.set(CONTENT_TYPE, APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String url = context.get("appoServerAddress")
            .concat(String.format(APPO_CREATE_APPINSTANCE, context.get(TENANT_ID)));

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            LOGGER.info("response is: {}", response.getStatusCode());
            if (HttpStatus.OK.equals(response.getStatusCode()) || HttpStatus.ACCEPTED.equals(
                response.getStatusCode())) {
                JsonObject jsonObject = new JsonParser().parse(response.getBody()).getAsJsonObject();
                JsonObject responseBody = jsonObject.get("response").getAsJsonObject();
                if (null != responseBody) {
                    String appInstanceId = responseBody.get("app_instance_id").getAsString();
                    LOGGER.info("appInstanceId: {}", appInstanceId);
                    if (getApplicationInstance(context, appInstanceId, CREATED) && instantiateAppFromAppo(context,
                        parameters, appInstanceId)) {
                        if (getApplicationInstance(context, appInstanceId, INSTANTIATED)) {
                            return appInstanceId;
                        }
                    }
                    return null;
                }
            }
        } catch (RestClientException e) {
            LOGGER.error("Failed to create app instance from appo which appId is {} exception {}", context.get(APP_ID),
                e.getMessage());
        }
        return null;
    }

    /**
     * get application instance from appo.
     *
     * @param context context
     * @param appInstanceId appInstanceId
     * @param status status
     * @return
     */
    private boolean getApplicationInstance(Map<String, String> context, String appInstanceId, String status) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS_TOKEN, context.get(ACCESS_TOKEN));
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url = context.get("appoServerAddress")
            .concat(String.format(APPO_GET_INSTANCE, context.get(TENANT_ID), appInstanceId));
        LOGGER.warn("getApplicationInstance URL: " + url);

        long startTime = System.currentTimeMillis();
        while (true) {
            try {
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
                if (!HttpStatus.OK.equals(response.getStatusCode())) {
                    LOGGER.error("get application instance from appo reponse failed. The status code is {}",
                        response.getStatusCode());
                    return false;
                }

                JsonObject jsonObject = new JsonParser().parse(response.getBody()).getAsJsonObject();
                JsonObject responseBody = jsonObject.get("response").getAsJsonObject();
                LOGGER.info("status: {}, operationalStatus: {}", status,
                    responseBody.get("operationalStatus").getAsString());

                String responseStatus = responseBody.get("operationalStatus").getAsString();
                if ("Instantiation failed".equalsIgnoreCase(responseStatus) || "Create failed".equalsIgnoreCase(
                    responseStatus)) {
                    LOGGER.error("instantiate or create app failed. The status  is {}", responseStatus);
                    return false;
                }

                if (status.equalsIgnoreCase(responseStatus)) {
                    LOGGER.info("{} is {}.", appInstanceId, status);
                    break;
                }

                if ((System.currentTimeMillis() - startTime) > 40000) {
                    LOGGER.error("get instance {} from appo time out", appInstanceId);
                    return false;
                }
                Thread.sleep(5000);
            } catch (RestClientException e) {
                LOGGER.error("Failed to get application instance from appo which app_instance_id is {} exception {}",
                    appInstanceId, e.getMessage());
                return false;
            } catch (InterruptedException e) {
                LOGGER.error("thead sleep exception.");
            }
        }

        return true;
    }

    /**
     * instantiate application by appo.
     *
     * @param context context info.
     * @param appInstanceId appInstanceId
     * @return instantiate app successful
     */
    public boolean instantiateAppFromAppo(Map<String, String> context, Map<String, Object> parameters,
        String appInstanceId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS_TOKEN, context.get(ACCESS_TOKEN));
        headers.set(CONTENT_TYPE, APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request;
        if (VM.equalsIgnoreCase(context.get(APP_CLASS))) {
            Map<String, Object> body = new HashMap<String, Object>();
            // if package is vm, need parameters body
            LOGGER.info("package is vm.");
            body.put("parameters", parameters);
            request = new HttpEntity<>(body, headers);
        } else {
            request = new HttpEntity<>(headers);
        }

        String url = context.get("appoServerAddress")
            .concat(String.format(APPO_INSTANTIATE_APP, context.get(TENANT_ID), appInstanceId));
        LOGGER.info("instantiateAppFromAppo URL : {}", url);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            if (!HttpStatus.ACCEPTED.equals(response.getStatusCode())) {
                LOGGER.error("instantiate application from appo reponse failed. The status code is {}",
                    response.getStatusCode());
                return false;
            }
            LOGGER.info("instantiateAppFromAppo: {}", response.getStatusCode());
        } catch (RestClientException e) {
            LOGGER.error("Failed to instantiate application from appo which app_instance_id is {} exception {}",
                appInstanceId, e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * instantiate application by appo.
     *
     * @param context context info.
     * @param appInstanceId appInstanceId
     * @return instantiate app successful
     */
    public String instantiateAppFromAppoOnce(Map<String, String> context, Map<String, Object> parameters,
        String appInstanceId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS_TOKEN, context.get(ACCESS_TOKEN));
        headers.set(CONTENT_TYPE, APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request;
        if (VM.equalsIgnoreCase(context.get(APP_CLASS))) {
            Map<String, Object> body = new HashMap<String, Object>();
            // if package is vm, need parameters body
            LOGGER.info("package is vm.");
            body.put("parameters", parameters);
            request = new HttpEntity<>(body, headers);
        } else {
            request = new HttpEntity<>(headers);
        }

        String url = context.get("appoServerAddress")
            .concat(String.format(APPO_INSTANTIATE_APP, context.get(TENANT_ID), appInstanceId));
        LOGGER.info("instantiateAppFromAppo URL : {}", url);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            if (!HttpStatus.ACCEPTED.equals(response.getStatusCode())) {
                LOGGER.error("instantiate application from appo reponse failed. The status code is {}",
                    response.getStatusCode());
                return Constant.INSTANTIATE_ERROR_STATUS;
            }
            LOGGER.info("instantiateAppFromAppo: {}", response.getStatusCode());
        } catch (RestClientException e) {
            LOGGER.error("Failed to instantiate application from appo which app_instance_id is {} exception {}",
                appInstanceId, e.getMessage());
            return Constant.INSTANTIATE_ERROR_STATUS;
        }
        return Constant.INSTANTIATING_STATUS;
    }

    /*    private void setBody(Map<String, Object> body, Map<String, String> context) {
        String configParam = context.get("configParamList");
        String[] configList = configParam.split(",");
        for (String config : configList) {
            String[] params = config.split(";");
            for (String param : params) {
                String[] configItem = param.split("=");
                // param patter: key = value or key = ;
                body.put(configItem[0].trim(), 1 == configItem.length ? "" : configItem[1].trim());
            }
        }
    }*/

    /**
     * delete edge package.
     *
     * @param context context
     * @param hostIp hostIp
     * @return delete successfully
     */
    public boolean deleteEdgePackage(Map<String, String> context, String hostIp) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS_TOKEN, context.get(ACCESS_TOKEN));
        HttpEntity<String> request = new HttpEntity<>(headers);

        String url = context.get("apmServerAddress")
            .concat(String.format(APM_DELETE_EDGE_PACKAGE, context.get(TENANT_ID), context.get(PACKAGE_ID), hostIp));
        LOGGER.warn("deleteEdgePkg URL: {}", url);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
            if (HttpStatus.OK.equals(response.getStatusCode())) {
                return true;
            }
            LOGGER.error("deleteEdgePkg reponse failed. The status code is {}", response.getStatusCode());
        } catch (RestClientException e) {
            LOGGER.error("deleteEdgePkg failed, exception {}", e.getMessage());
        }

        return false;
    }

    /**
     * delete apm package.
     *
     * @param context context
     * @return delete successfully
     */
    public boolean deleteApmPackage(Map<String, String> context) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS_TOKEN, context.get(ACCESS_TOKEN));
        HttpEntity<String> request = new HttpEntity<>(headers);

        String url = context.get("apmServerAddress")
            .concat(String.format(APM_DELETE_APM_PACKAGE, context.get(TENANT_ID), context.get(PACKAGE_ID)));
        LOGGER.warn("deleteApmPkg URL: {}", url);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
            if (HttpStatus.OK.equals(response.getStatusCode())) {
                return true;
            }
            LOGGER.error("deleteApmPkg reponse failed. The status code is {}", response.getStatusCode());
        } catch (RestClientException e) {
            LOGGER.error("deleteApmPkg failed, aexception {}", e.getMessage());
        }

        return false;
    }

    /*
    private String getMecHostAppInstantiated(Map<String, String> context) {
        String mecHostIpList = context.get("mecHostIpList");
        if (null == mecHostIpList) {
            return null;
        }
        String[] hostArray = mecHostIpList.split(",");
        return hostArray[0];
    }
    */

    /**
     * delete app instance from appo.
     *
     * @param appInstanceId appInstanceId
     * @param context context info
     * @return response success or not.
     */
    public boolean deleteAppInstance(String appInstanceId, Map<String, String> context) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS_TOKEN, context.get(ACCESS_TOKEN));
        HttpEntity<String> request = new HttpEntity<>(headers);

        String url = context.get("appoServerAddress")
            .concat(String.format(APPO_DELETE_APPLICATION_INSTANCE, context.get(TENANT_ID), appInstanceId));
        LOGGER.warn("deleteAppInstance URL: {}", url);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
            if (HttpStatus.OK.equals(response.getStatusCode()) || HttpStatus.ACCEPTED.equals(
                response.getStatusCode())) {
                return true;
            }
            LOGGER.error("delete app instance from appo reponse failed. The status code is {}",
                response.getStatusCode());
        } catch (RestClientException e) {
            LOGGER.error("delete app instance from appo failed, appInstanceId is {} exception {}", appInstanceId,
                e.getMessage());
        }

        return false;
    }

    /**
     * delay some time.
     */
    public void delay() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            LOGGER.error("thread sleep has error.");
        }
    }

}