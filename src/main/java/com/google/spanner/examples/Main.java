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

public class Main {

  // TODO: Developer to update the following constants
  private static final String PROJECT = "my-project";
  private static final String INSTANCE = "my-instance";
  private static final String DATABASE = "my-database";

  public static void main(String[] args) throws Exception {
    final ReadExample readExample = new ReadExample();
    final ExecuteSqlExample executeSqlExample = new ExecuteSqlExample();
    final CommitExample commitExample = new CommitExample();
    final ExecuteBatchDmlExample executeBatchDmlExample = new ExecuteBatchDmlExample();
    final PartitionedDmlExample partitionedDmlExample = new PartitionedDmlExample();
    final BatchPartitionQueryExample batchPartitionQueryExample = new BatchPartitionQueryExample();

    readExample.run(PROJECT, INSTANCE, DATABASE);
    executeSqlExample.run(PROJECT, INSTANCE, DATABASE);
    commitExample.run(PROJECT, INSTANCE, DATABASE);
    executeBatchDmlExample.run(PROJECT, INSTANCE, DATABASE);
    partitionedDmlExample.run(PROJECT, INSTANCE, DATABASE);
    batchPartitionQueryExample.run(PROJECT, INSTANCE, DATABASE);
  }
}
