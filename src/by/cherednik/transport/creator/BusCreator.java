package by.cherednik.transport.creator;

import by.cherednik.transport.dispatcher.Dispatcher;
import by.cherednik.transport.entity.Passenger;
import by.cherednik.transport.entity.Bus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BusCreator {
    private static int id = 1;
    private static final int MAX_CAPACITY = 25;
    private static final int MAX_STOP_TIME = 4;
    private static final ArrayList<String> BUS_STOPS = Dispatcher.getBusStops();

    private Random random = new Random();

    public Bus createBus() {
        int busSize = random.nextInt(MAX_CAPACITY);
        int passengersCount = random.nextInt(busSize + 1);
        Map<String, Integer> busStops = new HashMap<>();

        for (String s : BUS_STOPS) {
            if (random.nextBoolean()) {
                busStops.put(s, random.nextInt(MAX_STOP_TIME) + 1);
            }
        }

        ArrayList<Passenger> passengers = new ArrayList<>();
        PersonCreator personCreator = new PersonCreator();

        for (int i = 0; i < passengersCount; i++) {
            passengers.add(personCreator.getPerson());
        }

        return new Bus(id++, busSize, passengers, busStops);
    }
}