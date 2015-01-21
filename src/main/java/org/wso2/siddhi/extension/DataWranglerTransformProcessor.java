package org.wso2.siddhi.extension;
import org.apache.log4j.Logger;
import org.wso2.siddhi.core.config.SiddhiContext;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.in.InEvent;
import org.wso2.siddhi.core.event.in.InListEvent;
import org.wso2.siddhi.core.event.in.InStream;
import org.wso2.siddhi.core.executor.expression.ExpressionExecutor;
import org.wso2.siddhi.core.query.processor.transform.TransformProcessor;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import org.wso2.siddhi.query.api.expression.Expression;
import org.wso2.siddhi.query.api.expression.Variable;
import org.wso2.siddhi.query.api.extension.annotation.SiddhiExtension;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.List;
import java.util.Map;


@SiddhiExtension(namespace = "datawrangler", function = "trans")

public class DataWranglerTransformProcessor extends TransformProcessor {
   private Map<String, Integer> paramPositions = new HashMap<String, Integer>();
   private static final Log logger = LogFactory.getLog(DataWranglerTransformProcessor.class);
   private  ScriptExecutor engine;
    private RegistryClient registry=new RegistryClient();
    private String script="";

   public DataWranglerTransformProcessor() throws Exception{

        logger.error("Constructor Created ");

    }

    @Override
    protected InStream processEvent(InEvent inEvent){
        Object[] data=null;
        try {
            logger.error(objectArraytoCSV(inEvent.getData()));
            String outputString=((String)engine.execute(objectArraytoCSV(inEvent.getData())));
            outputString=outputString.substring(outputString.indexOf("\n")+1,outputString.lastIndexOf("\n"));
            data=DataFormater.csvToObjectConverter(outputString,this.getOutStreamDefinition());

        }catch (Exception e){
            logger.error("Error occured inProcess Event Method"+e.getMessage());
        }


        for(int i=0;i<data.length;i++)
        {
            logger.error(data[i]);
        }

/*
        Object[] data = new Object[]{vMagnitude * vxComponent / 10000000000L,
                vMagnitude * vyComponent / 10000000000L,
                vMagnitude * vzComponent / 10000000000L};

*/




        //logger.error("process method executing");
        //logger.error(inEvent.toString());
        //logger.error(inEvent.getStreamId());

        //logger.error(inEvent.getTimeStamp());
        ///logger.error("process method completed");



        return new InEvent(inEvent.getStreamId(), System.currentTimeMillis(), data);
    }


    public String objectArraytoCSV(Object ara[])
    {
        StringBuilder builder=new StringBuilder();

        for(int i=0;i<ara.length;i++)
        {
            builder.append(ara[i].toString());
            if(i!=ara.length-1)
                builder.append(",");
        }
      return builder.toString();
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
    protected void init(Expression[] parameters, List<ExpressionExecutor> expressionExecutors, StreamDefinition inStreamDefinition, StreamDefinition outStreamDefinition, String elementId, SiddhiContext siddhiContext) {

        for (Expression parameter : parameters) {
            if (parameter instanceof Variable) {
                Variable var = (Variable) parameter;
                String attributeName = var.getAttributeName();

                paramPositions.put(attributeName, inStreamDefinition.getAttributePosition(attributeName));
            }
        }




        this.outStreamDefinition=outStreamDefinition;
        // SiddhiContext con=new SiddhiContext();

        //"testScript.js"
        try {
            engine = new ScriptExecutor(registry.getScript("testScript.js"));
        }catch(Exception e)
        {

        }

        if(this.outStreamDefinition==null) {

            String stremdef = registry.getOuputStreamDefintion();

            StreamDefinition outDef = OutputDefJSONParser.parse(stremdef);

            this.outStreamDefinition=outDef;

            logger.error("Output definiton is created");
        }else
        {
            logger.error("Out put Definition is already specified");
        }

        logger.error("init method executing");
        logger.error(Arrays.toString(parameters));
        logger.error(elementId);
        logger.error(inStreamDefinition);
        //logger.error(siddhiContext.get);
        logger.error("init method completed");
    }


    @Override
    public void destroy() {
    	
    }
}
