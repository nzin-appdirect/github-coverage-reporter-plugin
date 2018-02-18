package io.jenkins.plugins.gcr;

import java.io.File;

public class TestUtils {

    public static final ClassLoader classLoader = TestUtils.class.getClassLoader();

    public static File loadResource(String filename) {
        return new File(classLoader.getResource(filename).getFile());
    }

}
