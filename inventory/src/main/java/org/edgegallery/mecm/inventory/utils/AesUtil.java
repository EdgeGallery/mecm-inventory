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

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.edgegallery.mecm.inventory.exception.InventoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AesUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AesUtil.class);

    private AesUtil() {
    }

    /**
     * AES encryption.
     */
    public static String encode(String clientId, String data) {
        String thisKey = generateKey(clientId);
        if (thisKey == null) {
            return null;
        }
        try {
            Key key = new SecretKeySpec(thisKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            LOGGER.error("Failed to encode");
            throw new InventoryException(e.getMessage());
        }
    }

    /**
     * AES decryption.
     */
    public static String decode(String clientId, String data) {
        String thisKey = generateKey(clientId);
        if (thisKey == null) {
            return null;
        }
        try {
            Key key = new SecretKeySpec(thisKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(Base64.getDecoder().decode(data));
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOGGER.error("Failed to decode");
            throw new InventoryException(e.getMessage());
        }
    }

    /**
     * Generate key.
     */
    public static String generateKey(String clientId) {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(clientId.getBytes(StandardCharsets.UTF_8));
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(secureRandom);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] byteKey = secretKey.getEncoded();
            return Base64.getEncoder().encodeToString(byteKey);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Failed to generate encryption key");
            throw new InventoryException(e.getMessage());
        }
    }

}
