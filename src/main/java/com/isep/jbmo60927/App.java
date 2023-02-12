package com.isep.jbmo60927;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    //initial state to begin with
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

    //objective to find from the initial state
    private static final Peg[] OBJECTIVE_GAME_PEGS = new Peg[] {
        new Peg(new Disc[] {
        }),
        new Peg(new Disc[] {
        }),
        new Peg(new Disc[] {
            new Disc(4),
            new Disc(3),
            new Disc(2),
            new Disc(1),
        })
    };

    /**
     * an array to contain all states to search for a solution.
     * it begin with the initial state and more states are added throw time until we find a solution
     */
    private final ArrayList<GameState> gameStates = new ArrayList<>();

    /*
     * setup of the app
     */
    public App() {
        //set logger level
        LOGGER.setLevel(Level.FINER);

        //objective
        final GameState objectiveGameState = new GameState(OBJECTIVE_GAME_PEGS);
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.log(Level.FINE, String.format("%s%n%s", "objective:", objectiveGameState.toString()));

        //first state
        GameState gameState = new GameState(INIT_GAME_PEGS);
        this.gameStates.add(gameState);
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.log(Level.FINE, String.format("%s%n%s", "begining:", gameState.toString()));

        int checkForChildren = 0;

        while (!(gameState.equals(objectiveGameState) || checkForChildren>=this.gameStates.size())) {
            for (final int[] possibility : this.gameStates.get(checkForChildren).getPossibleMoves()) {
                gameState = new GameState(this.gameStates.get(checkForChildren), possibility);

                //check if this state as not been used already
                for (int i = 0; i < this.gameStates.size(); i++) {
                    if(gameState.equals(this.gameStates.get(i)))
                        break;
                    
                    //if not we add it to the list to check his childs
                    else if (i == this.gameStates.size()-1) {
                        gameStates.add(gameState);
                        if (LOGGER.isLoggable(Level.FINER))
                            LOGGER.log(Level.FINER, "possibility added to the list");
                    }
                }

                //display the state
                if (LOGGER.isLoggable(Level.FINER))
                    LOGGER.log(Level.FINER, gameState.toString());

                if (gameState.equals(objectiveGameState)) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.log(Level.INFO, "objective found");
                        LOGGER.log(Level.INFO, gameState.drawMoves());
                    }
                    break;
                } else if (LOGGER.isLoggable(Level.FINER))
                    LOGGER.log(Level.FINER, "objective not found");
            }

            //check a new child
            checkForChildren++;
        }
    }

    /**
     * Start the app
     * @param args The arguments of the program.
     * @throws IOException Exception that could occure if the logger can't write on files
     */
    public static void main(final String[] args) throws IOException {
        MyLogger.setup(); //setup the logger for the app
        new App(); //begin the app
    }
}
