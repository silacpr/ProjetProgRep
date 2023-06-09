import raytracer.Disp;
import raytracer.Image;
import raytracer.Scene;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NetworkRaytracer {
    public static String aide = "Raytracer : synthèse d'image par lancé de rayons (https://en.wikipedia.org/wiki/Ray_tracing_(graphics))\n\nUsage : java LancerRaytracer [fichier-scène] [largeur] [hauteur]\n\tfichier-scène : la description de la scène (par défaut simple.txt)\n\tlargeur : largeur de l'image calculée (par défaut 512)\n\thauteur : hauteur de l'image calculée (par défaut 512)\n";

    public static void main(String args[]) throws RemoteException, NotBoundException {

        // Le fichier de description de la scène si pas fournie
        String fichier_description= "simple.txt";

        // largeur et hauteur par défaut de l'image à reconstruire
        int largeur = 1802/*512*/, hauteur = 902/*512*/, divisions = 100;

        if(args.length > 0){
            fichier_description = args[0];
            if(args.length > 1){
                largeur = Integer.parseInt(args[1]);
                if(args.length > 2) {
                    hauteur = Integer.parseInt(args[2]);
                    if (args.length > 3)
                        divisions = Integer.parseInt(args[3]);
                }

            }
        }else{
            System.out.println(aide);
        }

        Registry registry = LocateRegistry.getRegistry();
        ServerI server = (ServerI) registry.lookup("server");

        // création d'une fenêtre
        Disp disp = new Disp("Raytracer", largeur, hauteur);

        // Initialisation d'une scène depuis le modèle
        Scene scene = new Scene(fichier_description, largeur, hauteur);

        List<ShardI> shards = server.getShards();
        Iterator<ShardI> shardIterator = shards.iterator();

        List<PartImage> partImageList = PartImage.fullImage(largeur, hauteur, divisions);
        Iterator<PartImage> iterator = partImageList.iterator();

        if (!iterator.hasNext()) {
            System.exit(0);
        }
        PartImage partImage = iterator.next();
        while (true) {
            if (shardIterator.hasNext()) {
                if (partImage.mustBeLoaded()) {
                    partImage.compute(shardIterator.next(), scene, disp);
                    // success
                    if (!iterator.hasNext()) {
                        System.out.println("Toutes les demandes d'images ont été envoyés");
                        break;
                    }
                    partImage = iterator.next();
                }
            } else {
                shardIterator = shards.iterator();
            }
        }
    }
}
