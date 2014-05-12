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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
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
     * Returns the value of the variable.
     * 
     * @return Current value.
     */
    @Nullable
    public final String getValue() {
        if ((value == null) && (urlStr != null)) {
            value = read(getURL(), encoding);
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
     * @return URL.
     */
    @Nullable
    public final URL getURL() {
        if (url == null) {
            url = asURL(getName(), urlStr);
        }
        return url;
    }

    private static URL asURL(final String variable, final String url) {
        try {
            if (url.startsWith("classpath:")) {
                return new URL(null, url, new ClasspathURLStreamHandler());
            }
            return new URL(url);
        } catch (final MalformedURLException ex) {
            throw new IllegalArgumentException("Variable '" + variable
                    + "' has invalid URL: " + url, ex);
        }
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

    private static String read(final URL url, final String encoding) {
        final String enc;
        if (encoding == null) {
            enc = "utf-8";
        } else {
            enc = encoding;
        }
        try {
            final Reader reader = new InputStreamReader(url.openStream(), enc);
            try {
                final StringBuilder sb = new StringBuilder();
                final char[] cbuf = new char[1024];
                int count;
                while ((count = reader.read(cbuf)) > -1) {
                    sb.append(String.valueOf(cbuf, 0, count));
                }
                return sb.toString();
            } finally {
                reader.close();
            }
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    // CHECKSTYLE:ON

}
