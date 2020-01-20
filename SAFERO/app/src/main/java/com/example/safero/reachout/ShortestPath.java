package com.example.safero.reachout;

// A Java program for Dijkstra's single source shortest path algorithm.
// The program is for adjacency matrix representation of the graph
import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.*;
import java.lang.*;
import java.io.*;

class ShortestPath {

    public ArrayList<LatLng> PathArray = new ArrayList<LatLng>();
    public ArrayList<Point> PointsArray = new ArrayList<Point>();

    int Start = 0;
    int End = 0;
    double graph[][] = new double[][]{};
    static int V;
    Context context;

    public ShortestPath(int S, int E, ArrayList<Point> P, double[][] G, Context context){
        this.context = context;
        V = P.size();
        graph = G;
        PointsArray = P;
        Start = S;
        End = E;
        dijkstra(graph, Start);

        for (LatLng p: PathArray){
            System.out.println(Double.toString(p.latitude) + ", " + Double.toString(p.longitude) + "\n");
        }
    }

    // A utility function to find the vertex with minimum distance value,
    // from the set of vertices not yet included in shortest path tree

    int minDistance(double dist[], Boolean sptSet[])
    {
        // Initialize min value
        double min = Double.MAX_VALUE;
        int min_index = -1;

        for (int v = 0; v < V; v++)
            if (sptSet[v] == false && dist[v] <= min) {
                min = dist[v];
                //System.out.print("Vertex : " + v + " distance = " + min + "\n");
                min_index = v;
            }

        return min_index;
    }

    // A utility function to print the constructed distance array
    void printSolution(double dist[], int n, double distPath[][])
    {
        System.out.println("Vertex   Distance from Source");
        int i = End;
        System.out.println(i + " tt " + dist[i]);
        if (distPath[i][0] != -1){
            System.out.println("via : " + distPath[i][0] + " distance : " + distPath[i][1]);
            printPath(distPath, Start, i);
            double lat = PointsArray.get(i).x;
            double lon = PointsArray.get(i).y;
            PathArray.add(new LatLng(lat, lon));
            System.out.println(" -> " + i + "\n");
        }
        else {
            System.out.println("No Path \n");
            Toast.makeText(context, "No Path Found", Toast.LENGTH_SHORT).show();
        }
    }

    void printPath(double distPath[][], int from, int to){ //function to print the path
        if (distPath[to][0] == from){
            System.out.print("Path - " + Double.toString(distPath[to][0]));
            double lat = PointsArray.get((int)distPath[to][0]).x;
            double lon = PointsArray.get((int)distPath[to][0]).y;
            PathArray.add(new LatLng(lat, lon));
        }
        else {
            printPath(distPath, from, (int)distPath[to][0]);
            System.out.print(" -> " + Double.toString(distPath[to][0]));
            double lat = PointsArray.get((int)distPath[to][0]).x;
            double lon = PointsArray.get((int)distPath[to][0]).y;
            PathArray.add(new LatLng(lat, lon));
        }
    }

    // Funtion that implements Dijkstra's single source shortest path
    // algorithm for a graph represented using adjacency matrix
    // representation
    void dijkstra(double graph[][], int src)
    {
        double dist[] = new double[V]; // The output array. dist[i] will hold

        double distPath[][] = new double[V][2]; //my arrray to hold the path and the value

        // the shortest distance from src to i

        // sptSet[i] will true if vertex i is included in shortest
        // path tree or shortest distance from src to i is finalized
        Boolean sptSet[] = new Boolean[V];

        // Initialize all distances as INFINITE and stpSet[] as false
        for (int i = 0; i < V; i++) {
            dist[i] = Integer.MAX_VALUE;

            distPath[i][0] = -1; //set the path via as -1
            distPath[i][1] = 0; //set the dostance to 0

            sptSet[i] = false;
        }

        // Distance of source vertex from itself is always 0
        dist[src] = 0;
        distPath[src][0] = 0;
        distPath[src][1] = 0;


        // Find shortest path for all vertices
        for (int count = 0; count < V - 1; count++) {
            // Pick the minimum distance vertex from the set of vertices
            // not yet processed. u is always equal to src in first
            // iteration.
            int u = minDistance(dist, sptSet);

            // Mark the picked vertex as processed
            sptSet[u] = true;

            // Update dist value of the adjacent vertices of the
            // picked vertex.
            for (int v = 0; v < V; v++)

                // Update dist[v] only if is not in sptSet, there is an
                // edge from u to v, and total weight of path from src to
                // v through u is smaller than current value of dist[v]
                if (!sptSet[v] && graph[u][v] != 0 &&
                        dist[u] != Integer.MAX_VALUE && dist[u] + graph[u][v] < dist[v]) {
                    dist[v] = dist[u] + graph[u][v];

                    distPath[v][0] = u; //set the via as u
                    distPath[v][1] = dist[v];
                }

        }

        // print the constructed distance array
        printSolution(dist, V, distPath);
    }

    // Driver method
    /*
    public static void main(String[] args)
    {
        // Let us create the example graph discussed above
        int graph[][] = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 8, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 7, 0, 4, 0, 0, 2 },
                { 0, 0, 7, 0, 9, 14, 0, 0, 0 },
                { 0, 0, 0, 9, 0, 10, 0, 0, 0 },
                { 0, 0, 4, 14, 10, 0, 2, 0, 0 },
                { 0, 0, 0, 0, 0, 2, 0, 1, 6 },
                { 8, 0, 0, 0, 0, 0, 1, 0, 7 },
                { 0, 0, 2, 0, 0, 0, 6, 7, 0 } };
        ShortestPath t = new ShortestPath();
        t.dijkstra(graph, 1);
    }
    */
}