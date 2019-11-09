package com.calvin.oohw11;

import com.oocourse.specs3.models.NodeIdNotFoundException;
import com.oocourse.specs3.models.NodeNotConnectedException;
import com.oocourse.specs3.models.Graph;
import com.oocourse.specs3.models.Path;
import com.oocourse.specs3.models.PathIdNotFoundException;
import com.oocourse.specs3.models.PathNotFoundException;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MyGraph extends MyPathContainer implements Graph {
    private final int maxNodes = 150;
    private int nodeNum;
    private HashMap<Integer, Integer> nodeToGnode;
    private ArrayList<HashSet<Integer>> nodeToPathId;
    private ArrayList<HashSet<Integer>> linkednode;
    private int[][] plainDist;
    private int linkedBlockNum;
    
    public MyGraph() {
        nodeNum = 0;
        linkedBlockNum = 0;
        nodeToGnode = new HashMap<>();
        nodeToPathId = new ArrayList<HashSet<Integer>>();
        linkednode = new ArrayList<HashSet<Integer>>();
        for (int i = 0; i < maxNodes; ++i) {
            nodeToPathId.add(new HashSet<Integer>());
            nodeToPathId.add(new HashSet<>());
        }
        plainDist = new int[maxNodes][maxNodes];
    }
    
    public ArrayList<HashSet<Integer>> getNodeToPathId() {
        return nodeToPathId;
    }
    
    public int getLinkedBlockNum() {
        return linkedBlockNum;
    }
    
    public HashMap<Integer, Integer> getNodeToGnode() {
        return nodeToGnode;
    }
    
    @Override
    public int addPath(Path path) {
        int ret = super.addPath(path);
        updateGraphs(getPtTree());
        return ret;
    }
    
    @Override
    public int removePath(Path path) throws PathNotFoundException {
        int ret = super.removePath(path);
        updateGraphs(getPtTree());
        return ret;
    }
    
    @Override
    public void removePathById(int pathId) throws PathIdNotFoundException {
        super.removePathById(pathId);
        updateGraphs(getPtTree());
    }
    
    public void updateGraphs(HashMap<Path, Integer> ptTree) {
        // update graph for the plain shortest path and plain graph structure
        updatePlainGraph(ptTree);
        updatePlainDist();
    }
    
    public void updatePlainGraph(HashMap<Path, Integer> p2id) {
        // initialize parameters
        nodeNum = 0;
        nodeToGnode = new HashMap<Integer, Integer>();
        nodeToPathId = new ArrayList<HashSet<Integer>>();
        linkednode = new ArrayList<HashSet<Integer>>();
        for (int i = 0; i < maxNodes; ++i) {
            nodeToPathId.add(new HashSet<Integer>());
            linkednode.add(new HashSet<>());
        }
        linkedBlockNum = 0;
        Set<Path> paths = p2id.keySet();
        plainDist = new int[maxNodes][maxNodes];
        for (Path p : paths) {
            for (int i = 0; i < p.size() - 1; ++i) {
                int bgN = p.getNode(i);
                int nxN = p.getNode(i + 1);
                if (!nodeToGnode.containsKey(bgN)) {
                    nodeToGnode.put(bgN, ++nodeNum);
                }
                if (!nodeToGnode.containsKey(nxN)) {
                    nodeToGnode.put(nxN, ++nodeNum);
                }
                // link bi graph
                int fid = nodeToGnode.get(bgN);
                int tid = nodeToGnode.get(nxN);
                linkednode.get(fid).add(tid);
                linkednode.get(tid).add(fid);
            }
            int pathId = p2id.get(p);
            for (int i = 0; i < p.size(); ++i) {
                nodeToPathId.get(nodeToGnode.get(p.getNode(i))).add(pathId);
            }
        }
    }
    
    public void updatePlainDist() {
        linkedBlockNum = 0;
        plainDist = new int[maxNodes][maxNodes];
        // mark array for dfs
        int[] nodeMark = new int[maxNodes];
        LinkedList<Integer> queue = new LinkedList<>();
        for (int i = 1; i <= nodeNum; ++i) {
            if (nodeMark[i] == 0) {
                nodeMark[i] = 1;
                linkedBlockNum += 1;
            }
            // add aj nodes to queue
            queue.add(i);
            // traverse all the nodes in the graph and compute bfs
            while (queue.size() > 0) {
                int popid = queue.poll();
                for (int liNode : linkednode.get(popid)) {
                    if (liNode == popid) {
                        continue;
                    }
                    if (plainDist[i][liNode] == 0) {
                        plainDist[i][liNode] = plainDist[i][popid] + 1;
                        nodeMark[liNode] = 1;
                        queue.add(liNode);
                    }
                }
            }
        }
    }
    
    @Override
    public boolean containsNode(int nodeId) {
        return getDistinctMap().containsKey(nodeId);
    }
    
    @Override
    public boolean containsEdge(int fromNodeId, int toNodeId) {
        if (containsNode(fromNodeId) && containsNode(toNodeId)) {
            return linkednode.get(fromNodeId).contains(toNodeId);
        } else {
            return false;
        }
    }
    
    @Override
    public boolean isConnected(int fromNodeId, int toNodeId)
            throws NodeIdNotFoundException {
        if (containsNode(fromNodeId)) {
            if (containsNode(toNodeId)) {
                if (fromNodeId == toNodeId) {
                    return true;
                }
                int from = nodeToGnode.get(fromNodeId);
                int to = nodeToGnode.get(toNodeId);
                return plainDist[from][to] > 0;
            } else {
                throw new NodeIdNotFoundException(toNodeId);
            }
        } else {
            throw new NodeIdNotFoundException(fromNodeId);
        }
    }
    
    @Override
    public int getShortestPathLength(int fromNodeId, int toNodeId)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!containsNode(fromNodeId)) {
            throw new NodeIdNotFoundException(fromNodeId);
        }
        if (!containsNode(toNodeId)) {
            throw new NodeIdNotFoundException(toNodeId);
        }
        if (isConnected(fromNodeId, toNodeId)) {
            int from = nodeToGnode.get(fromNodeId);
            int to = nodeToGnode.get(toNodeId);
            return plainDist[from][to];
        } else {
            throw new NodeNotConnectedException(fromNodeId, toNodeId);
        }
    }
}
