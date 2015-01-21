package org.wso2.siddhi.extension;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONString;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.context.RegistryType;
import org.wso2.carbon.databridge.commons.StreamDefinition;
import org.wso2.carbon.databridge.commons.utils.EventDefinitionConverterUtils;
import org.wso2.carbon.databridge.streamdefn.registry.util.RegistryStreamDefinitionStoreUtil;
import org.wso2.carbon.registry.api.Registry;
import org.wso2.carbon.registry.api.RegistryException;
import org.wso2.carbon.registry.api.Resource;
import org.wso2.carbon.registry.core.utils.RegistryUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tharindu on 1/20/15.
 */
public class RegistryClient {
    public static String outputStreamDef="{\"name\": \"streamName\",\"version\": \"1.0.0\" , \"data\" : [{\"name\" : \"coloumn1\" , \"type\" : \"String\"},{\"name\" : \"coloumn1\" , \"type\" : \"String\"},{\"name\" : \"coloumn1\" , \"type\" : \"String\"}]}";
    private static final Log logger = LogFactory.getLog(RegistryClient.class);


    public Object[] getStreamDefinitions() throws Exception
    {
        ConcurrentHashMap<String, StreamDefinition> map = new ConcurrentHashMap<String, org.wso2.carbon.databridge.commons.StreamDefinition>();
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

    public void method(String resourcePath , Object value)
    {
        CarbonContext cCtx = CarbonContext.getCurrentContext();
        Registry registry = cCtx.getRegistry(RegistryType.SYSTEM_CONFIGURATION);
        String registryType = RegistryType.SYSTEM_GOVERNANCE.toString();
        if(registryType != null) {
            registry = cCtx.getRegistry(RegistryType.valueOf(registryType));
        }


        try {
            Resource resource = registry.newResource();

            resource.setContent(value);
           // resourcePath = "/_system/tharindudef";
            registry.put(resourcePath, resource);
           // logger.error("loaded");
        } catch (RegistryException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
    }


    public String getOuputStreamDefintion()
    {
        CarbonContext cCtx = CarbonContext.getCurrentContext();
        Registry registry = cCtx.getRegistry(RegistryType.SYSTEM_CONFIGURATION);
        String registryType = RegistryType.SYSTEM_GOVERNANCE.toString();
        if(registryType != null) {
            registry = cCtx.getRegistry(RegistryType.valueOf(registryType));
        }


        try {
            Resource resource = registry.newResource();

        //Registry data obtainig logicn is dess
            String stremdef = "{\"name\": \"velocityStream\",\"version\": \"1.0.0\" , \"data\" : [{\"name\" : \"velocityX\" , \"type\" : \"double\"},{\"name\" : \"velocityY\" , \"type\" : \"double\"},{\"name\" : \"velocityZ\" , \"type\" : \"double\"}]}";
             return stremdef;

        } catch (RegistryException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }

return null;
    }


    public String getScript(String name)
    {

        return FileHandler.readScript(name);
    }


}
