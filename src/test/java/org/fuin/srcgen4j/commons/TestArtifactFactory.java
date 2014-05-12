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
 * Test artifact factory.
 */
public final class TestArtifactFactory implements ArtifactFactory<Object> {

    private String artifact;

    @Override
    public final Class<? extends Object> getModelType() {
	return Object.class;
    }

    @Override
    public final boolean isIncremental() {
	return true;
    }

    @Override
    public final void init(final ArtifactFactoryConfig config) {
	this.artifact = config.getArtifact();
    }

    @Override
    public final GeneratedArtifact create(final Object modelObject)
	    throws GenerateException {
	return new GeneratedArtifact(artifact, "b/c/d.txt", "abcd".getBytes());
    }

}
