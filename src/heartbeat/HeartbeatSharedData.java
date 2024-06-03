package heartbeat;

import app.AppConfig;
import app.ServentInfo;

import java.util.Map;

public class HeartbeatSharedData {
    private static volatile HeartbeatSharedData instance;
    private static final Object mutex = new Object();
    private int serventPort = -1;
    private boolean hasResponded = false;
    private boolean isSuspicious =false;


    private HeartbeatSharedData(){}


    public static HeartbeatSharedData getInstance(){
        HeartbeatSharedData result = instance;
        if(instance == null){
            synchronized (mutex){
                result = instance;
                if(result == null){
                    result = instance = new HeartbeatSharedData();
                }
            }
        }
        return result;
    }

    public synchronized int getServentPort(){
        serventPort = AppConfig.chordState.getNextServentPort();
        return serventPort;
    }

    public synchronized void setServentPOrt(int port){
        serventPort = port;
    }

    public synchronized boolean getHasResponded(){
        return hasResponded;
    }

    public synchronized void setHasResponded(boolean hasResponded){
        this.hasResponded = hasResponded;
    }
    public synchronized boolean getIsSuspicious(){
        return isSuspicious;
    }

    public synchronized void setIsSuspicious(boolean isSuspicious){
        this.isSuspicious = isSuspicious;
        notifyAll();
    }
}
