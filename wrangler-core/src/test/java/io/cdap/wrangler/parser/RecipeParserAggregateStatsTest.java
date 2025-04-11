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

package io.cdap.wrangler.parser;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.DirectiveParseException;
import io.cdap.wrangler.proto.Recipe;

public class RecipeParserAggregateStatsTest {

    @Test
    public void testAggregateStatsParsing() throws Exception {
        List<String> recipe = Arrays.asList("aggregate-stats col1 col2");
        Recipe parser = new Recipe(recipe, null, null);
        List<Directive> directives = parser.parse();

        Assert.assertEquals(1, directives.size());
        Assert.assertEquals("aggregate-stats", directives.get(0).name());
    }

    @Test(expected = DirectiveParseException.class)
    public void testInvalidAggregateStatsSyntax() throws Exception {
        List<String> recipe = Arrays.asList("aggregate-stats"); // Missing arguments
        Recipe parser = new Recipe(recipe, null, null);
        parser.parse(); // Should throw
    }
} 
