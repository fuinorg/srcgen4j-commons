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

import java.util.Map;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

/**
 * Creates an artifact for a given model.
 * 
 * @param <TYPE>
 *            Type of the model to create an artifact for.
 */
public interface ArtifactFactory<TYPE> {

    /**
     * Returns the type of the model.
     * 
     * @return Model class.
     */
    @NotNull
    public Class<? extends TYPE> getModelType();

    /**
     * Initializes the factory with the configuration.
     * 
     * @param config
     *            Configuration to use.
     */
    public void init(@NotNull ArtifactFactoryConfig config);

    /**
     * Determines if the factory supports an incremental build.
     * 
     * @return If the factory supports an incremental build TRUE (default), else FALSE (only full build).
     */
    public boolean isIncremental();

    /**
     * Generates an artifact based on a given model object.
     * 
     * @param modelObject
     *            Model object to create the artifact for.
     * @param context
     *            Map used to store information during the generation process.
     * @param preparationRun
     *            TRUE if this is a (dry) preparation run. May be used to prepare a following real generation run.
     * 
     * @return The generated artifact. A <code>null</code> value signals that nothing was generated.
     * 
     * @throws GenerateException
     *             Error when generating.
     */
    @Nullable
    public GeneratedArtifact create(@NotNull TYPE modelObject, @NotNull Map<String, Object> context, boolean preparationRun)
            throws GenerateException;

}
