package dev.protocoldesigner.example.coupled;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.protocoldesigner.core.exec.DefaultEvent;
import dev.protocoldesigner.core.exec.ProtocolExecutor;

/**
 * CoupledExec
 */
public class CoupledExec {
    private static Logger logger = LoggerFactory.getLogger(CoupledExec.class);

    private static String loadFile(String path){
        String content;
        try(InputStream is = CoupledExec.class.getClassLoader().getResourceAsStream(path)){
            content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }catch(IOException e){
            logger.error("load file failed", e);
            return null;
        }
        return content;
    }
    
    public void run(){
        String protocol = loadFile("traffic_control.json");
        if(protocol==null) return;
        

        ProtocolExecutor p = new ProtocolExecutor(protocol);
        logger.info(p.getEvents().toString());

        DefaultEvent[] eventsH = new DefaultEvent[]{
            new DefaultEvent("H", "Change A"),
            new DefaultEvent("H", "Change B"),
            new DefaultEvent("H", "Change C"),
            new DefaultEvent("H", "Change D")
        };
        DefaultEvent[] eventsV = new DefaultEvent[]{
            new DefaultEvent("V", "Change A"),
            new DefaultEvent("V", "Change B"),
            new DefaultEvent("V", "Change C"),
            new DefaultEvent("V", "Change D")
        };
        Road roadH = new Road("roadH");
        Road roadV = new Road("roadV");
        Cross       cross   = new Cross(p);

        p.registerHook(DefaultEvent.START,  cross::start);
        p.registerHook(DefaultEvent.START,  roadH::start);
        p.registerHook(DefaultEvent.START,  roadV::start);

        /**
         * Events except internal ones can be registered only once. 
         * Subsequent calls with the same event will simply override.
         * This could be changed.
         */
        p.registerHook(eventsH[0],  roadH::stop);
        p.registerHook(eventsV[0],  roadV::straight);

        p.registerHook(eventsH[1],  roadH::left);
        p.registerHook(eventsV[1],  roadV::stop);

        p.registerHook(eventsH[2],  roadH::stop);
        p.registerHook(eventsV[2],  roadV::left);

        p.registerHook(eventsH[3],  roadH::straight);
        p.registerHook(eventsV[3],  roadV::stop);

        logger.info("init");
        p.init();
        p.start();
        logger.info("started");

        // logger.info(p.getEvents().toString());
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    p.shutdown();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }));

    }
    
}
