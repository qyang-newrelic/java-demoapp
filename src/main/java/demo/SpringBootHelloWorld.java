package demo;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import java.util.function.*;
import com.newrelic.api.agent.Agent;
import com.newrelic.api.agent.NewRelic;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException; 
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.OpenTelemetryDemo;

/*
for (Map.Entry<String, String> entry : linkingMetadata.entrySet()) {
    eventObject.getMDCPropertyMap().put(entry.getKey(), entry.getValue());
    }
*/

@RestController
@EnableAutoConfiguration
public class SpringBootHelloWorld {
    //public static Supplier<Agent> agentSupplier = NewRelic::getAgent;
    private Logger logger = LoggerFactory.getLogger(SpringBootHelloWorld.class);
    private OpenTelemetryDemo opDemo = new OpenTelemetryDemo();
    

  

    @RequestMapping("/opentelemetry")
    String openTelemetry() throws Exception 
    {
        //ObjectMapper objectMapper = new ObjectMapper();
        //opDemo = new OpenTelemetryDemo();
        //opDemo.init();

        //String agentData = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(linkingMetadata);
        //String agentData = objectMapper.writeValueAsString(linkingMetadata);
        info("start a new transaction /opentelemetry" );
        opDemo.doRequest("request name");
        //return "Hello World Spring Boot! " + agentData + "\n";
        return "Start a transaction with Open Telemetry Instrumentation! \n";
    }

    @RequestMapping("/")
    String home() throws Exception
    {
      info("get a request /" );
      return "Hello World Spring Boot! \n"; 


    }


    private void info(String msg) 
    {
      /*
      Map<String, String> linkingMetadata = agentSupplier.get().getLinkingMetadata();
      for (Map.Entry<String, String> entry : linkingMetadata.entrySet()) {
        MDC.put(entry.getKey(), entry.getValue()); 
      }
      */

      logger.info(msg);


      //MDC.clear();

    } 

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringBootHelloWorld.class, args);
    }
}


