/*
 * Copyright © 2025 Lishagoel
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.wrangler.api.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class ByteSize implements Token {
    private final String rawValue;
    private final long bytes;

    /**
     * Constructs a new {@code ByteSize} from the given string value.
     *
     * @param value The string representation of the byte size (e.g., "10KB", "2GB").
     */
    public ByteSize(String value) {
        this.rawValue = value;
        // Parse the value (e.g., "10KB") into bytes
        // Extract number and unit, then convert to bytes
        String numStr = value.replaceAll("[^0-9.]", "");
        String unit = value.replaceAll("[0-9.]", "").toUpperCase();
        double num = Double.parseDouble(numStr);

        switch (unit) {
            case "KB":
                bytes = (long) (num * 1024);
                break;
            case "MB":
                bytes = (long) (num * 1024 * 1024);
                break;
            case "GB":
                bytes = (long) (num * 1024 * 1024 * 1024);
                break;
            case "TB":
                bytes = (long) (num * 1024 * 1024 * 1024 * 1024);
                break;
            default:
                bytes = (long) num;
        }
    }

    public long getBytes() {
        return bytes;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(rawValue);
    }

    @Override
    public String value() {
        return rawValue;
    }

    @Override
    public TokenType type() {
        return TokenType.BYTE_SIZE; // You'll need to add this to TokenType enum
    }

    // Add methods to convert to KB, MB, etc.
    public double toKB() {
        return bytes / 1024.0;
    }

    public double toMB() {
        return bytes / (1024.0 * 1024.0);
    }

    public double toGB() {
        return bytes / (1024.0 * 1024.0 * 1024.0);
    }

    public Object getSize() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSize'");
    }
}
