package Lab3;

import Lab2.Point3d;

/**
 * This class represents a specific location in a 2D map.  Coordinates are
 * integer values.
 **/
public class Location
{
    /** X coordinate of this location. **/
    public int xCoord;

    /** Y coordinate of this location. **/
    public int yCoord;


    /** Creates a new location with the specified integer coordinates. **/
    public Location(int x, int y)
    {
        xCoord = x;
        yCoord = y;
    }

    /** Creates a new location with coordinates (0, 0). **/
    public Location()
    {
        this(0, 0);
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Location)) return false;

        Location otherLocation = (Location) obj;

        return this.xCoord == otherLocation.xCoord
                && this.yCoord == otherLocation.yCoord;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + xCoord;
        result = 31 * result + yCoord;
        return result;
    }
}
