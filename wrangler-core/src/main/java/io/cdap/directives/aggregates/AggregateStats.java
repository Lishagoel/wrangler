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

package io.cdap.directives.aggregates;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.cdap.wrangler.api.Arguments;
import io.cdap.wrangler.api.DirectiveExecutionException;
import io.cdap.wrangler.api.DirectiveParseException;
import io.cdap.wrangler.api.ExecutorContext;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.ColumnName;
import io.cdap.wrangler.api.parser.TimeDuration;
import io.cdap.wrangler.api.parser.TokenType;
import io.cdap.wrangler.api.parser.UsageDefinition;

public class AggregateStats implements io.cdap.wrangler.api.Directive {
    private String sizeColumn;
    private String timeColumn;
    private String totalSizeColumn;
    private String totalTimeColumn;
    private String sizeUnit;
    private String timeUnit;

    @Override
    public UsageDefinition define() {
        UsageDefinition.Builder builder = UsageDefinition.builder("aggregate-stats");
        builder.define("source_size_column", TokenType.COLUMN_NAME);
        builder.define("source_time_column", TokenType.COLUMN_NAME);
        builder.define("target_size_column", TokenType.COLUMN_NAME);
        builder.define("target_time_column", TokenType.COLUMN_NAME);
        builder.define("size_unit", TokenType.TEXT, Optional.of("MB"));
        builder.define("time_unit", TokenType.TEXT, Optional.of("s"));
        return builder.build();
    }

    @Override
    public void initialize(Arguments args) throws DirectiveParseException {
        this.sizeColumn = ((ColumnName) args.value("source_size_column")).value();
        this.timeColumn = ((ColumnName) args.value("source_time_column")).value();
        this.totalSizeColumn = ((ColumnName) args.value("target_size_column")).value();
        this.totalTimeColumn = ((ColumnName) args.value("target_time_column")).value();
        // Corrected casting here based on UsageDefinition
        this.sizeUnit = ((io.cdap.wrangler.api.parser.Text) args.value("size_unit")).value();
        this.timeUnit = ((io.cdap.wrangler.api.parser.Text) args.value("time_unit")).value();
    }

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext context) throws DirectiveExecutionException {
        // Get or create store for aggregation
        Map<String, Object> store = (Map<String, Object>) context.getTransientStore();

        // Initialize counters if not present
        if (!store.containsKey("totalBytes")) {
            store.put("totalBytes", 0L);
            store.put("totalNanos", 0L);
            store.put("rowCount", 0);
        }

        // Aggregate values from all rows
        for (Row row : rows) {
            if (row.has(sizeColumn) && row.has(timeColumn)) {
                Object sizeObj = row.getValue(sizeColumn);
                Object timeObj = row.getValue(timeColumn);

                long bytes = 0;
                double nanos = 0;

                // Parse size value
                if (sizeObj instanceof ByteSize) {
                    bytes = ((ByteSize) sizeObj).getBytes();
                } else if (sizeObj instanceof String) {
                    bytes = new ByteSize((String) sizeObj).getBytes();
                }

                // Parse time value
                if (timeObj instanceof TimeDuration) {
                    nanos = ((TimeDuration) timeObj).toSeconds();
                } else if (timeObj instanceof String) {
                    nanos = new TimeDuration((String) timeObj).toSeconds();
                }

                // Update totals
                store.put("totalBytes", (long) store.get("totalBytes") + bytes);
                store.put("totalNanos", (long) store.get("totalNanos") + nanos);
                store.put("rowCount", (int) store.get("rowCount") + 1);
            }
        }

        // Check if this is the last batch
        if (context.isLast()) {
            // Convert totals to requested units
            double totalSizeInRequestedUnit = convertBytes((long) store.get("totalBytes"), sizeUnit);
            double totalTimeInRequestedUnit = convertNanos((long) store.get("totalNanos"), timeUnit);

            // Create result row
            Row resultRow = new Row();
            resultRow.add(totalSizeColumn, totalSizeInRequestedUnit);
            resultRow.add(totalTimeColumn, totalTimeInRequestedUnit);

            return Collections.singletonList(resultRow);
        }

        // Return empty list for intermediate batches
        return Collections.emptyList();
    }

    private double convertBytes(long bytes, String unit) {
        switch (unit.toUpperCase()) {
            case "KB": return bytes / 1024.0;
            case "MB": return bytes / (1024.0 * 1024.0);
            case "GB": return bytes / (1024.0 * 1024.0 * 1024.0);
            default: return bytes;
        }
    }

    private double convertNanos(long nanos, String unit) {
        switch (unit.toLowerCase()) {
            case "ms": return nanos / 1_000_000.0;
            case "s": return nanos / 1_000_000_000.0;
            case "m": return nanos / (60.0 * 1_000_000_000.0);
            default: return nanos;
        }
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'destroy'");
    }

    @Override
    public Object name() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'name'");
    }
}