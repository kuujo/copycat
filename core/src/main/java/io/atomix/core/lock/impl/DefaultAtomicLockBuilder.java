/*
 * Copyright 2017-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.atomix.core.lock.impl;

import io.atomix.core.lock.AsyncAtomicLock;
import io.atomix.core.lock.AtomicLock;
import io.atomix.core.lock.AtomicLockBuilder;
import io.atomix.core.lock.AtomicLockConfig;
import io.atomix.primitive.PrimitiveManagementService;
import io.atomix.primitive.proxy.ProxyClient;
import io.atomix.primitive.service.ServiceConfig;

import java.util.concurrent.CompletableFuture;

/**
 * Default distributed lock builder implementation.
 */
public class DefaultAtomicLockBuilder extends AtomicLockBuilder {
  public DefaultAtomicLockBuilder(String name, AtomicLockConfig config, PrimitiveManagementService managementService) {
    super(name, config, managementService);
  }

  @Override
  @SuppressWarnings("unchecked")
  public CompletableFuture<AtomicLock> buildAsync() {
    ProxyClient<AtomicLockService> proxy = protocol().newProxy(
        name(),
        primitiveType(),
        AtomicLockService.class,
        new ServiceConfig(),
        managementService.getPartitionService());
    return new AtomicLockProxy(proxy, managementService.getPrimitiveRegistry())
        .connect()
        .thenApply(AsyncAtomicLock::sync);
  }
}
