# metroProject

Repo qui rassemble les classes et les données nécéssaire au projet

## Pour lancer le projet

1) Ajouter les deux bibliothèque et les importer dans les classes:
      import org.json.*;
      import com.peertopark.java.geocalc.*;
 
2) Dans la classe Main, lors de l'instanciation de l'objet graph.Graph, lui donner en paramètre le chemin de réseaux.txt

3) C'est parti !
 
## Fonctionnement:
  
Le projet est composé d'un objet graph.Graph dont le constructeur s'occupe de remplir une Map des Nodes (stations de métro) qui le constituent.

Chaque graph.Node de cette Map possède une liste de graph.Edge, qui est remplis par le constructeur de graph.Graph. Les Nodes possèdent aussi toute sorte d'autre propriétées qui sont utiles par la suite.
