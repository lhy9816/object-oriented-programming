package com.calvin.oohw11;

import com.oocourse.specs3.models.Path;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CstmGraph {
    private final int maxNum = 80 * 50 + 121;
    private final int halfPc = 1;
    private final int halfUs = 16;
    
    private int[][] leastPcDist;
    private int[][] leastSfDist;
    private int nodeNum;        // to expand the nodeset to accomodate cstmnodes
    // private HashMap<Integer, Integer> nodeToCnode;
    private ArrayList<HashSet<CstmNode>> linkedNodes;
    
    public CstmGraph() {
        leastPcDist = new int[maxNum][maxNum];
        leastSfDist = new int[maxNum][maxNum];
        nodeNum = 0;
        // nodeToCnode = new HashMap<Integer, Integer>();
        linkedNodes = new ArrayList<HashSet<CstmNode>>();
        for (int i = 0; i < maxNum; i++) {
            linkedNodes.add(new HashSet<CstmNode>());
        }
    }
    
    private class CstmNode implements Comparable<CstmNode> {
        private int id;
        private int cost;
        
        private CstmNode(int id, int cost) {
            this.id = id;
            this.cost = cost;
        }
        
        @Override
        public int compareTo(CstmNode node) {
            if (this.cost > node.cost) {
                return 1;
            } else if (this.cost < node.cost) {
                return -1;
            } else {
                return 0;
            }
        }
        
        public int getId() {
            return id;
        }
        
        public int getCost() {
            return cost;
        }
    }
    
    public void updateCstmGraph(HashMap<Path, Integer> p2id,
                                HashMap<Integer, Integer> nodeToGnode) {
        // original nodes on the graph
        nodeNum = nodeToGnode.size();
        leastPcDist = new int[maxNum][maxNum];
        leastSfDist = new int[maxNum][maxNum];
        // nodeToCnode = nodeToGnode;  //
        // 'extends' nodeToGcode to nodeToCnode in one path
        linkedNodes = new ArrayList<HashSet<CstmNode>>();
        for (int i = 0; i < maxNum; i++) {
            linkedNodes.add(new HashSet<CstmNode>());
        }
        // get pathset
        Set<Path> pathSet = p2id.keySet();
        for (Path p : pathSet) {
            HashMap<Integer, Integer> tmpPathMap =
                    new HashMap<Integer, Integer>();
            for (int i = 0; i < p.size() - 1; ++i) {
                int bgN = p.getNode(i);
                if (i == 0) {
                    if (!tmpPathMap.containsKey(bgN)) {
                        tmpPathMap.put(bgN, ++nodeNum);
                    }
                    int graphId = tmpPathMap.get(bgN);
                    int cstmId = nodeToGnode.get(bgN);
                    linkedNodes.get(cstmId).add(new CstmNode(graphId, halfUs));
                    linkedNodes.get(graphId).add(new CstmNode(cstmId, halfUs));
                }
                int nxN = p.getNode(i + 1);
                if (!tmpPathMap.containsKey(nxN)) {
                    tmpPathMap.put(nxN, ++nodeNum);
                    int graphId = tmpPathMap.get(nxN);
                    int cstmId = nodeToGnode.get(nxN);
                    linkedNodes.get(cstmId).add(new CstmNode(graphId, halfUs));
                    linkedNodes.get(graphId).add(new CstmNode(cstmId, halfUs));
                }
                
                int prevGid = tmpPathMap.get(bgN);
                int latGid = tmpPathMap.get(nxN);
                int dg = maxCost(p.getUnpleasantValue(bgN),
                        p.getUnpleasantValue(nxN));
                linkedNodes.get(prevGid).add(new CstmNode(latGid, dg));
                linkedNodes.get(latGid).add(new CstmNode(prevGid, dg));
            }
        }
    }
    
    public int computePrice(int fromNodeId, int toNodeId,
                            HashMap<Integer, Integer> nodeToGnode) {
        int from = nodeToGnode.get(fromNodeId);
        int to = nodeToGnode.get(toNodeId);
        if (leastPcDist[from][to] > 0) {
            return leastPcDist[from][to] - 2 * halfPc;
        }
        return bfs(from, to, linkedNodes);
    }
    
    public int computeUns(int fromNodeId, int toNodeId,
                          HashMap<Integer, Integer> nodeToGnode) {
        int from = nodeToGnode.get(fromNodeId);
        int to = nodeToGnode.get(toNodeId);
        if (leastSfDist[from][to] > 0) {
            return leastSfDist[from][to];
        }
        int[] res = dijkstra(from, linkedNodes);
        for (int i = 1; i <= nodeNum; ++i) {
            leastSfDist[from][i] = res[i];
            leastSfDist[i][from] = res[i];
        }
        return leastSfDist[from][to] - 2 * halfUs;
    }
    
    public int maxCost(int a, int b) {
        if (a > b) {
            return a;
        } else {
            return b;
        }
    }
    
    // 每次都重建图保证了图是dense的！
    public int[] dijkstra(int from, ArrayList<HashSet<CstmNode>> unsatDg) {
        int[] visit = new int[maxNum];
        int[] dist = new int[maxNum];
        final int maxInt = 2147483647;
        for (int i = 0; i < dist.length; ++i) {
            dist[i] = maxInt;
        }
        dist[from] = 0;
        Queue<CstmNode> queue = new PriorityQueue<>();
        queue.add(new CstmNode(from, 0));
        while (queue.size() > 0) {
            CstmNode curNode = queue.poll();
            
            for (CstmNode node : unsatDg.get(curNode.getId())) {
                int id = node.getId();
                int cost = node.getCost();
                if (dist[id] > dist[curNode.getId()] + cost && visit[id] == 0) {
                    dist[id] = dist[curNode.getId()] + cost;
                    queue.add(new CstmNode(id, dist[id]));
                }
            }
            visit[curNode.getId()] = 1;
        }
        return dist;
    }
    
    public int bfs(int from, int to, ArrayList<HashSet<CstmNode>> priceDg) {
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(from);
        while (queue.size() > 0) {
            int curNode = queue.poll();
            for (CstmNode cnode : priceDg.get(curNode)) {
                int nextNode = cnode.getId();
                if (nextNode == curNode) {
                    continue;
                }
                if (leastPcDist[from][nextNode] == 0) {
                    leastPcDist[from][nextNode] =
                            leastPcDist[from][curNode] + 1;
                    leastPcDist[nextNode][from] = leastPcDist[from][nextNode];
                    queue.add(nextNode);
                }
            }
        }
        return leastPcDist[from][to] - 2 * halfPc;
    }
}
