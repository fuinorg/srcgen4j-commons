/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.srcgen4j.commons;

import java.util.Map;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Parser and Generator specific configurations are wrapped with a "config" tag. Otherwise the "xs:any" causes problems together with other
 * elements.
 * 
 * @param <PARENT>
 *            Parent type.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public final class Config<PARENT> implements InitializableElement<Config<PARENT>, PARENT> {

    @Nullable
    @Valid
    @XmlAnyElement(lax = true)
    private Object cfg;

    /**
     * Package visible default constructor for deserialization.
     */
    Config() {
        super();
    }

    /**
     * Constructor with specific configuration.
     * 
     * @param config
     *            Configuration.
     */
    public Config(@Nullable final Object config) {
        super();
        this.cfg = config;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Config<PARENT> init(final SrcGen4JContext context, final PARENT parent, final Map<String, String> vars) {
        if (cfg instanceof InitializableElement) {
            final InitializableElement<?, Config<PARENT>> ie;
            ie = (InitializableElement<?, Config<PARENT>>) cfg;
            ie.init(context, this, vars);
        }
        return this;
    }

    /**
     * Returns the specific configuration.
     * 
     * @return Configuration.
     */
    @Nullable
    public final Object getCfg() {
        return cfg;
    }

    /**
     * Sets the specific configuration.
     * 
     * @param config
     *            Configuration.
     */
    public final void setCfg(@Nullable final Object config) {
        this.cfg = config;
    }

}
