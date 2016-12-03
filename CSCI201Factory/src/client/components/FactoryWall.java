package client.components;

import libraries.ImageLibrary;

import java.awt.*;

public class FactoryWall extends FactoryObject {

    public FactoryWall(Rectangle inDimensions) {
        super(inDimensions);
        mImage = ImageLibrary.getImage("resources/img/Wall.png");
    }

}
