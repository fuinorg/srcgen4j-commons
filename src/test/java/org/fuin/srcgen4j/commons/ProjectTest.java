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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;

import org.custommonkey.xmlunit.XMLAssert;
import org.junit.Test;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.PojoValidator;

/**
 * Tests for {@link Project}.
 */
public class ProjectTest extends AbstractTest {

    // CHECKSTYLE:OFF

    @Test
    public final void testPojoStructureAndBehavior() {

        final PojoClass pc = PojoClassFactory.getPojoClass(Project.class);
        final PojoValidator pv = createPojoValidator();
        pv.runValidation(pc);

    }

    @Test
    public final void testMarshal() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(Project.class,
                Folder.class);
        final Project testee = new Project("abc", "def");
        testee.setMaven(true);
        testee.addFolder(new Folder("NAME", "PATH"));

        // TEST
        final String result = new JaxbHelper(false).write(testee, jaxbContext);

        // VERIFY
        XMLAssert.assertXMLEqual(XML
                + "<project path=\"def\" maven=\"true\" name=\"abc\" "
                + "xmlns=\"http://www.fuin.org/srcgen4j/commons\">"
                + "<folder path=\"PATH\" name=\"NAME\"/></project>", result);

    }

    @Test
    public final void testUnmarshal() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(Project.class,
                Folder.class);

        // TEST
        final Project testee = new JaxbHelper()
                .create("<project name=\"abc\" path=\"def\" maven=\"true\" xmlns=\"http://www.fuin.org/srcgen4j/commons\"><folder name=\"NAME\" path=\"PATH\"/></project>",
                        jaxbContext);

        // VERIFY
        assertThat(testee).isNotNull();
        assertThat(testee.getName()).isEqualTo("abc");
        assertThat(testee.getPath()).isEqualTo("def");
        assertThat(testee.isMaven()).isTrue();
        assertThat(testee.getFolders()).isNotNull();
        assertThat(testee.getFolders()).hasSize(1);
        assertThat(testee.getFolders().get(0).getName()).isEqualTo("NAME");
        assertThat(testee.getFolders().get(0).getPath()).isEqualTo("PATH");

    }

    @Test
    public final void testInit() {

        // PREPARE
        final SrcGen4JConfig parent = new SrcGen4JConfig();
        final Project testee = new Project("A${x}", "${y}B");
        testee.addFolder(new Folder("${a}name", "folder${b}"));

        final Map<String, String> vars = new HashMap<String, String>();
        vars.put("x", "NAME");
        vars.put("y", "PATH");
        vars.put("a", "A");
        vars.put("b", "B");

        // TEST
        testee.init(new DefaultContext(), parent, vars);

        // VERIFY
        assertThat(testee.getParent()).isSameAs(parent);
        assertThat(testee.getName()).isEqualTo("ANAME");
        assertThat(testee.getPath()).isEqualTo("PATHB");
        final Folder folder = testee.getFolders().get(0);
        assertThat(folder.getName()).isEqualTo("Aname");
        assertThat(folder.getPath()).isEqualTo("folderB");

    }

    @Test
    public final void testInitMaven() {

        // PREPARE
        final SrcGen4JConfig parent = new SrcGen4JConfig();
        final Project testee = new Project("A", "B");

        // TEST
        testee.init(new DefaultContext(), parent, new HashMap<String, String>());

        // VERIFY
        assertThat(testee.getFolders()).hasSize(8);
        assertThat(testee.getFolders()).contains(new Folder("mainJava", ""),
                new Folder("mainRes", ""), new Folder("genMainJava", ""),
                new Folder("genMainRes", ""), new Folder("testJava", ""),
                new Folder("testRes", ""), new Folder("genTestJava", ""),
                new Folder("genTestRes", ""));

    }

    @Test
    public final void testInitMavenOverrideDefault() {

        // PREPARE
        final SrcGen4JConfig parent = new SrcGen4JConfig();
        final Project testee = new Project("A", "B");
        final Folder folder = new Folder("mainJava", "different");
        testee.addFolder(folder);

        // TEST
        testee.init(new DefaultContext(), parent, new HashMap<String, String>());

        // VERIFY
        assertThat(testee.getFolders()).contains(folder,
                new Folder("mainRes", ""), new Folder("genMainJava", ""),
                new Folder("genMainRes", ""), new Folder("testJava", ""),
                new Folder("testRes", ""), new Folder("genTestJava", ""),
                new Folder("genTestRes", ""));
        final int idx = testee.getFolders().indexOf(folder);
        assertThat(testee.getFolders().get(idx)).isSameAs(folder);

    }

    // CHECKSTYLE:ON

}
