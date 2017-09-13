package com.myththewolf.BotServ.lib.tool;

import java.io.FileOutputStream;
import java.io.InputStream;

public class Tools {
	/**
     * Export a resource embedded into a Jar file to the local file path.
     *
     * @param resourceName ie.: "/SmartLibrary.dll"
     * @return The path to the exported resource
     * @throws Exception
     */
    static public String ExportResource(String resourceName,Class<?> ExecutingClass,String path) throws Exception  {
    	try {
    	InputStream ddlStream = ExecutingClass
    		    .getClassLoader().getResourceAsStream(resourceName);

    		try (FileOutputStream fos = new FileOutputStream(path);){
    		    byte[] buf = new byte[2048];
    		    int r;
    		    while(-1 != (r = ddlStream.read(buf))) {
    		        fos.write(buf, 0, r);
    		    }
    		}
    	}catch(Exception e) {
    		throw e;
    	}
			return path;
    }
}
