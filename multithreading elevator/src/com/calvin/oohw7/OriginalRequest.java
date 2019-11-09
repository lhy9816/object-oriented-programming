package com.calvin.oohw7;

import com.calvin.oohw7.enums.ElevIndex;
import com.calvin.oohw7.enums.Status;
import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;

public class OriginalRequest {
    private static final ArrayList<int[]> elevStops = new Graph().getGraph();
    private static final int[] elevTransfer =
            new int[]{1, 15, -2, -1, 5, 7, 9, 11, 13};
    private ArrayList<SplitRequest> splitRequests;
    
    public OriginalRequest(PersonRequest pr) {
        this.splitRequests = new ArrayList<SplitRequest>();
        DismantleRequest(pr);
    }
    
    public OriginalRequest(int end) {
        if (end == -1) {
            this.splitRequests = new ArrayList<SplitRequest>();
            SplitRequest spr = new SplitRequest(new ArrayList<LiftRequest>());
            spr.getLiftRequests().add(new LiftRequest(
                    -1, 0, 0, Status.FINISH, ElevIndex.A, null));
            splitRequests.add(spr);
            
        } else {
            System.out.println("Wrong Call for end!");
        }
    }
    
    private void DismantleRequest(PersonRequest pr) {
        // 每new一个OriginalRequest后都会将splitRequest new 一个出来
        SplitRequest splitRequest = new SplitRequest(
                new ArrayList<LiftRequest>());
        int from = pr.getFromFloor();
        int to = pr.getToFloor();
        int id = pr.getPersonId();
        // 对楼层存在3的单独处理
        if (from == 3 && to == 2) {
            LiftRequest lr2 = new LiftRequest(
                    id, 1, 2, Status.WAIT, ElevIndex.B, null);
            LiftRequest lr1 = new LiftRequest(
                    id, 3, 1, Status.WAIT, ElevIndex.C, lr2);
            splitRequest.add(lr1);
            splitRequest.add(lr2);
            splitRequests.add(splitRequest);
        } else if (from == 3 && to == 4) {
            LiftRequest lr2 = new LiftRequest(
                    id, 5, 4, Status.WAIT, ElevIndex.B, null);
            LiftRequest lr1 = new LiftRequest(
                    id, 3, 5, Status.WAIT, ElevIndex.C, lr2);
            
            splitRequest.add(lr1);
            splitRequest.add(lr2);
            splitRequests.add(splitRequest);
        } else if (from == 2 && to == 3) {
            LiftRequest lr2 = new LiftRequest(
                    id, 1, 3, Status.WAIT, ElevIndex.C, null);
            LiftRequest lr1 = new LiftRequest(
                    id, 2, 1, Status.WAIT, ElevIndex.B, lr2);
            
            splitRequest.add(lr1);
            splitRequest.add(lr2);
            splitRequests.add(splitRequest);
        } else if (from == 4 && to == 3) {
            LiftRequest lr2 = new LiftRequest(
                    id, 5, 3, Status.WAIT, ElevIndex.C, null);
            LiftRequest lr1 = new LiftRequest(
                    id, 4, 5, Status.WAIT, ElevIndex.B, lr2);
            
            splitRequest.add(lr1);
            splitRequest.add(lr2);
            splitRequests.add(splitRequest);
        } else {
            runInSingle(from, to, id, null, 1);
        }
        shuffleRequest();
    }
    
    private void shuffleRequest() {
        for (int i = 0; i < splitRequests.size(); i++) {
            SplitRequest spr = splitRequests.get(i);
            if (spr.getLiftRequests().get(0).
                    getElevIndex().equals(ElevIndex.C)) {
                SplitRequest tmp = spr;
                SplitRequest first = splitRequests.get(0);
                splitRequests.set(i, first);
                splitRequests.set(0, tmp);
            }
        }
    }
    
    private void runInSingle(
            int from, int to, int id, SplitRequest sprr, int times) {
        // traverse each floor to see if there is one route in a single elevator
        for (int i = 0; i < elevStops.size(); i++) {
            boolean b1 = floorInElevStops(from, elevStops.get(i));
            boolean b2 = floorInElevStops(to, elevStops.get(i));
            if (b1 && b2) {
                // succeed, 加入可分发解
                LiftRequest tmpLrq = new LiftRequest(
                        id, from, to, Status.WAIT, ElevIndex.valueOf(i), null);
                if (times == 1) {
                    SplitRequest spr = new SplitRequest(
                            new ArrayList<LiftRequest>());
                    spr.add(tmpLrq);
                    splitRequests.add(spr);
                } else {
                    if (sprr.getLiftRequests().size() == 0) {
                        sprr.add(tmpLrq);
                    } else {
                        tmpLrq.setMutex(true);
                        sprr.add(tmpLrq);
                        // 换乘的前半截需要知道后半截的lrq
                        sprr.getLiftRequests().get(0).setNextLrq(tmpLrq);
                        splitRequests.add(sprr);
                    }
                    // 现在只找一种换乘方式
                    break;
                }
            } else {
                // 换乘
                if (b1 && times <= 1) {
                    for (int j = 0; j < elevTransfer.length; j++) {
                        int mid = elevTransfer[j];
                        if (from < mid && mid < to || from > mid && mid > to) {
                            SplitRequest tspr = new SplitRequest(
                                    new ArrayList<LiftRequest>());
                            runInSingle(from, mid, id, tspr, times + 1);
                            runInSingle(mid, to, id, tspr, times + 1);
                        }
                    }
                }
            }
        }
    }
    
    private boolean floorInElevStops(int floor, int[] elev) {
        boolean ret = false;
        for (int i = 0; i < elev.length; i++) {
            if (elev[i] == floor) {
                ret = true;
                break;
            }
        }
        return ret;
    }
    
    public ArrayList<SplitRequest> getSplitRequests() {
        return splitRequests;
    }
}
