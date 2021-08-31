package Project1;

import java.util.ArrayList;

/**
 *
 * @author Kevin Ramirez
 */
public class Room {

    private ArrayList<Interval> timeOccupied = new ArrayList<>();
    private int id, openTime, closeTime;

    public Room(int id, int open, int close) {
        this.id = id;
        this.openTime = open;
        this.closeTime = close;
    }

    public void addInterval(Interval newInterval) {
        if (timeOccupied.isEmpty() && newInterval.getEndTime() <= closeTime && newInterval.getStartTime() >= openTime) {
            timeOccupied.add(newInterval);
        } else {
            if (this.getLastInterval().getEndTime() <= newInterval.getStartTime() && newInterval.getEndTime() <= closeTime) {
                timeOccupied.add(newInterval);
            }
        }
    }

    public Interval getLastInterval() {
        if (timeOccupied.size() <= 0) {
            return null;
        }
        return timeOccupied.get(timeOccupied.size() - 1);
    }

    public String toString() {
        String str = "Room {id = " + id + ", intervals = ";
        for (int i = 0; i < timeOccupied.size(); i++) {
            if (i == timeOccupied.size() - 1) {
                str += timeOccupied.get(i).toString();
                continue;
            }
            str += timeOccupied.get(i).toString() + ", ";
        }
        return str + "}";
    }
}
