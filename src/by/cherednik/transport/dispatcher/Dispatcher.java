package by.cherednik.transport.dispatcher;

import by.cherednik.transport.entity.Bus;
import by.cherednik.transport.entity.BusStop;
import by.cherednik.transport.exception.UnknownBusStopException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class Dispatcher {
    private static Logger LOG = LogManager.getLogger();

    private static final ArrayList<String> CITIES = new ArrayList<>();
    private static final ArrayList<BusStop> BUS_STOPS = new ArrayList<>();


    private static final Dispatcher instance = new Dispatcher();

    static {
        CITIES.add("Minsk");
        CITIES.add("Grodno");
        CITIES.add("Brest");

        for (String s : CITIES) {
            BUS_STOPS.add(new BusStop(s));
        }
    }

    public static Dispatcher getInstance() {
        return instance;
    }

    public void occupyBusStop(String name, Bus bus) {
        try {
            BusStop busStop = getBusStop(name);
            busStop.acquire(bus);
        } catch (UnknownBusStopException | InterruptedException e) {
            LOG.error(e);
        }
    }

    public void releaseBusStop(String name, Bus bus) {
        try {
            BusStop busStop = getBusStop(name);
            busStop.release(bus);
        } catch (UnknownBusStopException e) {
            LOG.error(e);
        }
    }

    public static BusStop getBusStop(String city) throws UnknownBusStopException {
        BusStop busStop = null;

        for (BusStop p : BUS_STOPS) {
            if (p.getName().equals(city)) {
                busStop = p;
            }
        }

        if (null == busStop) {
            throw new UnknownBusStopException();
        }

        return busStop;
    }

    public static ArrayList<String> getBusStops() {
        return CITIES;
    }
}
