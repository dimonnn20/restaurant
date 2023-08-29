package com.restaurant.kitchen;

import com.restaurant.ConsoleHelper;
import com.restaurant.statistic.StatisticManager;
import com.restaurant.statistic.event.CookedOrderEventDataRow;

import java.util.Observable;
import java.util.concurrent.LinkedBlockingQueue;

public class Cook extends Observable implements Runnable {
    private LinkedBlockingQueue<Order> queue;

    public void setQueue(LinkedBlockingQueue<Order> queue) {
        this.queue = queue;
    }

    private final String name;
    private boolean busy;

    public boolean isBusy() {
        return busy;
    }

    public Cook(String name) {
        this.name = name;

    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(10);
                if (!queue.isEmpty()) {
                    if (!this.isBusy()) {
                        this.startCookingOrder(queue.take());
                    }
                }
            }
        } catch (InterruptedException e) {
        }
    }

    public void startCookingOrder(Order arg) throws InterruptedException {
        busy = true;
        Order order = (Order) arg;
        ConsoleHelper.writeMessage("Start cooking - " + order);
        setChanged();
        notifyObservers(order);
        StatisticManager.getInstance().register(new CookedOrderEventDataRow(order.getTablet().toString(), name, order.getTotalCookingTime() * 60, order.getDishes()));
        Thread.sleep(order.getTotalCookingTime()*10);
        busy = false;
    }

    @Override
    public String toString() {
        return name;
    }
}
