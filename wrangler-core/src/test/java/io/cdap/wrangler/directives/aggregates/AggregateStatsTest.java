
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

 package io.cdap.wrangler.directives.aggregates;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.cdap.cdap.etl.api.Lookup;
import io.cdap.cdap.etl.api.StageMetrics;
import io.cdap.directives.aggregates.AggregateStats;
import io.cdap.wrangler.api.ExecutorContext;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.TransientStore;

public class AggregateStatsTest {

    private AggregateStats directive;

    @Before
    public void setup() {
        directive = new AggregateStats(); // Assuming no args or default
    }

    @Test
    public void testAggregateSumAndCount() throws Exception {
        List<Row> rows = Arrays.asList(
            new Row("col1", 10),
            new Row("col1", 20),
            new Row("col1", 30)
        );

        List<Row> result = directive.execute(rows, new DummyExecutorContext());

        Row agg = result.get(0);
        Assert.assertEquals(60, agg.getValue("col1_sum"));
        Assert.assertEquals(3, agg.getValue("col1_count"));
    }

    public static class DummyExecutorContext implements ExecutorContext {
        public boolean isLast() { return true; }

        @Override
        public Environment getEnvironment() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getNamespace() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public StageMetrics getMetrics() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getContextName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Map<String, String> getProperties() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public URL getService(String applicationId, String serviceId) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public TransientStore getTransientStore() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T> Lookup<T> provide(String table, Map<String, String> arguments) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
