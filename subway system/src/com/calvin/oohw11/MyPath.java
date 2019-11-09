package com.calvin.oohw11;

import com.oocourse.specs3.models.NodeIdNotFoundException;
import com.oocourse.specs3.models.NodeNotConnectedException;
import com.oocourse.specs3.models.Path;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class MyPath implements Path {
    private ArrayList<Integer> nodes;
    private HashSet<Integer> nodesSet;
    
    public MyPath(int... nodes) {
        this.nodes = new ArrayList<Integer>();
        this.nodesSet = new HashSet<Integer>();
        for (int i = 0; i < nodes.length; i++) {
            this.nodes.add(nodes[i]);
            this.nodesSet.add(nodes[i]);
        }
    }
    
    public ArrayList<Integer> getNodes() {
        return nodes;
    }
    
    public HashSet<Integer> getNodesSet() {
        return nodesSet;
    }
    
    @Override
    public int size() {
        return nodes.size();
    }
    
    @Override
    public int getNode(int index) {
        if (index >= 0 && index < nodes.size()) {
            return nodes.get(index);
        } else {
            return -1;
        }
    }
    
    @Override
    public boolean containsNode(int nodeId) {
        return nodesSet.contains(nodeId);
    }
    
    @Override
    public int getDistinctNodeCount() {
        return nodesSet.size();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Path) {
            ArrayList<Integer> objNodes = ((MyPath) obj).getNodes();
            if (objNodes.size() == nodes.size()) {
                for (int i = 0; i < nodes.size(); i++) {
                    if (!nodes.get(i).equals(objNodes.get(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int arrayHashCode = nodes.hashCode();
        int setHashCode = nodesSet.hashCode();
        int len = nodes.size();
        return arrayHashCode * 31 + setHashCode * 10 + len;
    }
    
    @Override
    public boolean isValid() {
        return nodes.size() >= 2;
    }
    
    @Override
    public int compareTo(Path o) {
        ArrayList<Integer> cmpNodes = ((MyPath) o).getNodes();
        int i;
        for (i = 0; i < nodes.size() && i < cmpNodes.size(); i++) {
            if (nodes.get(i) < cmpNodes.get(i)) {
                return -1;
            } else if (nodes.get(i) > cmpNodes.get(i)) {
                return 1;
            }
        }
        if (nodes.size() == cmpNodes.size()) {
            return 0;
        } else if (i == nodes.size()) {
            return -1;
        } else {
            return 1;
        }
    }
    
    @Override
    public Iterator<Integer> iterator() {
        return nodes.iterator();
    }
    
    @Override
    public int getUnpleasantValue(int nodeId) {
        if (!containsNode(nodeId)) {
            return 0;
        } else {
            int v = (nodeId % 5 + 5) % 5;
            if (v == 0) {
                return 1;
            } else if (v == 1) {
                return 4;
            } else if (v == 2) {
                return 16;
            } else if (v == 3) {
                return 64;
            } else {
                return 256;
            }
        }
    }
    
    public /*@pure@*/ boolean containsEdge(int fromNodeId, int toNodeId) {
        return false;
    }
    
    public /*@pure@*/ int getShortestPathLength(int fromNodeId, int toNodeId)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        return 0;
    }
    
}
