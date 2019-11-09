package com.calvin.oohw7;

import com.oocourse.TimableOutput;

import java.util.concurrent.LinkedBlockingDeque;

public class OoMain {
    public static void main(String[] args) {
        try {
            TimableOutput.initStartTimestamp();
            // your code here
            
            // lockers
            TopScheduler topScheduler = new TopScheduler(
                    new LinkedBlockingDeque<OriginalRequest>());
            InputHandler inputHandler = new InputHandler(topScheduler);
            
        
            // begin program
            inputHandler.setPriority(Thread.MAX_PRIORITY);
            // schedulerServer.setPriority(Thread.MIN_PRIORITY);
            inputHandler.start();
            Thread topScheThread = new Thread(topScheduler);
            topScheThread.start();
            topScheduler.startElevatorSchedulersAndElevs();
            // schedulerServer.start();
        
        } catch (Exception e) {
            StackTraceElement trace = e.getStackTrace()[1];
            System.out.println(String.format("[%s : %s] [%s] %s",
                    trace.getFileName(), trace.getLineNumber(),
                    e.getClass().getName(), e.getMessage()));
            System.exit(1);
        }
    }
}
