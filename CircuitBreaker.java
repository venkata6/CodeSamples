package com.roku.server.authsvc.restclients;


import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CircuitBreaker {

    private String partner;
    private State state;

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    private volatile int threshold;
    public Lock lock;
    private static int THRESHOLD_LEVEL = 3;  // three 5xx in a minute will make the circuit open


    public CircuitBreaker(  ) {
        state = State.CLOSE;
        threshold = 0;
        lock = new ReentrantLock();
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public int getThreshold() {
        return threshold;
    }

    public boolean isOpen() {
        return state == State.OPEN;
    }

    public boolean isClosed() {
        return state == State.CLOSE;
    }

    public boolean isHalfOpen() {
        return state == State.HALF_OPEN;
    }


    public void closeCircuit() {
        state = State.CLOSE;
        threshold = 0 ;
    }
    public void openCircuit() {
        state = State.OPEN;
    }

    public void setHalfOpen() {
        state = State.HALF_OPEN;
      }

    public State getState() {
        return state;
    }

    public static void main ( String[] args) {
        Map<String, CircuitBreaker> circuitBreakerMap = new HashMap<>();
        CircuitBreaker cb = new CircuitBreaker();
        circuitBreakerMap.put("hulu",cb);

    }

    public enum  State {
        CLOSE,
        OPEN,
        HALF_OPEN;
    }

    public void updateErrorThreshold() {
        if ( threshold  > THRESHOLD_LEVEL)  {
            // threshold level already high , nothing to do
            return ;
        }
        threshold++;
        if ( threshold == THRESHOLD_LEVEL && state == State.CLOSE ) {
            state = State.OPEN;
        }
    }

}
