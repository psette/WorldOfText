package client.components;

import client.Constants;
import libraries.ImageLibrary;
import resource.Product;
import utilities.Util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* NOTES
FactoryTaskBoard contains the JTable of data that will be displayed to the GUI whenever information is updated regarding
- workers
- products
- tasks

 */

public class FactoryTaskBoard extends FactoryObject {

    private int totalProducts;
    private int productsMade;
    //Lock for accessing the table
    private Lock mLock;
    //The table from the GUI to display
    private JTable mTable;
    private Vector<Vector<Object>> workerTableDataVector;
    private Vector<Object> workerTableColumnNames;
    //Products the workers must build
    private Queue<Product> mProducts;

    { //instance constructor
        mImage = ImageLibrary.getImage(Constants.resourceFolder + "taskboard" + Constants.png);
        mLabel = "Task Board";
        workerTableColumnNames = new Vector<>();
        workerTableDataVector = new Vector<>();
        mProducts = new LinkedList<>();
        mLock = new ReentrantLock();
    }

    public FactoryTaskBoard(JTable inTable, Vector<Product> inProducts, int x, int y) {
        super(new Rectangle(x, y, 1, 1));
        //Add the information to the task board
        mTable = inTable;
        for (Product product : inProducts) {
            for (int i = 0; i < product.getQuantity(); i++) {
                mProducts.add(product);
            }
        }

        Collections.addAll(workerTableColumnNames, Constants.tableColumnNames);
        for (Product product : inProducts) {
            Vector<Object> productRow = new Vector<>();
            synchronized (this) {
                productRow.add(product.getName()); //Name of product
                productRow.add(product.getQuantity()); //How many to make
                productRow.add(0); //None in progress
                productRow.add(0); //None completed
                workerTableDataVector.add(productRow);
                updateWorkerTable();
            }
        }
        productsMade = 0;
        totalProducts = mProducts.size();
    }

    Product getTask() {
        mLock.lock();
        Product toAssign;
        if (mProducts.isEmpty()) {
            mLock.unlock();
            return null;
        }

        toAssign = mProducts.remove();
        for (Vector<Object> vect : workerTableDataVector) {
            String name = (String) vect.get(Constants.productNameIndex);
            if (name.equals(toAssign.getName())) {
                vect.setElementAt((int) vect.get(Constants.startedIndex) + 1, Constants.startedIndex);
                break;
            }
        }
        mTable.revalidate();
        mTable.repaint();
        mLock.unlock();
        return toAssign;
    }

    void endTask(Product productMade) {
        mLock.lock();
        productsMade++;
        for (Vector<Object> vect : workerTableDataVector) {
            String name = (String) vect.get(Constants.productNameIndex);
            if (name.equals(productMade.getName())) {
                vect.setElementAt((int) vect.get(Constants.startedIndex) - 1, Constants.startedIndex);
                vect.setElementAt((int) vect.get(Constants.completedIndex) + 1, Constants.completedIndex);
                break;
            }
        }
        mTable.revalidate();
        mTable.repaint();
        mLock.unlock();
    }

    private synchronized void updateWorkerTable() {
        mLock.lock();
        try {
            SwingUtilities.invokeLater(() -> ((DefaultTableModel) mTable.getModel()).setDataVector(workerTableDataVector, workerTableColumnNames));
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            Util.printExceptionToCommand(aioobe);
        }
        mLock.unlock();
    }

    public boolean isDone() {
        return (productsMade == totalProducts);
    }

}
