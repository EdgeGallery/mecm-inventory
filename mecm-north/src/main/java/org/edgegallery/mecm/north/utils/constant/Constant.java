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

package org.edgegallery.mecm.north.utils.constant;

public interface Constant {

    String EMPTY = "";

    String TASK_ID = "taskId";

    String UNDER_LINE = "_";

    int MAX_TASK_THREAD_NUM = 10;

    String PROVIDER_ID = "app_provider_id";

    String APP_NAME = "app_product_name";

    String APP_VERSION = "app_package_version";

    String ARCHITECTURE = "app_architecture";

    String ACCESS_TOKEN = "access_token";

    String USER_ID = "userId";

    String USER_NAME = "userName";

    String TENANT_ID = "tenantId";

    String APP_INSTANCE_ID = "appInstanceId";

    String APP_CLASS = "app_class";

    String FAIL_TO_DISTRIBUTE_STATUS = "failed to distribute";

    String UPLOADED_STATUS = "Uploaded";

    String DISTRIBUTING_STATUS = "Distributing";

    String DISTRIBUTED_STATUS = "Distributed";

    String DISTRIBUTE_ERROR_STATUS = "Distribute Error";

    String INSTANTIATING_STATUS = "Instantiating";

    String INSTANTIATED_STATUS = "Instantiated";

    String INSTANTIATE_ERROR_STATUS = "Instantiate Error";

    String FINISHED_STATUS = "Finished";

    String INVALID_ACCESS_TOKEN = "Invalid access token";

    String DOT = ".";

    String COLON = ":";

    String SLASH = "/";

    String COMMA = ",";

    String STRIKE = "-";

    String DEFINITIONS = "Definition";

    String APP_ID = "appId";

    String PACKAGE_ID = "packageId";

    String SUCCESS = "success";

    String RUNNING = "running";

    String FAILED = "failed";

    String WAITING = "waiting";

    String CREATED = "Created";

    String CREATING = "Creating";

    String CREATE_ERROR = "Create Error";

    String REG = "[^\\s\\\\/:*?\"<>|](\\x20|[^\\s\\\\/:*?\"<>|])*[^\\s\\\\/:*?\"<>|.]$";

    int MAX_LENGTH_FILE_NAME = 255;

    String PACKAGE_YAML_FORMAT = ".yaml";

    int BUFFER = 512;

    long TOO_BIG = 0x280000000L; // max size of unzipped data, 10GB

    int TOO_MANY = 1024; // max number of files

    String NODE_TEMPLATES = "node_templates";

    String APP_CONFIGURATION = "app_configuration";

    String PROPERTIES = "properties";

    String APP_SERVICE_REQUIRED = "appServiceRequired";

    String APPLICATION_JSON = "application/json";

    String JAVA = "java";

    String PYTHON = "python";

    String JAR = "jar";

    String JAVA_FILE = ".java";

    String TEST_CASE_DIR = "testCases";

    String LOCALE_CH = "ch";

    String LOCALE_EN = "en";

    String FILE_TYPE_SCENARIO = "scenario";

    String ICON = "icon";

    String TASK_TYPE_MANUAL = "manual";

    String TASK_TYPE_AUTOMATIC = "automatic";

    String CONTRIBUTION_TYPE_SCRIPT = "script";

    int LENGTH_64 = 64;

    int LENGTH_255 = 255;

    int LENGTH_2000 = 2000;

    String REG_ID = "[0-9a-f]{8}(-[0-9a-f]{4}){3}-[0-9a-f]{12}";

    String ZIP = ".zip";

    String ID = "id";

    String NAME_EN = "nameEn";

    String TYPE = "type";

    String ERROR_CODE = "errCode";

    String ERROR_MSG = "errMsg";

    String PARAMS = "params";

    String TEST_SCENARIO = "testScenario";

    String TEST_SUITE = "testSuite";

    String TEST_CASE = "testCase";

    String APM_SERVER_ADDRESS = "apmServerAddress";

    String APPO_SERVER_ADDRESS = "appoServerAddress";

    String INVENTORY_SERVER_ADDRESS = "inventoryServerAddress";

    String APPSTORE_SERSVER_ADDRESS = "appstoreServerAddress";

    String SEMICOLON = ";";

    String EQUAL_MARK = "=";

    String CONFIG_PARAM_LIST = "configParamList";

    String SIGNATURE_RESULT = "signatureResult";

    String CONFIG_ID = "config id";

    String TEST_CASE_ID = "test case id";

    int STATUS_ERROR = 1;

    int STATUS_FINISHED = 0;

    int STATUS_DISTRIBUTING = 2;

    int STATUS_DISTRIBUTED = 3;

    int STATUS_INSTANTIATING = 4;

    int STATUS_INSTANTIATED = 5;

    int STATUS_CREATED = 6;

}
