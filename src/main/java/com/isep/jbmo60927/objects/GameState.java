package com.isep.jbmo60927.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameState {

    //logger for this class
    private static final Logger LOGGER = Logger.getLogger(GameState.class.getName());
    
    //number of pegs
    public static final int PEG_NUMBER = 3;

    //characters used to draw the game state
    private static final char PEG_DRAW_BACKGROUND = ' ';
    private static final char PEG_DRAW_BOTTOM = '_';
    private static final char PEG_DRAW_CENTER = '|';
    private static final char PEG_DRAW_DISC = '.';

    //static value to enable the data verification at each step of the solution finding
    private static final Boolean ENABLE_VERIFICATION = false;

    //level of complexity to reach a solution
    private final int level;
    //list of stored peg in a game state
    private final Peg[] pegList;
    //number of discs into this state (should be the same for every state)
    private final int discNumber;
    //history of states
    private final int[][] movesBefore;

    /**
     * constructor for an initial state
     * @param pegList the list of peg to add to this state
     */
    public GameState(final Peg[] pegList) {
        LOGGER.setLevel(Level.INFO);

        this.level = 0;
        this.pegList = pegList;
        this.discNumber = getDiscNumber(this.pegList);
        this.movesBefore = new int[0][0];

        //we verify if the data seems correct
        if (Boolean.TRUE.equals(ENABLE_VERIFICATION))
            this.checkData();
    }

    /**
     * constructor for other states
     * @param gameStateBefore the state to copy data from
     * @param move the move we wants to do from the old state
     */
    public GameState(final GameState gameStateBefore, final int[] move) {
        LOGGER.setLevel(Level.INFO);

        final Peg[] pegListBuilder = gameStateBefore.getPegList();


        this.level = gameStateBefore.getLevel()+1;
        this.pegList = makeAMove(pegListBuilder, move);
        this.discNumber = getDiscNumber(this.pegList);

        final ArrayList<int[]> moveHistoryBuilder = new ArrayList<>();
        Collections.addAll(moveHistoryBuilder, gameStateBefore.getMovesBefore());
        moveHistoryBuilder.add(move);
        this.movesBefore = moveHistoryBuilder.toArray(new int[moveHistoryBuilder.size()][]);

        //we verify if the data seems correct
        if (Boolean.TRUE.equals(ENABLE_VERIFICATION))
            this.checkData();
    }

    /**
     * make a move to a list of peg that would be used to create a new state
     * @param stateBefore the state to copy from
     * @param move the move to do
     * @return the new peg list with disc modified corresponding to the move
     */
    private static final Peg[] makeAMove(final Peg[] stateBefore, final int[] move) {

        //uncorrect move
        if (move.length != 2 || move[0] < 0 || move[0] >= PEG_NUMBER || move[1] < 0 || move[1] >= PEG_NUMBER) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(Level.SEVERE, "the move is not correct");
            System.exit(0);
        }

        //remove a disk from this list
        Disc movedDisc = new Disc(0);
        final Peg[] returnPeg = new Peg[GameState.PEG_NUMBER];
        ArrayList<Disc> pegBuilder;
        for (int i = 0; i < stateBefore.length; i++) {
            pegBuilder = new ArrayList<>();
            if (stateBefore[i].getDiscs().length > 0) {
                for (int j = 0; j < stateBefore[i].getDiscs().length; j++) {
                    if (i==move[0] && j==stateBefore[i].getDiscs().length-1)
                        movedDisc = stateBefore[i].getDiscs()[j];
                    else
                        pegBuilder.add(stateBefore[i].getDiscs()[j]);
                }
            }
            returnPeg[i] = new Peg(pegBuilder.toArray(new Disc[pegBuilder.size()]));
        }

        //if no disk can be withdraw
        if (movedDisc == new Disc(0)) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(Level.SEVERE, "No disc found");
            System.exit(0);
        }

        //add the removed disk to the new list
        pegBuilder = new ArrayList<>();
        for (int j = 0; j < stateBefore[move[1]].getDiscs().length; j++) {
            pegBuilder.add(stateBefore[move[1]].getDiscs()[j]);
        }
        if (!pegBuilder.isEmpty() && pegBuilder.get(pegBuilder.size()-1).getSize() <= movedDisc.getSize()) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(Level.SEVERE, "the disc we try to move is greater than the last disc in the peg");
            System.exit(0);
        }
        pegBuilder.add(movedDisc);
        returnPeg[move[1]] = new Peg(pegBuilder.toArray(new Disc[pegBuilder.size()]));

        return returnPeg;
    }

    /**
     * check the data inside this state to be sure it has not be badly modified
     */
    private final void checkData() {
        if (this.pegList.length != GameState.PEG_NUMBER) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(Level.SEVERE, String.format("ther is not %d pegs", GameState.PEG_NUMBER));
            System.exit(0);
        }
        
        for (final Peg peg : this.pegList)
            if (Boolean.FALSE.equals(peg.isOrdered())) {
                if (LOGGER.isLoggable(Level.SEVERE))
                    LOGGER.log(Level.SEVERE, "pegs are not ordered (max disc at first and min at the end)");
                System.exit(0);
            }
        
        //only one number each time
        int lastPegSize = 0;
        for (int i = 0; i < discNumber; i++) {
            boolean hasBreak = false;
            for (final Peg peg : pegList) {
                for (final Disc disc : peg.getDiscs()) {
                    if (disc.getSize() == lastPegSize+1) {
                        lastPegSize++;
                        hasBreak = true;
                        break;
                    }
                }
                if (hasBreak)
                    break;
            }
            if (!hasBreak) {
                if (LOGGER.isLoggable(Level.SEVERE))
                    LOGGER.log(Level.SEVERE, "a disc is missing or in double");
                System.exit(0);
            }
        }
    }

    
    /** 
     * hashcode
     * @return int
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(pegList);
        return result;
    }

    
    /** 
     * methode to allow comparison between states (the comparison compare only the pegs)
     * @param obj the object to compare with
     * @return boolean true if equivalent
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final GameState other = (GameState) obj;
        if (!Arrays.equals(pegList, other.pegList))
            return false;
        return true;
    }


    /** 
     * display quickly evrything we need about a game state
     * @return String
     */
    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();

        str.append(this.drawLevel()+"\n");
        str.append(this.draw()+"\n");
        str.append(this.drawMoves()+"\n");
        str.append(this.drawPossibleMoves());

        return str.toString();
    }


    /** 
     * display the complexity level of this state (how many state to arrive at this point)
     * @return String
     */
    private final String drawLevel() {
        return String.format("state from level %d:", this.level);
    }

    
    /** 
     * draw some charactere to the screen to display something to represent pegs and discs
     * @return String
     */
    private final String draw() {
        final StringBuilder str = new StringBuilder();
        for (int i = 0; i < this.discNumber+1; i++) {
            str.append('\n');
            for (int j = 0; j < GameState.PEG_NUMBER; j++) {
                str.append(drawDisc(i, pegList[j], this.discNumber));
            }
        }
        return str.toString();
    }

    
    /** 
     * method used by the draw method to display a specific disc
     * @param line the line we are drawing
     * @param peg the peg from wich the disc is been drawed
     * @param discNumber the number of char to display in a line
     * @return String
     */
    private static final String drawDisc(final int line, final Peg peg, final int discNumber) {
        final StringBuilder str = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            for (int k = 0; k < discNumber; k++) {
                if (line == discNumber) {
                    str.append(GameState.PEG_DRAW_BOTTOM);
                } else if (peg.getDiscs().length >= discNumber - line && 
                    ((i == 0 && peg.getDiscs()[discNumber - line - 1].getSize() > (discNumber - k - 1)) || 
                    ( i == 1 && peg.getDiscs()[discNumber - line - 1].getSize() > k))) {
                    str.append(GameState.PEG_DRAW_DISC);
                } else {
                    str.append(GameState.PEG_DRAW_BACKGROUND);
                }
            }
            if (i == 0) {
                str.append(GameState.PEG_DRAW_CENTER);
            } else {
                str.append(GameState.PEG_DRAW_BACKGROUND);
            }
        }

        return str.toString();
    }

    
    /** 
     * display the possible move to do from this state (evry possibilities are displayed event the one we already make)
     * @return String
     */
    private final String drawPossibleMoves() {
        final int[][] possibilites = this.getPossibleMoves();
        final StringBuilder str = new StringBuilder(String.format("There is %d move possibilities:", possibilites.length));

        for (final int[] possibility : possibilites)
            str.append(String.format("%n - %d -> %d", possibility[0], possibility[1]));
        
        return str.toString();
    }

    
    /** 
     * display moves done before this state
     * @return String
     */
    public final String drawMoves() {
        final StringBuilder st = new StringBuilder("move history:");
        for (final int[] moveHistory : this.movesBefore) {
            st.append(String.format("%n%d -> %d", moveHistory[0], moveHistory[1]));
        }
        return st.toString();
    }

    
    /** 
     * get the complexity level of this state
     * @return int
     */
    public int getLevel() {
        return level;
    }

    
    /** 
     * get the peg list of this state
     * @return Peg[]
     */
    public Peg[] getPegList() {
        return pegList;
    }

    
    /** 
     * get the history of states done before this state
     * @return int[][]
     */
    public int[][] getMovesBefore() {
        return movesBefore;
    }

    
    /** 
     * get the move we can do from this state (evry move is returned, even the one we have already done)
     * @return int[][]
     */
    public final int[][] getPossibleMoves() {
        final ArrayList<int[]> possibilities = new ArrayList<>();

        for (int i = 0; i < this.pegList.length; i++) {
            for (int j = 0; j < this.pegList.length; j++) {

                if (this.pegList[i] != this.pegList[j]) {
                    final Disc discToMove = this.pegList[i].getFirstDisc();
                    final Disc LastDiscToMoveOn = this.pegList[j].getFirstDisc();

                    if (discToMove != null && (LastDiscToMoveOn == null || discToMove.getSize() < LastDiscToMoveOn.getSize()))
                        possibilities.add(new int[] {i, j});
                }
            }
        }

        return possibilities.toArray(new int[possibilities.size()][]);
    }

    
    /** 
     * get the number of disc we have in all pegs
     * @param pegList the list of peg to search for
     * @return int
     */
    private static final int getDiscNumber(final Peg[] pegList) {
        int discNumber = 0;
        for (final Peg peg : pegList) {
            discNumber += peg.getDiscs().length;
        }
        return discNumber;
    }
}
