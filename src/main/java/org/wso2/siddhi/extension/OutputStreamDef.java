package org.wso2.siddhi.extension;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tharindu on 1/20/15.
 */
public class OutputStreamDef {

    private String streamName;
    private String version;
    private List<Attribute> attributes;
    public OutputStreamDef()
    {
        attributes=new ArrayList<Attribute>();
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public  void setAttributes(List<Attribute> attributeList) {
        attributes=attributeList;
    }


    public void setAttribute(Attribute attribute , int index) {
        attributes.add(index,attribute);

    }

    public Attribute getAttribute(int index){
        return attributes.get(index);
    }


    public void show()
    {
        System.out.println("name"+this.getStreamName());
        System.out.println("version"+this.getVersion());
        for(int i=0;i<attributes.size();i++){
            System.out.println("\nAttribute 1");
            attributes.get(i).show();
        }
    }
}


class Attribute
{

    private String name;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void show()
    {
        System.out.println("name :"+this.name);
        System.out.println("type :"+this.type);
    }
}