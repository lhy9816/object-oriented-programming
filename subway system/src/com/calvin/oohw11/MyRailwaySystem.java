package com.calvin.oohw11;

import com.oocourse.specs3.models.NodeIdNotFoundException;
import com.oocourse.specs3.models.NodeNotConnectedException;
import com.oocourse.specs3.models.RailwaySystem;
import com.oocourse.specs3.models.Path;
import com.oocourse.specs3.models.PathIdNotFoundException;
import com.oocourse.specs3.models.PathNotFoundException;

import java.util.HashSet;

public class MyRailwaySystem extends MyGraph implements RailwaySystem {
    
    private PathMap pathMap;
    private CstmGraph cstmGraph;
    
    public MyRailwaySystem() {
        pathMap = new PathMap();
        cstmGraph = new CstmGraph();
    }
    
    @Override
    public int addPath(Path path) {
        int ret = super.addPath(path);
        pathMap.addPath(path, ret, getNodeToPathId(), getNodeToGnode());
        pathMap.updatePathMap(getPtTree().size());
        cstmGraph.updateCstmGraph(getPtTree(), getNodeToGnode());
        return ret;
    }
    
    @Override
    public int removePath(Path path) throws PathNotFoundException {
        int ret = super.removePath(path);
        pathMap.removePath(ret);
        pathMap.updatePathMap(getPtTree().size());
        cstmGraph.updateCstmGraph(getPtTree(), getNodeToGnode());
        return ret;
    }
    
    @Override
    public void removePathById(int pathId) throws PathIdNotFoundException {
        super.removePathById(pathId);
        pathMap.removePathById(pathId);
        pathMap.updatePathMap(getPtTree().size());
        cstmGraph.updateCstmGraph(getPtTree(), getNodeToGnode());
    }
    
    @Override
    public int getLeastTicketPrice(int fromNodeId, int toNodeId)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!containsNode(toNodeId)) {
            throw new NodeIdNotFoundException(toNodeId);
        }
        if (!containsNode(fromNodeId)) {
            throw new NodeIdNotFoundException(fromNodeId);
        }
        if (!isConnected(fromNodeId, toNodeId)) {
            throw new NodeNotConnectedException(fromNodeId, toNodeId);
        }
        if (fromNodeId == toNodeId) {
            return 0;
        }
        return cstmGraph.computePrice(fromNodeId, toNodeId, getNodeToGnode());
    }
    
    @Override
    public int getLeastTransferCount(int fromNodeId, int toNodeId)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!containsNode(fromNodeId)) {
            throw new NodeIdNotFoundException(fromNodeId);
        }
        if (!containsNode(toNodeId)) {
            throw new NodeIdNotFoundException(toNodeId);
        }
        if (!isConnected(fromNodeId, toNodeId)) {
            throw new NodeNotConnectedException(fromNodeId, toNodeId);
        }
        // compute
        
        int min = 2019;
        HashSet<Integer> fromSet = getNodeToPathId().get(
                getNodeToGnode().get(fromNodeId));
        HashSet<Integer> toSet = getNodeToPathId().get(
                getNodeToGnode().get(toNodeId));
        for (int from : fromSet) {
            for (int to : toSet) {
                int[][] distTransfer = pathMap.getPathDist();
                if (distTransfer[from][to] < min) {
                    min = distTransfer[from][to];
                }
            }
        }
        return min;
    }
    
    @Override
    public int getLeastUnpleasantValue(int fromNodeId, int toNodeId)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!containsNode(fromNodeId)) {
            throw new NodeIdNotFoundException(fromNodeId);
        }
        if (!containsNode(toNodeId)) {
            throw new NodeIdNotFoundException(toNodeId);
        }
        if (!isConnected(fromNodeId, toNodeId)) {
            throw new NodeNotConnectedException(fromNodeId, toNodeId);
        }
        if (fromNodeId == toNodeId) {
            return 0;
        }
        return cstmGraph.computeUns(fromNodeId, toNodeId, getNodeToGnode());
    }
    
    @Override
    public int getConnectedBlockCount() {
        return getLinkedBlockNum();
    }
    
    public /*@pure@*/ boolean containsPathSequence(Path[] pseq) {
        return false;
    }
    
    public /*@pure@*/ boolean isConnectedInPathSequence(
            Path[] pseq, int fromNodeId, int toNodeId)
            throws NodeIdNotFoundException, PathNotFoundException {
        return false;
    }
    
    public int getTicketPrice(Path[] pseq, int fromNodeId, int toNodeId) {
        return 0;
    }
    
    public int getUnpleasantValue(Path path, int[] idx) {
        return 0;
    }
    
    public int getUnpleasantValue(Path path, int fromIndex, int toIndex) {
        return 0;
    }
    
    public int getUnpleasantValue(Path[] pseq, int fromNodeId, int toNodeId) {
        return 0;
    }
}
