package org.wso2.siddhi.extension.io;

import org.apache.log4j.Logger;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.context.RegistryType;
import org.wso2.carbon.registry.api.Registry;
import org.wso2.carbon.registry.api.RegistryException;
import org.wso2.carbon.registry.api.Resource;


/*********************************************************************
 * This class handle the interactions with WSO2 CEP registry
 * Created by tharindu on 1/20/15.
 *********************************************************************/

public class RegistryClient {

    //Attributes ..................................................................

    private static Logger logger = Logger.getLogger(RegistryClient.class);
    private static final String BASEURL="/repository/components/org.wso2.cep.wrangler/";



    //Method ......................................................................

    /**
     * This method return the JS script for given configuration
     *
     * @param name : name of the Script Configuration
     * @return : content of the JS code as a string
     */
    public String getScript(String name)
    {
        String output=getText(name,"script.js");
        if(output!=null)
        {
            return output;
        }
        else{
            logger.error("script output is null");
            return null;
        }

    }



    /**
     * This method read the output stream definition of a given configuration name
     *
     * @param name: name of the Configuration in registry
     * @return : content of the definition file as a String
     */
    public String getOutputStreamDefinition(String name)
    {
        String output=getText(name,"config.json");
        if(output!=null)
        {
            logger.info("outputStream Definition is read successfully ");
            return output;
        }
        else{
            logger.error("output definition is null");
            return null;
        }
    }



    /**
     * This is a generic method to read a file from the registry
     *
     * @param folderName : name of the folder (which is same as configuration name)
     * @param fileName : config.json | script.js
     * @return : content of the file as a String
     */
    public String getText(String folderName,String fileName)
    {

        CarbonContext cCtx = CarbonContext.getCurrentContext();
        Registry registry = cCtx.getRegistry(RegistryType.SYSTEM_CONFIGURATION);
        String registryType = RegistryType.SYSTEM_GOVERNANCE.toString();
        if(registryType != null) {
            registry = cCtx.getRegistry(RegistryType.valueOf(registryType));
        }


        try {
            logger.error("Retrieving path :" + BASEURL + folderName + "/" + fileName);
            Resource res=registry.get(BASEURL + folderName + "/" + fileName);
            logger.error(new String((byte[]) res.getContent()));

            return new String((byte[])res.getContent());

        } catch (RegistryException e) {
            logger.error("Error in Registry client" + e.getMessage());

        }
        catch (Exception e){
            logger.error("Error in Registry client"+e.getMessage());
        }

        return  null;
    }


}
