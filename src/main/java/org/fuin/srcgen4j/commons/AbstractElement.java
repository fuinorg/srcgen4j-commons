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

import java.util.Map;

/**
 * Basic functionality used by all configuration sub classes.
 */
public abstract class AbstractElement {

	/**
	 * Replaces all variables inside a string with values from a map.
	 * 
	 * @param str
	 *            Text with variables (Format: ${key} ) - May be
	 *            <code>null</code> or empty.
	 * @param vars
	 *            Map with key/values (both of type <code>String</code> - May be
	 *            <code>null</code>.
	 * 
	 * @return String with replaced variables. Unknown variables will remain
	 *         unchanged.
	 */
	public final String replaceVars(final String str, final Map<String, String> vars) {

		if ((str == null) || (str.length() == 0) || (vars == null) || (vars.size() == 0)) {
			return str;
		}

		final StringBuffer sb = new StringBuffer();

		int end = -1;
		int from = 0;
		int start = -1;
		while ((start = str.indexOf("${", from)) > -1) {
			sb.append(str.substring(end + 1, start));
			end = str.indexOf('}', start + 1);
			if (end == -1) {
				// No closing bracket found...
				sb.append(str.substring(start));
				from = str.length();
			} else {
				final String key = str.substring(start + 2, end);
				final String value = (String) vars.get(key);
				if (value == null) {
					sb.append("${");
					sb.append(key);
					sb.append("}");
				} else {
					sb.append(value);
				}
				from = end + 1;
			}
		}

		sb.append(str.substring(from));

		return sb.toString();

	}

}
