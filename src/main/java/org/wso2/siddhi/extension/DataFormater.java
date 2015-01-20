package org.wso2.siddhi.extension;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.siddhi.query.api.definition.*;

import java.util.ArrayList;
import java.util.List;
import  org.wso2.siddhi.query.api.definition.Attribute;
/**
 * Created by tharindu on 1/20/15.
 */
public class DataFormater {
    private static final Log logger = LogFactory.getLog(DataFormater.class);


    public static Object[] csvToObjectConverter(String csv,OutputStreamDef def)
    {

        String [] output=csv.split(",");

        List<Object> list=new ArrayList<>();
        for(int i=0;i<output.length;i++)
        {

            logger.error(output[i]);
            logger.error(def.getAttribute(i).getType());
            list.add(i, converter(output[i], def.getAttribute(i).getType()));
        }

       return list.toArray();
    }

    public static Object converter(String str,String def)
    {

        Object value=null;

        switch (def)
        {
            case "int" : value=Integer.valueOf(str);break;
            case "double" : value=Double.valueOf(str);break;
            case "float" : value=Float.valueOf(str);break;
            case "boolean" : value=Double.valueOf(str);break;
            case "long" : value=Long.valueOf(str);break;
            case "string": value=str;
        }
        return value;
    }


}
