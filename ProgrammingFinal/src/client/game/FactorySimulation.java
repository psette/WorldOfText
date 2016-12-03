package client.game;

import client.components.*;
import resource.Factory;
import resource.Resource;
import server.FactoryServerGUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* NOTES
FactorySimulation is the most important class in this package. It contains
- an instance of the Factory object
- instances of objects on the gameboard
- instances of workers on the gameboard
- 2D array of FactoryNodes that workers traverse

 */

public class FactorySimulation {

	private Factory mFactory;
	private boolean isDone = false;
	private ArrayList<FactoryWorker> mFWorkers;
	static public ArrayList<FactoryObject> mFObjects;
	static public FactoryNode mFNodes[][];
	static public Map<String, FactoryNode> mFNodeMap;
	public static ArrayList<Integer> indexes = new ArrayList<Integer>();
	private FactoryTaskBoard mTaskBoard;

	private void createWalls() {
		for (int i = 0; i < 10; i++) {
			FactoryObject fw = new FactoryWall(new Rectangle(7, i, 1, 1));
			mFObjects.add(fw);
			mFNodes[fw.getX()][fw.getY()].setObject(fw);
		}
		for (int i = 0; i < 6; i++) {
			FactoryObject fw = new FactoryWall2(new Rectangle(i, 9, 1, 1));
			mFObjects.add(fw);
			mFNodes[fw.getX()][fw.getY()].setObject(fw);
		}
	}

	void update(double deltaTime) {
		// Update all the objects in the factor that need updating each tick
		if (isDone)
			return;
		for (FactoryObject object : mFWorkers)
			object.update(deltaTime);
		if (mTaskBoard.isDone()) {
			isDone = true;
			// print a report from each FactoryReporter object:
			mFObjects.stream().filter(object -> object instanceof FactoryReporter)
					.forEach(object -> ((FactoryReporter) object).report());
			FactoryServerGUI.printOut();
		}
	}

	ArrayList<FactoryObject> getObjects() {
		return mFObjects;
	}

	ArrayList<FactoryWorker> getWorkers() {
		return mFWorkers;
	}

	String getName() {
		return mFactory.getName();
	}

	double getWidth() {
		return mFactory.getWidth();
	}

	double getHeight() {
		return mFactory.getHeight();
	}

	FactoryNode[][] getNodes() {
		return mFNodes;
	}

	public FactoryNode getNode(String key) {
		return mFNodeMap.get(key);
	}

	public FactoryTaskBoard getTaskBoard() {
		return mTaskBoard;
	}

	// instance constructor
	{
		mFObjects = new ArrayList<>();
		mFWorkers = new ArrayList<>();
		mFNodeMap = new HashMap<>();
	}

	@SuppressWarnings("static-access")
	FactorySimulation(Factory inFactory, JTable inTable) {
		mFactory = inFactory;
		mFNodes = new FactoryNode[mFactory.getWidth()][mFactory.getHeight()];

		// create the nodes of the factory
		for (int height = 0; height < mFactory.getHeight(); height++) {
			for (int width = 0; width < mFactory.getWidth(); width++) {
				mFNodes[width][height] = new FactoryNode(width, height);
				mFObjects.add(mFNodes[width][height]);
			}
		}

		// link neighbors together
		for (FactoryNode[] nodes : mFNodes) {
			for (FactoryNode node : nodes) {
				int x = node.getX();
				int y = node.getY();
				if (x != 0)
					node.addNeighbor(mFNodes[x - 1][y]);
				if (x != mFactory.getWidth() - 1)
					node.addNeighbor(mFNodes[x + 1][y]);
				if (y != 0)
					node.addNeighbor(mFNodes[x][y - 1]);
				if (y != mFactory.getHeight() - 1)
					node.addNeighbor(mFNodes[x][y + 1]);
			}
		}

		// create the resources

		for (Resource resource : mFactory.getResources()) {
			FactoryResource fr = new FactoryResource(resource);
			mFObjects.add(fr);
			mFNodes[fr.getX()][fr.getY()].setObject(fr);
			mFNodeMap.put(fr.getName(), mFNodes[fr.getX()][fr.getY()]);
		}

		// Create the task board
		Point taskBoardLocation = mFactory.getTaskBoardLocation();
		mTaskBoard = new FactoryTaskBoard(inTable, mFactory.getProducts(), taskBoardLocation.x, taskBoardLocation.y);
		mFObjects.add(mTaskBoard);
		mFNodes[taskBoardLocation.x][taskBoardLocation.y].setObject(mTaskBoard);
		mFNodeMap.put("Task Board", mFNodes[taskBoardLocation.x][taskBoardLocation.y]);

		// Create the workers, set their first task to look at the task board
		for (int i = 0; i < mFactory.getNumberOfWorkers(); i++) {
			// Start each worker at the first node (upper left corner)
			// Note, may change this, but all factories have an upper left
			// corner(0,0) so it makes sense
			FactoryWorker fw = new FactoryWorker(i, mFNodes[0][0], this);
			mFObjects.add(fw);
			mFWorkers.add(fw);
		}

		createWalls();
		// createToolbox();
	}

}
