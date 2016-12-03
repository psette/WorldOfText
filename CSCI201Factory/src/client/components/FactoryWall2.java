package client.components;

import libraries.ImageLibrary;

import java.awt.*;

public class FactoryWall2 extends FactoryObject {

    public FactoryWall2(Rectangle inDimensions) {
        super(inDimensions);
        mImage = ImageLibrary.getImage("resources/img/Box.png");
    }

}
