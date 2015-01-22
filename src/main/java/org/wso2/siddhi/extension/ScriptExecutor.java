package org.wso2.siddhi.extension;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.script.*;
import java.io.*;

/**
 * Created by tharindu on 1/19/15.
 */


public class ScriptExecutor {
    private static final Log logger = LogFactory.getLog(ScriptExecutor.class);
    private ScriptEngineManager factory;
    private ScriptEngine engine ;
    private String baseURL="";

    public ScriptExecutor(String script) throws IOException ,ScriptException
    {

        baseURL= ScriptExecutor.class.getResource("").toString();
        factory = new ScriptEngineManager();
        //    create JavaScript engine
        engine= factory.getEngineByName("JavaScript");
        //     evaluate JavaScript code from given file - specified by first argument
        engine.eval(FileHandler.readScript("lib/datavore/datavore-r0.1.js"));
        engine.eval(FileHandler.readScript("dw.js"));
        engine.eval(script);
        logger.error("All the files added successfully");

    }


    public  Object execute(String parameter) throws Exception
    {
        return ((Invocable) engine).invokeFunction("myfunction", parameter);
    }

}
