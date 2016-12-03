package client.components;

import java.awt.Rectangle;
import java.util.concurrent.Semaphore;

import libraries.ImageLibrary;

public class DiningTable extends FactoryObject {
	public static Semaphore semaphore = new Semaphore(3);
	public static FactoryNode mNode;
	public static FactoryResource toTake;

	public DiningTable(Rectangle inDimensions, FactoryNode mNode) {
		super(inDimensions);
		mImage = ImageLibrary.getImage("resources/img/table.png");
		toTake = (FactoryResource) mNode.getObject();
	}

	public static void sitDown() {

		try {
			Thread.sleep(5000);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static void getUp() {
		semaphore.release();
	}

}
// create the resources
