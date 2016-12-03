package client.components;

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* NOTES
FactoryNode is each node on the x-by-y simulation table, being a drawable GUI component
- it also contains PointNodes and an A* shortest path implementation, so that workers can traverse the gameboard
- Nodes can also contain a single object (such as a wall) that blocks traversal

FactoryNodes are instantiated and kept inside FactorySimulation
 */

public class FactoryNode extends FactoryObject {

    private final int x;
    private final int y;
    //Each node can own a FactoryObject, doing so will render it in its place
    private FactoryObject mFObject;
    //This lock is used to lock the node or the object within the node
    private Lock nodeLock;
    //Used for path finding
    private ArrayList<FactoryNode> mNeighbors;

    { //instance constructor
        mFObject = null;
        nodeLock = new ReentrantLock();
        mNeighbors = new ArrayList<>();
    }

    public FactoryNode(int x, int y) {
        super(new Rectangle(x, y, 1, 1)); //each node is 1 tile
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Graphics g, Point mouseLocation) {
        g.setColor(Color.BLACK);
        if (mFObject == null) {
            g.setColor(Color.WHITE); //draw a border, makes it look like a tiled grid in the factory
            g.drawRect(renderBounds.x, renderBounds.y, renderBounds.width, renderBounds.height);
        } else {
            mFObject.draw(g, mouseLocation);
        }
    }

    FactoryObject getObject() {
        return mFObject;
    }

    public void setObject(FactoryObject inFObject) {
        mFObject = inFObject;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void addNeighbor(FactoryNode neighbor) {
        mNeighbors.add(neighbor);
    }

    public ArrayList<FactoryNode> getNeighbors() {
        return mNeighbors;
    }

    boolean aquireNode() {
        return nodeLock.tryLock();
    }

    void releaseNode() {
        nodeLock.unlock();
    }

    //Below this line is the methods for path finding

    private int heuristicCostEstimate(FactoryNode factoryNode) {
        //Manhattan distance between the nodes
        //This method assumes we are path finding TO "this"
        return (Math.abs(this.x - factoryNode.x) + Math.abs(this.y - factoryNode.y));
    }

    private PathNode lowestFScore(ArrayList<PathNode> openList) {
        PathNode toReturn = null;
        int lowest = Integer.MAX_VALUE;
        for (PathNode pn : openList) {
            if (pn.fScore < lowest) {
                toReturn = pn;
                lowest = pn.fScore;
            }
        }
        return toReturn;
    }

    private Stack<FactoryNode> makePath(PathNode start, PathNode end) {
        Stack<FactoryNode> shortestPath = new Stack<>();
        PathNode current = end;
        shortestPath.add(end.fNode);
        while (current.fNode != start.fNode) {
            shortestPath.add(current.parent.fNode);
            current = current.parent;
        }
        return shortestPath;
    }

    private PathNode containsNode(ArrayList<PathNode> list, FactoryNode node) {
        for (PathNode pn : list) {
            if (pn.fNode == node) return pn;
        }
        return null;
    }

    // implementation of A* path finding
    Stack<FactoryNode> findShortestPath(FactoryNode mDestinationNode) {
        ArrayList<PathNode> openList = new ArrayList<>();
        ArrayList<PathNode> closedList = new ArrayList<>();

        PathNode start = new PathNode();
        start.fNode = this;
        start.gScore = 0;
        start.hScore = heuristicCostEstimate(start.fNode);
        start.fScore = start.gScore + start.hScore;
        openList.add(start);

        while (!openList.isEmpty()) {
            PathNode current = lowestFScore(openList);
            if (current.fNode == mDestinationNode) return makePath(start, current);
            openList.remove(current);
            closedList.add(current);
            for (FactoryNode neighbor : current.fNode.mNeighbors) {
                if (neighbor.getObject() != null) {
                    if (neighbor != mDestinationNode) continue;
                }
                if (containsNode(closedList, neighbor) != null) continue;
                int temp_gScore = current.gScore + 1;//nodes always have distance 1 in our case
                PathNode neighborPathNode = containsNode(openList, neighbor);
                if (neighborPathNode == null || (temp_gScore < neighborPathNode.gScore)) {
                    if (neighborPathNode == null) neighborPathNode = new PathNode();
                    neighborPathNode.fNode = neighbor;
                    neighborPathNode.parent = current;
                    neighborPathNode.gScore = temp_gScore;
                    neighborPathNode.hScore = heuristicCostEstimate(neighbor);
                    neighborPathNode.fScore = neighborPathNode.gScore + neighborPathNode.hScore;
                    if (containsNode(openList, neighbor) == null) {
                        openList.add(neighborPathNode);
                    }
                }
            }
        }
        return null;//no path exists
    }

    //We need a wrapper class for path finding
    //Otherwise, two threads couldn't path find at a time
    class PathNode {
        FactoryNode fNode; //the actual node
        int gScore; //cost from the start of the best known path
        int hScore; //Manhattan distance to the end
        int fScore; //g+h
        PathNode parent;
    }

}
