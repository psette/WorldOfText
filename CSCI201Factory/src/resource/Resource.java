package resource;

import utilities.Constants;

import java.io.Serializable;

public class Resource implements Serializable {
    public static final long serialVersionUID = 1;
    private String name;
    private int quantity;
    private int x, y;

    // CONSTRUCTORS
    public Resource(String name, int quantity, int x, int y) {
        setName(name);
        setQuantity(quantity);
        setX(x);
        setY(y);
    }
    public Resource(String name, int quantity) {
        setName(name);
        setQuantity(quantity);
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
        this.quantity = quantity;
    }

    // POSITION
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public void deductFromQuantity(int quantity) {
        setQuantity(this.quantity - quantity);
    }

    // UTILITIES
    public String toString() {
        return Constants.resourceString + ": " + name + " has quantity " + quantity;
    }
    public boolean equals(Resource resource) {
        return (getName().equals(resource.getName()));
    }

}
