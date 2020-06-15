package org.fartpig.imageshuffler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.commons.io.IOUtils;

/**
 * Hello world!
 *
 */
public class App 
{
	private static Map<String, String> originPairsMap = new HashMap<String, String>();
	private static Set<String> aNameSet = new HashSet<String>();
	private static Set<String> bNameSet = new HashSet<String>();
	private static Map<String, String> renameAMap = new HashMap<String, String>();
	private static Map<String, String> renameBMap = new HashMap<String, String>();
	
	private static Map<String, String> newPairsMap = new HashMap<String, String>();
	private static final String NULLFILE = "NULLFILE";
	private static final String BFILEPREFIX = "b";
	private static final int RANDOM_BOUND = 10000;
	
    public static void main( String[] args )
    {
    	String fileNamePrefix = "";
    	if (args.length < 2) {
    		System.err.println( "please input the shuffle folders: A, B" );
    		return ;
    	}
        File aDir = new File(args[0]);
        File bDir = new File(args[1]);
        if (!aDir.exists()) {
        	System.err.println( "A dir is not found" );
    		return ;
        }
        
        if (!bDir.exists()) {
        	System.err.println( "B dir is not found" );
    		return ;
        }
        
        if (args.length >= 3) {
        	if (!args[2].equalsIgnoreCase("none")) {
        		System.out.println( "with the origin pairs" );
            	try {
    				IOUtils.readLines(new FileInputStream(args[2]), "UTF-8").forEach(new Consumer<String>() {
    					@Override
    					public void accept(String t) {
    						if (t.isEmpty()) {
    							return ;
    						}
    						
    						String[] contents = t.split("[ ]");
    						if (contents.length >= 2) {
    							originPairsMap.put(contents[0], contents[1]);
    						} else {
    							originPairsMap.put(t, t);
    						}
    					}
    				});
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
        	}
        }
        
        if (args.length >= 4) {
        	fileNamePrefix = args[3];
        	System.out.println( "with file name prefix:" + fileNamePrefix );
        }
        
        // get origin pairs by the file names
        if (originPairsMap.isEmpty()) {
        	for(File aFile : aDir.listFiles()){
        		String bFileName = NULLFILE;
        		String aFileName = aFile.getName();
        		int dotPosition = aFileName.lastIndexOf(".");
        		if (dotPosition != -1) {
        			bFileName = BFILEPREFIX + aFileName.substring(1, dotPosition) + aFileName.substring(dotPosition);
        		} else {
        			bFileName = BFILEPREFIX + aFileName.substring(1);
        		}
        		
        		if (new File(bDir, bFileName).exists()) {
        			originPairsMap.put(aFileName, bFileName);
        		} else {
        			originPairsMap.put(aFileName, NULLFILE);
        		}
        	}
        }
        
        //begin shuffle
        Random random = new Random(new Date().getTime());
        for(Map.Entry<String, String> originEntry : originPairsMap.entrySet()) {
        	String aName = originEntry.getKey();
        	String bName = originEntry.getValue();
        	
        	String aNewName = null;
        	do {
        		aNewName = String.format("a%s%s.jpg", fileNamePrefix, random.nextInt(RANDOM_BOUND));
        	} while(aNameSet.contains(aNewName));
        	aNameSet.add(aNewName);
        	renameAMap.put(aName, aNewName);
        	
        	String bNewName = bName;
        	if (!NULLFILE.equals(bName)) {
        		bNewName = null;
            	do {
            		bNewName = String.format("b%s%s.jpg", fileNamePrefix, random.nextInt(RANDOM_BOUND));
            	} while(bNameSet.contains(bNewName));
            	bNameSet.add(bNewName);
            	renameBMap.put(bName, bNewName);
        	}
        	
        	newPairsMap.put(aNewName, bNewName);
        }
        // rename files
        for (Map.Entry<String, String> renameAEntry : renameAMap.entrySet()) {
        	String aName = renameAEntry.getKey();
        	String newAName = renameAEntry.getValue();
        	File aFile = new File(aDir, aName);
        	Util.removeJpgMetaData(aFile);
        	aFile.renameTo(new File(aDir, newAName));
        }
        
        for (Map.Entry<String, String> renameBEntry : renameBMap.entrySet()) {
        	String bName = renameBEntry.getKey();
        	String newBName = renameBEntry.getValue();
        	File bFile = new File(bDir, bName);
        	Util.removeJpgMetaData(bFile);
        	bFile.renameTo(new File(bDir, newBName));
        }
        
        //output pairs file
        List<String> pairsList = new ArrayList<String>();
        for (Map.Entry<String, String> newPairEntry : newPairsMap.entrySet()) {
        	pairsList.add(newPairEntry.getKey() + " " + newPairEntry.getValue());
        }
        
        File pairsFile = new File("pairs.txt");
        PrintWriter pr = null;
        try {
        	pr = new PrintWriter(pairsFile);
			IOUtils.writeLines(pairsList, "\r\n", pr);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (pr != null) {
				pr.close();
			}
		}
        
    }
}
