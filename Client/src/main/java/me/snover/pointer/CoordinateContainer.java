package me.snover.pointer;

import com.google.common.base.Preconditions;
import org.bukkit.Utility;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a container associated with a server. This container is serializable and can be used to save
 * data in a YAML file
 */
public class CoordinateContainer implements ConfigurationSerializable {

    private final String SERVER_NAME;
    private final List<CoordinateSet> COORDINATE_SET_LIST = new ArrayList<>();

    public CoordinateContainer(@NotNull final String SERVER_NAME) {
        Preconditions.checkNotNull(SERVER_NAME, "Cannot set a null entry");
        this.SERVER_NAME = SERVER_NAME;
    }

    /**
     * Adds a set of coordinates to the container as whole numbers
     * @param id
     * @param x
     * @param y
     * @param z
     * @param x2
     * @param y2
     * @param z2
     */
    @SuppressWarnings("JavadocDeclaration")
    public void addCoordinateSet(int id, int x, int y, int z, int x2, int y2, int z2) {

        if(coordinateSetExists(id, x, y, z, x2, y2, z2)) return;
        CoordinateSet set = new CoordinateSet(id, x, y, z, x2, y2, z2);
        COORDINATE_SET_LIST.add(set);
    }

    /**
     * Removes a coordinate set from the container
     * @param id
     * @param x
     * @param y
     * @param z
     * @param x2
     * @param y2
     * @param z2
     */
    @SuppressWarnings("JavadocDeclaration")
    public void removeCoordinateSet(int id, int x, int y, int z, int x2, int y2, int z2) {
        CoordinateSet coordinateSet = getCoordinateSet(id, x, y, z, x2, y2, z2) ;

        if(coordinateSet == null)
            return;
        COORDINATE_SET_LIST.remove(coordinateSet);
    }

    /**
     * Get all coordinate sets within this container
     * @return Returns a list of coordinate sets
     */
    public CoordinateSet[] getCoordinateSets() {
        CoordinateSet[] coordinateSets = new CoordinateSet[COORDINATE_SET_LIST.size()];
        for(int i = 0; i < COORDINATE_SET_LIST.size(); i++) coordinateSets[i] = COORDINATE_SET_LIST.get(i);
        return coordinateSets;
    }

    /**
     * Check to see if a set of coordinates exist within this container
     * @param id
     * @param x
     * @param y
     * @param z
     * @param x2
     * @param y2
     * @param z2
     * @return Returns {@code true} if the coordinate set exists.
     */
    @SuppressWarnings("JavadocDeclaration")
    public boolean coordinateSetExists(int id, int x, int y, int z, int x2, int y2, int z2) {
        return getCoordinateSet(id, x, y, z, x2, y2, z2) != null;
    }

    /**
     * Obtain the CoordinateSet object represented by the specified values
     * @param id
     * @param x
     * @param y
     * @param z
     * @param x2
     * @param y2
     * @param z2
     * @return Returns null if a match was not found
     */
    @SuppressWarnings("JavadocDeclaration")
    private CoordinateSet getCoordinateSet(int id, int x, int y, int z, int x2, int y2, int z2) {
        // Consider a better matching algorithm?
        for (CoordinateSet coordinateSet : COORDINATE_SET_LIST) {
            int idFromSet = coordinateSet.getID();
            int xFromSet = coordinateSet.getX();
            int yFromSet = coordinateSet.getY();
            int zFromSet = coordinateSet.getZ();
            int x2FromSet = coordinateSet.getX2();
            int y2FromSet = coordinateSet.getY2();
            int z2FromSet = coordinateSet.getZ2();
            if (idFromSet == id && xFromSet == x && yFromSet == y && zFromSet == z
            && x2FromSet == x2 && y2FromSet == y2 && z2FromSet == z2) return coordinateSet;
        }

        return null;
    }

    /**
     * Get the server name associated with this container.
     * @return Returns the name of the server as a {@link String}
     */
    public String getServerName() {
        return SERVER_NAME;
    }

    @Override
    @Utility
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        data.put("server", SERVER_NAME);
        data.put("size", COORDINATE_SET_LIST.size());
        for(int i = 0; i < COORDINATE_SET_LIST.size(); i++) {
            CoordinateSet set = COORDINATE_SET_LIST.get(i);

            sb.setLength(0);
            sb.append(set.getID());
            sb.append('_');
            sb.append(set.getX());
            sb.append(':');
            sb.append(set.getX2());
            sb.append('_');
            sb.append(set.getY());
            sb.append(':');
            sb.append(set.getY2());
            sb.append('_');
            sb.append(set.getZ());
            sb.append(':');
            sb.append(set.getZ2());
            sb.append('_');

            data.put("set" + i, sb.toString());
        }
        return data;
    }

    @NotNull
    public static CoordinateContainer deserialize(Map<String, Object> data) {
        CoordinateContainer container = new CoordinateContainer((String) data.get("server"));
        int size = (int) data.get("size");
        for(int i = 0; i < size; i++) {
            String coords = (String) data.get("set" + i);
            String[] split = coords.split("_");

            int id;
            int [] startingCorner = new int[3];
            int [] endCorner = new int[3];
            String[] coordRange;


            //reverse compatibility
            if(split.length < 4) id = 0;
            else id = Integer.parseInt(split[0]);

            for (int j = 1; j < split.length; ++j) {
                coordRange = split[j - 1].split(":");
                startingCorner[j - 1] = Integer.parseInt(coordRange[0]);
                if(coordRange.length == 1)
                    endCorner[j - 1] = startingCorner[j - 1];
                else
                    endCorner[j - 1] = Integer.parseInt(coordRange[1]);
            }

            container.addCoordinateSet(id,
                    startingCorner[0], startingCorner[1], startingCorner[2],
                    endCorner[0], endCorner[1], endCorner[2]);
        }
        return container;
    }
}
