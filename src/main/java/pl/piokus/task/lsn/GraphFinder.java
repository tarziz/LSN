package pl.piokus.task.lsn;

import java.util.*;
import java.util.stream.Stream;

public class GraphFinder {
    private Map<Integer,Set<Integer>> vertexes;
    private Map<Integer, Boolean> visited;

    public GraphFinder(int numberOfEdges) {
        this.vertexes = new HashMap<>();
        this.visited = new HashMap<>();
    }

    public void buildGraph(Stream<String> lines) {
        lines.forEach(line -> {
            var edge = Arrays.stream(line.split(" ")).map(Integer::parseInt).toList();
            vertexes.putIfAbsent(edge.get(0), new HashSet<>());
            vertexes.putIfAbsent(edge.get(1), new HashSet<>());
            vertexes.get(edge.get(0)).add(edge.get(1));
            vertexes.get(edge.get(1)).add(edge.get(0));
            visited.put(edge.get(0), false);
            visited.put(edge.get(1), false);
        });
    }

    public int findNumberOfGraphs() {
        int result = 0;

        for (Integer vertex :visited.keySet()) {
            if(!visited.get(vertex)){
                searchGraph(vertex);
                result++;
            }
        }

        return result;
    }

    private void searchGraph(Integer vertexLabel) {
        visited.put(vertexLabel, true);
        vertexes.get(vertexLabel).forEach(child -> {
            if (!visited.get(child)) {
                searchGraph(child);
            }
        });
    }
}
