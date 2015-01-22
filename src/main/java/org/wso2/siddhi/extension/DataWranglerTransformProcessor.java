package org.wso2.siddhi.extension;
import org.apache.log4j.Logger;
import org.wso2.siddhi.core.config.SiddhiContext;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.in.InEvent;
import org.wso2.siddhi.core.event.in.InListEvent;
import org.wso2.siddhi.core.event.in.InStream;
import org.wso2.siddhi.core.executor.expression.ExpressionExecutor;
import org.wso2.siddhi.core.query.processor.transform.TransformProcessor;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import org.wso2.siddhi.query.api.expression.Expression;
import org.wso2.siddhi.query.api.extension.annotation.SiddhiExtension;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.List;
import java.util.Map;
import org.wso2.siddhi.query.api.expression.constant.StringConstant;

@SiddhiExtension(namespace = "datawrangler", function = "wrangleEvent")

public class DataWranglerTransformProcessor extends TransformProcessor {
   private Map<String, Integer> paramPositions = new HashMap<String, Integer>();
   private static final Log logger = LogFactory.getLog(DataWranglerTransformProcessor.class);
   private  ScriptExecutor engine;
    private RegistryClient registry=new RegistryClient();


   public DataWranglerTransformProcessor() throws Exception{

        logger.error("Constructor is Created ");

    }


    @Override
    protected InStream processEvent(InEvent inEvent){
        Object[] data=null;
        try {
            logger.error(DataFormater.objectArraytoCSV(inEvent.getData()));

            String outputString=((String)engine.execute(DataFormater.objectArraytoCSV(inEvent.getData())));

            outputString=outputString.substring(outputString.indexOf("\n")+1,outputString.lastIndexOf("\n"));
            logger.error(outputString);
            data=DataFormater.csvToObjectConverter(outputString,this.getOutStreamDefinition());

        }catch (Exception e){
            logger.error("Error occured inProcess Event Method" + e.getMessage());
        }

        for(int i=0;i<data.length;i++)
        {
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
    @Override
    protected void init(Expression[] expressions, List<ExpressionExecutor> expressionExecutors, StreamDefinition inStreamDefinition, StreamDefinition outStreamDefinition, String elementId, SiddhiContext siddhiContext) {

        logger.error("Before fetching scripts");

        String configName = ((StringConstant) expressions[0]).getValue();
        logger.error("configName");

        initialization(configName, outStreamDefinition);
    }


    public void initialization(String configName,StreamDefinition outStreamDefinition)
    {
        this.outStreamDefinition=outStreamDefinition;

        try {
            engine = new ScriptExecutor(registry.getScript(configName));
        }catch(Exception e)
        {
            logger.error("Unable to  fetching velocityStream enine");

        }

        if(this.outStreamDefinition==null) {

            String stremdef = registry.getOuputStreamDefintion(configName);
            StreamDefinition outDef = OutputDefJSONParser.parse(stremdef);
            this.outStreamDefinition=outDef;
            logger.error("Output definiton is created");
        }else
        {
            logger.error("Out put Definition is already specified");
        }

    }


    @Override
    public void destroy() {
    	
    }
}
