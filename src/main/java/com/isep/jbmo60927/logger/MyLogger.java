package com.isep.jbmo60927.logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * setup the logger for the app
 */
public class MyLogger {

    /**
     * Default constructor
     * @throws IllegalStateException the constructor is not used
     */
    private MyLogger() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * setup the logger for the app
     * @throws IOException if something bad occure
     */
    public static void setup() throws IOException {
        //root logger
        final Logger rootLogger = Logger.getLogger("");
        
        //create file log handler
        final String date = dateFormatter(System.currentTimeMillis());
        FileHandler logfile;
        try {
            logfile = new FileHandler("log/client_"+date+".log"); //we try to create the log file
        } catch (final NoSuchFileException e) { //if the log folder doesn't exist
            new File("log").mkdirs(); //we create the folder
            logfile = new FileHandler("log/client_"+date+".log"); //and create the log file
        }

        //we add the file log handler to the root logger
        rootLogger.addHandler(logfile);

        //apply the custom formatter to all rootlogger's handeler
        for (final Handler handler : rootLogger.getHandlers()) {
            handler.setFormatter(new MyFormatter());
        }
    }

    /**
     * date formatter for the file name of the log
     * @param millisecs current time used for the file name
     * @return a string with the date correctly displayed
     */
    private static String dateFormatter(final long millisecs) {
        final Date resultDate = new Date(millisecs);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return dateFormat.format(resultDate);
    }
}