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

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.NeverNull;

/**
 * Represents a file marker.
 */
public final class DefaultFileMarker implements FileMarker {

    @NotNull
    private final DefaultFileMarkerType type;

    @NotNull
    private final FileMarkerSeverity severity;

    @NotNull
    private final String message;

    private final int line;

    private final int start;

    private final int length;

    /**
     * Constructor for a file marker.
     * 
     * @param severity
     *            Severity.
     * @param message
     *            Message.
     */
    public DefaultFileMarker(@NotNull final FileMarkerSeverity severity,
	    @NotNull final String message) {
	super();
	Contract.requireArgNotNull("severity", severity);
	Contract.requireArgNotNull("message", message);

	this.type = DefaultFileMarkerType.FILE;
	this.severity = severity;
	this.message = message;
	this.line = -1;
	this.start = -1;
	this.length = -1;
    }

    /**
     * Constructor for a line marker.
     * 
     * @param severity
     *            Severity.
     * @param message
     *            Message.
     * @param line
     *            Line number.
     */
    public DefaultFileMarker(@NotNull final FileMarkerSeverity severity,
	    @NotNull final String message, @Min(0) final int line) {
	super();
	Contract.requireArgNotNull("severity", severity);
	Contract.requireArgNotNull("message", message);

	this.type = DefaultFileMarkerType.LINE;
	this.severity = severity;
	this.message = message;
	this.line = line;
	this.start = -1;
	this.length = -1;
    }

    /**
     * Creates a position and length based marker.
     * 
     * @param severity
     *            Severity.
     * @param message
     *            Message.
     * @param start
     *            Start position of the marked block.
     * @param length
     *            Length of the marked block.
     */
    public DefaultFileMarker(@NotNull final FileMarkerSeverity severity,
	    @NotNull final String message, @Min(0) final int start,
	    @Min(1) final int length) {
	super();
	Contract.requireArgNotNull("severity", severity);
	Contract.requireArgNotNull("message", message);

	this.type = DefaultFileMarkerType.POS_LENGTH;
	this.severity = severity;
	this.message = message;
	this.line = -1;
	this.start = start;
	this.length = length;
    }

    /**
     * Returns the type of the marker.
     * 
     * @return Type.
     */
    @NeverNull
    public final DefaultFileMarkerType getType() {
	return type;
    }

    /**
     * Returns the severity of the marker.
     * 
     * @return Severity indicator.
     */
    @NeverNull
    public final FileMarkerSeverity getSeverity() {
	return severity;
    }

    /**
     * Returns the message.
     * 
     * @return Message.
     */
    @NeverNull
    public final String getMessage() {
	return message;
    }

    /**
     * Returns the marked line.
     * 
     * @return Line (if type is {@link DefaultFileMarkerType#LINE}), otherwise
     *         zero.
     */
    public final int getLine() {
	return line;
    }

    /**
     * Returns the marked start position.
     * 
     * @return Start position (if type is
     *         {@link DefaultFileMarkerType#POS_LENGTH}), otherwise zero.
     */
    public final int getStart() {
	return start;
    }

    /**
     * Returns the marked length.
     * 
     * @return Block length (if type is {@link DefaultFileMarkerType#POS_LENGTH}
     *         ), otherwise zero.
     */
    public final int getLength() {
	return length;
    }

    @Override
    public final String toString() {
	if (type == DefaultFileMarkerType.FILE) {
	    return severity + " " + message;
	}
	if (type == DefaultFileMarkerType.LINE) {
	    return severity + " [Line " + line + "]" + message;
	}
	if (type == DefaultFileMarkerType.POS_LENGTH) {
	    return severity + " [" + start + "-" + (start + length) + "]"
		    + message;
	}
	throw new IllegalStateException("Unknown enum value: " + type);
    }

}
