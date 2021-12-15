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

import com.alibaba.fastjson.JSONObject;
import java.util.Map;

public class InitParamsUtil {

    /**
     * Transfer data from json string to params.
     *
     * @param params params
     * @return
     */
    public static Map<String, Object> handleParams(String params) {
        Map<String, Object> jsonToMap = JSONObject.parseObject(params);
        return jsonToMap;
    }
}
