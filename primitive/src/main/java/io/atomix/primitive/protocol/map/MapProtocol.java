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
package io.atomix.primitive.protocol.map;

import java.util.Map;

/**
 * Gossip map protocol.
 */
public interface MapProtocol<K, V> extends Map<K, V> {

  /**
   * Returns the map name.
   *
   * @return the map name
   */
  String name();

  /**
   * Adds the specified listener to the map which will be notified whenever the mappings in the map are changed.
   *
   * @param listener listener to register for events
   */
  void addListener(MapProtocolEventListener<K, V> listener);

  /**
   * Removes the specified listener from the map such that it will no longer receive change notifications.
   *
   * @param listener listener to deregister for events
   */
  void removeListener(MapProtocolEventListener<K, V> listener);

  /**
   * Closes the map.
   */
  void close();
}
