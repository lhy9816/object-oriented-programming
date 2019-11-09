package com.calvin.oohw11;

import com.oocourse.specs3.models.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class PathMap {
    private int[][] pathDist;
    private ArrayList<HashSet<Integer>> linkedPath;
    private final int maxPathNum = 52;
    
    public int[][] getPathDist() {
        return pathDist;
    }
    
    public ArrayList<HashSet<Integer>> getLinkedPath() {
        return linkedPath;
    }
    
    public PathMap() {
        pathDist = new int[maxPathNum][maxPathNum];
        linkedPath = new ArrayList<HashSet<Integer>>();
        for (int i = 0; i < maxPathNum; ++i) {
            linkedPath.add(new HashSet<Integer>());
        }
    }
    
    public void addPath(Path path, int pathId,
                        ArrayList<HashSet<Integer>> nodeToPathId,
                        HashMap<Integer, Integer> nodeMap) {
        for (int i = 0; i < path.size(); ++i) {
            int nodeId = path.getNode(i);
            for (int pid : nodeToPathId.get(nodeMap.get(nodeId))) {
                if (pathId == pid) {
                    continue;
                }
                linkedPath.get(pid).add(pathId);
                linkedPath.get(pathId).add(pid);
            }
        }
    }
    
    public void removePath(int pathId) {
        for (int pid : linkedPath.get(pathId)) {
            linkedPath.get(pid).remove(pathId);
        }
        linkedPath.set(pathId, new HashSet<Integer>());
    }
    
    public void removePathById(int pathId) {
        removePath(pathId);
    }
    
    public void updatePathMap(int pathNum) {
        pathDist = new int[pathNum + 1][pathNum + 1];
        for (int bgId = 1; bgId <= pathNum; ++bgId) {
            LinkedList<Integer> queue = new LinkedList<>();
            queue.add(bgId);
            while (queue.size() > 0) {
                int curId = queue.poll();
                for (int nxtId : linkedPath.get(curId)) {
                    if (pathDist[bgId][nxtId] == 0) {
                        pathDist[bgId][nxtId] = pathDist[bgId][curId] + 1;
                        queue.add(nxtId);
                    }
                }
            }
        }
    }
}
