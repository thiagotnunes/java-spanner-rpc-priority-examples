/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.spanner.examples;

import com.google.cloud.spanner.BatchClient;
import com.google.cloud.spanner.BatchReadOnlyTransaction;
import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.Options;
import com.google.cloud.spanner.Options.RpcPriority;
import com.google.cloud.spanner.Partition;
import com.google.cloud.spanner.PartitionOptions;
import com.google.cloud.spanner.ResultSet;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerOptions;
import com.google.cloud.spanner.Statement;
import com.google.cloud.spanner.TimestampBound;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BatchPartitionQueryExample {

  public void run(String projectId, String instanceId, String databaseId)
      throws InterruptedException {
    final SpannerOptions options = SpannerOptions.newBuilder().build();
    final Spanner spanner = options.getService();
    final DatabaseId id = DatabaseId.of(projectId, instanceId, databaseId);
    final BatchClient batchClient = spanner.getBatchClient(id);
    final ExecutorService executor = Executors.newCachedThreadPool();

    final BatchReadOnlyTransaction transaction = batchClient
        .batchReadOnlyTransaction(TimestampBound.strong());

    final List<Partition> partitions = transaction.partitionQuery(
        PartitionOptions.getDefaultInstance(),
        Statement.of("SELECT * FROM Singers"),
        Options.priority(RpcPriority.LOW)
    );

    try {
      for (Partition partition : partitions) {
        executor.submit(() -> {
          try (ResultSet resultSet = transaction.execute(partition)) {
            while (resultSet.next()) {
              final long singerId = resultSet.getLong("SingerId");
              final String firstName = resultSet.getString("FirstName");
              final String lastName = resultSet.getString("LastName");

              System.out.println(
                  "[ExecuteBatchDml - " + partition + "] Retrieved row: " + singerId + ", "
                      + firstName + ", " + lastName);
            }
          }
        });
      }
    } finally {
      executor.shutdown();
      executor.awaitTermination(1, TimeUnit.HOURS);
      spanner.close();
    }
  }
}
