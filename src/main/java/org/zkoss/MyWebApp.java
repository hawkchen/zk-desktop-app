package org.zkoss;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class MyWebApp {
    public static void main(String[] args) {
        SpringApplication.run(MyWebApp.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void ready() {
        MyFxApp.notifyStart();
    }
}
