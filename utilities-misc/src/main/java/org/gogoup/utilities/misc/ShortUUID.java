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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

public class ShortUUID {
    
    public static String randomUUID() {
        try {
            return new String(Base64.encodeBytes(toByteArray(UUID.randomUUID()), Base64.URL_SAFE)).toLowerCase();
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
    
}
