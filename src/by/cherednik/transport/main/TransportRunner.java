package by.cherednik.transport.main;

import by.cherednik.transport.creator.BusCreator;

public class TransportRunner {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(new BusCreator().createBus()).start();
        }
    }
}