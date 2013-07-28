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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Single generated artifact.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "artifact")
@XmlType(propOrder = { "targets" })
public class Artifact extends AbstractNamedTarget {

	@XmlElement(name = "target")
	private List<Target> targets;

	/**
	 * Default constructor.
	 */
	public Artifact() {
		super();
	}

	/**
	 * Constructor with name, project and folder.
	 * 
	 * @param name
	 *            Name to set.
	 * @param project
	 *            Project to set.
	 * @param folder
	 *            Folder to set.
	 */
	public Artifact(final String name, final String project, final String folder) {
		super(name, project, folder);
	}

	/**
	 * Returns the list of targets.
	 * 
	 * @return Targets or NULL.
	 */
	public final List<Target> getTargets() {
		return targets;
	}

	/**
	 * Sets the list of targets.
	 * 
	 * @param targets
	 *            Targets or NULL.
	 */
	public final void setTargets(final List<Target> targets) {
		this.targets = targets;
	}

	/**
	 * Adds a target to the list. If the list does not exist it's created.
	 * 
	 * @param target
	 *            Target to add - Cannot be NULL.
	 */
	public final void addTarget(final Target target) {
		if (targets == null) {
			targets = new ArrayList<Target>();
		}
		targets.add(target);
	}

}
