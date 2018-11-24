package com.kane.practice.program.pingpong;

public class PingPong extends Thread {
    private String word;
    private long delay;

    public PingPong(String word,long delay){
        this.word = word;
        this.delay = delay;
    }

    public void run(){
        for (;;) {
            System.out.print(word + " ");
            try {
               sleep(delay);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public static void main(String[] args) {
         new PingPong("ping",33).start();
        new PingPong("PONG",100).start();
    }
}
