package io.jenkins.plugins.gcr;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URISyntaxException;

public class TestUtils {

    public static final ClassLoader classLoader = TestUtils.class.getClassLoader();

    public static File loadResource(String filename) throws Exception {
        return FileUtils.toFile(classLoader.getResource(filename).toURI().toURL());
    }

}
