package by.cherednik.transport.entity;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class BusStop {
    private static int MAX_BUSES = 2;

    private String name;
    private Semaphore semaphore;

    private ArrayList<Bus> buses;
    private ArrayList<Passenger> passengers;

    public BusStop(String name) {
        this.name = name;
        this.semaphore = new Semaphore(MAX_BUSES);
        this.passengers = new ArrayList<>();
        this.buses = new ArrayList<>();
    }

    public void release(Bus bus) {
        buses.remove(bus);
        semaphore.release();
    }

    public void acquire(Bus bus) throws InterruptedException {
        semaphore.acquire();
        buses.add(bus);
    }

    void addPassenger(Passenger passenger) {
        passengers.add(passenger);
    }

    void removePassenger(Passenger passenger) {
        passengers.remove(passenger);
    }

    ArrayList<Bus> getOtherBuses(Bus bus) {
        ArrayList<Bus> list = new ArrayList<>();
        for (Bus b :
                buses) {
            if (bus != b) {
                list.add(b);
            }
        }
        return list;
    }

    boolean isBusy() {
        return buses.size() > 1;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "BusStop{" +
                "name='" + name + '\'' +
                ", buses=" + buses.size() +
                '}';
    }
}
