/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project1;

import java.util.ArrayList;

/**
 *
 * @author Kevin Ramirez
 */
public class RoomPriorityQueue {

    private ArrayList<Room> data;
    private int roomID, openTime, closeTime;
    private static final int HEAD = 0;

    public RoomPriorityQueue(int openTime, int closeTime) {
        data = new ArrayList<>();
        this.openTime = openTime;
        this.closeTime = closeTime;
        roomID = 0;
    }

    public void offer(Interval element) {
        if (data.isEmpty()) {
            data.add(new Room(roomID, openTime, closeTime));
            data.get(HEAD).addInterval(element);
            roomID++;
        } else {
            if (data.get(HEAD).getLastInterval().getEndTime() < element.getStartTime() && closeTime >= element.getEndTime()) {
                data.get(HEAD).addInterval(element);
                this.heapifyDown(HEAD);
            } else {
                data.add(new Room(roomID, openTime, closeTime));
                data.get(data.size() - 1).addInterval(element);
                roomID++;
                this.heapifyUp(data.size() - 1);
            }
        }
    }

    public Room poll() {
        if (data.isEmpty()) {
            System.out.println("Error! Queue is empty!");
            return null;
        }
        Room element = data.get(HEAD);
        data.remove(HEAD);
        heapify();
        return element;
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    private void heapify() {
        for (int i = data.size() / 2 - 1; i >= 0; i--) {
            this.heapifyDown(i);
        }
    }

    private void heapifyDown(int parent) {
        int left = 2 * parent + 1;
        int right = left + 1;
        int smallest;

        if (left < data.size() && data.get(left).getLastInterval().getEndTime() < data.get(parent).getLastInterval().getEndTime()) {
            smallest = left;
        } else {
            smallest = parent;
        }
        if (right < data.size() && data.get(right).getLastInterval().getEndTime() < data.get(smallest).getLastInterval().getEndTime()) {
            smallest = right;
        }

        if (smallest != parent) {
            Room temp = data.get(parent);
            data.set(parent, data.get(smallest));
            data.set(smallest, temp);
            heapifyDown(smallest);
        }
    }

    private void heapifyUp(int child) {
        int parent = (child - 1) / 2;
        if (parent > HEAD && data.get(child).getLastInterval().getEndTime() < data.get(parent).getLastInterval().getEndTime()) {
            Room temp = data.get(parent);
            data.set(parent, data.get(child));
            data.set(child, temp);
            heapifyUp(parent);
        }
    }
}
