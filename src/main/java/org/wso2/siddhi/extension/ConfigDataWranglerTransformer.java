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
 /********************************************************************************************************************************************************
 * This class can wrangle the events based on the WRANGLER CONFIGURATIONS saved in wso2 registry
 * Once the user graphically wrangle event using carbon component inside the Admin console , Generated JS CODE and OUTPUT DEFINITION will be stored in folder with a GIVEN NAME
 * This transform process will transform the events based on that CONFIGURATION

 * Usage:
 *  CONFIGURATION NAME should be passed as a parameter
        1)    from streamA#transform:datawrangler.wrangleConfig('CONFIGURATION NAME')
              Select .....
        2)    from streamA#transform:datawrangler.wrangleConfig('CONFIGURATION NAME','Define statement for output stream')
              Select .....

 * Created by tharindu on 1/23/15.
 **********************************************************************************************************************************************************/


@SiddhiExtension(namespace = "datawrangler", function = "wrangleConfig")
public class ConfigDataWranglerTransformer extends DataWranglerTransformProcessor {

    private static Logger logger = Logger.getLogger(ConfigDataWranglerTransformer.class);

    @Override
    protected void init(Expression[] expressions, List<ExpressionExecutor> expressionExecutors, StreamDefinition inStreamDefinition, StreamDefinition outStreamDefinition, String elementId, SiddhiContext siddhiContext) {
        StreamDefinition definition=null;
        String script="";

        if(expressions.length<=2 && expressions.length>0) {                     // if only 2 parameters presents
            String configName = ((StringConstant) expressions[0]).getValue();   //first parameter should be the configuration name
            script = getRegistry().getScript(configName);                       //get the script in registry
            String stremdef = "";
            try {
                if (expressions.length == 1) {
                    // if only one parameter presents=> use the output definition in registry
                    stremdef = getRegistry().getOutputStreamDefinition(configName);

                }else{
                    // if exactly 2 parameters presents=> second parameter should be the output definition
                    stremdef = ((StringConstant) expressions[1]).getValue();

                }

                if(stremdef!=null)
                    logger.info("OutputStream Definition and script resolved ");
                else
                    logger.info("OutputStream Definition and script is not resolved");

                definition = SiddhiCompiler.parseStreamDefinition(stremdef);

                if(definition==null)
                    logger.error("outputStream doesn't parsed");
                else
                    logger.error("outputStream parsed successfully");

                initialization(script, definition);
            }catch(Exception e){
                logger.error("Definition parsing exception" + e.getMessage());
            }
        }else{
            logger.error("incorrect input Format");
        }
    }
}
