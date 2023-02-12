package com.isep.jbmo60927.objects;

import java.util.Arrays;

/**
 * a peg contain some discs
 * all discs should be ordered (greater to lower) into a same peg
 */
public class Peg {

    //the list of discs into this peg
    private final Disc[] discs;

    /**
     * the constructor of a peg
     * @param discs the discs to add to this peg
     */
    public Peg(final Disc[] discs) {
        this.discs = discs;
    }

    
    /** 
     * return the disc in this peg
     * @return Disc[] the disc contained in this peg
     */
    public Disc[] getDiscs() {
        return discs;
    }

    
    /** 
     * get the disc at the top of the peg (the first that will be removed)
     * @return Disc
     */
    public Disc getFirstDisc() {
        if (this.discs.length != 0)
            return this.discs[this.discs.length-1];
        else
            return null;
    }

    
    /** 
     * method to verify that this peg is ordered (greater to lower discs)
     * @return Boolean true if it is ordered and false if not
     */
    public Boolean isOrdered() {
        if (discs.length > 0 && discs[0] != null) {
            final int maxDiscSize = discs[0].getSize()+1;
            for (final Disc disc : discs) {
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

    
    /** 
     * hashcode
     * @return int
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(discs);
        return result;
    }

    
    /** 
     * methode to allow comparison between peg (the comparison compare disc)
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
        final Peg other = (Peg) obj;
        if (!Arrays.equals(discs, other.discs))
            return false;
        return true;
    }
}
