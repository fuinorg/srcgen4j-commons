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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.fuin.utils4j.Utils4J;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses models based on a configuration and generates something with it.
 */
public final class SrcGen4J {

    private static final Logger LOG = LoggerFactory.getLogger(SrcGen4J.class);

    private final SrcGen4JConfig config;

    /**
     * Constructor with configuration.
     * 
     * @param config
     *            Configuration - Cannot be NULL.
     */
    public SrcGen4J(final SrcGen4JConfig config) {
        super();
        if (config == null) {
            throw new IllegalArgumentException("Argument 'config' cannot be NULL");
        }
        if (!config.isInitialized()) {
            throw new IllegalArgumentException("The configuration is not initialized");
        }
        this.config = config;
    }

    private void enhanceClasspath() {
        final Classpath cp = config.getClasspath();
        if (cp == null) {
            LOG.debug("No additional classpath found");
        } else {
            final List<BinClasspathEntry> binList = cp.getBinList();
            if (binList == null) {
                LOG.debug("No binary list found");
            } else {
                for (final BinClasspathEntry entry : binList) {
                    final String url;
                    if (entry.getPath().startsWith("file:")) {
                        url = entry.getPath();
                    } else {
                        url = "file:///" + entry.getPath();
                    }
                    LOG.info("Adding to classpath: " + url);
                    Utils4J.addToClasspath(url);
                }
            }
        }
    }

    private void cleanFolders() {
        final List<Project> projects = config.getProjects();
        if (projects == null) {
            LOG.warn("No projects configured!");
        } else {
            for (final Project project : projects) {
                final List<Folder> folders = project.getFolders();
                if (folders == null) {
                    LOG.warn("No project folders configured for: " + project.getName());
                } else {
                    for (final Folder folder : folders) {
                        final File dir = new File(folder.getDirectory());
                        if (folder.isClean() && dir.exists()) {
                            try {
                                LOG.info("Cleaning: " + dir);
                                FileUtils.cleanDirectory(dir);
                            } catch (final IOException ex) {
                                throw new RuntimeException("Error cleaning: " + dir, ex);
                            }
                        } else {
                            LOG.debug("Nothing to to [clean=" + folder.isClean() + ", exists="
                                    + dir.exists() + "]: " + dir);
                        }
                    }
                }
            }
        }
    }

    /**
     * Parse and generate with class loader from this class.
     * 
     * @throws ParseException
     *             Error during parse process.
     * @throws GenerateException
     *             Error during generation process.
     */
    public final void execute() throws ParseException, GenerateException {
        execute(getClass().getClassLoader());
    }

    /**
     * Parse and generate.
     * 
     * @param classLoader
     *            Dedicated class loader to use.
     * 
     * @throws ParseException
     *             Error during parse process.
     * @throws GenerateException
     *             Error during generation process.
     */
    public final void execute(final ClassLoader classLoader) throws ParseException,
            GenerateException {

        LOG.info("Executing full build");

        enhanceClasspath();

        cleanFolders();

        // Parse models & generate
        final List<ParserConfig> parserConfigs = config.getParsers();
        if (parserConfigs == null) {
            LOG.warn("No parsers configured");
        } else {
            for (final ParserConfig pc : parserConfigs) {
                final Parser<Object> parser = pc.getParser(classLoader);
                final Object model = parser.parse();
                final List<GeneratorConfig> generatorConfigs = config.findGeneratorsForParser(pc
                        .getName());
                for (final GeneratorConfig gc : generatorConfigs) {
                    final Generator<Object> generator = gc.getGenerator(classLoader);
                    generator.generate(model);
                }
            }
        }

    }

    /**
     * Incremental parse and generate. The class loader of this class will be
     * used.
     * 
     * @param files
     *            Set of files to parse for the model - Cannot be NULL.
     * 
     * @throws ParseException
     *             Error during parse process.
     * @throws GenerateException
     *             Error during generation process.
     */
    public final void execute(final Set<File> files) throws ParseException, GenerateException {
        execute(files, getClass().getClassLoader());
    }

    /**
     * Incremental parse and generate.
     * 
     * @param files
     *            Set of files to parse for the model - Cannot be NULL.
     * @param classLoader
     *            Dedicated class loader to use - Cannot be NULL.
     * 
     * @throws ParseException
     *             Error during parse process.
     * @throws GenerateException
     *             Error during generation process.
     */
    public final void execute(final Set<File> files, final ClassLoader classLoader)
            throws ParseException, GenerateException {

        LOG.info("Executing incremental build (" + files.size() + " files)");
        if (LOG.isDebugEnabled()) {
            for (final File file : files) {
                LOG.debug(file.toString());
            }
        }

        // Parse models & generate
        final List<ParserConfig> parserConfigs = config.getParsers();
        if (parserConfigs == null) {
            LOG.warn("No parsers configured");
        } else {
            for (final ParserConfig pc : parserConfigs) {
                final Parser<Object> pars = pc.getParser(classLoader);
                if (pars instanceof IncrementalParser) {
                    final IncrementalParser<?> parser = (IncrementalParser<?>) pars;
                    final Object model = parser.parse(files);
                    final List<GeneratorConfig> generatorConfigs = config
                            .findGeneratorsForParser(pc.getName());
                    for (final GeneratorConfig gc : generatorConfigs) {
                        final Generator<Object> generator = gc.getGenerator(classLoader);
                        generator.generate(model);
                    }
                } else {
                    LOG.debug("No incremental parser: " + pars.getClass().getName());
                }
            }
        }

    }

}
