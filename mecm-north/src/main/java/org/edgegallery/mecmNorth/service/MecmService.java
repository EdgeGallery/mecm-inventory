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

package org.edgegallery.mecmNorth.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.edgegallery.mecmNorth.domain.ResponseConst;
import org.edgegallery.mecmNorth.utils.constant.Constant;
import org.edgegallery.mecmNorth.utils.exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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

    private static final String LOCAL_FILE_PATH = "/usr/app/NorthSystem/";

    private static final String VM = "vm";

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${serveraddress.apm}")
    private String apmServerAddress;

    @Value("${mecm.urls.appo}")
    private String appoUrl;

    @Value("${mecm.urls.inventory}")
    private String inventoryUrl;

    /**
     * save file to local file path.
     *
     * @param uploadFile    uploadFile
     * @param mecmPackageId mecmPackageId
     * @return save File Path
     */
    public String saveFileToLocal(MultipartFile uploadFile, String mecmPackageId) {
        File filePath = new File(LOCAL_FILE_PATH + mecmPackageId + "/");
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
        return LOCAL_FILE_PATH + mecmPackageId + "/" + fileName;
    }


    /**
     * upload file to apm service.
     *
     * @param filePath file path
     * @param context  context info
     * @param hostIp   host ip
     * @return response from atp
     */
    public ResponseEntity<String> uploadFileToAPM(String filePath, Map<String, String> context, String hostIp,
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
     * get package info from csar file.
     *
     * @param filePath file path
     * @return package info
     */
    private Map<String, String> getPackageInfo(String filePath) {
        Map<String, String> packageInfo = new HashMap<String, String>();
        try (ZipFile zipFile = new ZipFile(filePath)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().split("/").length == 1 && fileSuffixValidate("mf", entry.getName())) {
                    analysizeMfFile(zipFile, entry, packageInfo);
                }
                if (2 == entry.getName().split("/").length && "SwImageDesc.json"
                        .equals(entry.getName().substring(entry.getName().lastIndexOf("/") + 1))) {
                    analysizeSwImageDescFile(zipFile, entry, packageInfo);
                }
            }
        } catch (IOException e) {
            LOGGER.error("getPackageInfo failed. {}", e.getMessage());
        }
        return packageInfo;
    }

    /**
     * analysize SwImageDesc.json and get app info.
     *
     * @param zipFile     zipFile
     * @param entry       entry
     * @param packageInfo packageInfo
     * @throws IOException IOException
     */
    private void analysizeSwImageDescFile(ZipFile zipFile, ZipEntry entry, Map<String, String> packageInfo)
            throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(zipFile.getInputStream(entry), StandardCharsets.UTF_8))) {
            String line = "";
            StringBuffer fileContentFile = new StringBuffer();
            while ((line = br.readLine()) != null) {
                fileContentFile.append(line).append("\n");
            }
            JsonParser parser = new JsonParser();
            JsonArray fileContent = parser.parse(fileContentFile.toString()).getAsJsonArray();
            for (JsonElement element : fileContent) {
                String architecture = null == element.getAsJsonObject().get("architecture")
                        ? null
                        : element.getAsJsonObject().get("architecture").getAsString();
                packageInfo.put(ARCHITECTURE, architecture);
            }
        }
    }

    /**
     * analysize mf file and get app info.
     *
     * @param zipFile     zipFile
     * @param entry       entry
     * @param packageInfo packageInfo
     * @throws IOException IOException
     */
    private void analysizeMfFile(ZipFile zipFile, ZipEntry entry, Map<String, String> packageInfo) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(zipFile.getInputStream(entry), StandardCharsets.UTF_8))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                // prefix: path
                if (line.trim().startsWith(APP_NAME)) {
                    packageInfo.put(APP_NAME, line.split(":")[1].trim());
                }
                if (line.trim().startsWith(APP_VERSION)) {
                    packageInfo.put(APP_VERSION, line.split(":")[1].trim());
                }
                if (line.trim().startsWith(PROVIDER_ID)) {
                    packageInfo.put(PROVIDER_ID, line.split(":")[1].trim());
                }
                if (line.trim().startsWith(APP_CLASS)) {
                    packageInfo.put(APP_CLASS, line.split(":")[1].trim());
                }
            }
        }
    }

    /**
     * validate fileName is .pattern.
     *
     * @param pattern  filePattern
     * @param fileName fileName
     * @return
     */
    private boolean fileSuffixValidate(String pattern, String fileName) {
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        return null != suffix && "" != suffix && suffix.equals(pattern);
    }

    /**
     * get all mecm hosts.
     *
     * @param token access token
     * @return mecm host list
     */
    public List<Map<String, Object>> getAllMecmHosts(String token, String tenantId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(Constant.ACCESS_TOKEN, token);
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url = inventoryUrl.concat(String.format(MECM_URL_GET_MECHOSTS, tenantId));
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


}