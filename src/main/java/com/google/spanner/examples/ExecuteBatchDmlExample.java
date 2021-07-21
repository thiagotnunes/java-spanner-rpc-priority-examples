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

import com.google.cloud.spanner.DatabaseClient;
import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.Options;
import com.google.cloud.spanner.Options.RpcPriority;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerOptions;
import com.google.cloud.spanner.Statement;
import java.util.ArrayList;
import java.util.List;

public class ExecuteBatchDmlExample {

  public void run(String projectId, String instanceId, String databaseId) {
    final SpannerOptions options = SpannerOptions.newBuilder().build();
    final Spanner spanner = options.getService();
    final DatabaseId id = DatabaseId.of(projectId, instanceId, databaseId);
    final DatabaseClient databaseClient = spanner.getDatabaseClient(id);

    try {
      System.out.println("[ExecuteBatchDml] Batch Updating Singer 1 and Singer 2");
      databaseClient
          .readWriteTransaction()
          .run(transaction -> {
            final List<Statement> statements = new ArrayList<>();
            statements.add(
                Statement.of("UPDATE Singers SET FirstName = 'Batch First Name' WHERE SingerId = 1")
            );
            statements.add(
                Statement.of("UPDATE Singers SET LastName = 'Batch Last Name' WHERE SingerId = 2")
            );
            transaction.batchUpdate(statements, Options.priority(RpcPriority.LOW));
            return null;
          });
    } finally {
      spanner.close();
    }
  }
}
