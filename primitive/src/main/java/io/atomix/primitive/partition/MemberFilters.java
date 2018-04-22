/*
 * Copyright 2018-present Open Networking Foundation
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
package io.atomix.primitive.partition;

import io.atomix.utils.ConfigurationException;
import io.atomix.utils.Services;

/**
 * Member filters.
 */
public final class MemberFilters {

  /**
   * Returns the member filter with the given type.
   *
   * @param type the member filter type
   * @return the member filter
   */
  public static TypedMemberFilter getTypedFilter(String type) {
    for (TypedMemberFilter filter : Services.loadAll(TypedMemberFilter.class)) {
      if (filter.type().equals(type)) {
        return filter;
      }
    }
    throw new ConfigurationException("Unknown filter type: " + type);
  }

  private MemberFilters() {
  }
}
