import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;

public class PathFinder
{
    public static int[][] Directions = new int[][]
    {
        {1,0},
        {-1,0},
        {0,1},
        {0,-1}
    };

public static class Cell implements Comparable<Cell> {
    public int x, y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cell cell = (Cell) obj;
        return x == cell.x && y == cell.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public int compareTo(Cell o) {
        if (this.x != o.x) {
            return Integer.compare(this.x, o.x);
        }
        return Integer.compare(this.y, o.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

    public static List<Cell> DijkstraPath(char[][] graph, Cell objective, Cell start)
    {
        /*
         * n x n matrix
         */
        int n = graph.length;
        boolean[][] visited = new boolean[n][n];
        for(boolean[] i: visited)
        {
            Arrays.fill(i, false);
        }
        int[][] distance = new int[n][n];
        for(int[] i : distance)
        {
            Arrays.fill(i, Integer.MAX_VALUE);
        }
        Map<Cell,Cell> parent = new HashMap<>();
        PriorityQueue<Cell> queue = new PriorityQueue<>((a, b) -> Integer.compare(
            distance[a.x][a.y] + heuristic(a, objective),
            distance[b.x][b.y] + heuristic(b, objective)));
        
        distance[start.x][start.y] = 0;
        queue.add(start);
        while(!queue.isEmpty())
        {
            Cell coords = queue.poll();
            int x = coords.x;
            int y = coords.y;
            if(!visited[x][y])
            {
                visited[x][y] = true;
                if(coords.equals(objective))
                {
                    return FindFinalPath(parent, objective, start);
                }
                for(int[] direction : Directions)
                {
                    int dirx = x + direction[0];
                    int diry = y+ direction[1];
                    if(dirx>=0 && diry>=0 && dirx < n && diry < n && !visited[dirx][diry] && graph[dirx][diry] != 'W')
                    {
                        int directCost = distance[x][y] + GetTileCost(graph[dirx][diry]);
                        if(distance[dirx][diry] > directCost)
                        {
                            distance[dirx][diry] = directCost;
                            parent.put(new Cell(dirx, diry), coords);
                            queue.add(new Cell(dirx, diry));
                        }
                    }
                }
            }
        }
        return new ArrayList<>();
    }
    public static Integer GetTileCost(char tile)
    {
        switch (tile) {
            //Wall
            case 'W':
                return Integer.MAX_VALUE;
            //Mud
            case 'M':
                return 1;
            //Ice
            case 'I':
                return 2;
            //Fire
            case 'F':
                return 15;
            default:
                return 0;
        }
    }
    /*
     * A* search heuristic Euclidian distance
     */
    private static int heuristic(Cell a, Cell b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }
    public static List<Cell> FindFinalPath(Map<Cell,Cell> parent, Cell objective, Cell start)
    {
        List<Cell> path = new ArrayList<>();
        for (Cell i = objective; i != null; i = parent.get(i)) {
            path.add(i);
            if (i.equals(start))
            {
                break;
            } 
        }
        Collections.reverse(path);
        return path;
    }
    public static int TotalCost(List<Cell> path, char[][] graph)
    {
        if(path.isEmpty())
        {
            return 0;
        }
        int cost = 0;
        for(Cell c : path)
        {
            cost += GetTileCost(graph[c.x][c.y]);
        }
        return cost;
    }
}