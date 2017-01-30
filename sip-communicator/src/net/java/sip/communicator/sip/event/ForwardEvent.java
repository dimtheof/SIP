package net.java.sip.communicator.sip.event;

import java.util.Vector;

public class ForwardEvent {
protected Vector<String> forwardReceived;
	
    public ForwardEvent(Vector<String> bls)
    {
        this.forwardReceived=bls;
    }
    public Vector<String> getVector()
    {
        return forwardReceived;
    }
}
