package org.wso2.siddhi.extension;

import org.json.JSONArray;
import org.json.JSONObject;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
/**
 * Created by tharindu on 1/20/15.
 */


public class OutputDefJSONParser {
    public static StreamDefinition parse(String outputStreamDef)

    {    StreamDefinition def=null;
        try{
            JSONObject obj = new JSONObject(outputStreamDef);
            //String pageNam= obj.
            def=new StreamDefinition().name(obj.getString("name"));
            //def(obj.getString("name"));

            JSONArray array=obj.getJSONArray("data");

            for(int i=0;i<array.length();i++) {

                def.attribute(array.getJSONObject(i).getString("name"), Attribute.Type.valueOf(array.getJSONObject(i).getString("type").toUpperCase()));

            }


        }catch(Exception pe){
            System.out.println("hellow"+pe);
        }
    return def;
    }
}
