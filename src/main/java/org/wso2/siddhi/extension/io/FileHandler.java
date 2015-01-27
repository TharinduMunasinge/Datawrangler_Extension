package org.wso2.siddhi.extension.io;

import org.wso2.siddhi.extension.util.ScriptExecutor;
import org.apache.log4j.Logger;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


/******************************************************************************
 * FileHandler Class will read resource files and return the contents as  texts
 * Created by tharindu on 1/21/15.
 ******************************************************************************/

public class FileHandler {

    //Attributes .............................................................
    private static Logger logger = Logger.getLogger(FileHandler.class);


    //Methods ................................................................

    /**
     * This method read the content of a file inside the resouces folder( dw.js, lib/*)
     * @param fileName : Name of the file you want to read (within the resources folder)
     * @return : content of the file as a String
     */
    public static  String readScript(String fileName){

        StringBuilder out=null;
        try {

            InputStream in=ScriptExecutor.class.getResourceAsStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            out             = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line+"\n");
            }
            reader.close();
            logger.info(fileName + " is added Successfully");

        }catch (Exception e){

             logger.error(e.getMessage());
        }
        return out.toString();
    }


}
