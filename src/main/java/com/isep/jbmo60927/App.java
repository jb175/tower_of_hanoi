package com.isep.jbmo60927;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.isep.jbmo60927.errors.PegInitException;
import com.isep.jbmo60927.logger.MyLogger;
import com.isep.jbmo60927.objects.Disc;
import com.isep.jbmo60927.objects.GameState;
import com.isep.jbmo60927.objects.Peg;

/*
 * Main app to begin the app
 */
public class App {

    //logger for this class
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    private static final Peg[] INIT_GAME_PEGS = new Peg[] {
        new Peg(new Disc[] {
            new Disc(4),
            new Disc(3),
            new Disc(2),
            new Disc(1),
        }),
        new Peg(new Disc[] {
        }),
        new Peg(new Disc[] {
        })
    };

    private ArrayList<GameState> gameStates = new ArrayList<>();

    /*
     * setup of the app
     */
    public App() throws PegInitException {
        //set logger level
        LOGGER.setLevel(Level.INFO);

        //init the list
        // !!!!!! We don't verify that the original input is valid !!!!!!!
        this.gameStates.add(new GameState(0, INIT_GAME_PEGS));



        if (LOGGER.isLoggable(Level.INFO))
            LOGGER.log(Level.INFO, this.gameStates.get(0).toString());
    }

    /**
     * Start the app
     * @param args The arguments of the program.
     * @throws IOException Exception that could occure if the logger can't write on files
     * @throws PegInitException Exception that could occure if the initial peg game is uncorrect
     */
    public static void main(String[] args) throws IOException, PegInitException {
        MyLogger.setup(); //setup the logger for the app
        new App(); //begin the app
    }
}
