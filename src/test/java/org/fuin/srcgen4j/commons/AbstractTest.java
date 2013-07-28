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

import com.openpojo.validation.PojoValidator;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.NoFieldShadowingRule;
import com.openpojo.validation.rule.impl.NoPublicFieldsRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.DefaultValuesNullTester;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;

/**
 * Basic functions for all tests.
 */
public abstract class AbstractTest {

	/** XML Prefix. */
	protected static final String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
	
	/**
	 * Creates a configured POJO validator.
	 * 
	 * @return New instance.
	 */
	protected final PojoValidator createPojoValidator() {
		
		final PojoValidator pv = new PojoValidator();
		        
		pv.addRule(new NoPublicFieldsRule());
		pv.addRule(new NoFieldShadowingRule());
		pv.addRule(new GetterMustExistRule());
		pv.addRule(new SetterMustExistRule());

		pv.addTester(new DefaultValuesNullTester());
		pv.addTester(new SetterTester());
		pv.addTester(new GetterTester());
		
        return pv;
	}
	
}