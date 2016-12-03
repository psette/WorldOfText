package client.components;

import client.Constants;
import client.game.FactorySimulation;
import libraries.ImageLibrary;
import resource.Product;
import resource.Resource;

import java.awt.*;
import java.sql.Timestamp;
import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* NOTES
Each FactoryWorker instance must constantly have a purpose (task) otherwise it will die.
- it will always report back to the Task Board (a designated location on the gameboard) whenever it
    - completes a task
    - needs a new task to begin
- as it runs, it will acquire each individual resource necessary to build the product (as determined by Product object)
 */

public class FactoryWorker extends FactoryObject implements Runnable, FactoryReporter {

	private int mNumber;

	private FactorySimulation mFactorySimulation;
	private Product mProductToMake;

	private Lock mLock;
	private Condition atLocation;

	private Timestamp finished;

	// Nodes each worker keeps track of for path finding
	private FactoryNode mCurrentNode;
	private FactoryNode mNextNode;
	private FactoryNode mDestinationNode;
	private Stack<FactoryNode> mShortestPath;

	{ // instance constructor
		mImage = ImageLibrary.getImage(Constants.resourceFolder + "worker" + Constants.png);
		mLock = new ReentrantLock();
		atLocation = mLock.newCondition();
	}

	public FactoryWorker(int inNumber, FactoryNode startNode, FactorySimulation inFactorySimulation) {
		super(new Rectangle(startNode.getX(), startNode.getY(), 1, 1));
		mNumber = inNumber;
		mCurrentNode = startNode;
		mFactorySimulation = inFactorySimulation;
		mLabel = Constants.workerString + String.valueOf(mNumber);
		new Thread(this).start();
	}

	@Override
	public void update(double deltaTime) {
		if (!mLock.tryLock())
			return;
		// if we have somewhere to go, go there
		if (mDestinationNode != null) {
			if (moveTowards(mNextNode, deltaTime * Constants.workerSpeed)) {
				// if we arrived, save our current node
				mCurrentNode = mNextNode;
				if (!mShortestPath.isEmpty()) {
					// if we have somewhere else to go, save that location
					mNextNode = mShortestPath.pop();
				} // if we arrived at the location, signal the worker thread so
					// they can do more actions
				if (mCurrentNode == mDestinationNode)
					atLocation.signal();
			}
		}
		mLock.unlock();
	}

	public void run() {
		mLock.lock();
		try {
			// Thread.sleep(1000*mNumber); //used to space out the factory
			// workers
			while (true) {

				// if factoryWorker doesn't already have a purpose, let's assign
				// it one
				if (mProductToMake == null) {
					// get an assignment from the table
					mDestinationNode = mFactorySimulation.getNode("Task Board");
					mShortestPath = mCurrentNode.findShortestPath(mDestinationNode);
					mNextNode = mShortestPath.pop();
					// all workers need to go to the Task Board in order to
					// acquire a task
					atLocation.await(); // waiting for animation
					// don't do anything if the destination node is currently
					// locked
					while (!mDestinationNode.aquireNode())
						Thread.sleep(1);
					// get the next task in the queue
					mProductToMake = mFactorySimulation.getTaskBoard().getTask();
					Thread.sleep(1000); // worker takes 1 second to learn task
					// unlock node for future workers to use
					mDestinationNode.releaseNode();
					if (mProductToMake == null)
						break; // No more tasks, end here
				}
				{
					mDestinationNode = mFactorySimulation.getNode("coffee");
					mShortestPath = mCurrentNode.findShortestPath(mDestinationNode);
					mNextNode = mShortestPath.pop();
					// all workers need to go to the Task Board in order to
					// acquire a task
					atLocation.await(); // waiting for animation
					// don't do anything if the destination node is
					// currently
					// locked
					CoffeShop.getThere(mLabel);

				}
				{
					mDestinationNode = mFactorySimulation.getNode("table");
					mShortestPath = mCurrentNode.findShortestPath(mDestinationNode);
					mNextNode = mShortestPath.pop();
					// all workers need to go to the Task Board in order to
					// acquire a task
					atLocation.await(); // waiting for animation
					// don't do anything if the destination node is
					// currently
					// locked
					DiningTable.sitDown();
					DiningTable.getUp();
				}

				// build the product
				for (Resource resource : mProductToMake.getResourcesNeeded()) {
					mDestinationNode = mFactorySimulation.getNode(resource.getName());
					mShortestPath = mCurrentNode.findShortestPath(mDestinationNode);
					mNextNode = mShortestPath.pop();
					atLocation.await(); // waiting for animation
					FactoryResource toTake = (FactoryResource) mDestinationNode.getObject();
					toTake.takeResource(resource.getQuantity());
				}

				// update JTable by going back to the task board and ending the
				// task
				mDestinationNode = mFactorySimulation.getNode("Task Board");
				mShortestPath = mCurrentNode.findShortestPath(mDestinationNode);
				mNextNode = mShortestPath.pop();
				atLocation.await(); // waiting for animation
				finished = new Timestamp(System.currentTimeMillis());
				mFactorySimulation.getTaskBoard().endTask(mProductToMake);
				mProductToMake = null;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mLock.unlock();
	}
	// Use a separate thread for expensive operations
	// Path finding
	// Making objects
	// Waiting
	// @Override
	// public void run() {
	// boolean hasTools = false;
	// boolean done = false;
	// mLock.lock();
	// try {
	// // Thread.sleep(1000*mNumber); //used to space out the factory
	// // workers
	// while (true) {
	// // if factoryWorker doesn't already have a purpose, let's assign
	// // it one
	// if (mProductToMake == null) {
	//
	// hasTools = false;
	// // get an assignment from the table
	// mDestinationNode = mFactorySimulation.getNode("Task Board");
	// mShortestPath = mCurrentNode.findShortestPath(mDestinationNode);
	// mNextNode = mShortestPath.pop();
	// // all workers need to go to the Task Board in order to
	// // acquire a task
	// atLocation.await(); // waiting for animation
	// // don't do anything if the destination node is
	// // currently
	// // locked
	// while (!mDestinationNode.aquireNode())
	// Thread.sleep(1);
	// // get the next task in the queue
	// mProductToMake = mFactorySimulation.getTaskBoard().getTask();
	// Thread.sleep(1000); // worker takes 1 second to learn
	// // task
	// // unlock node for future workers to use
	// mDestinationNode.releaseNode();
	// if (mProductToMake == null)
	// break; // No more tasks, end here
	//
	// }
	// if (!hasTools) {
	// // get an assignment from the table
	// mDestinationNode = mFactorySimulation.getNode("Toolbox");
	// mShortestPath = mCurrentNode.findShortestPath(mDestinationNode);
	// mNextNode = mShortestPath.pop();
	// // all workers need to go to the Task Board in order to
	// // acquire a task
	// atLocation.await(); // waiting for animation
	// // don't do anything if the destination node is currently
	// // locked
	// while (!mDestinationNode.aquireNode())
	// Thread.sleep(1);
	// CoffeShop.getTools(mDestinationNode);
	// // unlock node for future workers to use
	// hasTools = true;
	// mDestinationNode.releaseNode();
	// if (mProductToMake == null)
	// break; // No more tasks, end here
	// }
	//
	// // build the product
	// for (Resource resource : mProductToMake.getResourcesNeeded()) {
	// mDestinationNode = mFactorySimulation.getNode(resource.getName());
	// mShortestPath = mCurrentNode.findShortestPath(mDestinationNode);
	// mNextNode = mShortestPath.pop();
	// atLocation.await(); // waiting for animation
	// FactoryResource toTake = (FactoryResource) mDestinationNode.getObject();
	// toTake.takeResource(resource.getQuantity());
	// }
	// {
	// mDestinationNode = mFactorySimulation.getNode("Toolbox");
	// mShortestPath = mCurrentNode.findShortestPath(mDestinationNode);
	// mNextNode = mShortestPath.pop();
	// atLocation.await(); // waiting for animation
	// // don't do anything if the destination node is
	// // currently
	// // locked
	// System.out.println("HEREE");
	// System.out.println("HEREE2");
	// CoffeShop.setTools(mDestinationNode);
	// Thread.sleep(1000); // worker takes 1 second to learn
	// // task
	// mDestinationNode.releaseNode();
	// hasTools = false;
	// if (mProductToMake == null)
	// break; // No more tasks, end here
	// }
	//
	// // update JTable by going back to the task board and ending the
	// // task
	// mDestinationNode = mFactorySimulation.getNode("Task Board");
	// mShortestPath = mCurrentNode.findShortestPath(mDestinationNode);
	// mNextNode = mShortestPath.pop();
	// atLocation.await(); // waiting for animation
	// finished = new Timestamp(System.currentTimeMillis());
	// mFactorySimulation.getTaskBoard().endTask(mProductToMake);
	// mProductToMake = null;
	// }
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// mLock.unlock();
	// }

	@Override
	public void report() {
		System.out.println(mNumber + " finished at " + finished);
	}

}
