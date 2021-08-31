package Project2;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author kevin
 */
public class DijkstraImplementation {

    public static final int FROM = 0, TO = 1, WEIGHT = 2, COLUMNS = 3;
    public static final double THRESHOLD = 0.5;    //Chance of an edge forming between two nodes
    public static final boolean DIJKSTRA = true, WIDEST = false;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter a number of nodes: ");
        int amount = in.nextInt();
        while (amount <= 0) {
            System.out.print("Error! Cannot have negative or zero elements in a graph! Try again: ");
            amount = in.nextInt();
        }
        int[] nodes = createNodes(amount);
        ArrayList<int[]> edges = createEdges(nodes);
        printGraph(nodes, edges);
        System.out.print("Enter a starting node within the range [1, " + nodes[nodes.length - 1] + "]: ");
        int source = in.nextInt();
        while (source < 1 || source > nodes[nodes.length - 1]) {
            System.out.print("Out of bounds! Enter a starting node within the range [1, " + nodes[nodes.length - 1] + "]: ");
            source = in.nextInt();
        }
        System.out.print("Enter a destination node within the range [" + source + ", " + nodes[nodes.length - 1] + "]: ");
        int destination = in.nextInt();
        while (destination < source || destination > nodes[nodes.length - 1]) {
            System.out.print("Out of bounds! Enter a destination node within the range [" + source + ", " + nodes[nodes.length - 1] + "]: ");
            destination = in.nextInt();
        }
        int minDist = DijkstraImplementation(nodes, edges, source, destination, DIJKSTRA);
        System.out.println("Minimum Weight: " + minDist);
        int widestPath = DijkstraImplementation(nodes, edges, source, destination, WIDEST);
        System.out.println("Widest Path: " + widestPath);
    }

    public static int DijkstraImplementation(int[] nodes, ArrayList<int[]> edges, int source, int destination, boolean flag) {
        if (source == destination) {
            return 0;
        }
        //IF TRUE THEN DIJKSTRA ALGORITHM
        if (flag == DIJKSTRA) {
            int[] dist = new int[nodes.length];
            for (int i = 0; i < dist.length; i++) {
                if (i == source - 1) {
                    continue;
                }
                dist[i] = Integer.MAX_VALUE;
            }
            ArrayList<Integer> remainingNodes = new ArrayList<>();
            for (int i = 0; i < nodes.length; i++) {
                remainingNodes.add(nodes[i]);
            }
            while (!remainingNodes.isEmpty()) {
                int minDistNodeInd = minDistDivConq(remainingNodes, dist, 0, dist.length - 1);
                removeNode(remainingNodes, nodes[minDistNodeInd]);
                for (int i = 0; i < edges.size(); i++) {
                    if (edges.get(i)[FROM] == nodes[minDistNodeInd]) {
                        if (dist[minDistNodeInd] == Integer.MAX_VALUE) {    //THIS CHECK PREVENTS INTEGER OVERFLOW THAT COULD OCCUR IN ELSE IF CONDITION
                            continue;
                        } else if (dist[edges.get(i)[TO] - 1] > dist[minDistNodeInd] + edges.get(i)[WEIGHT]) {
                            dist[edges.get(i)[TO] - 1] = dist[minDistNodeInd] + edges.get(i)[WEIGHT];
                        }
                    }
                }
            }
            return dist[destination - 1];
        }

        //ELSE: WIDEST PATH ALGO
        int[] dist = new int[nodes.length];
        for (int i = 0; i < dist.length; i++) {
            if (i == source - 1) {
                continue;
            }
            dist[i] = Integer.MIN_VALUE;
        }
        ArrayList<Integer> remainingNodes = new ArrayList<>();
        for (int i = 0; i < nodes.length; i++) {
            remainingNodes.add(nodes[i]);
        }
        while (!remainingNodes.isEmpty()) {
            int maxDistNodeInd = maxDistDivConq(remainingNodes, dist, 0, dist.length - 1);
            if (nodes[maxDistNodeInd] == destination) {
                break;
            }
            removeNode(remainingNodes, nodes[maxDistNodeInd]);
            for (int i = 0; i < edges.size(); i++) {
                if (edges.get(i)[FROM] == nodes[maxDistNodeInd]) {
                    //ORIGINAL PSEUDOCODE DOES NOT WORK, WHILE THIS VERSION OF THE ALGORITHM WORKS ONLY SOMETIMES
                    if (dist[edges.get(i)[TO] - 1] < Math.max(edges.get(i)[WEIGHT], Math.min(dist[maxDistNodeInd], edges.get(i)[WEIGHT]))) {
                        dist[edges.get(i)[TO] - 1] = Math.max(edges.get(i)[WEIGHT], Math.min(dist[maxDistNodeInd], edges.get(i)[WEIGHT]));
                    }
                }
            }
        }
        return dist[destination - 1];
    }

    public static int minDistDivConq(ArrayList<Integer> remainingNodes, int[] dist, int left, int right) {
        if (left == right) {
            if (remainingNodes.indexOf(left + 1) == -1) {
                return -1;
            }
            return left;
        }
        int mid = (left + right) / 2;
        int leftMinInd = minDistDivConq(remainingNodes, dist, left, mid);
        int rightMinInd = minDistDivConq(remainingNodes, dist, mid + 1, right);

        if (leftMinInd != -1 && rightMinInd != -1) {
            if (dist[leftMinInd] < dist[rightMinInd]) {
                return leftMinInd;
            }
            return rightMinInd;
        } else if (leftMinInd != -1) {
            return leftMinInd;
        }
        return rightMinInd;
    }

    public static int maxDistDivConq(ArrayList<Integer> remainingNodes, int[] dist, int left, int right) {
        if (left == right) {
            if (remainingNodes.indexOf(left + 1) == -1) {
                return -1;
            }
            return left;
        }
        int mid = (left + right) / 2;
        int leftMaxInd = maxDistDivConq(remainingNodes, dist, left, mid);
        int rightMaxInd = maxDistDivConq(remainingNodes, dist, mid + 1, right);

        if (leftMaxInd != -1 && rightMaxInd != -1) {
            if (dist[leftMaxInd] > dist[rightMaxInd]) {
                return leftMaxInd;
            }
            return rightMaxInd;
        } else if (leftMaxInd != -1) {
            return leftMaxInd;
        }
        return rightMaxInd;
    }

    public static void removeNode(ArrayList<Integer> remainingNodes, int nodeToRemove) {
        for (int i = 0; i < remainingNodes.size(); i++) {
            if (remainingNodes.get(i) == nodeToRemove) {
                remainingNodes.remove(i);
                break;
            }
        }
    }

    public static int[] createNodes(int size) {
        int[] nodes = new int[size];
        for (int i = 0; i < size; i++) {
            nodes[i] = i + 1;
        }
        return nodes;
    }

    public static ArrayList<int[]> createEdges(int[] nodes) {
        ArrayList<int[]> edgeData = new ArrayList<>();
        for (int i = 0; i < nodes.length - 1; i++) {
            for (int j = i + 1; j <= nodes.length - 1; j++) {
                if (Math.random() < THRESHOLD) {
                    int[] newEdge = {nodes[i], nodes[j], (int) (Math.ceil(Math.random() * (5 * nodes.length)))};
                    edgeData.add(newEdge);
                }
            }
        }
        return edgeData;
    }

    public static void printGraph(int[] nodes, ArrayList<int[]> edges) {
        System.out.print("Nodes List: {");
        for (int i = 0; i < nodes.length; i++) {
            if (i == nodes.length - 1) {
                System.out.print(nodes[i]);
                continue;
            }
            System.out.print(nodes[i] + ", ");
        }
        System.out.print("}\nEdge List: {");
        for (int i = 0; i < edges.size(); i++) {
            if (i == edges.size() - 1) {
                System.out.print("(" + edges.get(i)[FROM] + ", " + edges.get(i)[TO] + ", " + edges.get(i)[WEIGHT] + ")");
                continue;
            }
            System.out.print("(" + edges.get(i)[FROM] + ", " + edges.get(i)[TO] + ", " + edges.get(i)[WEIGHT] + "), ");
        }
        System.out.println("}");
    }

}
