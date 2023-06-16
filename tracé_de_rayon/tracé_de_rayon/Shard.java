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
        String host = "127.0.0.1";
        int port = 1099;

        if (args.length >= 1) {
            host = args[0];
            if (args.length == 2) {
                port = Integer.parseInt(args[1]);
            }
        }

        Shard shard = new Shard();
        UnicastRemoteObject.exportObject(shard, 0);

        Registry registry = LocateRegistry.getRegistry(host, port);//"iutnc-127-08");
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
