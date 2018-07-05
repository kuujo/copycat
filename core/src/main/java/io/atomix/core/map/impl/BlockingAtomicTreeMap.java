/*
 * Copyright 2016-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.atomix.core.map.impl;

import io.atomix.core.map.AsyncAtomicTreeMap;
import io.atomix.core.map.AtomicTreeMap;

/**
 * Default implementation of {@code AtomicTreeMap}.
 *
 * @param <K> type of key.
 * @param <V> type of value.
 */
public class BlockingAtomicTreeMap<K extends Comparable<K>, V> extends BlockingAtomicNavigableMap<K, V> implements AtomicTreeMap<K, V> {

  private final AsyncAtomicTreeMap<K, V> asyncMap;

  public BlockingAtomicTreeMap(AsyncAtomicTreeMap<K, V> asyncMap, long operationTimeoutMillis) {
    super(asyncMap, operationTimeoutMillis);
    this.asyncMap = asyncMap;
  }

  @Override
  public AsyncAtomicTreeMap<K, V> async() {
    return asyncMap;
  }
}