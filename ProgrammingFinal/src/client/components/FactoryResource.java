package client.components;

import client.Constants;
import libraries.ImageLibrary;
import resource.Resource;

import java.awt.*;

public class FactoryResource extends FactoryObject implements FactoryReporter {

	private final Resource mResource;
	private int startAmount;

	public FactoryResource(Resource inResource) {
		super(new Rectangle(inResource.getX(), inResource.getY(), 1, 1));
		mResource = inResource;
		mLabel = inResource.getName();
		System.out.println(Constants.resourceFolder + inResource.getName() + Constants.png);
		mImage = ImageLibrary.getImage(Constants.resourceFolder + inResource.getName() + Constants.png);
		startAmount = mResource.getQuantity();
	}

	@Override
	public void draw(Graphics g, Point mouseLocation) {
		super.draw(g, mouseLocation);
		g.setColor(Color.BLACK);
		g.drawString(mResource.getQuantity() + "", centerTextX(g, mResource.getQuantity() + ""), centerTextY(g));
	}

	void takeResource(int amount) {

		mResource.deductFromQuantity(amount);
	}

	public int getQuantity() {
		return mResource.getQuantity();
	}

	void returnResource() {
		mResource.deductFromQuantity(-1);
	}

	public int getX() {
		return mResource.getX();
	}

	public int getY() {
		return mResource.getY();
	}

	public String getName() {
		return mResource.getName();
	}

	@Override
	public void report() {
		System.out.println("Total Resources: " + mResource.getQuantity() + "/" + startAmount + "Taken: "
				+ (startAmount - mResource.getQuantity()));
	}
}
