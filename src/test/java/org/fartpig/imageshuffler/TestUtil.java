package org.fartpig.imageshuffler;

import java.io.File;

import junit.framework.TestCase;

public class TestUtil extends TestCase {
	
	public void testRemoveJpgMetaData() {
		Util.removeJpgMetaData(new File("D:/zz.jpg"));
	}

}
