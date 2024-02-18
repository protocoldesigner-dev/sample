package dev.protocoldesigner.example.cohesive;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.protocoldesigner.core.exec.DefaultEvent;
import dev.protocoldesigner.core.exec.ProtocolExecutor;
import dev.protocoldesigner.example.Car;
import dev.protocoldesigner.example.ReuseTimer;

/**
 * Road
 */
public class Road {
    private BlockingQueue<Car> straightQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<Car> leftQueue     = new LinkedBlockingQueue<>();
    private Logger             logger        = LoggerFactory.getLogger(Road.class);
    private Timer t                          = new Timer();
    private String name;

    private ReuseTimer straightTimer              = ReuseTimer.fixRateTimer(()->{
        Car c = straightQueue.poll();
        if(c!=null){
            logger.info("{} straight",  c);
        }
    }, 150);
    private ReuseTimer leftTimer                  = ReuseTimer.fixRateTimer(()->{
        Car c = leftQueue.poll();
        if(c!=null){
            logger.info("{}     left",  c);
        }
    }, 150);
    /**
     * internal counter represents the current state
     * counter%4 == 0   => straight
     * counter%4 == 1   => stop
     * counter%4 == 2   => left
     * counter%4 == 3   => stop
     */
    private long   counter                   ;

    /**
     * @param name name tag of the road
     * @param startPhase   startPhase of the road
     */
    public Road(String name, int startPhase){
        this.name = name;
        this.counter = startPhase%4;
    }

    /**
     * TODO manually trigger an event
     * 
     * Initialization can be done here. register it via 
     * {@linkplain ProtocolExecutor#registerHook(DefaultEvent, dev.protocoldesigner.core.exec.TriFunction)}
     * with the event Parameter set to {@link DefaultEvent#START}
     * 
     */
    public void start(DefaultEvent arg0, String arg1, String arg2){
        t.scheduleAtFixedRate(new TimerTask() {
            Random r = new Random();
            
            @Override
            public void run() {
                int select = r.nextInt(5);
                if(select==0) straightQueue.add(new Car());
                else if(select == 1) leftQueue.add(new Car());
            }
        }, 0,  100);
    }
    public void toggle(DefaultEvent arg0, String arg1, String arg2){
        int phase = (int)(++counter % 4);
        // counter++;

        switch(phase){
            case(0):{
                //stop    => straight
                straightTimer.resume();
                logger.info("{}    stop     =>   straight : {} -> {}", name, arg1, arg2);
                return;
            }
            case(1):{
                //stright => stop
                straightTimer.pause();
                logger.info("{}    straight =>   stop     : {} -> {}", name, arg1, arg2);
                return;
            }
            case(2):{
                //stop    => left
                leftTimer.resume();
                logger.info("{}    stop     =>   left     : {} -> {}", name, arg1, arg2);
                return;
            }
            case(3):{
                //left    => stop
                leftTimer.pause();
                logger.info("{}    left     =>   stop     : {} -> {}", name, arg1, arg2);
                return;
            }
            default:{
                return;
            }
        }

        
    }

    
}
