// CHECKSTYLE:OFF
package a.b.c;

import org.fuin.srcgen4j.commons.Generator;
import org.fuin.srcgen4j.commons.GeneratorConfig;

public class Generator2 implements Generator<String> {

    private GeneratorConfig config;

    private String model;

    @Override
    public void generate(GeneratorConfig config, String model) {
        this.config = config;
        this.model = model;
    }

    public GeneratorConfig getConfig() {
        return config;
    }

    public String getModel() {
        return model;
    }

}
// CHECKSTYLE:ON
