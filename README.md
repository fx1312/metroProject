# metroProject

Repo qui rassemble les classes et les données nécéssaire au projet

## TODO

* inverser le chemin lors du pathFinding (actuellement on récupère le chemin cible -> source)
* Graph lignes 54 et suivantes : ligne 3B et 7B traitées comme si elles faisaient partie de la 3 et de la 7 : bof bof
* Refactoring Graph / Pathfinding :
  * Faire de la classe Graph une factory pour construire le graphe à partir d'un chemin de fichier
  * avoir les méthodes de pathfinding sur la classe graph
* extraire et refactoriser la méthode addEdge sous forme d'un addEdgeBetweenNodes(int from, int to, String ligne, String type)
* enum EdgeType { ligne, correspondance } ?

## Pour lancer le projet

1) Ajouter les deux bibliothèque et les importer dans les classes:
      import org.json.*;
      import com.peertopark.java.geocalc.*;
 
2) Dans la classe Main, lors de l'instanciation de l'objet Graph, lui donner en paramètre le chemin de réseaux.txt

3) C'est parti !
 
 
## Fonctionnement:
  
Le projet est composé d'un objet Graph dont le constructeur s'occupe de remplir une Map des Nodes (stations de métro) qui le constituent.

Chaque Node de cette Map possède une liste de Edge, qui est remplis par le constructeur de Graph. Les Nodes possèdent aussi toute sorte d'autre propriétées qui sont utiles par la suite.


## Bibliographie

Tu fais une recherche et tu tombes sur un truc intéressant ? Balance-le ici !


