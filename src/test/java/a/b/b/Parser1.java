// CHECKSTYLE:OFF
package a.b.b;

import org.fuin.srcgen4j.commons.Parser;
import org.fuin.srcgen4j.commons.ParserConfig;

public class Parser1 implements Parser<String> {

    private ParserConfig config;

    private String model = "PARSER_1_MODEL";

    @Override
    public String parse(final ParserConfig config) {
        this.config = config;
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
