package org.edgegallery.mecm.inventory.service;

import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class FileCheckTest {

    @Test(expected = IllegalArgumentException.class)
    public void testBlankSpaceFile() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:New File");
        FileChecker.check(file);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidYamlFile() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:File123");
        FileChecker.check(file);
    }

}
