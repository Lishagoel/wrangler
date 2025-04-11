/*
 * Copyright © 2025 Lishgoel
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

public class TimeDuration implements Token {
    private final String rawValue;
    private final long nanoseconds;

    /**
     * Constructs a new {@code TimeDuration} from the given string value.
     *
     * @param value The string representation of the time duration (e.g., "150ms", "2s").
     */
    public TimeDuration(String value) {
        this.rawValue = value;
        // Parse the value (e.g., "150ms", "2s") into nanoseconds
        String numStr = value.replaceAll("[^0-9.]", "");
        String unit = value.replaceAll("[0-9.]", "").toLowerCase();
        double num = Double.parseDouble(numStr);

        switch (unit) {
            case "ns":
                nanoseconds = (long) num;
                break;
            case "us":
            case "μs":
                nanoseconds = (long) (num * 1000);
                break;
            case "ms":
                nanoseconds = (long) (num * 1000 * 1000);
                break;
            case "s":
                nanoseconds = (long) (num * 1000 * 1000 * 1000);
                break;
            case "m":
                nanoseconds = (long) (num * 60 * 1000 * 1000 * 1000);
                break;
            case "h":
                nanoseconds = (long) (num * 60 * 60 * 1000 * 1000 * 1000);
                break;
            default:
                nanoseconds = (long) num;
        }
    }

    public long toNanos() {
        return nanoseconds;
    }

    public double toMillis() {
        return nanoseconds / (1000.0 * 1000.0);
    }

    public double toSeconds() {
        return nanoseconds / (1000.0 * 1000.0 * 1000.0);
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
        return TokenType.TIME_DURATION;
    }

    public Object getTime() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTime'");
    }
}
