package dev.razboy.resonance.request;

import dev.razboy.resonance.Resonance;
import dev.razboy.resonance.network.Request;

import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

abstract class IRequestManager implements Runnable {
    protected boolean looping = true;
    protected static Resonance plugin;
    protected ConcurrentLinkedQueue<Request> incomingQueue = new ConcurrentLinkedQueue<>();
    protected ConcurrentLinkedQueue<Request> outgoingQueue = new ConcurrentLinkedQueue<>();

    protected abstract void handleIncoming(Request request);
    protected abstract void handleOutgoing(Request request);
    protected void additional() {}


    public void stop()
    {
        looping = false;
    }


    public void addIncoming(Request request) {
        incomingQueue.add(Objects.requireNonNull(request));
    }
    public void addOutgoing(Request request) {
        outgoingQueue.add(Objects.requireNonNull(request));
    }

    @Override
    public void run() {
        try {
            if (looping) {
                if (!incomingQueue.isEmpty()) {
                    for (Request request : incomingQueue) {
                        if (request != null) {
                            handleIncoming(request);
                        }
                    }
                    incomingQueue.clear();
                }
                for (Request request : outgoingQueue) {
                    //System.out.println("o: " + request);
                    handleOutgoing(request);
                }
                outgoingQueue.clear();
                additional();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
