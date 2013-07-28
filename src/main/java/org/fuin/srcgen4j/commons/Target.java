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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Maps a single generated artifact based on a regular expression to a project/folder. 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "target")
@XmlType(propOrder = { "pattern" })
public class Target extends AbstractTarget {

	@XmlAttribute
	private String pattern;

	/**
	 * Default constructor.
	 */
	public Target() {
		super();
	}

	/**
	 * Constructor with pattern, project and folder.
	 * 
	 * @param pattern
	 *            Pattern to set.
	 * @param project
	 *            Project to set.
	 * @param folder
	 *            Folder to set.
	 */
	public Target(final String pattern, final String project, final String folder) {
		super(project, folder);
		this.pattern = pattern;
	}

	/**
	 * Returns the pattern.
	 * 
	 * @return Current pattern.
	 */
	public final String getPattern() {
		return pattern;
	}

	/**
	 * Sets the pattern.
	 * 
	 * @param pattern
	 *            Pattern to set.
	 */
	public final void setPattern(final String pattern) {
		this.pattern = pattern;
	}

}
