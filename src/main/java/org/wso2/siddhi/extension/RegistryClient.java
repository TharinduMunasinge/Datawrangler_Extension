package org.wso2.siddhi.extension;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONString;

/**
 * Created by tharindu on 1/20/15.
 */
public class RegistryClient {
    public static String outputStreamDef="{\"name\": \"streamName\",\"version\": \"1.0.0\" , \"data\" : [{\"name\" : \"coloumn1\" , \"type\" : \"String\"},{\"name\" : \"coloumn1\" , \"type\" : \"String\"},{\"name\" : \"coloumn1\" , \"type\" : \"String\"}]}";

    public String getStreamDefinitions(){
        return "";
    }

}
