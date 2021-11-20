// CHECKSTYLE:OFF
package org.fuin.srcgen4j.commons;

import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "child", namespace = "http://www.fuin.org/srcgen4j/commons/other-ns")
public class ChildElement extends AbstractElement {

    public void init(@NotNull Map<String, String> varMap) {
        inheritVariables(varMap);
    }

}
// CHECKSTYLE:ON
