package org.wso2.siddhi.extension;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by tharindu on 1/20/15.
 */
public class OutputDefJSONParser {
    public static OutputStreamDef parse(String outputStreamDef)
    {    OutputStreamDef def=null;
        try{
            JSONObject obj = new JSONObject(outputStreamDef);
            //String pageNam= obj.
            def=new OutputStreamDef();
            def.setStreamName(obj.getString("name"));
            def.setVersion(obj.getString("version"));
            JSONArray array=obj.getJSONArray("data");

            for(int i=0;i<array.length();i++)
            {
                Attribute attr=new Attribute();
                attr.setName(array.getJSONObject(i).getString("name"));
                attr.setType(array.getJSONObject(i).getString("type"));
                def.setAttribute(attr,i);
            }


        }catch(Exception pe){
            System.out.println("hellow"+pe);
        }
    return def;
    }
}
