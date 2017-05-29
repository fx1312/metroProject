import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import com.peertopark.java.geocalc.Point;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.json.JSONArray;
import com.peertopark.java.geocalc.*;

import javax.swing.*;

/**
 * Created by francoisxavier on 26/05/2017.
 * Projet GPS métro
 */
public class Graph{
    private Map<Integer, Node> allNodes = new HashMap<>();

    Graph(String path){
        try {
            //Ouvre le JSON.
            String text = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);

            //Cré un objet JSON dont on peut récupérer les données
            JSONObject json = new JSONObject(text);

            //Cré un objet JSON qui contient la liste des stations + un itérateur pour les parcourire
            JSONObject stations = (JSONObject) json.get("stations");
            Iterator iteratorStation = stations.keys();

            //Cré un objet JSON qui contient les données sur les lignes + un itérateur pour les parcourire
            JSONObject lignes = (JSONObject) json.get("lignes");
            Iterator iteratorLignes = lignes.keys();

            //Cré un tableau JSON qui contient les données de correspondances
            JSONArray correspondances = (JSONArray) json.get("corresp");


            //Parcour les stations et les ajoute au graph.
            while(iteratorStation.hasNext()){

                //Variable temporaire qui stocke les lignes auxquels appartiennent les stations
                List<Integer> lignesMetroTemp = new ArrayList<>();

                //Cré un objet JSON avec les données de la station
                JSONObject station = (JSONObject) stations.get(iteratorStation.next().toString());

                //Ne sélectionne que les stations de type "metro"
                if(station.get("type").toString().equals("metro")){

                    //Remplis la variable temporaire des lignes auxquels appartiennet les stations
                    JSONObject lignesMetro = (JSONObject) station.get("lignes");
                    JSONArray listeLignesMetro = (JSONArray) lignesMetro.get("metro");

                    for(int i=0; i<listeLignesMetro.length(); i++){
                        if(listeLignesMetro.get(i).equals("3B") || listeLignesMetro.get(i).equals("7B")){
                            if(listeLignesMetro.get(i).equals("3B")){
                                lignesMetroTemp.add(3);
                            }else{
                                lignesMetroTemp.add(7);
                            }
                        }else{
                            lignesMetroTemp.add(Integer.parseInt(listeLignesMetro.get(i).toString()));
                        }
                    }

                    //Cré la station ainsi que ses différentes propriétés et l'ajoute au graphe.
                    allNodes.put(Integer.parseInt(station.get("num").toString()),
                            new Node(
                                    Integer.parseInt(station.get("num").toString()),
                                    station.get("nom").toString(),
                                    lignesMetroTemp,
                                    Double.parseDouble(station.get("lng").toString()),
                                    Double.parseDouble(station.get("lat").toString())
                            )
                    );
                }
            }

            while(iteratorLignes.hasNext()){

                String numeroLigneString = iteratorLignes.next().toString();

                //Cré un objet JSON contenant les données de la ligne.
                JSONObject ligne = (JSONObject) lignes.get(numeroLigneString);

                //Ne cré les connexions que pour les lignes de metro.
                if(ligne.has("arrets") && ligne.get("type").equals("metro")){

                    int numeroLigneInt;

                    if(numeroLigneString.equals("3B") || numeroLigneString.equals("7B")){
                        if(numeroLigneString.equals("3B")){
                            numeroLigneInt = 3;
                        }else{
                            numeroLigneInt = 7;
                        }
                    }else{
                         numeroLigneInt = Integer.parseInt(numeroLigneString);
                    }

                    //Cré  un tableau contenant les arrets de la ligne.
                    JSONArray arretsTab = (JSONArray) ligne.get("arrets");

                    for(int j=0; j<arretsTab.length(); j++){

                        //Cré une liste contenant les arrets de la ligne dans l'ordre réel.
                        JSONArray arrets = (JSONArray) arretsTab.get(j);

                        //Pour toute le lignes sauf la 10 (cas spécial)
                        if(numeroLigneInt!=10){
                            for(int i=0; i<arrets.length()-1; i++){

                                //Ajoute une lien entre chaque station dans les deux sens.
                                allNodes
                                        .get(Integer.parseInt(arrets.getString(i)))
                                        .addEdge(
                                                new Edge(
                                                        allNodes.get(Integer.parseInt(arrets.getString(i))),
                                                        allNodes.get(Integer.parseInt(arrets.getString(i+1))),
                                                        numeroLigneInt,
                                                        "metro"
                                                )
                                        );

                                allNodes
                                        .get(Integer.parseInt(arrets.getString(i+1)))
                                        .addEdge(
                                                new Edge(
                                                        allNodes.get(Integer.parseInt(arrets.getString(i+1))),
                                                        allNodes.get(Integer.parseInt(arrets.getString(i))),
                                                        numeroLigneInt,
                                                        "metro"
                                                )
                                        );
                            }
                            //Cas de la ligne 10
                        }else {
                            if(j==0){
                                for (int i = 0; i < arrets.length() - 1; i++) {

                                    //Ajoute une lien entre chaque station dans les deux sens.
                                    allNodes
                                            .get(Integer.parseInt(arrets.getString(i)))
                                            .addEdge(
                                                    new Edge(
                                                            allNodes.get(Integer.parseInt(arrets.getString(i))),
                                                            allNodes.get(Integer.parseInt(arrets.getString(i + 1))),
                                                            numeroLigneInt,
                                                            "metro"
                                                    )
                                            );
                                }

                                for (int i = arrets.length()-1; i > 5; i--) {

                                    //Ajoute une lien entre chaque station dans les deux sens.
                                    allNodes
                                            .get(Integer.parseInt(arrets.getString(i)))
                                            .addEdge(
                                                    new Edge(
                                                            allNodes.get(Integer.parseInt(arrets.getString(i))),
                                                            allNodes.get(Integer.parseInt(arrets.getString(i -1))),
                                                            numeroLigneInt,
                                                            "metro"
                                                    )
                                            );
                                }
                            }
                            if(j==1){
                                for (int i = arrets.length()-1; i > 0; i--) {

                                    //Ajoute une lien entre chaque station dans les deux sens.
                                    allNodes
                                            .get(Integer.parseInt(arrets.getString(i)))
                                            .addEdge(
                                                    new Edge(
                                                            allNodes.get(Integer.parseInt(arrets.getString(i))),
                                                            allNodes.get(Integer.parseInt(arrets.getString(i -1))),
                                                            numeroLigneInt,
                                                            "metro"
                                                    )
                                            );
                                }
                            }
                        }
                    }
                }
            }

            //Rajoute les correpondances
            for(int i=0; i<correspondances.length(); i++){

                //Cré une liste contenant la correspondance (deux ou trois arrêts)
                JSONArray correspTemp = (JSONArray) correspondances.get(i);

                //Si la correspondance est constitué de deux arrets de type metro
                if(correspTemp.length()==2 && !correspTemp.getString(0).contains("_") && !correspTemp.getString(1).contains("_") && allNodes.containsKey(Integer.parseInt(correspTemp.getString(0))) && allNodes.containsKey(Integer.parseInt(correspTemp.getString(1)))){

                    allNodes.get(Integer.parseInt(correspTemp.get(0).toString()))
                            .addEdge(
                                new Edge(
                                        allNodes.get(Integer.parseInt(correspTemp.getString(0))),
                                        allNodes.get(Integer.parseInt(correspTemp.getString(1))),
                                        -1,
                                        "correspondance"
                                ));

                    allNodes.get(Integer.parseInt(correspTemp.get(0).toString()))
                            .addEdge(
                                    new Edge(
                                            allNodes.get(Integer.parseInt(correspTemp.getString(1))),
                                            allNodes.get(Integer.parseInt(correspTemp.getString(0))),
                                            -1,
                                            "correspondance"
                                    ));
                }

                //Si la correspondance est constitué de trois arrets de type metro
                if(correspTemp.length()==3 && !correspTemp.getString(0).contains("_") && !correspTemp.getString(1).contains("_") && !correspTemp.getString(2).contains("_") && allNodes.containsKey(Integer.parseInt(correspTemp.getString(0))) && allNodes.containsKey(Integer.parseInt(correspTemp.getString(1))) && allNodes.containsKey(Integer.parseInt(correspTemp.getString(2)))){
                    allNodes.get(Integer.parseInt(correspTemp.get(0).toString()))
                            .addEdge(
                                    new Edge(
                                            allNodes.get(Integer.parseInt(correspTemp.getString(0))),
                                            allNodes.get(Integer.parseInt(correspTemp.getString(1))),
                                            -1,
                                            "correspondance"
                                    ));

                    allNodes.get(Integer.parseInt(correspTemp.get(0).toString()))
                            .addEdge(
                                    new Edge(
                                            allNodes.get(Integer.parseInt(correspTemp.getString(1))),
                                            allNodes.get(Integer.parseInt(correspTemp.getString(0))),
                                            -1,
                                            "correspondance"
                                    ));

                    allNodes.get(Integer.parseInt(correspTemp.get(0).toString()))
                            .addEdge(
                                    new Edge(
                                            allNodes.get(Integer.parseInt(correspTemp.getString(0))),
                                            allNodes.get(Integer.parseInt(correspTemp.getString(2))),
                                            -1,
                                            "correspondance"
                                    ));

                    allNodes.get(Integer.parseInt(correspTemp.get(0).toString()))
                            .addEdge(
                                    new Edge(
                                            allNodes.get(Integer.parseInt(correspTemp.getString(2))),
                                            allNodes.get(Integer.parseInt(correspTemp.getString(0))),
                                            -1,
                                            "correspondance"
                                    ));

                    allNodes.get(Integer.parseInt(correspTemp.get(0).toString()))
                            .addEdge(
                                    new Edge(
                                            allNodes.get(Integer.parseInt(correspTemp.getString(2))),
                                            allNodes.get(Integer.parseInt(correspTemp.getString(3))),
                                            -1,
                                            "correspondance"
                                    ));

                    allNodes.get(Integer.parseInt(correspTemp.get(0).toString()))
                            .addEdge(
                                    new Edge(
                                            allNodes.get(Integer.parseInt(correspTemp.getString(3))),
                                            allNodes.get(Integer.parseInt(correspTemp.getString(2))),
                                            -1,
                                            "correspondance"
                                    ));
                }
            }

            for(Map.Entry<Integer,Node> mapentry : allNodes.entrySet()){
                mapentry.getValue().fillNeighbors();
                mapentry.getValue().fillWeights();
            }

            //Javel, Paris
            Coordinate lat = new DegreeCoordinate(48.8461034637876);
            Coordinate lng = new DegreeCoordinate(2.27845497841475);
            Point javel = new Point(lat, lng);

            //Charles-Michel, Paris
            Coordinate lat1 = new DegreeCoordinate(48.846336);
            Coordinate lng1 = new DegreeCoordinate(2.2863324);
            Point charles = new Point(lat1, lng1);

            int distance = (int) EarthCalc.getHarvesineDistance(javel, charles); //in meters

            System.out.println("Distance: "+distance);


        }catch(IOException e){
            e.printStackTrace();
        }



    }

    public void printGraph(){

        for(Map.Entry<Integer, Node> mapentry : allNodes.entrySet()){
            System.out.println("*****************************");
            System.out.println("Nom: "+mapentry.getValue().getName());
            System.out.println("ID: "+mapentry.getValue().getId());
            System.out.println("LNG: "+mapentry.getValue().getLng());
            System.out.println("LAT: "+mapentry.getValue().getLat());

            System.out.println("Voisins: ");
            for(int i=0; i<mapentry.getValue().getEdges().size(); i++){
                System.out.println("    "+mapentry.getValue().getEdges().get(i).getNodeTo().getName()+", distance: "+mapentry.getValue().getEdges().get(i).getWeight());
            }

            System.out.println("Lignes: ");
            for(int i=0; i<mapentry.getValue().getLines().size(); i++){
                System.out.println("    "+mapentry.getValue().getLines().get(i));
            }
        }
    }

    //Method to select the current Node for Dijkstra algorithm
    public int selectNodeForDijkstra(){
        List<Node> sortedNodes = new ArrayList<Node>();
        if(areAllMarked()){
            return(-1);
        }

        for(int currentNode : allNodes.keySet()){
            if(!allNodes.get(currentNode).isMarked()){
                if(sortedNodes.size()==0){
                    sortedNodes.add(allNodes.get(currentNode));
                }else{
                    if(allNodes.get(currentNode).getDistanceFromSource()<=sortedNodes.get(0).getDistanceFromSource()){
                        sortedNodes.add(0, allNodes.get(currentNode));
                    }
                    if(allNodes.get(currentNode).getDistanceFromSource()>sortedNodes.get(0).getDistanceFromSource()){
                        sortedNodes.add(sortedNodes.size()-1, allNodes.get(currentNode));
                    }
                }
            }
        }
        return (sortedNodes.get(0).getId());
    }

    public boolean areAllMarked(){
        boolean b=true;
        for(int currentID : allNodes.keySet()){
            if(!allNodes.get(currentID).isMarked()){
                b=false;
            }
        }
        return(b);
    }

    //Dijkstra algorithm for shortestPath
    public void dijkstra(String sourceString, String targetString){
        int source = nameToId(sourceString);
        System.out.println(source);
        int target = nameToId(targetString);
        System.out.println(target);
        int distance=0;
        int currentNode = source;//ne pas oublier de le mettre a jour
        allNodes.get(source).setDistanceFromSource(0);

        while(currentNode!=-1){
            for(Node neighbor : allNodes.get(currentNode).getNeighbors()){
                if(allNodes.get(currentNode).getDistanceFromSource()+ allNodes.get(currentNode).getWeightForNeigbhor(neighbor)<neighbor.getDistanceFromSource()){
                    neighbor.setDistanceFromSource(allNodes.get(currentNode).getDistanceFromSource()+allNodes.get(currentNode).getWeightForNeigbhor(neighbor));
                    neighbor.setPredecessor(currentNode);
                }
            }
            allNodes.get(currentNode).setMarked(true);
            currentNode = selectNodeForDijkstra();
        }
        distance = allNodes.get(target).getDistanceFromSource();


        List<Integer> cheminLignes = new ArrayList<>();
        List<String> cheminArrets = new ArrayList<>();

        if(distance==100000){
            System.out.println("No path found!");
        }else{
            System.out.println("La distance la plus courte est: "+distance);
            int pred = allNodes.get(target).getPredecessor();
            System.out.println("Chemin : ");
            System.out.println(allNodes.get(target).getName());
            cheminArrets.add(allNodes.get(target).getName());
            System.out.println(allNodes.get(target).getLineForNeigbhor(idToNode(allNodes.get(target).getPredecessor())));
            cheminLignes.add(allNodes.get(target).getLineForNeigbhor(idToNode(allNodes.get(target).getPredecessor())));
            while(pred!=source){
                System.out.println(allNodes.get(pred).getName());
                cheminArrets.add(allNodes.get(pred).getName());
                System.out.println(allNodes.get(pred).getLineForNeigbhor(idToNode(allNodes.get(pred).getPredecessor())));
                cheminLignes.add(allNodes.get(pred).getLineForNeigbhor(idToNode(allNodes.get(pred).getPredecessor())));
                pred = allNodes.get(pred).getPredecessor();
            }
            System.out.println(allNodes.get(source).getName());
            cheminArrets.add(allNodes.get(source).getName());
            cheminLignes.add(allNodes.get(pred).getLineForNeigbhor(idToNode(allNodes.get(pred).getPredecessor())));


        }

    }

    public int nameToId(String name){
        int id;
        for(Map.Entry<Integer,Node> mapentry : allNodes.entrySet()){
            if(mapentry.getValue().getName().equals(name)){
                return(mapentry.getValue().getId());
            }
        }
        return(0);
    }

    public Node idToNode(int id){
        for(Map.Entry<Integer,Node> mapentry : allNodes.entrySet()){
            if(mapentry.getValue().getId()==id){
                return(mapentry.getValue());
            }
        }
        return(allNodes.get(0));
    }

}
