import java.rmi.ConnectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Server implements ServerI {
    private List<ShardI> shardList;

    public static void main(String[] args) throws RemoteException {
        ServerI server = new Server();
        Registry registry = LocateRegistry.createRegistry(1099);
        ServerI remote = (ServerI) UnicastRemoteObject.exportObject(server, 0);
        registry.rebind("server", remote);
    }

    public Server() {
        this.shardList = Collections.synchronizedList(new ArrayList<>());
    }



    @Override
    public void registerShard(ShardI shard) {
        this.clear();
        this.shardList.add(shard);
    }

    private void clear() {
        for (int i = 0; i < this.shardList.size(); i++) {
            ShardI shard = this.shardList.get(i);
            new Thread(() -> {
                try {
                    shard.isConnected();
                } catch (ConnectException e) {
                    this.shardList.remove(shard);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    @Override
    public List<ShardI> getShards() {
        this.clear();
        return Collections.unmodifiableList(this.shardList);
    }
}
