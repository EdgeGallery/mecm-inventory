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

package org.edgegallery.mecm.inventory.utils;

/**
 * Common constants.
 */
public final class Constants {

    public static final String IP_REGEX = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.)"
            + "{3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
    public static final String PORT_REGEX = "^([1-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d"
            + "|6553[0-5])$";
    public static final String TENANT_ID_REGEX = "[0-9a-f]{8}(-[0-9a-f]{4}){3}-[0-9a-f]{12}";

    public static final String NAME_REGEX = "^[\\d\\p{L}]*$|^[\\d\\p{L}][\\d\\p{L}_\\-]*[\\d\\p{L}]$";
    public static final String CITY_REGEX = "^[\\d\\p{L}]*$|^[\\d\\p{L}][\\d\\p{L}\\/\\s]*[\\d\\p{L}]$";
    public static final String AFFINITY_REGEX = "^[\\d\\p{L}]*$|^[\\d\\p{L}][\\d\\p{L}_\\-\\,]*[\\d\\p{L}]$";

    public static final String URI_REGEX = "^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?";
    public static final int MIN_PWD_SIZE = 8;
    public static final int MAX_PWD_SIZE = 16;
    public static final String SPECIAL_CHAR_REGEX = ".*[`~!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?].*";
    public static final String SINGLE_DIGIT_REGEX = ".*\\d.*";
    public static final String LOWER_CASE_REGEX = ".*[a-z].*";
    public static final String UPPER_CASE_REGEX = ".*[A-Z].*";
    public static final int MAX_PWD_COUNT = 2;

    public static final String APPLCM_URI = "/lcmcontroller/v1/configuration";
    public static final String RECORD_NOT_FOUND_ERROR = "Record not found";

    // Below values can be taken from config file later.
    public static final int MAX_ENTRY_PER_TENANT_PER_MODEL = 50;
    public static final int MAX_TENANTS = 10;
    public static final String MAX_LIMIT_REACHED_ERROR = "Max record limit exceeded";

    public static final String VAR_OVERFLOW_ERROR = "Max count overflow";
    public static final String VAR_UNDERFLOW_ERROR = "Min count underflow";
    public static final String INVALID_MODEL_TYPE = "Model type not supported";

    private Constants() {
    }
}
