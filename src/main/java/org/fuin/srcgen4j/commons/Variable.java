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

import java.net.URL;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.Validate;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.fuin.utils4j.Utils4J;

/**
 * Represents a variable definition (name and value).
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "variable")
public final class Variable {

    @NotEmpty
    @XmlAttribute
    private String name;

    @NotEmpty
    @XmlAttribute
    private String value;

    @Nullable
    @XmlAttribute(name = "url")
    private String urlStr;

    @Nullable
    @XmlAttribute(name = "encoding")
    private String encoding;

    private transient URL url;

    /**
     * Package visible default constructor for deserialization.
     */
    Variable() {
        super();
    }

    /**
     * Constructor with name and value.
     * 
     * @param name
     *            Name to set.
     * @param value
     *            Value to set.
     */
    public Variable(@NotEmpty final String name, @NotEmpty final String value) {
        super();
        Validate.notEmpty(name);
        Validate.notEmpty(value);
        this.name = name;
        this.value = value;
    }

    /**
     * Constructor with name and URL.
     * 
     * @param name
     *            Name to set.
     * @param url
     *            URL that references a text resource.
     * @param encoding
     *            Encoding of the text resource the URL points to.
     */
    public Variable(@NotEmpty final String name, @NotNull final URL url, @NotEmpty final String encoding) {
        super();
        Validate.notEmpty(name);
        Validate.notNull(url);
        Validate.notEmpty(encoding);
        this.name = name;
        this.url = url;
        this.urlStr = url.toString();
        this.encoding = encoding;
    }

    /**
     * Returns the name.
     * 
     * @return Current name.
     */
    @NotEmpty
    public final String getName() {
        return name;
    }

    /**
     * Returns the value. If no value but an URL is defined, the value will be loaded once from the URL. Later calls will only return the
     * cached value.
     * 
     * @return Value or <code>null</code>.
     */
    public final String getValue() {
        if ((value == null) && (urlStr != null)) {
            value = Utils4J.readAsString(getURL(), getEncodingOrDefault(), 1024);
        }
        return value;
    }

    /**
     * Returns the URL.
     * 
     * @return URL or <code>null</code>.
     */
    public final URL getURL() {
        if (url == null) {
            try {
                url = Utils4J.url(urlStr);
            } catch (final RuntimeException ex) {
                throw new RuntimeException("Variable '" + getName() + "' has illegal URL: " + urlStr, ex);
            }
        }
        return url;
    }

    /**
     * Returns the encoding to use for reading the value from the URL.
     * 
     * @return Encoding or <code>null</code>.
     */
    public final String getEncoding() {
        return encoding;
    }

    /**
     * Returns the encoding to use for reading the value from the URL. If no encoding is defined this method returns 'utf-8' as default.
     * 
     * @return Encoding - Never <code>null</code>.
     */
    public final String getEncodingOrDefault() {
        if (encoding == null) {
            return "utf-8";
        }
        return encoding;
    }

    /**
     * Replaces variables (if defined) in the value.
     * 
     * @param vars
     *            Variables to use.
     */
    public final void init(@Nullable final Map<String, String> vars) {
        value = Utils4J.replaceVars(getValue(), vars);
    }

    // CHECKSTYLE:OFF Generated code
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Variable other = (Variable) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }
    // CHECKSTYLE:ON

    @Override
    public final String toString() {
        return "Variable [name=" + name + ", value=" + value + ", urlStr=" + urlStr + ", encoding=" + encoding + "]";
    }

}
