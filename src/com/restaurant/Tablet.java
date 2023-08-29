package com.restaurant;

import com.restaurant.ad.AdvertisementManager;
import com.restaurant.ad.NoVideoAvailableException;
import com.restaurant.kitchen.Order;
import com.restaurant.kitchen.TestOrder;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tablet {
    private LinkedBlockingQueue<Order> queue;
    private final int number;
    private static Logger logger = Logger.getLogger(Tablet.class.getName());

    public void setQueue(LinkedBlockingQueue<Order> queue) {
        this.queue = queue;
    }

    public Tablet(int number) {
        this.number = number;
    }

    public Order createOrder() {
        Order order = null;
        try {
            order = new Order(this);
            processOrder(order);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Console is unavailable.");
        } catch (NoVideoAvailableException nve) {
            logger.log(Level.INFO, "No video is available for the order " + order);
        }
        return order;
    }

    private boolean processOrder(Order order) {
        ConsoleHelper.writeMessage(order.toString());
        if (order.isEmpty()) {
            return true;
        }
        queue.offer(order);
        // setChanged();
        //notifyObservers(order);

        AdvertisementManager advertisementManager = new AdvertisementManager(order.getTotalCookingTime() * 60);
        advertisementManager.processVideos();

        return false;
    }

    public void createTestOrder() {
        Order order = null;
        try {
            order = new TestOrder(this);
            processOrder(order);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Console is unavailable.");
        } catch (NoVideoAvailableException nve) {
            logger.log(Level.INFO, "No video is available for the order " + order);
        }
    }


    public String toString() {
        return "Tablet{" +
                "number=" + number +
                '}';
    }
}
