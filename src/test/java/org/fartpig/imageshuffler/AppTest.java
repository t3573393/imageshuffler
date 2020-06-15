package org.fartpig.imageshuffler;

import junit.framework.TestCase;

public class AppTest 
    extends TestCase
{

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	//1 a文件夹，2 b文件夹，3 原始图片映射文件（none，为无）， 4 新的文件名称前缀
    	String[] args = new String[]{"D:/samples/a", "D:/samples/b", "none", "01"};
        App.main(args);
    }
    
    public void testWithPairsFile() {
    	//1 a文件夹，2 b文件夹，3 原始图片映射文件， 4 新的文件名称前缀
    	String[] args = new String[]{"D:/samples/a", "D:/samples/b", "D:/workspace-my/imageshuffler/old_pairs.txt", "03"};
        App.main(args);
    	
    }
}
