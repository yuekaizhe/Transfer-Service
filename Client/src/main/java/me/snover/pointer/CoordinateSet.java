package me.snover.pointer;

/**
 * This class is a representation of a location in the world by use of its coordinates after being deserialized.
 */
public class CoordinateSet {
    private final int ID;
    private final int X;
    private final int Y;
    private final int Z;
    private final int X2;
    private final int Y2;
    private final int Z2;


    public CoordinateSet(final int ID, final int X, int Y, final int Z, int X2, int Y2, int Z2) {
        this.ID = ID;
        this.X = X;
        this.Y = Y;
        this.Z = Z;
        this.X2 = X2;
        this.Y2 = Y2;
        this.Z2 = Z2;
    }

    public int getID() {
        return ID;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public int getX2() {
        return X2;
    }

    public int getY2() {
        return Y2;
    }

    public int getZ2() {
        return Z2;
    }

    public int getZ() {
        return Z;
    }
}