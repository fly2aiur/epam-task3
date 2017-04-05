package by.cherednik.transport.entity;

import by.cherednik.transport.dispatcher.Dispatcher;
import by.cherednik.transport.exception.UnknownBusStopException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Bus implements Runnable {
    private static final Logger LOG = LogManager.getLogger();

    private static final int MIN_CHANGE_TIME = 100;

    private long id;
    private ArrayList<Passenger> passengers;
    private Map<String, Integer> busStops;

    private Map.Entry<String, Integer> currentBusStop;
    private long arrivalTime;

    public Bus(int id, ArrayList<Passenger> passengers, Map<String, Integer> busStops) {
        this.id = id;
        this.passengers = passengers;
        this.busStops = busStops;
    }

    @Override
    public void run() {
        Dispatcher dispatcher = Dispatcher.getInstance();
        for (Map.Entry<String, Integer> pair : busStops.entrySet()) {
            separatePassengers(pair.getKey());
            this.currentBusStop = pair;
            LOG.log(Level.INFO, pair.getKey() + ":\t" + id + " подьехал");
            dispatcher.occupyBusStop(pair.getKey(), this);
            LOG.log(Level.INFO, pair.getKey() + ":\t" + id + " занял");
            this.arrivalTime = System.currentTimeMillis();
            stayOnBusStop(pair.getKey(), pair.getValue());
            LOG.log(Level.INFO, pair.getKey() + ":\t" + id + " отправление");
            dispatcher.releaseBusStop(pair.getKey(), this);
            LOG.log(Level.INFO, pair.getKey() + ":\t" + id + " освободил");
        }
    }

    private void stayOnBusStop(String city, int sec) {
        long curTime = System.currentTimeMillis();
        long endTime = curTime + sec * 1000;

        try {
            BusStop busStop = Dispatcher.getBusStop(city);
            while (System.currentTimeMillis() + MIN_CHANGE_TIME < endTime) {
                if (busStop != null && busStop.isBusy()) {
                    changePeople(busStop);
                    break;
                }
            }
            TimeUnit.MILLISECONDS.sleep(endTime - System.currentTimeMillis());
        } catch (UnknownBusStopException | InterruptedException e) {
            LOG.log(Level.ERROR, e);
        }
    }

    private void changePeople(BusStop busStop) {

        ArrayList<Bus> buses = busStop.getOtherBuses(this);
        Iterator<Passenger> iterator = leavingPassengers.iterator();

        while (iterator.hasNext()) {
            Passenger passenger = iterator.next();

            if (passenger.getChangeBusStop().equals(this.currentBusStop.getKey()) && !passenger.isChanged()) {
                iterator.remove();
                LOG.log(Level.INFO, passenger + " сошел с автобуса " + this.id);
                busStop.addPassenger(passenger);
                LOG.log(Level.INFO, passenger + " на астоновке " + busStop.getName());

                if (!buses.isEmpty()) {
                    Bus bus = buses.get(new Random().nextInt(buses.size()));
                    busStop.removePassenger(passenger);
                    bus.addPassenger(passenger);
                    passenger.setLeaving(false);
                    LOG.log(Level.INFO, passenger + " сел на автобус " + bus.getID());
                }
            }
        }
    }

    public long getID() {
        return id;
    }

    long getTimeLeft() {
        long currentTime = System.currentTimeMillis();
        return (currentBusStop.getValue() * 1000) - (currentTime - arrivalTime);
    }

    private void addPassenger(Passenger p) {
        passengers.add(p);
    }

    private void removePassenger(Passenger p) {
        passengers.remove(p);
    }

    private ArrayList<Passenger> getLeavingPassengers() {
        ArrayList<Passenger> list = new ArrayList<>();
        for (Passenger passenger :
                passengers) {
            if (passenger.isLeaving()) {
                list.add(passenger);
            }
        }
        return list;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "id = " + id +
                ", passengers=" + passengers.size() +
                '}';
    }
}
