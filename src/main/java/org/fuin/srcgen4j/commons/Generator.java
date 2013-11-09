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
 * Generates something using a given model.
 * 
 * @param <MODEL>
 *            Type of the model.
 */
public interface Generator<MODEL> {

    /**
     * Initializes the generator with a given configuration. This is only called
     * once per instance. The type of the generator implementation is the same
     * as defined in {@link GeneratorConfig#getClassName()}.
     * 
     * @param config
     *            Configuration to use - Cannot be NULL.
     */
    public void initialize(GeneratorConfig config);

    /**
     * Generates something using the initially given configuration and model.
     * 
     * @param model
     *            Model to use - Cannot be NULL.
     * @param incremental
     *            If this is an incremental build TRUE, else FALSE (full build).
     * 
     * @throws GenerateException
     *             Error when generating.
     */
    public void generate(MODEL model, boolean incremental) throws GenerateException;

}
