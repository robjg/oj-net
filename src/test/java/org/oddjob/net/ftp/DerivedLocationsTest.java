package org.oddjob.net.ftp;

import org.junit.jupiter.api.Test;
import org.oddjob.OurDirs;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DerivedLocationsTest {

    @Test
    public void testAllCombinations() {

        DerivedLocations test1 = new DerivedLocations(null, null);

        assertNull(test1.getRemote());
        assertNull(test1.getFile());

        DerivedLocations test2 = new DerivedLocations(null,
                new File("somewhere/a-file.txt"));

        assertEquals("a-file.txt", test2.getRemote());
        assertEquals(new File("somewhere/a-file.txt"), test2.getFile());

        DerivedLocations test3 = new DerivedLocations(
                "somewhere/a-file.txt", null);

        assertEquals("somewhere/a-file.txt", test3.getRemote());
        assertEquals(new File("a-file.txt"), test3.getFile());

        DerivedLocations test4 = new DerivedLocations(
                "somewhere/a-file.txt",
                new File("elsewhere/another-file.txt"));

        assertEquals("somewhere/a-file.txt", test4.getRemote());
        assertEquals(new File("elsewhere/another-file.txt"), test4.getFile());
    }

    @Test
    public void testLocalIsDirectory() throws IOException {

        File aDir = OurDirs.workPathDir(getClass().getSimpleName(), true)
                .toFile();

        DerivedLocations test = new DerivedLocations("a-file.txt",
                aDir);

        assertEquals("a-file.txt", test.getRemote());
        assertEquals(new File(aDir, "a-file.txt"), test.getFile());
    }
}
