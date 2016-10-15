package client;

import java.awt.Rectangle;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import libraries.ImageLibrary;
import resource.Resource;

public class FactoryMailbox extends FactoryObject implements Runnable{

	private Vector<Resource> available;
	private Vector<Resource> stock;
	Random rand;
	
	//Lab 7 Expansion code
	Lock mLock;
	
	protected FactoryMailbox(Vector<Resource> deliveries) {
		super(new Rectangle(0,0,1,1));
		available = deliveries;
		stock = new Vector<Resource>();
		mImage = ImageLibrary.getImage(Constants.resourceFolder + "mailbox" + Constants.png);
		mLabel = "Mailbox";
		rand = new Random();
		
		//Lab 7 expansion code
		mLock = new ReentrantLock();
		new Thread(this).start();
	}
	
	public Resource getStock(){ 
		//int toStock = Math.abs(rand.nextInt() % available.size());
		//int number = Math.abs(rand.nextInt() % 25 + 1);
		//return new Resource(available.elementAt(toStock).getName(),number);
		mLock.lock(); //Lock the MailBox, no concurrent modification exceptions allowed!
		if(stock.isEmpty()) //No resources to give out
		{
			mLock.unlock(); //unlock it
			return null; //buh bye
		}
		mLock.unlock(); //Unlock it
		return stock.remove(0); //return the first one and delete it from the list
	}
	
	@Override
	public void run()
	{
		while(true){ //always generate
			try{
				int toStock = Math.abs(rand.nextInt() % available.size()); //Generate an index for a random resource
				int number = Math.abs(rand.nextInt() % 25 + 1); //Generate a random amount
				Resource temp = new Resource(available.elementAt(toStock).getName(),number); //create a new resource
				System.out.println("Generated a " + temp.getName() + " with amount " + temp.getQuantity() + " and stored it in mailbox, currently " + stock.size() + " items in the mailbox."); //Debugging
				stock.addElement(temp); //add it to the stock
				Thread.sleep(5000); //sleep for 5 seconds real time
			} catch (InterruptedException e) {
				System.out.println("Interrupted exception in FactoryMailbox!");
				e.printStackTrace();
			}
		}
	}
}
