package com.calvin.oohw11;

import com.oocourse.specs3.models.Path;
import com.oocourse.specs3.models.PathContainer;
import com.oocourse.specs3.models.PathIdNotFoundException;
import com.oocourse.specs3.models.PathNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPathContainer implements PathContainer {
    
    private final HashMap<Integer, Path> pidTree = new HashMap<Integer, Path>();
    private final HashMap<Path, Integer> ptTree = new HashMap<Path, Integer>();
    private final HashMap<Integer, Integer>
            distinctMap = new HashMap<Integer, Integer>();
    // new node added by add path or deprecated node
    // removed by remove path
    private static int pathCount = 1;
    
    // <integer>的valueOf方法有常量池，保证-127-128内的Integer只用一个内存！
    public MyPathContainer() {
    }
    
    public HashMap<Integer, Path> getPidTree() {
        return pidTree;
    }
    
    public HashMap<Path, Integer> getPtTree() {
        return ptTree;
    }
    
    public HashMap<Integer, Integer> getDistinctMap() {
        return distinctMap;
    }
    
    @Override
    public int size() {
        return ptTree.size();
    }
    
    @Override
    public boolean containsPath(Path path) {
        return ptTree.containsKey(path);
    }
    
    @Override
    public boolean containsPathId(int pathId) {
        return pidTree.containsKey(pathId);
    }
    
    @Override
    public Path getPathById(int pathId) throws PathIdNotFoundException {
        if (containsPathId(pathId)) {
            if (pidTree.size() == ptTree.size()) {
                return pidTree.get(pathId);
            }
        } else {
            throw new PathIdNotFoundException(pathId);
        }
        return null;
    }
    
    @Override
    public int getPathId(Path path) throws PathNotFoundException {
        if (path != null && path.isValid() && containsPath(path)) {
            if (ptTree.size() == pidTree.size()) {
                return ptTree.get(path);
            }
        } else {
            throw new PathNotFoundException(path);
        }
        return -1;
    }
    
    @Override
    public int addPath(Path path) {
        if (path != null && path.isValid()) {
            if (!containsPath(path)) {
                ptTree.put(path, pathCount);
                pidTree.put(pathCount, path);
                ArrayList<Integer> pathNodes = ((MyPath) path).getNodes();
                for (Integer nodeId : pathNodes) {
                    if (distinctMap.containsKey(nodeId)) {
                        distinctMap.put(nodeId, distinctMap.get(nodeId) + 1);
                    } else {
                        distinctMap.put(nodeId, 1);
                    }
                }
                return pathCount++;
            } else {
                int dupId = 0;
                try {
                    dupId = getPathId(path);
                } catch (PathNotFoundException e) {
                    e.printStackTrace();
                }
                return dupId;
            }
        } else {
            return 0;
        }
    }
    
    @Override
    public int removePath(Path path) throws PathNotFoundException {
        if (path != null && path.isValid() && containsPath(path)) {
            int pathId = getPathId(path);
            ptTree.remove(path);
            pidTree.remove(pathId);
            ArrayList<Integer> pathNodes = ((MyPath) path).getNodes();
            for (Integer nodeId : pathNodes) {
                int cnt = distinctMap.get(nodeId);
                if (cnt <= 1) {
                    distinctMap.remove(nodeId);
                } else {
                    distinctMap.put(nodeId, cnt - 1);
                }
            }
            return pathId;
        } else {
            throw new PathNotFoundException(path);
        }
    }
    
    @Override
    public void removePathById(int pathId) throws PathIdNotFoundException {
        if (containsPathId(pathId)) {
            Path p = getPathById(pathId);
            ptTree.remove(p);
            pidTree.remove(pathId);
            ArrayList<Integer> pathNodes = ((MyPath) p).getNodes();
            for (Integer nodeId : pathNodes) {
                int cnt = distinctMap.get(nodeId);
                if (cnt > 1) {
                    distinctMap.put(nodeId, cnt - 1);
                } else {
                    distinctMap.remove(nodeId);
                }
            }
        } else {
            throw new PathIdNotFoundException(pathId);
        }
    }
    
    @Override
    public int getDistinctNodeCount() {
        return distinctMap.size();
    }
}
