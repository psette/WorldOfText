package client.game;

import client.Constants;
import resource.Factory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* NOTES
FactoryManager keeps track of the FactorySimulation and the rate of animation on the RenderPanel
- the FactorySimulation is updated at the rate that is set by the JSlider in the FactoryClientGUI.
- after the data is updated, the FactoryPanel will render the data from the Simulation
- a single thread manages the rate of animation, called animator
 */

public class FactoryManager implements Runnable {

    // This lock ensures that only one animation thread will go at a time
    private Lock animationLock;

    // The simulation that is currently running, and where it will be displayed
    private FactorySimulation mFactorySimulation;
    private FactoryPanel mRenderPanel;

    // This thread will update and draw the factorySimulation member variable
    private Thread animator;
    private volatile boolean running;
    private static final long period = 10; // (magic number)

    // Speed of the simulation, 1x is normal time
    // set by JSlider
    private double speed = Constants.simulation_1x;

    { //instance constructor
        animationLock = new ReentrantLock();
        running = false;
    }

    public void loadFactory(Factory inFactory, JTable inTable) {
        // must stop the current animation
        stop();
        // load in the factory
        mFactorySimulation = new FactorySimulation(inFactory, inTable);
        mRenderPanel.refresh();
        // start the new factorySimulation
        start();
    }

    // this is called by FactoryClientGUI while initializing variables
    public void setFactoryRenderer(FactoryPanel inRenderPanel) {
        mRenderPanel = inRenderPanel;
    }

    //starts the animation of the factorySimulation member variable
    private void start() {
        startAnimation();
    }

    private void startAnimation() {
        if (animator == null || !running) {
            animator = new Thread(this);
            animator.start();
        }
    }

    //halts the animation of the factorySimulation
    private void stop() {
        running = false;
        animator = null;
    }

    @Override
    public void run() {
        animationLock.lock();
        long beforeTime, deltaTime = 0, sleepTime;
        beforeTime = System.nanoTime();
        running = true;
        while (running) {
            //if we have a simulation to run, run the simulation
            if (mFactorySimulation != null) {
                mFactorySimulation.update(((double) deltaTime / (double) 1000000000) * speed);//seconds
            }
            //if we have somewhere to render the simulation, render it there
            if (mRenderPanel != null) {
                mRenderPanel.render(mFactorySimulation); //create the graphic
                mRenderPanel.paint(); //paint the graphic onto the screen
            }
            deltaTime = System.nanoTime() - beforeTime;
            sleepTime = period - deltaTime;

            if (sleepTime <= 0L) {
                sleepTime = 5L;
            }

            try {//sleep a bit so we don't hog the CPU
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignored) { }

            beforeTime = System.nanoTime();
        }
        animationLock.unlock();
    }

    // Instantiated by FactoryClientGUI's initializeVariables
    // when the JSlider changes its value, change the speed of the animation.
    public class SpeedChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent ce) {
            // listen for JSlider changing–-- changes speed of simulation
            JSlider source = (JSlider) ce.getSource();
            speed = source.getValue();
        }
    }

}
