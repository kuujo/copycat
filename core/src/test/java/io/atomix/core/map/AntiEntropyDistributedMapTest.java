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
package io.atomix.core.map;

import io.atomix.core.AbstractPrimitiveTest;
import io.atomix.primitive.protocol.PrimitiveProtocol;
import io.atomix.protocols.gossip.AntiEntropyProtocol;
import io.atomix.utils.time.WallClockTimestamp;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Anti-entropy based distributed map test.
 */
public class AntiEntropyDistributedMapTest extends AbstractPrimitiveTest {

  private static final String KEY1 = "one";
  private static final String KEY2 = "two";
  private static final String VALUE1 = "oneValue";
  private static final String VALUE2 = "twoValue";

  @Override
  protected PrimitiveProtocol protocol() {
    return AntiEntropyProtocol.builder()
        .<Map.Entry<String, String>>withTimestampProvider(entry -> new WallClockTimestamp())
        .build();
  }

  @Test
  public void testReplication() throws Exception {
    DistributedMap<String, String> map1 = atomix().<String, String>mapBuilder("testAntiEntropyMapReplication", protocol()).build();
    DistributedMap<String, String> map2 = atomix().<String, String>mapBuilder("testAntiEntropyMapReplication", protocol()).build();

    TestMapEventListener listener1 = new TestMapEventListener();
    map1.addListener(listener1);

    TestMapEventListener listener2 = new TestMapEventListener();
    map2.addListener(listener2);

    assertEquals(0, map1.size());
    assertTrue(map1.isEmpty());
    assertNull(map1.get(KEY1));

    assertEquals(0, map2.size());
    assertTrue(map2.isEmpty());
    assertNull(map2.get(KEY1));

    map1.put(KEY1, VALUE1);

    MapEvent<String, String> event1;
    MapEvent<String, String> event2;

    event1 = listener1.event();
    assertEquals(MapEvent.Type.INSERT, event1.type());
    assertEquals(KEY1, event1.key());
    assertEquals(VALUE1, event1.newValue());
    assertEquals(VALUE1, map1.get(KEY1));
    assertEquals(1, map1.size());
    assertFalse(map1.isEmpty());

    event2 = listener2.event();
    assertEquals(MapEvent.Type.INSERT, event2.type());
    assertEquals(KEY1, event2.key());
    assertEquals(VALUE1, event2.newValue());
    assertEquals(VALUE1, map2.get(KEY1));
    assertEquals(1, map2.size());
    assertFalse(map2.isEmpty());

    assertEquals(VALUE1, map2.remove(KEY1));

    event2 = listener2.event();
    assertEquals(MapEvent.Type.REMOVE, event2.type());
    assertEquals(KEY1, event2.key());
    assertEquals(VALUE1, event2.oldValue());
    assertNull(map2.get(KEY1));
    assertEquals(0, map2.size());
    assertTrue(map2.isEmpty());

    event1 = listener1.event();
    assertEquals(MapEvent.Type.REMOVE, event1.type());
    assertEquals(KEY1, event1.key());
    assertEquals(VALUE1, event1.oldValue());
    assertNull(map1.get(KEY1));
    assertEquals(0, map1.size());
    assertTrue(map1.isEmpty());
  }

  @Test
  public void testSize() throws Exception {
    DistributedMap<String, String> map = atomix().<String, String>mapBuilder("testAntiEntropyMapSize", protocol()).build();

    assertEquals(0, map.size());
    map.put(KEY1, VALUE1);
    assertEquals(1, map.size());
    map.put(KEY1, VALUE2);
    assertEquals(1, map.size());
    map.put(KEY2, VALUE2);
    assertEquals(2, map.size());
    for (int i = 0; i < 10; i++) {
      map.put("" + i, "" + i);
    }
    assertEquals(12, map.size());
    map.remove(KEY1);
    assertEquals(11, map.size());
    map.remove(KEY1);
    assertEquals(11, map.size());
  }

  @Test
  public void testIsEmpty() throws Exception {
    DistributedMap<String, String> map = atomix().<String, String>mapBuilder("testAntiEntropyMapSize", protocol()).build();

    assertTrue(map.isEmpty());
    map.put(KEY1, VALUE1);
    assertFalse(map.isEmpty());
    map.remove(KEY1);
    assertTrue(map.isEmpty());
  }

  @Test
  public void testContainsKey() throws Exception {
    DistributedMap<String, String> map = atomix().<String, String>mapBuilder("testAntiEntropyMapSize", protocol()).build();

    assertFalse(map.containsKey(KEY1));
    map.put(KEY1, VALUE1);
    assertTrue(map.containsKey(KEY1));
    assertFalse(map.containsKey(KEY2));
    map.remove(KEY1);
    assertFalse(map.containsKey(KEY1));
  }

  @Test
  public void testContainsValue() throws Exception {
    DistributedMap<String, String> map = atomix().<String, String>mapBuilder("testAntiEntropyMapSize", protocol()).build();

    assertFalse(map.containsValue(VALUE1));
    map.put(KEY1, VALUE1);
    assertTrue(map.containsValue(VALUE1));
    assertFalse(map.containsValue(VALUE2));
    map.put(KEY1, VALUE2);
    assertFalse(map.containsValue(VALUE1));
    assertTrue(map.containsValue(VALUE2));
    map.remove(KEY1);
    assertFalse(map.containsValue(VALUE2));
  }

  @Test
  public void testGet() throws Exception {
    DistributedMap<String, String> map = atomix().<String, String>mapBuilder("testAntiEntropyMapSize", protocol()).build();

    // Local put
    assertNull(map.get(KEY1));
    map.put(KEY1, VALUE1);
    assertEquals(VALUE1, map.get(KEY1));

    // Create a latch so we know when the put operation has finished
    TestMapEventListener listener = new TestMapEventListener();
    map.addListener(listener);

    assertNull(map.get(KEY2));
    assertNotNull(listener.event());
    assertEquals(VALUE2, map.get(KEY2));

    // Local remove
    map.remove(KEY2);
    assertNull(map.get(KEY2));

    assertNotNull(listener.event());
    assertNull(map.get(KEY1));
  }

  @Test
  public void testPut() throws Exception {
    DistributedMap<String, String> map = atomix().<String, String>mapBuilder("testAntiEntropyMapSize", protocol()).build();

    TestMapEventListener listener = new TestMapEventListener();
    map.addListener(listener);

    // Put first value
    assertNull(map.get(KEY1));
    map.put(KEY1, VALUE1);
    assertEquals(VALUE1, map.get(KEY1));

    MapEvent<String, String> event = listener.event();
    assertNotNull(event);
    assertEquals(MapEvent.Type.INSERT, event.type());
    assertEquals(KEY1, event.key());
    assertEquals(VALUE1, event.newValue());

    // Update same key to a new value
    map.put(KEY1, VALUE2);
    assertEquals(VALUE2, map.get(KEY1));

    map.put(KEY1, VALUE1);
    // Value should not have changed.
    assertEquals(VALUE2, map.get(KEY1));
  }

  @Test
  public void testRemove() throws Exception {
    DistributedMap<String, String> map = atomix().<String, String>mapBuilder("testAntiEntropyMapSize", protocol()).build();

    TestMapEventListener listener = new TestMapEventListener();
    map.addListener(listener);

    // Put in an initial value
    map.put(KEY1, VALUE1);
    assertEquals(VALUE1, map.get(KEY1));

    MapEvent<String, String> event = listener.event();
    assertNotNull(event);
    assertEquals(MapEvent.Type.INSERT, event.type());
    assertEquals(KEY1, event.key());
    assertEquals(VALUE1, event.newValue());

    map.remove(KEY1);
    assertNull(map.get(KEY1));

    event = listener.event();
    assertNotNull(event);
    assertEquals(MapEvent.Type.REMOVE, event.type());
    assertEquals(KEY1, event.key());
    assertEquals(VALUE1, event.oldValue());

    map.remove(KEY1);
    assertNull(map.get(KEY1));

    assertFalse(listener.eventReceived());

    map.put(KEY2, VALUE2);

    map.remove(KEY2);
  }

  @Test
  public void testCompute() throws Exception {
    DistributedMap<String, String> map = atomix().<String, String>mapBuilder("testAntiEntropyMapSize", protocol()).build();

    TestMapEventListener listener = new TestMapEventListener();
    map.addListener(listener);

    // Put in an initial value
    map.compute(KEY1, (k, v) -> VALUE1);
    assertEquals(VALUE1, map.get(KEY1));

    MapEvent<String, String> event = listener.event();
    assertNotNull(event);
    assertEquals(MapEvent.Type.INSERT, event.type());
    assertEquals(KEY1, event.key());
    assertEquals(VALUE1, event.newValue());

    map.compute(KEY1, (k, v) -> null);
    assertNull(map.get(KEY1));

    event = listener.event();
    assertNotNull(event);
    assertEquals(MapEvent.Type.REMOVE, event.type());
    assertEquals(KEY1, event.key());
    assertEquals(VALUE1, event.oldValue());

    map.compute(KEY1, (k, v) -> null);
    assertNull(map.get(KEY1));

    assertFalse(listener.eventReceived());

    map.compute(KEY2, (k, v) -> VALUE2);

    event = listener.event();
    assertNotNull(event);
    assertEquals(MapEvent.Type.INSERT, event.type());
    assertEquals(KEY2, event.key());
    assertEquals(VALUE2, event.newValue());

    map.compute(KEY2, (k, v) -> null);

    event = listener.event();
    assertNotNull(event);
    assertEquals(MapEvent.Type.REMOVE, event.type());
    assertEquals(KEY2, event.key());
    assertEquals(VALUE2, event.oldValue());
  }

  @Test
  public void testPutAll() throws Exception {
    DistributedMap<String, String> map = atomix().<String, String>mapBuilder("testAntiEntropyMapSize", protocol()).build();

    map.putAll(new HashMap<>());

    TestMapEventListener listener = new TestMapEventListener();
    map.addListener(listener);

    Map<String, String> putAllValues = new HashMap<>();
    putAllValues.put(KEY1, VALUE1);
    putAllValues.put(KEY2, VALUE2);

    MapEvent<String, String> event;

    event = listener.event();
    assertNotNull(event);
    assertEquals(MapEvent.Type.INSERT, event.type());
    assertEquals(KEY1, event.key());
    assertEquals(VALUE1, event.newValue());

    event = listener.event();
    assertNotNull(event);
    assertEquals(MapEvent.Type.INSERT, event.type());
    assertEquals(KEY2, event.key());
    assertEquals(VALUE2, event.newValue());

    // Put the values in the map
    map.putAll(putAllValues);
  }

  @Test
  public void testClear() throws Exception {
    DistributedMap<String, String> map = atomix().<String, String>mapBuilder("testAntiEntropyMapSize", protocol()).build();

    assertTrue(map.isEmpty());
    map.clear();

    // Put some items in the map
    map.put(KEY1, VALUE1);
    map.put(KEY2, VALUE2);

    TestMapEventListener listener = new TestMapEventListener();
    map.addListener(listener);

    map.clear();

    MapEvent<String, String> event;

    event = listener.event();
    assertNotNull(event);
    assertEquals(MapEvent.Type.REMOVE, event.type());
    assertEquals(KEY1, event.key());
    assertEquals(VALUE1, event.oldValue());

    event = listener.event();
    assertNotNull(event);
    assertEquals(MapEvent.Type.REMOVE, event.type());
    assertEquals(KEY2, event.key());
    assertEquals(VALUE2, event.oldValue());
  }

  @Test
  public void testKeySet() throws Exception {
    DistributedMap<String, String> map = atomix().<String, String>mapBuilder("testAntiEntropyMapSize", protocol()).build();

    assertTrue(map.keySet().isEmpty());

    // Generate some keys
    Set<String> keys = new HashSet<>();
    for (int i = 1; i <= 10; i++) {
      keys.add("" + i);
    }

    // Put each key in the map
    keys.forEach(k -> map.put(k, "value" + k));

    // Check keySet() returns the correct value
    assertEquals(keys, map.keySet());

    // Update the value for one of the keys
    map.put(keys.iterator().next(), "new-value");

    // Check the key set is still the same
    assertEquals(keys, map.keySet());

    // Remove a key
    String removeKey = keys.iterator().next();
    keys.remove(removeKey);
    map.remove(removeKey);

    // Check the key set is still correct
    assertEquals(keys, map.keySet());
  }

  @Test
  public void testValues() throws Exception {
    DistributedMap<String, String> map = atomix().<String, String>mapBuilder("testAntiEntropyMapSize", protocol()).build();

    assertTrue(map.values().isEmpty());

    // Generate some values
    Map<String, String> expectedValues = new HashMap<>();
    for (int i = 1; i <= 10; i++) {
      expectedValues.put("" + i, "value" + i);
    }

    // Add them into the map
    expectedValues.entrySet().forEach(e -> map.put(e.getKey(), e.getValue()));

    // Check the values collection is correct
    assertEquals(expectedValues.values().size(), map.values().size());
    expectedValues.values().forEach(v -> assertTrue(map.values().contains(v)));

    // Update the value for one of the keys
    Map.Entry<String, String> first = expectedValues.entrySet().iterator().next();
    expectedValues.put(first.getKey(), "new-value");
    map.put(first.getKey(), "new-value");

    // Check the values collection is still correct
    assertEquals(expectedValues.values().size(), map.values().size());
    expectedValues.values().forEach(v -> assertTrue(map.values().contains(v)));

    // Remove a key
    String removeKey = expectedValues.keySet().iterator().next();
    expectedValues.remove(removeKey);
    map.remove(removeKey);

    // Check the values collection is still correct
    assertEquals(expectedValues.values().size(), map.values().size());
    expectedValues.values().forEach(v -> assertTrue(map.values().contains(v)));
  }

  @Test
  public void testEntrySet() throws Exception {
    DistributedMap<String, String> map = atomix().<String, String>mapBuilder("testAntiEntropyMapSize", protocol()).build();

    assertTrue(map.entrySet().isEmpty());

    // Generate some values
    Map<String, String> expectedValues = new HashMap<>();
    for (int i = 1; i <= 10; i++) {
      expectedValues.put("" + i, "value" + i);
    }

    // Add them into the map
    expectedValues.entrySet().forEach(e -> map.put(e.getKey(), e.getValue()));

    // Check the entry set is correct
    assertTrue(entrySetsAreEqual(expectedValues, map.entrySet()));

    // Update the value for one of the keys
    Map.Entry<String, String> first = expectedValues.entrySet().iterator().next();
    expectedValues.put(first.getKey(), "new-value");
    map.put(first.getKey(), "new-value");

    // Check the entry set is still correct
    assertTrue(entrySetsAreEqual(expectedValues, map.entrySet()));

    // Remove a key
    String removeKey = expectedValues.keySet().iterator().next();
    expectedValues.remove(removeKey);
    map.remove(removeKey);

    // Check the entry set is still correct
    assertTrue(entrySetsAreEqual(expectedValues, map.entrySet()));
  }

  private static boolean entrySetsAreEqual(Map<String, String> expectedMap, Set<Map.Entry<String, String>> actual) {
    if (expectedMap.entrySet().size() != actual.size()) {
      return false;
    }

    for (Map.Entry<String, String> e : actual) {
      if (!expectedMap.containsKey(e.getKey())) {
        return false;
      }
      if (!Objects.equals(expectedMap.get(e.getKey()), e.getValue())) {
        return false;
      }
    }
    return true;
  }

  private static class TestMapEventListener implements MapEventListener<String, String> {
    private final BlockingQueue<MapEvent<String, String>> queue = new LinkedBlockingQueue<>();

    @Override
    public void event(MapEvent<String, String> event) {
      try {
        queue.put(event);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }

    public boolean eventReceived() {
      return !queue.isEmpty();
    }

    public MapEvent<String, String> event() throws InterruptedException {
      return queue.poll(10, TimeUnit.SECONDS);
    }
  }
}
