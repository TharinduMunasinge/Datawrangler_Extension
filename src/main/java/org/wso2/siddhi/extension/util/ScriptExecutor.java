package org.wso2.siddhi.extension.util;

import org.apache.log4j.Logger;
import org.wso2.siddhi.extension.io.FileHandler;
import javax.script.*;
import java.io.*;

/************************************************************************
 * This class Execute the generated JS code within JS Environment
 * Created by tharindu on 1/19/15.
 ************************************************************************/

public class ScriptExecutor {

    private static Logger logger = Logger.getLogger(ScriptExecutor.class);
    private ScriptEngineManager factory;
    private ScriptEngine engine ;
    private String baseURL="";

    /**
     * This method load dynamically the JS code need to be executed (When init() method executed"
     * @param script  : Script need to be executed
     * @throws IOException
     * @throws ScriptException
     */
    public ScriptExecutor(String script) throws IOException ,ScriptException
    {

        baseURL= ScriptExecutor.class.getResource("").toString();   // get the root folder path
        factory = new ScriptEngineManager();

        engine= factory.getEngineByName("JavaScript");            //    create JavaScript engine

        //Load the dependent libraries to the engine
        engine.eval(FileHandler.readScript("lib/datavore/datavore-r0.1.js"));
        engine.eval(FileHandler.readScript("dw.js"));
        engine.eval(script);
        logger.info("All the files added successfully");

    }

    /**
     * This method execute the JS code as a function (For each event -> Inprocess() )
     * @param parameter : Parameter for the JS code
     * @return : the resultant object
     * @throws Exception
     */
    public  Object execute(String parameter) throws Exception
    {
        return ((Invocable) engine).invokeFunction("myfunction", parameter);
    }

}
