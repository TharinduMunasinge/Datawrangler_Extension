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


    ScriptEngineManager factory;
    ScriptEngine engine ;
    String baseURL;
    public ScriptExecutor(String script) throws IOException ,ScriptException
    {
      //  baseURL=(new File("")).getAbsolutePath()+"/WranglerEngine/";

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





/*
ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
Compilable compilable = (Compilable) engine;
Bindings bindings = engine.createBindings();
String script = "function add(op1,op2){return op1+op2} add(a, b)";
CompiledScript jsFunction = compilable.compile(script);
bindings.put("a", 1);bindings.put("b", 2); //put java object into js runtime environment
Object result = jsFunction.eval(bindings);
System.out.println(result);
*/








    /*
    *
    *
import java.sql.Timestamp;
import java.util.Date;
public class EvalScript2 {
    public static void main(String[] args) throws Exception {

	ScriptEngineManager factory = new ScriptEngineManager();
 	ScriptEngine engine = factory.getEngineByName("JavaScript");
	java.util.Date date2= new java.util.Date();
	System.out.println(new Timestamp(date2.getTime()));


	engine.eval(new java.io.FileReader("lib/datavore/datavore-r0.1.js"));
	engine.eval(new java.io.FileReader("dw.js"));
//Compilable)engine).compile(new java.io.FileReader("test/crime.js"));


	engine.eval(new java.io.FileReader("script3.js"));


	Bindings bindings = engine.createBindings();


for(int i=0;i<10;i++){

java.util.Date date1= new java.util.Date();
System.out.println("\n"+new Timestamp(date1.getTime()));

String crime2="Reported crime in Alabama					\n\nYear	Population	Property crime rate	Burglary rate	Larceny-theft rate	Motor vehicle theft rate\n2004	4525375	4029.3	987	2732.4	309.9\n2005	4548327	3900	955.8	2656	289\n2006	4599030	3937	968.9	2645.1	322.9\n2007	4627851	3974.9	980.2	2687	307.7\n2008	4661900	4081.9	1080.7	2712.6	288.6\n";




}










}
}

    *
    *
    *
    *
    *
    *
    *
    *
    *
    *
    *
    *
    *
    * */


