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

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.NeverNull;
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
     *            Initialized configuration (Method {@link SrcGen4JConfig#init(SrcGen4JContext, File)} was at least called once before).
     * @param context
     *            Build context.
     */
    public SrcGen4J(@NotNull final SrcGen4JConfig config, @NotNull final SrcGen4JContext context) {
        super();
        Contract.requireArgNotNull("config", config);
        Contract.requireArgNotNull("context", context);
        if (!config.isInitialized()) {
            throw new IllegalArgumentException("The configuration is not initialized");
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
        if ((projects == null) || (projects.size() == 0)) {
            LOG.warn("No projects configured!");
        } else {
            for (final Project project : projects) {
                final List<Folder> folders = project.getFolders();
                if ((folders == null) || (folders.size() == 0)) {
                    LOG.warn("No project folders configured for: " + project.getName());
                } else {
                    for (final Folder folder : folders) {
                        final File dir = folder.getCanonicalDir();
                        if (folder.isClean() && dir.exists()) {
                            LOG.info("Cleaning: " + dir);
                            cleanDirectory(dir, folder);
                        } else {
                            LOG.debug("Nothing to to [clean=" + folder.isClean() + ", exists=" + dir.exists() + "]: " + dir);
                        }
                    }
                }
            }
        }
    }

    private boolean cleanDirectory(final File dir, final Folder folder) {

        File[] files = dir.listFiles();
        if (files == null) {
            return true;
        }
        for (final File file : files) {
            if (folder.cleanAllowed(file)) {
                if (file.isDirectory()) {
                    if (cleanDirectory(file, folder)) {
                        delete(file);
                    }
                } else {
                    delete(file);
                }
            } else {
                LOG.info("Excluded from cleaning: " + file);
            }
        }
        files = dir.listFiles();
        final boolean empty = (files == null || files.length == 0);
        if (!empty) {
            LOG.debug("Directory still contains " + files.length + " files after cleaning");
        }
        return empty;
    }

    private void delete(final File file) {
        final boolean ok = file.delete();
        if (!ok) {
            if (file.isDirectory()) {
                LOG.error("Couldn't delete directory: " + file);
            } else {
                LOG.error("Couldn't delete file: " + file);
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
        final Parsers parsers = config.getParsers();
        if (parsers == null) {
            LOG.warn("No parsers element");
        } else {
            final List<ParserConfig> parserConfigs = parsers.getList();
            if (parserConfigs == null) {
                LOG.warn("No parsers configured");
            } else {
                for (final ParserConfig pc : parserConfigs) {
                    final Parser<Object> parser = pc.getParser();
                    final Object model = parser.parse();
                    final List<GeneratorConfig> generatorConfigs = config.findGeneratorsForParser(pc.getName());
                    for (final GeneratorConfig gc : generatorConfigs) {
                        final Generator<Object> generator = gc.getGenerator();
                        generator.generate(model, false);
                    }
                }
            }
        }

    }

    /**
     * Returns a file filter that combines all filters for all incremental parsers. It should be used for selecting the appropriate files.
     * 
     * @return File filter.
     */
    @NeverNull
    public FileFilter getFileFilter() {
        if (fileFilter == null) {
            final List<IOFileFilter> filters = new ArrayList<IOFileFilter>();
            final Parsers parsers = config.getParsers();
            if (parsers != null) {
                final List<ParserConfig> parserConfigs = parsers.getList();
                if (parserConfigs != null) {
                    for (final ParserConfig pc : parserConfigs) {
                        final Parser<Object> pars = pc.getParser();
                        if (pars instanceof IncrementalParser) {
                            final IncrementalParser<?> parser = (IncrementalParser<?>) pars;
                            filters.add(parser.getFileFilter());
                        }
                    }
                }
            }
            fileFilter = new OrFileFilter(filters);
        }
        return fileFilter;
    }

    /**
     * Incremental parse and generate. The class loader of this class will be used.
     * 
     * @param files
     *            Set of files to parse for the model.
     * 
     * @throws ParseException
     *             Error during parse process.
     * @throws GenerateException
     *             Error during generation process.
     */
    public final void execute(@NotNull final Set<File> files) throws ParseException, GenerateException {

        Contract.requireArgNotNull("files", files);

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
        final Parsers parsers = config.getParsers();
        if (parsers == null) {
            LOG.warn("No parsers element");
        } else {
            final List<ParserConfig> parserConfigs = parsers.getList();
            if (parserConfigs == null) {
                LOG.warn("No parsers configured");
            } else {
                for (final ParserConfig pc : parserConfigs) {
                    final Parser<Object> pars = pc.getParser();
                    if (pars instanceof IncrementalParser) {
                        final IncrementalParser<?> parser = (IncrementalParser<?>) pars;
                        final Object model = parser.parse(files);
                        final List<GeneratorConfig> generatorConfigs = config.findGeneratorsForParser(pc.getName());
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

}
