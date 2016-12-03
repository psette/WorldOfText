package resource;

import utilities.Constants;

import java.awt.*;
import java.io.Serializable;
import java.util.Vector;

public class Factory implements Serializable {
	public static final long serialVersionUID = 1;

	int width;
	int height;

	private String name;
	private int numberOfWorkers;

	public static Vector<Resource> resources;
	private Vector<Product> products;

	private Point taskBoardLocation;

	public Factory() {
		name = "";
		numberOfWorkers = 0;
		width = 0;
		height = 0;
		resources = new Vector<>();
		products = new Vector<>();
	}

	public Factory(String name, int numberOfWorkers, int width, int height, Vector<Resource> resources,
			Vector<Product> products) {
		setName(name);
		setNumberOfWorkers(numberOfWorkers);
		setDimensions(width, height);
		setResources(resources);
		setProducts(products);
	}

	// NAME

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// WORKERS

	public int getNumberOfWorkers() {
		return numberOfWorkers;
	}

	public void setNumberOfWorkers(int numberOfWorkers) {
		this.numberOfWorkers = numberOfWorkers;
	}

	// DIMENSIONS
	public void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	// RESOURCE
	public void addResource(Resource resource) {
		if (resources == null)
			resources = new Vector<>();
		resources.add(resource);
	}

	public static Vector<Resource> getResources() {
		return resources;
	}

	@SuppressWarnings("static-access")
	public void setResources(Vector<Resource> resources) {
		this.resources = new Vector<>();
		resources.forEach(this::addResource);
	}

	// PRODUCT
	public void addProduct(Product product) {
		if (products == null)
			products = new Vector<>();
		products.add(product);
	}

	public Vector<Product> getProducts() {
		return products;
	}

	public void setProducts(Vector<Product> products) {
		this.products = new Vector<>();
		products.forEach(this::addProduct);
	}

	// LOCATION
	public Point getTaskBoardLocation() {
		return taskBoardLocation;
	}

	public void setTaskBoardLocation(Point taskBoardLocation) {
		this.taskBoardLocation = taskBoardLocation;
	}

	// UTILITIES
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(Constants.factoryNameString + ": ").append(name);
		for (Resource resource : resources) {
			sb.append("\n");
			sb.append("\t").append(resource);
		}
		for (Product product : products) {
			sb.append("\n");
			sb.append("\t").append(product);
		}
		return sb.toString();
	}

}
