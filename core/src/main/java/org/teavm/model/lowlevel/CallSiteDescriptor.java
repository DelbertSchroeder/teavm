/*
 *  Copyright 2016 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.teavm.model.lowlevel;

import java.util.ArrayList;
import java.util.List;

public class CallSiteDescriptor {
    private int id;
    private List<ExceptionHandlerDescriptor> handlers = new ArrayList<>();
    private CallSiteLocation[] locations;

    public CallSiteDescriptor(int id, CallSiteLocation[] locations) {
        this.id = id;
        this.locations = locations != null ? locations.clone() : null;
    }

    public int getId() {
        return id;
    }

    public CallSiteLocation[] getLocations() {
        return locations != null ? locations.clone() : null;
    }

    public List<ExceptionHandlerDescriptor> getHandlers() {
        return handlers;
    }
}
