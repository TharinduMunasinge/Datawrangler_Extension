package org.wso2.siddhi.extension;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.siddhi.query.api.definition.*;

import java.util.ArrayList;
import java.util.List;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import  org.wso2.siddhi.query.api.definition.Attribute.Type;
import  org.wso2.siddhi.query.api.definition.Attribute;

/**
 * Created by tharindu on 1/20/15.
 */
public class DataFormater {
    private static final Log logger = LogFactory.getLog(DataFormater.class);

    public static String objectArraytoCSV(Object ara[])
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


    public static Object[] csvToObjectConverter(String csv,StreamDefinition def)
    {

        String [] output=csv.split(",");

        List<Attribute>  attributes=   def.getAttributeList();
        List<Object> list=new ArrayList<>();
        for(int i=0;i<output.length;i++)
        {

            logger.error(output[i]);

            logger.error(attributes.get(i).getName());
            list.add(i, converter(output[i], attributes.get(i).getType()));
        }

       return list.toArray();
    }



    public static Object converter(String str,Type def)
    {

        Object value=null;

        switch (def)
        {
            case INT: value=Integer.valueOf(str);break;
            case DOUBLE:value=Double.valueOf(str);break;
            case FLOAT:  value=Float.valueOf(str);break;
            case BOOL:  value=Double.valueOf(str);break;
            case LONG:  value=Long.valueOf(str);break;
            case STRING: value=str;
        }
        return value;
    }


}
