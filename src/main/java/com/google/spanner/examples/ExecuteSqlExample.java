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
import com.google.cloud.spanner.ResultSet;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerOptions;
import com.google.cloud.spanner.Statement;

public class ExecuteSqlExample {

  public void run(String projectId, String instanceId, String databaseId) {
    final SpannerOptions options = SpannerOptions.newBuilder().build();
    final Spanner spanner = options.getService();
    final DatabaseId id = DatabaseId.of(projectId, instanceId, databaseId);
    final DatabaseClient databaseClient = spanner.getDatabaseClient(id);

    try (ResultSet resultSet = databaseClient
        .singleUse()
        .executeQuery(Statement
                .newBuilder("SELECT * FROM Singers WHERE SingerId BETWEEN @from AND @to")
                .bind("from")
                .to(1L)
                .bind("to")
                .to(10L)
                .build(),
            Options.priority(RpcPriority.LOW))
    ) {
      while (resultSet.next()) {
        final long singerId = resultSet.getLong("SingerId");
        final String firstName = resultSet.getString("FirstName");
        final String lastName = resultSet.getString("LastName");

        System.out.println(
            "[ExecuteSql] Retrieved row: " + singerId + ", " + firstName + ", " + lastName);
      }
    } finally {
      spanner.close();
    }
  }

}
