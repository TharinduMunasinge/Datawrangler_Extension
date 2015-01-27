package org.wso2.siddhi.extension;

import org.apache.log4j.Logger;
import org.wso2.siddhi.core.config.SiddhiContext;
import org.wso2.siddhi.core.executor.expression.ExpressionExecutor;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import org.wso2.siddhi.query.api.expression.Expression;
import org.wso2.siddhi.query.api.expression.constant.StringConstant;
import org.wso2.siddhi.query.api.extension.annotation.SiddhiExtension;
import org.wso2.siddhi.query.compiler.SiddhiCompiler;
import java.util.List;

/********************************************************************************************************
 * This class can wrangle the events based on the JS script passed as the 1st parameter in siddhi query.
 * Second parameter should be the definition of the output stream of transform processor.

 * Usage:
       from streamA#transform:datawrangler.wrangleScript('js Code goes here','define statement')
       Select .....

 * Created by tharindu on 1/23/15.
 ********************************************************************************************************/

@SiddhiExtension(namespace = "datawrangler", function = "wrangleScript")
public class ScriptDataWranglerTransformer extends DataWranglerTransformProcessor{

    private static Logger logger = Logger.getLogger(ScriptDataWranglerTransformer.class);

    @Override
    protected void init(Expression[] expressions, List<ExpressionExecutor> expressionExecutors, StreamDefinition inStreamDefinition, StreamDefinition outStreamDefinition, String elementId, SiddhiContext siddhiContext) {
        StreamDefinition definition=null;
        String script="";

        if(expressions.length==2) {
            script = ((StringConstant) expressions[0]).getValue();  //first parameter is the script

            Expression exp=expressions[1];    //second parameter should be the output definition

            definition=SiddhiCompiler.parseStreamDefinition(((StringConstant)exp).getValue());

           initialization(script, definition);  //initialize the transform processor

        }else{
            logger.error("incorrect input Format");
        }
    }
}
