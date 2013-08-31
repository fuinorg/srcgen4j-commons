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

/**
 * Used by elements that require initialization with parent and variables.
 * 
 * @param <ELEMENT>
 *            Type of the instance that implements this interface.
 * @param <PARENT>
 *            Type of the element's parent.
 */
public interface InitializableElement<ELEMENT, PARENT> {

    /**
     * Initializes this object and it's childs.
     * 
     * @param parent
     *            Parent.
     * @param vars
     *            Variables to use.
     * 
     * @return This instance.
     */
    public ELEMENT init(PARENT parent, Map<String, String> vars);

}