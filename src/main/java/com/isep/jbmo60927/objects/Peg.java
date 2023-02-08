package com.isep.jbmo60927.objects;

public class Peg {

    private Disc[] discs;

    public Peg(Disc[] discs) {
        this.discs = discs;
    }

    public Disc[] getDiscs() {
        return discs;
    }

    public Disc getFirstDisc() {
        if (this.discs.length != 0)
            return this.discs[this.discs.length-1];
        else
            return null;
    }

    public Boolean isOrdered() {
        if (discs.length > 0 && discs[0] != null) {
            int maxDiscSize = discs[0].getSize()+1;
            for (Disc disc : discs) {
                if (disc != null) {
                    if (disc.getSize() >= maxDiscSize) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } else if(discs.length > 0) {
            return false;
        }
        return true;
    }
}
