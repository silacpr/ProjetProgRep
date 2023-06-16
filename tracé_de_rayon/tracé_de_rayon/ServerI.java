import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

public interface ServerI extends Remote {
    void registerShard(ShardI shard) throws RemoteException;

    List<ShardI> getShards() throws RemoteException;

    boolean hasNext() throws RemoteException;

    ShardI next() throws RemoteException;
}
