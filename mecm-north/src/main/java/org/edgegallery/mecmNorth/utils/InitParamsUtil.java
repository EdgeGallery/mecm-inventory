package org.edgegallery.mecmNorth.utils;

import com.alibaba.fastjson.JSONObject;
import java.util.Map;

public class InitParamsUtil {

    /**
     * Transfer data from json string to params.
     * @param params
     * @return
     */
    public static Map<String, Object> handleParams(String params) {
        Map<String, Object> jsonToMap =  JSONObject.parseObject(params);
        return jsonToMap;
    }
}
