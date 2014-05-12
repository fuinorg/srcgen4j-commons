// CHECKSTYLE:OFF
package a.b.b;

import org.fuin.srcgen4j.commons.Parser;
import org.fuin.srcgen4j.commons.ParserConfig;
import org.fuin.srcgen4j.commons.SrcGen4JContext;

public class Parser1 implements Parser<String> {

    private ParserConfig config;

    private String model = "PARSER_1_MODEL";

    @Override
    public void initialize(SrcGen4JContext context, ParserConfig config) {
        this.config = config;
    }

    @Override
    public String parse() {
        return model;
    }

    public ParserConfig getConfig() {
        return config;
    }

    public String getModel() {
        return model;
    }

}
// CHECKSTYLE:ON
