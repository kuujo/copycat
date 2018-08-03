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
package io.atomix.rest.resources;

import com.google.common.collect.Maps;
import io.atomix.core.PrimitivesService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Primitives resource.
 */
@Path("/v1/primitives")
public class PrimitivesResource extends AbstractRestResource {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPrimitives(@Context PrimitivesService primitives) {
    Map<String, PrimitiveInfo> primitivesInfo = primitives.getPrimitives()
        .stream()
        .map(info -> Maps.immutableEntry(info.name(), new PrimitiveInfo(info.name(), info.type().name())))
        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    return Response.ok(primitivesInfo).build();
  }

  static class PrimitiveInfo {
    private String name;
    private String type;

    PrimitiveInfo(String name, String type) {
      this.name = name;
      this.type = type;
    }

    public String getName() {
      return name;
    }

    public String getType() {
      return type;
    }
  }
}
