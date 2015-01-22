package org.wso2.siddhi.extension;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by tharindu on 1/21/15.
 */
public class FileHandler {
    private static final Log logger = LogFactory.getLog(ScriptExecutor.class);

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

            logger.error(fileName + " is added Successfully");

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return out.toString();
    }


}
