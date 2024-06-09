package com.model;

import java.util.logging.*;

public class getLogger {

    private static Logger log;
    private static FileHandler handler;

    public static Logger logger() {
        try {
            log = Logger.getLogger("Logger");
            handler = new FileHandler("log.txt", true);
            handler.setFormatter(new SimpleFormatter());
            log.addHandler(handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return log;
    }
}
