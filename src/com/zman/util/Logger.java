package com.zman.util;

import java.util.logging.Level;

/**
 * Simple Wrapper for java.util.logging.Logger
 * 
 * @author Zeb
 */
public class Logger {
    
    private static java.util.logging.Logger log;
    
    public Logger(String logInstance) {
	log = java.util.logging.Logger.getLogger(logInstance);
    }

    public void log(Level level, Object obj, String msg) {
	// This seems easier
	log.log(level, obj.getClass().getSimpleName() + ": " + msg);
    }

}