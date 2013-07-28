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

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents a folder in a target folder.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "folder")
@XmlType(propOrder = { "clean", "override", "create", "path", "name" })
public class Folder extends AbstractElement {

	@XmlAttribute
	private String name;

	@XmlAttribute
	private String path;

	@XmlAttribute
	private Boolean create;

	@XmlAttribute
	private Boolean override;

	@XmlAttribute
	private Boolean clean;

	/**
	 * Default constructor.
	 */
	public Folder() {
		super();
	}

	/**
	 * Constructor with name.
	 * 
	 * @param name
	 *            Name to set.
	 */
	public Folder(final String name) {
		super();
		this.name = name;
	}
	
	/**
	 * Constructor with name and path.
	 * 
	 * @param name
	 *            Name to set.
	 * @param path
	 *            path to set.
	 */
	public Folder(final String name, final String path) {
		super();
		this.name = name;
		this.path = path;
	}

	/**
	 * Returns the name.
	 * 
	 * @return Current name.
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            Name to set.
	 */
	public final void setName(final String name) {
		this.name = name;
	}

	/**
	 * Returns the path.
	 * 
	 * @return Current path.
	 */
	public final String getPath() {
		return path;
	}

	/**
	 * Sets the path.
	 * 
	 * @param path
	 *            Path to set.
	 */
	public final void setPath(final String path) {
		this.path = path;
	}

	/**
	 * Returns the information if the directories should be created if they
	 * don't exist.
	 * 
	 * @return Create directories (TRUE) or not (FALSE).
	 */
	public final boolean isCreate() {
		if (create == null) {
			return false;
		}
		return create.booleanValue();
	}

	/**
	 * Returns the information if the directories should be created if they
	 * don't exist.
	 * 
	 * @return Create directories (TRUE) or not (NULL or FALSE).
	 */
	public final Boolean getCreate() {
		return create;
	}

	/**
	 * Sets the information if the directories should be created if they don't
	 * exist.
	 * 
	 * @param create
	 *            Create directories (TRUE) or not (NULL or FALSE).
	 */
	public final void setCreate(final Boolean create) {
		this.create = create;
	}

	/**
	 * Returns the information if the directory should be cleaned (delete all
	 * files in it).
	 * 
	 * @return Clean directory (TRUE) or not (FALSE).
	 */
	public final boolean isClean() {
		if (clean == null) {
			return false;
		}
		return clean.booleanValue();
	}

	/**
	 * Returns the information if the directory should be cleaned (delete all
	 * files in it).
	 * 
	 * @return Clean directory (TRUE) or not (NULL or FALSE).
	 */
	public final Boolean getClean() {
		return clean;
	}

	/**
	 * Sets the information if the directory should be cleaned (delete all files
	 * in it).
	 * 
	 * @param clean
	 *            Clean directory (TRUE) or not (NULL or FALSE).
	 */
	public final void setClean(final Boolean clean) {
		this.clean = clean;
	}

	/**
	 * Returns the information if existing files in the directory should be
	 * overridden.
	 * 
	 * @return Override files (TRUE) or not (FALSE).
	 */
	public final boolean isOverride() {
		if (override == null) {
			return false;
		}
		return override.booleanValue();
	}

	/**
	 * Returns the information if existing files in the directory should be
	 * overridden.
	 * 
	 * @return Override files (TRUE) or not (NULL or FALSE).
	 */
	public final Boolean getOverride() {
		return override;
	}

	/**
	 * Sets the information if existing files in the directory should be
	 * overridden.
	 * 
	 * @param override
	 *            Override files (TRUE) or not (NULL or FALSE).
	 */
	public final void setOverride(final Boolean override) {
		this.override = override;
	}

	/**
	 * Replaces all variables in all configuration objects.
	 * 
	 * @param vars Variables to use.
	 */
	public final void replaceVariables(final Map<String, String> vars) {
		name = replaceVars(name, vars);
		path = replaceVars(path, vars);
	}	
	
	// CHECKSTYLE:OFF Generated code
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Folder other = (Folder) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	// CHECKSTYLE:ON

}
