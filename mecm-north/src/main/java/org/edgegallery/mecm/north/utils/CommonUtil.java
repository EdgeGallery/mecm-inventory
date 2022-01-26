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

package org.edgegallery.mecm.north.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitConfigUtil.class);

    /**
     * Transfer data from json string to params.
     *
     * @param params params
     * @return
     */
    public static Map<String, Object> handleParams(String params) {
        Map<String, Object> resMap = new HashMap<>();
        String[] paramArr = params.split(";");
        for (String param : paramArr) {
            String[] configItem = param.split("=");
            resMap.put(configItem[0].trim(), 1 == configItem.length ? "" : configItem[1].trim());
        }
        return resMap;
    }

    /**
     * delete file.
     *
     * @param filePath file
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        try {
            FileUtils.forceDelete(file);
        } catch (IOException e) {
            LOGGER.error("delete file failed, {}", e.getMessage());
        }
    }

    private CommonUtil() {
        throw new IllegalStateException("Utility class");
    }
}
