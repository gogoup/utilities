/*******************************************************************************
 * Copyright 2014 Rui Sun
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
 *******************************************************************************/
package org.gogoup.utilities.misc;

import com.fasterxml.uuid.Generators;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

public class ShortUUID {
    
    public static String randomUUID() {
        try {
            String id = new String(Base64.encodeBytes(toByteArray(UUID.randomUUID()), Base64.URL_SAFE)).toLowerCase();
            return id.replace("=", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static byte[] toByteArray(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public static String generateOrderedTimeBasedUUID() {
        UUID uuid = Generators.timeBasedGenerator().generate();
        String uuidString = uuid.toString();
        String orderedUUIDString = uuidString.substring(14, 18)
                + uuidString.substring(9, 13)
                + uuidString.substring(0, 8)
                + uuidString.substring(19, 23)
                + uuidString.substring(24);
        return orderedUUIDString;
    }

    public static byte[] convertHexStringToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String convertBytesToHexString(byte[] bytes) {
        char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j*2] = hexArray[v/16];
            hexChars[j*2 + 1] = hexArray[v%16];
        }
        return new String(hexChars);
    }

    public static String generateBase64HMACUUID() {
        try {
            return Base64.encodeBytes(generateHMACUUID(), Base64.URL_SAFE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateHexHMACUUID() {
        return convertBytesToHexString(generateHMACUUID());
    }

    private static byte[] generateHMACUUID() {
        return generateHMACUUID(ShortUUID.randomUUID());
    }

    public static byte[] generateHMACUUID(String data) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] key = new byte[24];
            random.nextBytes(key);

            SecretKeySpec secretKey = new SecretKeySpec(key, "HmacSHA512");
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(secretKey);
            return mac.doFinal(data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

}
