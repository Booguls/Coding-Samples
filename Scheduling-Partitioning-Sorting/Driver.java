package Project1;

/**
 *
 * @author Kevin Ramirez
 * Driver of the project, directed to schedule rooms by both start and end times using heap sort and priority queues
 */
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Driver {

    private static final boolean SORT_BY_END = false;
    private static final boolean SORT_BY_START = true;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the number of intervals: ");
        int intervals = input.nextInt();
        System.out.print("Enter the start time of intervals: ");
        int startTime = input.nextInt();
        while (startTime <= -1) {
            System.out.print("Error! Start time of an interval cannot be less than 0! Try again: ");
            startTime = input.nextInt();
        }
        System.out.print("Enter the end time of intervals: ");
        int endTime = input.nextInt();
        while (endTime <= startTime) {
            System.out.print("Error! End time of an interval cannot be less than or equal to start time! Try again: ");
            endTime = input.nextInt();
        }
        Interval[] times = createIntervals(intervals, startTime, endTime);
        scheduling(times, startTime, endTime);
        partitioning(times, startTime, endTime);
    }

    public static Interval[] createIntervals(int size, int roomOpen, int roomClose) {
        Interval[] intervals = new Interval[size];
        System.out.println("\nIntervals:");
        for (int i = 0; i < size; i++) {
            int intervalStart = (int) (Math.random() * (roomClose - roomOpen) + roomOpen);
            int intervalEnd = (int) (Math.ceil(Math.random() * (roomClose - intervalStart) + intervalStart));
            intervals[i] = new Interval(i, intervalStart, intervalEnd);
            System.out.println(intervals[i]);
        }
        return intervals;
    }

    public static void scheduling(Interval[] times, int startTime, int endTime) {
        System.out.println("\nInterval Scheduling Results:");
        Room schedule = new Room(0, startTime, endTime);
        heapSort(times, SORT_BY_END);
        for (int i = 0; i < times.length; i++) {
            System.out.println(times[i]);   //Prints the sorting result.
            schedule.addInterval(times[i]); //Adds next valid time, DOES NOT CHECK FOR BETTER START TIME.
        }
        System.out.println(schedule);   //Print algorithm outcome
    }

    public static void partitioning(Interval[] times, int startTime, int endTime) {
        System.out.println("\nInterval Partitioning Results:");
        heapSort(times, SORT_BY_START);
        RoomPriorityQueue roomQueue = new RoomPriorityQueue(startTime, endTime);
        for (int i = 0; i < times.length; i++) {
            System.out.println(times[i]);   //Prints the sorting result first.
            roomQueue.offer(times[i]);      //Add onto min PriorityQueue
        }
        while (!roomQueue.isEmpty()) {
            System.out.println(roomQueue.poll());   //Print algorith outcome
        }
    }

    public static void heapSort(Interval[] times, boolean startOrEndTime) {
        buildMaxHeap(times, startOrEndTime);
        for (int i = times.length - 1; i >= 1; i--) {
            Interval temp = times[0];
            times[0] = times[i];
            times[i] = temp;
            maxHeapify(times, 0, startOrEndTime, i - 1);
        }
    }

    public static void buildMaxHeap(Interval[] times, boolean startOrEndTime) {
        for (int i = times.length / 2 - 1; i >= 0; i--) {
            maxHeapify(times, i, startOrEndTime);
        }
    }

    public static void maxHeapify(Interval[] times, int parent, boolean startOrEndTime) {
        maxHeapify(times, parent, startOrEndTime, times.length - 1);
    }

    public static void maxHeapify(Interval[] times, int parent, boolean startOrEndTime, int heapSize) {
        int left = parent * 2 + 1;
        int right = left + 1;
        int largest;

        if (startOrEndTime == true) {   //Sorting by Start Time
            if (left <= heapSize && times[left].getStartTime() > times[parent].getStartTime()) {
                largest = left;
            } else {
                largest = parent;
            }
            if (right <= heapSize && times[right].getStartTime() > times[largest].getStartTime()) {
                largest = right;
            }

            if (largest != parent) {
                Interval temp = times[largest];
                times[largest] = times[parent];
                times[parent] = temp;
                maxHeapify(times, largest, startOrEndTime, heapSize);
            }
        } else {    //Sorting by End Time
            if (left <= heapSize && times[left].getEndTime() > times[parent].getEndTime()) {
                largest = left;
            } else {
                largest = parent;
            }
            if (right <= heapSize && times[right].getEndTime() > times[largest].getEndTime()) {
                largest = right;
            }

            if (largest != parent) {
                Interval temp = times[largest];
                times[largest] = times[parent];
                times[parent] = temp;
                maxHeapify(times, largest, startOrEndTime, heapSize);
            }
        }
    }
}
