package net.java.sip.communicator.sip.event;

import java.util.EventObject;
import java.util.Vector;

import net.java.sip.communicator.sip.Call;

public class BlockedEvent 
{
	protected Vector<String> blockedReceived;
	
    public BlockedEvent(Vector<String> bls)
    {
        this.blockedReceived=bls;
    }
    public Vector<String> getVector()
    {
        return blockedReceived;
    }
    


}

