/**
 * Oshi (https://github.com/dblock/oshi)
 *
 * Copyright (c) 2010 - 2016 The Oshi Project Team
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Maintainers:
 * dblock[at]dblock[dot]org
 * widdis[at]gmail[dot]com
 * enrico.bianchi[at]gmail[dot]com
 *
 * Contributors:
 * https://github.com/dblock/oshi/graphs/contributors
 */
package oshi.json;

import java.util.Properties;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import oshi.json.hardware.HardwareAbstractionLayer;
import oshi.json.hardware.impl.HardwareAbstractionLayerImpl;
import oshi.json.json.AbstractOshiJsonObject;
import oshi.json.json.NullAwareJsonObjectBuilder;
import oshi.json.json.OshiJsonObject;
import oshi.json.software.os.OperatingSystem;
import oshi.json.software.os.impl.OperatingSystemImpl;
import oshi.json.util.PropertiesUtil;

/**
 * System information. This is the main entry point to Oshi. This object
 * provides getters which instantiate the appropriate platform-specific
 * implementations of {@link OperatingSystem} (software) and
 * {@link HardwareAbstractionLayer} (hardware).
 * 
 * @author dblock[at]dblock[dot]org
 */
public class SystemInfo extends AbstractOshiJsonObject implements OshiJsonObject {

    private static final long serialVersionUID = 1L;

    private oshi.SystemInfo si;

    private OperatingSystem os = null;

    private HardwareAbstractionLayer hardware = null;

    private JsonBuilderFactory jsonFactory = Json.createBuilderFactory(null);

    public SystemInfo() {
        this.si = new oshi.SystemInfo();
    }

    /**
     * Creates a new instance of the appropriate platform-specific
     * {@link OperatingSystem}.
     * 
     * @return A new instance of {@link OperatingSystem}.
     */
    public OperatingSystem getOperatingSystem() {
        if (this.os == null) {
            this.os = new OperatingSystemImpl(si.getOperatingSystem());
        }
        return this.os;
    }

    /**
     * Creates a new instance of the appropriate platform-specific
     * {@link HardwareAbstractionLayer}.
     * 
     * @return A new instance of {@link HardwareAbstractionLayer}.
     */
    public HardwareAbstractionLayer getHardware() {
        if (this.hardware == null) {
            this.hardware = new HardwareAbstractionLayerImpl(si.getHardware());
        }
        return this.hardware;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonObject toJSON(Properties properties) {
        JsonObjectBuilder json = NullAwareJsonObjectBuilder.wrap(jsonFactory.createObjectBuilder());
        if (PropertiesUtil.getBoolean(properties, "platform")) {
            json.add("platform", si.getCurrentPlatformEnum().toString());
        }
        if (PropertiesUtil.getBoolean(properties, "operatingSystem")) {
            json.add("operatingSystem", getOperatingSystem().toJSON(properties));
        }
        if (PropertiesUtil.getBoolean(properties, "hardware")) {
            json.add("hardware", getHardware().toJSON(properties));
        }
        return json.build();
    }
}