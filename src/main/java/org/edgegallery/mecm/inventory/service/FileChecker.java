/*
 *    Copyright 2020 Huawei Technologies Co., Ltd.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.edgegallery.mecm.inventory.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileChecker {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileChecker.class);

    private static final String REG
            = "[^\\s\\\\/:*?\"<>|](\\x20|[^\\s\\\\/:*?\"<>|])*[^\\s\\\\/:*?\"<>|.]$";

    private static final int MAX_LENGTH_FILE_NAME = 255;
    private static final long MAX_FILE_SIZE = 1 * 1024 * 1024L;
    private static final Pattern FILE_NAME_PATTERN = Pattern.compile(REG);
    private static final Pattern WHITE_SPACE_PATTERN = Pattern.compile("\\s");

    private FileChecker() {
    }

    /**
     * Checks file if is invalid.
     *
     * @param file object.
     */
    static void check(File file) {
        String fileName = file.getName();

        // file name should not contains blank.
        if (fileName != null && WHITE_SPACE_PATTERN.split(fileName).length > 1) {
            throw new IllegalArgumentException(fileName + " :fileName contain blank");
        }

        if (!isAllowedFileName(fileName)) {
            throw new IllegalArgumentException(fileName + " :fileName is Illegal");
        }

        if (file.length() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(fileName + " :fileSize is too big");
        }

        if (isFileValidYaml(file)) {
            LOGGER.info("Valid yaml file");
        } else {
            throw new IllegalArgumentException(fileName + " :file type is invalid");
        }

    }

    private static boolean isFileValidYaml(File file) {
        boolean flag;
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        try {
            om.readValue(file, Object.class);
            flag = true;
        } catch (IOException e) {
            LOGGER.error("File type validation failed");
            flag = false;
        }
        return flag;
    }

    private static boolean isAllowedFileName(String originalFilename) {
        return isValid(originalFilename)
                && Files.getFileExtension(originalFilename.toLowerCase()).isEmpty();
    }

    private static boolean isValid(String fileName) {
        if (StringUtils.isEmpty(fileName) || fileName.length() > MAX_LENGTH_FILE_NAME) {
            return false;
        }
        fileName = Normalizer.normalize(fileName, Normalizer.Form.NFKC);
        return FILE_NAME_PATTERN.matcher(fileName).matches();
    }
}