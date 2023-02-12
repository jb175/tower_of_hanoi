package com.isep.jbmo60927.objects;

/**
 * respresent a disc into this app
 * a disc has only a size. That's an unique value
 */
public class Disc {

    // size of the disc
    private final int size;

    /**
     * disc constructor
     * @param size size of the disc
     */
    public Disc(final int size) {
        this.size = size;
    }


    /** 
     * get the size of this disc
     * @return int
     */
    public int getSize() {
        return size;
    }


    /** 
     * hashcode
     * @return int
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + size;
        return result;
    }

    
    /** 
     * methode to allow comparison between discs (the comparison compare size of discs)
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
        final Disc other = (Disc) obj;
        if (size != other.size)
            return false;
        return true;
    }
}
