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

/**
 * Parses some kind of model.
 * 
 * @param <MODEL>
 *            Type of the model.
 */
public interface Parser<MODEL> {

    /**
     * Initializes the parser using the given configuration. Only called once
     * for every parser instance.
     * 
     * @param context
     *            Current context - Cannot be NULL.
     * @param config
     *            Configuration to use - Cannot be NULL.
     */
    public void initialize(SrcGen4JContext context, ParserConfig config);

    /**
     * Parses a model using the initially given configuration.
     * 
     * @return Model.
     * 
     * @throws ParseException
     *             Error during parse process.
     */
    public MODEL parse() throws ParseException;

}
