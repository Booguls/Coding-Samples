package Project1;

/**
 *
 * @author Kevin Ramirez
 */
public class Interval {

    private int id, startTime, endTime;

    public Interval(int id, int startTime, int endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "[" + id + " (" + startTime + ", " + endTime + ")]";
    }
}
