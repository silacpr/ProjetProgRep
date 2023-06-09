import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerI extends Remote {
    void registerShard(ShardI shard) throws RemoteException;

    List<ShardI> getShards() throws RemoteException;
}
