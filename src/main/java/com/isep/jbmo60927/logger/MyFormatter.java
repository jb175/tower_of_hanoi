package com.isep.jbmo60927.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * formatter used for the log
 */
public class MyFormatter extends Formatter {
    
    /**
     * this method is called for every log records
     * [Date][ClassName <MethodName>] Level: Message
     */
    public String format(final LogRecord rec) {
        final String date = dateFormatter(rec.getMillis()); //date
        final String className = rec.getSourceClassName(); //logger class
        final String methodName = rec.getSourceMethodName(); //logger method
        final String level = rec.getLevel().toString(); //log level
        final String message = rec.getMessage(); //log message

        try {
            final Throwable errorCause = rec.getThrown(); //if the log contain an error

            StringBuilder out = new StringBuilder(); //we create a string builder

            //we add basic log informations
            out.append(String.format("[%s][%s <%s>] %s: %s %n%s%n", date, className, methodName, level, message, errorCause.getMessage()));
            
            //and all the traces
            for (StackTraceElement stackTraceElement : errorCause.getStackTrace()) {
                out.append("        "+stackTraceElement.toString()+"\n");
            }

            //and we return the custom error log
            return out.toString();

        } catch (Exception e) {
            //if the log doesn't get any error we return the basic custom log
            return String.format("[%s][%s %s] %s: %s %n", date, className, methodName, level, message);
        }
    }

    /**
     * date formatter for the log
     * @param millisecs current time used for the file name
     * @return a string with the date correctly displayed
     */
    private String dateFormatter(final long millisecs) {
        final Date resultDate = new Date(millisecs);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return dateFormat.format(resultDate);
    }
}