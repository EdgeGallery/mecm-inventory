/* Copyright 2020-2021 Huawei Technologies Co., Ltd.
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
 */

package org.edgegallery.mecm.inventory.service;

import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class FileCheckTest {

    @Test(expected = IllegalArgumentException.class)
    public void testBlankSpaceFile() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:New File");
        FileChecker.check(file);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidYamlFile() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:File123");
        FileChecker.check(file);
    }

}
