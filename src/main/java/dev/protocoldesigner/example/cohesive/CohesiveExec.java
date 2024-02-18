package dev.protocoldesigner.example.cohesive;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.protocoldesigner.example.Main;
import dev.protocoldesigner.core.exec.DefaultEvent;
import dev.protocoldesigner.core.exec.ProtocolExecutor;

/**
 * CohesiveExec
 */
public class CohesiveExec {
    private static Logger logger = LoggerFactory.getLogger(CohesiveExec.class);

    private static String loadFile(String path){
        String content;
        try(InputStream is = Main.class.getClassLoader().getResourceAsStream(path)){
            content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }catch(IOException e){
            logger.error("load file failed", e);
            return null;
        }
        return content;
    }

    public void run(){
        String protocol = loadFile("traffic_control_simple.json");
        if(protocol==null) return;
        
        ProtocolExecutor p= new ProtocolExecutor(protocol);
        logger.info(p.getEvents().toString());
        DefaultEvent toggleH = new DefaultEvent("H", "Change");
        DefaultEvent toggleV = new DefaultEvent("V", "Change");

        Cross cross = new Cross(p);
        Road  roadH = new Road("roadH", 0);
        Road  roadV = new Road("roadV", 3);

        p.registerHook(DefaultEvent.START, cross::start);
        p.registerHook(DefaultEvent.START, roadH::start);
        p.registerHook(DefaultEvent.START, roadV::start);

        p.registerHook(toggleH, roadH::toggle);
        p.registerHook(toggleV, roadV::toggle);


        logger.info("init");
        p.init();
        p.start();
        logger.info("started");

        // logger.info(p.getEvents().toString());
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    logger.info("halting down");
                    p.shutdown();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }));
        
    }
    


     
}
