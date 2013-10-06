// CHECKSTYLE:OFF
package a.b.c;

import org.fuin.srcgen4j.commons.Parser;
import org.fuin.srcgen4j.commons.ParserConfig;

public class Parser2 implements Parser<String> {

    private ParserConfig config;

    private String model = "PARSER_2_MODEL";

    @Override
    public void initialize(ParserConfig config) {
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
