package resource;

import utilities.Constants;

import java.io.Serializable;
import java.util.Vector;

public class Product implements Serializable {
    public static final long serialVersionUID = 1;

    private String name;
    private int quantity;

    private Vector<Resource> resourcesNeeded;


    // CONSTRUCTORS
    public Product() {
        setName("");
        setQuantity(0);
        resourcesNeeded = new Vector<>();
    }
    public Product(String name, int quantity, Vector<Resource> resourcesNeeded) {
        setName(name);
        setQuantity(quantity);
        setResourcesNeeded(resourcesNeeded);
    }


    // NAME
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    // QUANTITY
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        if (quantity < 0) {
            this.quantity = 0;
        }
        this.quantity = quantity;
    }


    // RESOURCES NEEDED
    public Vector<Resource> getResourcesNeeded() {
        return resourcesNeeded;
    }
    public void setResourcesNeeded(Vector<Resource> resourcesNeeded) {
        this.resourcesNeeded = new Vector<>();
        resourcesNeeded.forEach(this::addResourceNeeded);
    }
    public void addResourceNeeded(Resource resource) {
        if (resourcesNeeded == null) resourcesNeeded = new Vector<>();
        resourcesNeeded.add(resource);
    }


    // UTILITIES
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.productString + ": ").append(name).append(" needs quantity ").append(quantity);
        for (Resource resource : resourcesNeeded) {
            sb.append("\n");
            sb.append("\t\t").append(resource.toString());
        }
        return sb.toString();
    }

}
