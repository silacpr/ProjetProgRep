import raytracer.Disp;
import raytracer.Image;
import raytracer.Scene;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class PartImage {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private boolean loaded;

    public PartImage(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.loaded = false;
    }
    
    public void compute(ShardI shard, Scene scene, Disp disp) throws RemoteException {
        new Thread(() -> {
            try {
                Image image = shard.compute(scene, this.x, this.y, this.width, this.height);

                synchronized (disp) {
                    disp.setImage(image, this.x, this.y);
                    disp.repaint();
                }

                synchronized (this){
                    this.loaded = true;
                }
            } catch (RemoteException ignored) {
            }
        }).start();
    }

    public synchronized boolean mustBeLoaded() {
        return !this.loaded;
    }

    public static List<PartImage> fullImage(int width, int height, int baseCut) {
        ArrayList<PartImage> result = new ArrayList<>();

        int width_per_part = width/baseCut;
        int height_per_part = height/baseCut;

        int width_last_part = width%baseCut;
        int height_last_part = height%baseCut;

        int x, y = 0;
        for (x = 0; x < baseCut; x++) {
            for (y = 0; y < baseCut; y++) {
                result.add(new PartImage(x*width_per_part, y*height_per_part, width_per_part, height_per_part));
            }
        }

        for (int i = 0; i < baseCut; i++) {
            result.add(new PartImage(x * width_per_part, i * height_per_part, width_last_part, height_per_part));
            result.add(new PartImage(i*width_per_part, y*height_per_part, width_per_part, height_last_part));
        }
        result.add(new PartImage(x*width_per_part, y*height_per_part, width_last_part, height_last_part));

        return result;
    }

    @Override
    public String toString() {
        return "PartImage{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", loaded=" + loaded +
                '}';
    }
}
