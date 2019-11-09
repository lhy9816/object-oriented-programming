package com.calvin.oohw7;

import java.util.ArrayList;

public class Graph {
    private static final int[] AeStops =
            new int[]{-3, -2, -1, 1, 15, 16, 17, 18, 19, 20};
    private static final int[] BeStops =
            new int[]{-2, -1, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    private static final int[] CeStops = new int[]{1, 3, 5, 7, 9, 11, 13, 15};
    private static final ArrayList<int[]> elevStops = new ArrayList<int[]>();
    
    public Graph() {
        if (!elevStops.contains(AeStops)) {
            elevStops.add(AeStops);
            elevStops.add(BeStops);
            elevStops.add(CeStops);
        }
    }
    
    public ArrayList<int[]> getGraph() {
        return elevStops;
        
    }
}
