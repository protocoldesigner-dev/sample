package dev.protocoldesigner.example.cohesive;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.protocoldesigner.core.exec.DefaultEvent;
import dev.protocoldesigner.core.exec.ProtocolExecutor;

/**
 * Cross
 */
public class Cross {
    Logger logger = LoggerFactory.getLogger(Cross.class);
    private ProtocolExecutor p;
    private Timer t;
    private DefaultEvent[] events = new DefaultEvent[]{
        new DefaultEvent("Traffic cross", "Change"),
    };

    public Cross(ProtocolExecutor p){
        this. p = p;
        this.t = new Timer();
    }
    /**
     * issue an event immediately so that we start the road via event
     */
    public void start(DefaultEvent arg0, String arg1, String arg2){
        t.scheduleAtFixedRate(new TimerTask() {
            private long phase=0;
            @Override
            public void run() {
                try{
                    int index = (int)phase ++ % events.length;
                    logger.info("----------------------------------------------------");
                    logger.info("issuing event {}", events[index]);
                    p.issueEventBlocking(events[index]);
                    logger.info("event issued {}", events[index]);
                    logger.info("----------------------------------------------------");

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }, 0, 4*1000);
        logger.info("cross started");
        
    }
    
}
