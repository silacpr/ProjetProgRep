import raytracer.Image;
import raytracer.Scene;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ShardI extends Remote {
    Image compute(Scene scene, int x, int y, int width, int height) throws RemoteException;

    boolean isConnected() throws RemoteException;
}
