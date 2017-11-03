package org.oddjob.net.ftp;

import org.junit.Test;

import java.io.File;

import org.oddjob.tools.OurDirs;

import org.junit.Assert;

public class DerivedLocationsTest extends Assert {

	OurDirs dirs = new OurDirs();

	
    @Test
	public void testAllCombinations()  {
		
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
	public void testLocalIsDirectory()  {
		
		File aDir = dirs.relative("work/");
		
		DerivedLocations test = new DerivedLocations("a-file.txt", 
					aDir);
		
		assertEquals("a-file.txt", test.getRemote());
		assertEquals(new File(aDir, "a-file.txt"), test.getFile());
	}
}
