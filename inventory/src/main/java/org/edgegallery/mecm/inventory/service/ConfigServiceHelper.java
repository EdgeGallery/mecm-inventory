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

package org.edgegallery.mecm.inventory.service;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.edgegallery.mecm.inventory.exception.InventoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


public final class ConfigServiceHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigServiceHelper.class);
    private static final String FAILED_TO_CREATE_DIR = "failed to create local directory";
    private static final String FAILED_TO_GET_PATH = "failed to get local directory path";
    private static final String CONFIG_DIR_ROOT = "/usr/app/config";
    private static final String SLASH = "/";

    private ConfigServiceHelper() {

    }

    /**
     * Saves input stream to file.
     *
     * @param multipartFile configuration file
     * @param hostIp        host IP
     * @param tenantId      tenant ID
     * @return file path
     */
    static String saveInputStreamToFile(MultipartFile multipartFile, String hostIp, String tenantId) {
        Resource resource = multipartFile.getResource();
        String localDirPath = createDir(CONFIG_DIR_ROOT + SLASH + hostIp + tenantId);
        String localFilePath = localDirPath + SLASH + hostIp;
        File file = new File(localFilePath);
        try {
            FileUtils.copyInputStreamToFile(resource.getInputStream(), file);
            FileChecker.check(file);
            LOGGER.info("config file for host {} saved successfully", hostIp);
            return file.getCanonicalPath();
        } catch (IOException e) {
            LOGGER.error("failed to save input stream to file for host {}", hostIp);
            throw new InventoryException("failed to save input stream to file for host " + hostIp);
        }
    }

    /**
     * Creates directory to save config file.
     *
     * @param dirPath directory path to be created
     * @return directory's canonical path
     */
    private static String createDir(String dirPath) {
        File localFileDir = new File(dirPath);
        if (!localFileDir.mkdir()) {
            LOGGER.info(FAILED_TO_CREATE_DIR);
            throw new InventoryException(FAILED_TO_CREATE_DIR);
        }

        try {
            return localFileDir.getCanonicalPath();
        } catch (IOException e) {
            LOGGER.info(FAILED_TO_GET_PATH);
            throw new InventoryException(FAILED_TO_GET_PATH);
        }
    }

}
