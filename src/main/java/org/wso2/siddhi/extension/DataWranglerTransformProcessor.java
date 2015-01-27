package org.wso2.siddhi.extension;

import org.apache.log4j.Logger;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.in.InEvent;
import org.wso2.siddhi.core.event.in.InListEvent;
import org.wso2.siddhi.core.event.in.InStream;
import org.wso2.siddhi.core.query.processor.transform.TransformProcessor;
import org.wso2.siddhi.extension.io.RegistryClient;
import org.wso2.siddhi.extension.util.DataFormater;
import org.wso2.siddhi.extension.util.ScriptExecutor;
import org.wso2.siddhi.query.api.definition.StreamDefinition;

import java.util.HashMap;
import java.util.Map;

/*********************************************************************************************
 * This class doesn't provide concreate implementation of init method and annotations
 * But provides the common behavior of 2 concrete classes
 *********************************************************************************************/

public abstract class DataWranglerTransformProcessor extends TransformProcessor {

    //Attributes ..............................................................................

    private Map<String, Integer> paramPositions = new HashMap<String, Integer>();
    private static Logger logger = Logger.getLogger(DataWranglerTransformProcessor.class);
    private ScriptExecutor engine;                                    //JS execution environment
    private String script = "";                                       //Script as a String
    private RegistryClient registry = new RegistryClient();           //for registry interaction


    //Getters and Setters ......................................................................

    public ScriptExecutor getEngine() {
        return engine;
    }

    public void setEngine(ScriptExecutor engine) {
        this.engine = engine;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public RegistryClient getRegistry() {
        return registry;
    }

    public void setRegistry(RegistryClient registry) {
        this.registry = registry;
    }



    //Constructor Goes here......................................................................
    public DataWranglerTransformProcessor() {

        logger.error("Constructor is Created ");

    }


    //Methods.....................................................................................

    /**
     * This method is executed for each event (this is the method that performs any transformation)
     * @param inEvent :input Event Object
     * @return        :transformed event object
     */
    @Override
    protected InStream processEvent(InEvent inEvent) {
        Object[] data = null;
        try {
            logger.error(DataFormater.objectArraytoCSV(inEvent.getData()));

            //Convert the event payload to CSV and send as a parameter to the JS CODE
            String outputString = ((String) engine.execute(DataFormater.objectArraytoCSV(inEvent.getData())));

            //remove the unwanted strings withing the output
            outputString = outputString.substring(outputString.indexOf("\n") + 1, outputString.lastIndexOf("\n"));
            logger.error(outputString);

            //convert the output into objects according to the outputStream definitions
            data = DataFormater.csvToObjectConverter(outputString, this.getOutStreamDefinition());

        } catch (Exception e) {
            logger.error("Error occured inProcess Event Method" + e.getMessage());
        }

        //Show the content of outptustream
        for (int i = 0; i < data.length; i++) {
            logger.error(data[i]);
        }

        return new InEvent(inEvent.getStreamId(), System.currentTimeMillis(), data);
    }


    @Override
    protected InStream processEvent(InListEvent inListEvent) {
        InListEvent transformedListEvent = new InListEvent();

        for (Event event : inListEvent.getEvents()) {
            if (event instanceof InEvent) {

                transformedListEvent.addEvent((Event) processEvent((InEvent) event));
            }
        }
        return transformedListEvent;
    }

    @Override
    protected Object[] currentState() {
        return new Object[]{paramPositions};
    }

    @Override
    protected void restoreState(Object[] objects) {
        if (objects.length > 0 && objects[0] instanceof Map) {
            paramPositions = (Map<String, Integer>) objects[0];
        }
    }


    /**
     * This method setup the script ,JS environment and  output stream definition
     * @param script               : Script need to be executed
     * @param outStreamDefinition  : OutputStream definition of transformer
     */
    public void initialization(String script, StreamDefinition outStreamDefinition) {
        try {
            this.script = script;

            this.outStreamDefinition = outStreamDefinition;
            engine = new ScriptExecutor(script);
        } catch (Exception e) {
            logger.error("Unable to setup StreamDefinition and  engine");

        }
    }


    @Override
    public void destroy() {

    }
}
