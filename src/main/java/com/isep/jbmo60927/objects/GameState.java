package com.isep.jbmo60927.objects;

import java.util.ArrayList;

import com.isep.jbmo60927.errors.PegInitException;

public class GameState {
    
    public static final int PEG_NUMBER = 3;

    private static final char PEG_DRAW_BACKGROUND = ' ';
    private static final char PEG_DRAW_BOTTOM = '_';
    private static final char PEG_DRAW_CENTER = '|';
    private static final char PEG_DRAW_DISC = '.';

    private final int level;
    private final Peg[] pegList;
    private final int discNumber;

    public GameState(int level, Peg[] pegList) throws PegInitException {
        this.level = level;
        this.pegList = pegList;
        this.discNumber = getDiscNumber(this.pegList);

        //we verify if the data seem correct
        this.checkData();
    }

    public GameState(GameState gameStateBefore, int[][] move) {
        this.level = gameStateBefore.level+1;
        this.pegList = gameStateBefore.pegList;
        //modified with move

        this.discNumber = getDiscNumber(this.pegList);
    }

    private static final int getDiscNumber(Peg[] pegList) {
        int discNumber = 0;
        for (Peg peg : pegList) {
            discNumber += peg.getDiscs().length;
        }
        return discNumber;
    }

    private final void checkData() throws PegInitException {
        if (this.pegList.length != GameState.PEG_NUMBER)
            throw new PegInitException(String.format("ther is not %d pegs", GameState.PEG_NUMBER));
        
        for (Peg peg : this.pegList)
            if (Boolean.FALSE.equals(peg.isOrdered()))
                throw new PegInitException("pegs are not ordered (max disc at first and min at the end)");
        
        //only one number each time
        int lastPegSize = 0;
        for (int i = 0; i < discNumber; i++) {
            boolean hasBreak = false;
            for (Peg peg : pegList) {
                for (Disc disc : peg.getDiscs()) {
                    if (disc.getSize() == lastPegSize+1) {
                        lastPegSize++;
                        hasBreak = true;
                        break;
                    }
                }
                if (hasBreak)
                    break;
            }
            if (!hasBreak)
                throw new PegInitException("a disc is missing or in double");
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append("\n");
        str.append(String.format("state from level %d:", this.level));
        str.append("\n");
        str.append(this.draw());
        str.append("\n");
        str.append(this.getPossibleMovesMessage());

        return str.toString();
    }

    private final String draw() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < this.discNumber+1; i++) {
            str.append('\n');
            for (int j = 0; j < GameState.PEG_NUMBER; j++) {
                str.append(drawDisc(i, pegList[j], this.discNumber));
            }
        }
        return str.toString();
    }

    private static final String drawDisc(int line, Peg peg, int discNumber) {
        StringBuilder str = new StringBuilder();
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

    public final int[][] getPossibleMoves() {
        ArrayList<int[]> possibilities = new ArrayList<>();

        for (int i = 0; i < this.pegList.length; i++) {
            for (int j = 0; j < this.pegList.length; j++) {

                if (this.pegList[i] != this.pegList[j]) {
                    Disc discToMove = this.pegList[i].getFirstDisc();
                    Disc LastDiscToMoveOn = this.pegList[j].getFirstDisc();

                    if (discToMove != null && (LastDiscToMoveOn == null || discToMove.getSize() < LastDiscToMoveOn.getSize()))
                        possibilities.add(new int[] {i, j});
                }
            }
        }

        return possibilities.toArray(new int[possibilities.size()][]);
    }

    private final String getPossibleMovesMessage() {
        int[][] possibilites = this.getPossibleMoves();
        StringBuilder str = new StringBuilder(String.format("%nThere is %d move possibilities:", possibilites.length));

        for (int[] possibility : possibilites)
            str.append(String.format("%n - %d -> %d", possibility[0], possibility[1]));
        
        return str.toString();
    }
}
