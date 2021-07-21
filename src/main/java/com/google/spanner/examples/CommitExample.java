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
import com.google.cloud.spanner.Mutation;
import com.google.cloud.spanner.Options;
import com.google.cloud.spanner.Options.RpcPriority;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerOptions;
import java.util.UUID;

public class CommitExample {

  public void run(String projectId, String instanceId, String databaseId) {
    final SpannerOptions options = SpannerOptions.newBuilder().build();
    final Spanner spanner = options.getService();
    final DatabaseId id = DatabaseId.of(projectId, instanceId, databaseId);
    final DatabaseClient databaseClient = spanner.getDatabaseClient(id);
    final String uuid = UUID.randomUUID().toString();

    try {
      System.out.println("[Commit] Updating Singer 1 FirstName to " + uuid);
      databaseClient
          .readWriteTransaction(Options.priority(RpcPriority.LOW))
          .run(transaction -> {
            transaction.buffer(Mutation
                .newInsertOrUpdateBuilder("Singers")
                .set("SingerId")
                .to(1L)
                .set("FirstName")
                .to(uuid)
                .build());
            return null;
          });
    } finally {
      spanner.close();
    }
  }
}
