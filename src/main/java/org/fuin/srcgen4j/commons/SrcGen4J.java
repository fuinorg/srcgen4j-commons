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

import java.util.List;

import org.fuin.utils4j.Utils4J;

/**
 * Parses models based on a configuration and generates something with it.
 */
public final class SrcGen4J {

    private final SrcGen4JConfig config;

    /**
     * Constructor with configuration.
     * 
     * @param config
     *            Configuration - Cannot be NULL.
     */
    public SrcGen4J(final SrcGen4JConfig config) {
        super();
        this.config = config;
    }

    /**
     * Parse and generate.
     */
    public final void execute() {

        // Add classes directories or JARs to class path
        final Classpath cp = config.getClasspath();
        if (cp != null) {
            final List<BinClasspathEntry> binList = cp.getBinList();
            if (binList != null) {
                for (final BinClasspathEntry entry : binList) {
                    final String url;
                    if (entry.getPath().startsWith("file:")) {
                        url = entry.getPath();
                    } else {
                        url = "file:///" + entry.getPath();
                    }
                    Utils4J.addToClasspath(url);
                }
            }
        }

        // Parse models & generate
        final List<ParserConfig> parserConfigs = config.getParsers();
        if (parserConfigs != null) {
            for (final ParserConfig pc : parserConfigs) {
                final Parser<Object> parser = pc.getParser();
                final Object model = parser.parse(pc);
                final List<GeneratorConfig> generatorConfigs = config.findGeneratorsForParser(pc
                        .getName());
                for (final GeneratorConfig gc : generatorConfigs) {
                    final Generator<Object> generator = gc.getGenerator();
                    generator.generate(gc, model);
                }
            }
        }

    }

}
