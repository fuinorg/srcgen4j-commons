/**
 * Copyright (C) 2013 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
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
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.srcgen4j.commons;

import java.net.URL;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.NotEmpty;
import org.fuin.objects4j.common.Nullable;
import org.fuin.utils4j.Utils4J;

/**
 * Represents a variable definition (name and value).
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "variable")
@XmlType(propOrder = { "xpath", "value" })
public class Variable extends AbstractNamedElement {

    @NotEmpty
    @XmlAttribute
    private String value;

    @Nullable
    @XmlAttribute
    private String xpath;

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
        super(name);
        Contract.requireArgNotEmpty("value", value);
        this.value = value;
    }

    /**
     * Constructor with name, value and xpath.
     * 
     * @param name
     *            Name to set.
     * @param value
     *            Value to set.
     * @param xpath
     *            Value to set.
     */
    public Variable(@NotEmpty final String name, @NotEmpty final String value,
            @NotEmpty final String xpath) {
        super(name);
        Contract.requireArgNotEmpty("value", value);
        Contract.requireArgNotEmpty("xpath", xpath);
        this.value = value;
        this.xpath = xpath;
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
    public Variable(@NotEmpty final String name, @NotNull final URL url,
            @NotNull final String encoding) {
        super(name);
        Contract.requireArgNotNull("url", url);
        Contract.requireArgNotNull("encoding", encoding);
        this.url = url;
        this.urlStr = url.toString();
        this.encoding = encoding;
    }

    /**
     * Returns the value. If no value but an URL is defined, the value will be
     * loaded once from the URL. Later calls will only return the cached value.
     * 
     * @return Value or <code>null</code>.
     */
    public final String getValue() {
        if ((value == null) && (urlStr != null)) {
            value = Utils4J
                    .readAsString(getURL(), getEncodingOrDefault(), 1024);
        }
        return value;
    }

    /**
     * Returns the xpath of the variable.
     * 
     * @return Current xpath.
     */
    @Nullable
    public final String getXpath() {
        return xpath;
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
                throw new RuntimeException("Variable '" + getName()
                        + "' has illegal URL: " + urlStr, ex);
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
     * Returns the encoding to use for reading the value from the URL. If no
     * encoding is defined this method returns 'utf-8' as default.
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
        value = replaceVars(value, vars);
    }

    // CHECKSTYLE:ON

}
