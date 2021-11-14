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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.FileExists;
import org.fuin.objects4j.common.FileExistsValidator;
import org.fuin.objects4j.common.IsFile;
import org.fuin.objects4j.common.IsFileValidator;

/**
 * Helper to serialize and deserialize objects using JAXB.
 */
public final class JaxbHelper {

    private boolean formattedOutput = true;

    /**
     * Default constructor.
     */
    public JaxbHelper() {
        super();
    }

    /**
     * Constructor with format information.
     * 
     * @param formattedOutput
     *            If XML is formatted TRUE else FALSE.
     */
    public JaxbHelper(final boolean formattedOutput) {
        super();
        this.formattedOutput = formattedOutput;
    }

    /**
     * Checks if the given file contains a start tag within the first 1024 bytes.
     * 
     * @param file
     *            File to check.
     * @param tagName
     *            Name of the tag. A "&lt;" will be added to this name internally to locate the start tag.
     * 
     * @return If the file contains the start tag TRUE else FALSE.
     */
    public boolean containsStartTag(@NotNull @FileExists @IsFile final File file, @NotNull final String tagName) {
        Contract.requireArgNotNull("file", file);
        FileExistsValidator.requireArgValid("file", file);
        IsFileValidator.requireArgValid("file", file);
        Contract.requireArgNotNull("tagName", tagName);

        final String xml = readFirstPartOfFile(file);
        return xml.indexOf("<" + tagName) > -1;
    }

    private String readFirstPartOfFile(final File file) {
        try {
            final Reader reader = new FileReader(file);
            try {

                final char[] buf = new char[1024];
                reader.read(buf);
                return String.copyValueOf(buf);
            } finally {
                reader.close();
            }
        } catch (final IOException ex) {
            throw new RuntimeException("Could not read first part of file: " + file, ex);
        }
    }

    /**
     * Marshals the object as XML to a file.
     * 
     * @param obj
     *            Object to marshal.
     * @param file
     *            File to write the instance to.
     * @param jaxbContext
     *            Context to use.
     * 
     * @throws MarshalObjectException
     *             Error writing the object.
     * 
     * @param <TYPE>
     *            Type of the object.
     */
    public <TYPE> void write(@NotNull final TYPE obj, @NotNull final File file, @NotNull final JAXBContext jaxbContext)
            throws MarshalObjectException {

        Contract.requireArgNotNull("obj", obj);
        Contract.requireArgNotNull("file", file);
        Contract.requireArgNotNull("jaxbContext", jaxbContext);

        try {
            final FileWriter fw = new FileWriter(file);
            try {
                write(obj, fw, jaxbContext);
            } finally {
                fw.close();
            }
        } catch (final IOException ex) {
            throw new MarshalObjectException("Unable to write XML to file: " + file, ex);
        }
    }

    /**
     * Marshals the object as XML to a string.
     * 
     * @param obj
     *            Object to marshal.
     * @param jaxbContext
     *            Context to use.
     * 
     * @return XML String.
     * 
     * @throws MarshalObjectException
     *             Error writing the object.
     * 
     * @param <TYPE>
     *            Type of the object.
     */
    public <TYPE> String write(@NotNull final TYPE obj, @NotNull final JAXBContext jaxbContext) throws MarshalObjectException {

        Contract.requireArgNotNull("obj", obj);
        Contract.requireArgNotNull("jaxbContext", jaxbContext);

        final StringWriter writer = new StringWriter();
        write(obj, writer, jaxbContext);
        return writer.toString();
    }

    /**
     * Marshals the object as XML to a writer.
     * 
     * @param obj
     *            Object to marshal.
     * @param writer
     *            Writer to write the object to.
     * @param jaxbContext
     *            Context to use.
     * 
     * @throws MarshalObjectException
     *             Error writing the object.
     * 
     * @param <TYPE>
     *            Type of the object.
     */
    public <TYPE> void write(@NotNull final TYPE obj, @NotNull final Writer writer, @NotNull final JAXBContext jaxbContext)
            throws MarshalObjectException {

        Contract.requireArgNotNull("obj", obj);
        Contract.requireArgNotNull("writer", writer);
        Contract.requireArgNotNull("jaxbContext", jaxbContext);

        try {
            final Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formattedOutput);
            marshaller.marshal(obj, writer);
        } catch (final JAXBException ex) {
            throw new MarshalObjectException("Unable to write XML to writer", ex);
        }
    }

    /**
     * Returns the information if XML should be formatted.
     * 
     * @return If XML is formatted TRUE else FALSE.
     */
    public final boolean isFormattedOutput() {
        return formattedOutput;
    }

    /**
     * Sets the information if XML should be formatted.
     * 
     * @param formattedOutput
     *            If XML is formatted TRUE else FALSE.
     */
    public final void setFormattedOutput(final boolean formattedOutput) {
        this.formattedOutput = formattedOutput;
    }

}
