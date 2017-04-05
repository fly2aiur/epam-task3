package by.cherednik.transport.creator;

import by.cherednik.transport.dispatcher.Dispatcher;
import by.cherednik.transport.entity.Passenger;

import java.util.ArrayList;
import java.util.Random;

public class PersonCreator {

    private Random random = new Random();
    private long id = 0;

    Passenger getPerson() {
        ArrayList<String> busStops = Dispatcher.getBusStops();

        String changeBusStop = busStops.get(random.nextInt(busStops.size() - 1));

        return new Passenger(id++, changeBusStop);
    }
}
