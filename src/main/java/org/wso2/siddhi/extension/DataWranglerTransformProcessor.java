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

import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.registry.api.Registry;
import org.wso2.carbon.context.RegistryType;
import org.wso2.carbon.registry.api.Resource;
import org.wso2.carbon.registry.core.utils.RegistryUtils;


import org.wso2.carbon.databridge.commons.utils.EventDefinitionConverterUtils;
import org.wso2.carbon.databridge.streamdefn.registry.util.RegistryStreamDefinitionStoreUtil;
//
//





import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
 
@SiddhiExtension(namespace = "datawrangler", function = "trans")


public class DataWranglerTransformProcessor extends TransformProcessor {
   private Map<String, Integer> paramPositions = new HashMap<String, Integer>();
   private static final Log logger = LogFactory.getLog(DataWranglerTransformProcessor.class);
   private  ScriptExecutor engine=new ScriptExecutor("testScript.js");



   public DataWranglerTransformProcessor() throws Exception{

        if(this.outStreamDefinition==null) {

            String stremdef = "{\"name\": \"velocityStream\",\"version\": \"1.0.0\" , \"data\" : [{\"name\" : \"velocityX\" , \"type\" : \"double\"},{\"name\" : \"velocityY\" , \"type\" : \"double\"},{\"name\" : \"velocityZ\" , \"type\" : \"double\"}]}";
            OutputStreamDef outDef = OutputDefJSONParser.parse(stremdef);

            this.outStreamDefinition = new StreamDefinition().name(outDef.getStreamName());
            List<org.wso2.siddhi.extension.Attribute> attributes = outDef.getAttributes();
            for (int i = 0; i < attributes.size(); i++) {
                this.outStreamDefinition.attribute(attributes.get(i).getName(), Attribute.Type.valueOf(attributes.get(i).getType().toUpperCase()));
            }
            logger.error("Output definiton is created");
        }else
        {
            logger.error("Out put Definition is already specified");
        }


        logger.error("Constructor Created ");
/*
        CarbonContext cCtx = CarbonContext.getCurrentContext();
        Registry registry = cCtx.getRegistry(RegistryType.SYSTEM_CONFIGURATION);
        String registryType = RegistryType.SYSTEM_GOVERNANCE.toString();
        if(registryType != null) {
            registry = cCtx.getRegistry(RegistryType.valueOf(registryType));
        }


        try {
            Resource resource = registry.newResource();

            resource.setContent((getStreamDefinitions())[0]);
            String resourcePath = "/_system/tharindudef";
            registry.put(resourcePath, resource);
            logger.error("loaded");
        } catch (RegistryException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
        }


*/

    }
    @Override
    protected InStream processEvent(InEvent inEvent){


        logger.error("Executed");
    	double vMagnitude = (Double) inEvent.getData(paramPositions.get("v"));
        double vxComponent = (Double) inEvent.getData(paramPositions.get("vx"));
        double vyComponent = (Double) inEvent.getData(paramPositions.get("vy"));
        double vzComponent = (Double) inEvent.getData(paramPositions.get("vz"));
        Object[] data=null;

        try {
            logger.error(objectArraytoCSV(inEvent.getData()));
            String output=((String)engine.execute(objectArraytoCSV(inEvent.getData())));
            output=output.substring(output.indexOf("\n")+1,output.lastIndexOf("\n"));

            String outputStreamDef="{\"name\": \"velocityStream\",\"version\": \"1.0.0\" , \"data\" : [{\"name\" : \"velocityX\" , \"type\" : \"double\"},{\"name\" : \"velocityY\" , \"type\" : \"double\"},{\"name\" : \"velocityZ\" , \"type\" : \"double\"}]}";

            data=DataFormater.csvToObjectConverter(output,OutputDefJSONParser.parse(outputStreamDef));

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

        
        logger.error("init method executing");
        logger.error(Arrays.toString(parameters));
        logger.error(elementId);
        logger.error(inStreamDefinition);
        //logger.error(siddhiContext.get);
        logger.error("init method completed");
    }

    public Object[] getStreamDefinitions() throws Exception
    {
    	ConcurrentHashMap<String, org.wso2.carbon.databridge.commons.StreamDefinition> map = new ConcurrentHashMap<String, org.wso2.carbon.databridge.commons.StreamDefinition>();
        CarbonContext cCtx = CarbonContext.getCurrentContext();
        int tenantId = cCtx.getTenantId();

        logger.error(cCtx);
    Registry registry = cCtx.getRegistry(RegistryType.SYSTEM_GOVERNANCE);

        logger.error(registry);
    
    if (!registry.resourceExists(RegistryStreamDefinitionStoreUtil.getStreamDefinitionStorePath())) {
    registry.put(RegistryStreamDefinitionStoreUtil.getStreamDefinitionStorePath(), registry.newCollection());
    } else {
    org.wso2.carbon.registry.core.Collection collection =
    (org.wso2.carbon.registry.core.Collection) registry.get(RegistryStreamDefinitionStoreUtil.
    getStreamDefinitionStorePath());

    for (String streamNameCollection : collection.getChildren()) {

    org.wso2.carbon.registry.core.Collection innerCollection =
    (org.wso2.carbon.registry.core.Collection) registry.get(streamNameCollection);
    for (String streamVersionCollection : innerCollection.getChildren()) {

    Resource resource = registry.get(streamVersionCollection);

        org.wso2.carbon.databridge.commons.StreamDefinition streamDefinition = EventDefinitionConverterUtils
    .convertFromJson(RegistryUtils.decodeBytes((byte[]) resource.getContent()));
    map.put(streamDefinition.getStreamId(), streamDefinition);
    logger.error(streamDefinition.toString());
    }
    }
    }

    if(map.isEmpty())
        logger.error("map is null");
    else
        logger.error("map has values");

    //return map.values();
  return map.values().toArray();
    }

    @Override
    public void destroy() {
    	
    }
}
