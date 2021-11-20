// CHECKSTYLE:OFF
package org.fuin.srcgen4j.commons;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "parent", namespace = "http://www.fuin.org/srcgen4j/commons/other-ns")
public class ParentElement extends AbstractElement {

    @XmlElement(name = "child", namespace = "http://www.fuin.org/srcgen4j/commons/other-ns")
    private List<ChildElement> childs;

    public List<ChildElement> getChilds() {
        if (childs == null) {
            return null;
        }
        return Collections.unmodifiableList(childs);
    }

    public void init() {

        // No parent to inherit from
        inheritVariables(null);

        // Initialize childs
        if (childs != null) {
            for (ChildElement child : childs) {
                child.init(getVarMap());
            }
        }

    }

}
// CHECKSTYLE:ON
