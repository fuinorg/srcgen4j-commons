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

import javax.annotation.Nullable;

/**
 * A generator with a given name was not found in the configuration.
 */
public class GeneratorNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor with generator name.
     * 
     * @param generatorName
     *            Name of the generator that was not found - Should not be <code>null</code>.
     */
    public GeneratorNotFoundException(@Nullable final String generatorName) {
        super("The generator '" + generatorName + "' is not configured");
    }

}
