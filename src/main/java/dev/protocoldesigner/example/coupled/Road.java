package dev.protocoldesigner.example.coupled;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.protocoldesigner.example.Car;
import dev.protocoldesigner.example.ReuseTimer;
import dev.protocoldesigner.core.exec.DefaultEvent;

/**
 * Road
 * TODO more stage hooks and predicates
 */
public class Road{
    Logger logger = LoggerFactory.getLogger(Road.class);
    /**
     * queue of cars to go straight the cross
     */
    private BlockingQueue<Car> straightQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<Car> leftQueue     = new LinkedBlockingQueue<>();
    private Timer t                          = new Timer();
    private String name;

    private ReuseTimer straightTimer         = ReuseTimer.fixRateTimer(()->{
        Car c = straightQueue.poll();
        if(c!=null){
            logger.info("{} straight",  c);
        }
    }, 150);
    private ReuseTimer leftTimer             = ReuseTimer.fixRateTimer(()->{
        Car c = leftQueue.poll();
        if(c!=null){
            logger.info("{}     left",  c);
        }
    }, 150);
    public Road(String name){
        this.name = name;
    }
    public void start(DefaultEvent arg0, String arg1, String arg2) {
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
    public void stop(DefaultEvent arg0, String arg1, String arg2) {
        if("Straight".equals(arg1)){
            //straight => stop
            this.straightTimer.pause();
        }else if("Left".equals(arg1)){
            //left     => stop
            this.leftTimer.pause();
        }else{
            throw new RuntimeException("wrong state " + arg1);
        }
        logger.info("{}    stop    : {} -> {}", name, arg1, arg2);
    }
    public void straight(DefaultEvent arg0, String arg1, String arg2) {
        this.straightTimer.resume();
        logger.info("{}    straight: {} -> {}", name, arg1, arg2);
    }
    public void left(DefaultEvent arg0, String arg1, String arg2) {
        this.leftTimer.resume();
        logger.info("{}    left    : {} -> {}", name, arg1, arg2);
    }
}
