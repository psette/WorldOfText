package client.components;

import java.awt.Rectangle;
import java.time.LocalDateTime;

import libraries.ImageLibrary;
import server.FactoryServer;

public class CoffeShop extends FactoryObject {
	public static FactoryNode mNode;
	public static FactoryResource toTake;

	public CoffeShop(Rectangle inDimensions) {
		super(inDimensions);
		mImage = ImageLibrary.getImage("resources/img/coffee.png");
		toTake = (FactoryResource) mNode.getObject();
	}

	public static void getThere(String mLabel) {
		FactoryServer.addOrder(mLabel, LocalDateTime.now().toString());
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (!DiningTable.isReady()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
// create the resources
