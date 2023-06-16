import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Server implements ServerI {
    private List<ShardI> shardList;
    private int i;

    public static void main(String[] args) throws RemoteException {
        int port = 1099;

        Server server = new Server();
        Registry registry = LocateRegistry.createRegistry(port);
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

    @Override
    public List<ShardI> getShards() { // for test
        this.clear();
        return Collections.unmodifiableList(this.shardList);
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
    public boolean hasNext() {
        return shardList.size() != 0;
    }

    @Override
    public ShardI next() {
        ShardI shard = shardList.get(i++);
        if (i >= shardList.size()) i = 0;
        return shard;
    }
}
