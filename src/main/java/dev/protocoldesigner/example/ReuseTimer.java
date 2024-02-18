package dev.protocoldesigner.example;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * ReuseTimer
 * simple timer
 */
public class ReuseTimer {
    private ScheduledExecutorService exec  = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> f ;

    private Runnable r ;
    private long interval;

    private ReuseTimer(Runnable r, long interval){
        this.r= r;
        this.interval = interval;
    }

    public static ReuseTimer fixRateTimer(Runnable r, long interval){
        ReuseTimer t = new ReuseTimer(r, interval);
        return t;
    }

    public void pause(){
        if(this.f!=null && !this.f.isCancelled())
            this.f.cancel(false);
    }
    public void resume(){
        this.schedule();
    }
    public void start(){
        this.schedule();
    }

    /**
     * initialdely is 0
     */
    private void schedule(){
        this.f= this.exec.scheduleAtFixedRate(r, 0, interval, TimeUnit.MILLISECONDS);
    }

    

    
}
