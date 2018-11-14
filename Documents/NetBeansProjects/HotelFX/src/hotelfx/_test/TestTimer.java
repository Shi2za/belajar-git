/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx._test;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author ANDRI
 */
public class TestTimer {

    private static Timer timer;

    private final long idleTimeMillis = 1000;

    private int counter = 0;

    public TestTimer() {

    }

    private void runPeriodicAction() {
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                doTask();
            }

        };
        timer = new Timer();
        timer.schedule(timerTask, idleTimeMillis);
    }

    private void doTask() {
        //do task here..
        System.out.println("" + counter);
        counter++;
        //restart timer with same task
        TimerTask a = new TimerTask() {

            @Override
            public void run() {
                doTask();
            }

        };
        timer = new Timer();
        timer.schedule(a, idleTimeMillis);
    }

//    public static void main(String[] args) {
//        TestTimer test = new TestTimer();
//        test.runPeriodicAction();
//    }

}
