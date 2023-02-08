package com.isep.jbmo60927.errors;

public class PegInitException extends Exception{
    public PegInitException(String msg) {
        super("Error during the init of the first peg\n"+msg);
    }

    public PegInitException() {
        super("Error during the init of the first peg");
    }
}
