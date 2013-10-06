// CHECKSTYLE:OFF
package a.b.c;

import org.fuin.srcgen4j.commons.Generator;
import org.fuin.srcgen4j.commons.GeneratorConfig;

public class Generator2 implements Generator<String> {

    private GeneratorConfig config;

    private String model;

    @Override
    public void initialize(GeneratorConfig config) {
        this.config = config;
    }

    @Override
    public void generate(String model) {
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
