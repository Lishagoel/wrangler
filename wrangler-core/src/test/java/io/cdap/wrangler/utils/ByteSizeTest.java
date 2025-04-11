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

package io.cdap.wrangler.utils;

import org.junit.Assert;
import org.junit.Test;

import io.cdap.wrangler.api.parser.ByteSize;

public class ByteSizeTest {

    @Test
    public void testValidSizes() {
        Assert.assertEquals(1024L, new ByteSize("1KB").getSize());
        Assert.assertEquals(1048576L, new ByteSize("1MB").getSize());
        Assert.assertEquals(1073741824L, new ByteSize("1GB").getSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        new ByteSize("1XB");
    }

    @Test
    public void testZero() {
        Assert.assertEquals(0L, new ByteSize("0B").getSize());
    }
}
