package com.gp.quantificat.util;

import java.util.*;

public class TopologicalSort {


    public int[] findOrder(int numCourses, int[][] prerequisites) {
        List<List<Integer>> adj = new ArrayList<>(numCourses);
        for (int i = 0; i < numCourses; i++) {
            adj.add(i, new ArrayList<>());
        }
        for (int i = 0; i < prerequisites.length; i++) {
            adj.get(prerequisites[i][1]).add(prerequisites[i][0]);
        }
        boolean[] visited = new boolean[numCourses];
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < numCourses; i++) {
            if (!topologicalSort(adj, i, stack, visited, new boolean[numCourses])) {
                return new int[0];
            }
        }
        int i = 0;
        int[] result = new int[numCourses];
        while (!stack.isEmpty()) {
            result[i++] = stack.pop();
        }
        return result;
    }

    private boolean topologicalSort(List<List<Integer>> adj, int v, Stack<Integer> stack, boolean[] visited, boolean[] isLoop) {
        if (visited[v]) {
            return true;
        }
        if (isLoop[v]) {
            return false;
        }
        isLoop[v] = true;
        for (Integer u : adj.get(v)) {
            if (!topologicalSort(adj, u, stack, visited, isLoop)) {
                return false;
            }
        }
        visited[v] = true;
        stack.push(v);
        return true;
    }


    /**
     * Get topological ordering of the input directed graph
     * @param n number of nodes in the graph
     * @param adjacencyList adjacency list representation of the input directed graph
     * @return topological ordering of the graph stored in an List<Integer>.
     */
    public List<Integer> topologicalSort2(int n, int[][] adjacencyList) {
        List<Integer> topoRes = new ArrayList<>();
        int[] inDegree = new int[n];
        for (int[] parent : adjacencyList) {
//            for (int child : parent) {
//                inDegree[child]++;
//            }
            inDegree[parent[0]]++;


        }

        Deque<Integer> deque = new ArrayDeque<>();

        // start from nodes whose indegree are 0
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                deque.offer(i);
            }
        }

        while (!deque.isEmpty()) {
            int curr = deque.poll();
            topoRes.add(curr);
            for (int child : adjacencyList[curr]) {
                inDegree[child]--;
                if (inDegree[child] == 0) {
                    deque.offer(child);
                }
            }
        }

        return topoRes.size() == n ? topoRes : new ArrayList<>();
    }





    public int[] findOrder3(int numCourses, int[][] prerequisites) {
        int[] res = new int[numCourses];
        int[] indegree = new int[numCourses];

        // get the indegree for each course
        for(int[] pre : prerequisites) {
            indegree[pre[0]] ++;
        }

        // put courses with indegree == 0 to queue
        Queue<Integer> queue = new ArrayDeque<>();
        for(int i = 0; i < numCourses; i++) {
            if(indegree[i] == 0) {
                queue.offer(i);
            }
        }

        // execute the course
        int i = 0;
        while(!queue.isEmpty()) {
            Integer curr = queue.poll();
            res[i++] = curr;

            // remove the pre = curr
            for(int[] pre : prerequisites) {
                if(pre[1] == curr) {
                    indegree[pre[0]] --;
                    if(indegree[pre[0]] == 0) {
                        queue.offer(pre[0]);
                    }
                }
            }
        }

        return i == numCourses ? res : new int[]{};
    }

    public static void main(String[] args) {
        int[][] k = {{0,1},{1,2},{2,3},{2,4},{4,3}};
//        int[][] k = {{4,3}};
        int[] order = new TopologicalSort().findOrder(5, k);
        for (int i : order) {
            System.out.println(i);
        }

//        System.out.println(new TopologicalSort().topologicalSort2(5, k));


        int[] xx = new TopologicalSort().findOrder3(5, k);
        for (int i : xx) {
            System.out.println(i);
        }


    }



}