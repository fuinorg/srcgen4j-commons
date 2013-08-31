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

import static org.fest.assertions.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;

import org.junit.Test;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.PojoValidator;
import com.openpojo.validation.rule.impl.SetterMustExistRule;

/**
 * Tests for {@link Variable}.
 */
public class VariableTest extends AbstractTest {

    // CHECKSTYLE:OFF

    @Test
    public final void testPojoStructureAndBehavior() {

        final PojoClass pc = PojoClassFactory.getPojoClass(Variable.class);
        final PojoValidator pv = createPojoValidator();
        pv.addRule(new SetterMustExistRule());
        pv.runValidation(pc);

    }

    @Test
    public final void testMarshal() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(Variable.class, Folder.class);
        final Variable testee = new Variable("abc", "def");

        // TEST
        final String result = new JaxbHelper(false).write(testee, jaxbContext);

        // VERIFY
        assertThat(result)
                .isEqualTo(
                        XML
                                + "<variable name=\"abc\" value=\"def\" xmlns=\"http://www.fuin.org/srcgen4j/commons\"/>");

    }

    @Test
    public final void testUnmarshal() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(Variable.class, Folder.class);

        // TEST
        final Variable testee = new JaxbHelper()
                .create("<variable name=\"abc\" value=\"def\" xmlns=\"http://www.fuin.org/srcgen4j/commons\"/>",
                        jaxbContext);

        // VERIFY
        assertThat(testee).isNotNull();
        assertThat(testee.getName()).isEqualTo("abc");
        assertThat(testee.getValue()).isEqualTo("def");

    }

    @Test
    public final void testInit() {

        // PREPARE
        final Map<String, String> vars = new HashMap<String, String>();
        vars.put("a", "1");

        final Variable testee = new Variable("x", "${a}");

        // TEST
        testee.init(new SrcGen4JConfig(), vars);

        // VERIFY
        assertThat(testee.getValue()).isEqualTo("1");

    }

    // CHECKSTYLE:ON

}
