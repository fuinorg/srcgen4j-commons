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

import static org.fuin.utils4j.Utils4J.replaceVars;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.fuin.xmlcfg4j.AbstractElement;

/**
 * Specific parser configuration example.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "input", namespace = AbstractTest.NS_TEST)
public class TestInput extends AbstractElement implements InitializableElement<TestInput, Config<ParserConfig>> {

    @XmlAttribute
    private String path;

    /**
     * Default constructor.
     */
    public TestInput() {
        super();
    }

    /**
     * Constructor with path.
     * 
     * @param path
     *            Path.
     */
    public TestInput(final String path) {
        super();
        this.path = path;
    }

    /**
     * Returns the path.
     * 
     * @return Path information.
     */
    public final String getPath() {
        return path;
    }

    /**
     * Sets the path to a new value.
     * 
     * @param path
     *            Path information to set.
     */
    public final void setPath(final String path) {
        this.path = path;
    }

    @Override
    public final TestInput init(final SrcGen4JContext context, final Config<ParserConfig> parent, final Map<String, String> vars) {
        setPath(replaceVars(getPath(), vars));
        return this;
    }

}
