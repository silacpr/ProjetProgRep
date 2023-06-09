import raytracer.Image;
import raytracer.Scene;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Shard implements ShardI {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Shard shard = new Shard();
        UnicastRemoteObject.exportObject(shard, 0);

        Registry registry = LocateRegistry.getRegistry("100.64.80.240");
        ServerI server = (ServerI) registry.lookup("server");
        server.registerShard(shard);
    }

    @Override
    public Image compute(Scene scene, int x, int y, int width, int height) throws RemoteException {
        return scene.compute(x, y, width, height);
    }

    @Override
    public boolean isConnected() throws RemoteException {
        return true;
    }
}
