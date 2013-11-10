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
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.fuin.utils4j.Utils4J;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses models based on a configuration and generates something with it.
 */
public final class SrcGen4J {

    private static final Logger LOG = LoggerFactory.getLogger(SrcGen4J.class);

    private final SrcGen4JConfig config;

    private final SrcGen4JContext context;

    private FileFilter fileFilter;

    /**
     * Constructor with configuration.
     * 
     * @param config
     *            Configuration.
     * @param context
     *            Build context.
     */
    public SrcGen4J(@NotNull final SrcGen4JConfig config, @NotNull final SrcGen4JContext context) {
        super();
        if (config == null) {
            throw new IllegalArgumentException("Argument 'config' cannot be NULL");
        }
        if (!config.isInitialized()) {
            throw new IllegalArgumentException("The configuration is not initialized");
        }
        if (context == null) {
            throw new IllegalArgumentException("Argument 'context' cannot be NULL");
        }
        this.config = config;
        this.context = context;
    }

    private void enhanceClasspath() {
        final Classpath cp = config.getClasspath();
        if (cp == null) {
            LOG.debug("No additional classpath found");
        } else {

            // Add context class path
            if (cp.isAddContextCP()) {
                addToClasspath(context.getJarFiles(), "Context jar files");
                addToClasspath(context.getBinDirs(), "Context bin directories");
            }

            // Add class paths from configuration
            final List<BinClasspathEntry> binList = cp.getBinList();
            if (binList == null) {
                LOG.debug("No binary classpath list found");
            } else {
                for (final BinClasspathEntry entry : binList) {
                    final String url;
                    if (entry.getPath().startsWith("file:")) {
                        url = entry.getPath();
                    } else {
                        url = "file:///" + entry.getPath();
                    }
                    LOG.info("Adding to classpath: " + url);
                    Utils4J.addToClasspath(url, context.getClassLoader());
                }
            }
        }
    }

    private void addToClasspath(final List<File> files, final String message) {
        if (files.size() == 0) {
            LOG.debug("No files found (" + message + ")");
        } else {
            for (final File file : files) {
                LOG.info("Adding to classpath (" + message + "): " + file);
                Utils4J.addToClasspath(file, context.getClassLoader());
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
     * Parse and generate.
     * 
     * @throws ParseException
     *             Error during parse process.
     * @throws GenerateException
     *             Error during generation process.
     */
    public final void execute() throws ParseException, GenerateException {

        LOG.info("Executing full build");

        enhanceClasspath();

        cleanFolders();

        // Parse models & generate
        final List<ParserConfig> parserConfigs = config.getParsers();
        if (parserConfigs == null) {
            LOG.warn("No parsers configured");
        } else {
            for (final ParserConfig pc : parserConfigs) {
                final Parser<Object> parser = pc.getParser();
                final Object model = parser.parse();
                final List<GeneratorConfig> generatorConfigs = config.findGeneratorsForParser(pc
                        .getName());
                for (final GeneratorConfig gc : generatorConfigs) {
                    final Generator<Object> generator = gc.getGenerator();
                    generator.generate(model, false);
                }
            }
        }

    }

    /**
     * Returns a file filter that combines all filters for all incremental
     * parsers. It should be used for selecting the appropriate files.
     * 
     * @return File filter - Never NULL.
     */
    public FileFilter getFileFilter() {
        if (fileFilter == null) {
            final List<IOFileFilter> filters = new ArrayList<IOFileFilter>();
            final List<ParserConfig> parserConfigs = config.getParsers();
            if (parserConfigs != null) {
                for (final ParserConfig pc : parserConfigs) {
                    final Parser<Object> pars = pc.getParser();
                    if (pars instanceof IncrementalParser) {
                        final IncrementalParser<?> parser = (IncrementalParser<?>) pars;
                        filters.add(parser.getFileFilter());
                    }
                }
            }
            fileFilter = new OrFileFilter(filters);
        }
        return fileFilter;
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

        LOG.info("Executing incremental build (" + files.size() + " files)");
        if (LOG.isDebugEnabled()) {
            for (final File file : files) {
                LOG.debug(file.toString());
            }
        }

        if (files.size() == 0) {
            // Nothing to do...
            return;
        }

        // Parse models & generate
        final List<ParserConfig> parserConfigs = config.getParsers();
        if (parserConfigs == null) {
            LOG.warn("No parsers configured");
        } else {
            for (final ParserConfig pc : parserConfigs) {
                final Parser<Object> pars = pc.getParser();
                if (pars instanceof IncrementalParser) {
                    final IncrementalParser<?> parser = (IncrementalParser<?>) pars;
                    final Object model = parser.parse(files);
                    final List<GeneratorConfig> generatorConfigs = config
                            .findGeneratorsForParser(pc.getName());
                    for (final GeneratorConfig gc : generatorConfigs) {
                        final Generator<Object> generator = gc.getGenerator();
                        generator.generate(model, true);
                    }
                } else {
                    LOG.debug("No incremental parser: " + pars.getClass().getName());
                }
            }
        }

    }

}
