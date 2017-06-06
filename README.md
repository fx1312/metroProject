# metroProject

Repo qui rassemble les classes et les données nécéssaire au projet

## TODO

* **Shortest path sur graphe unweighted**
  * NB : un graphe unweighted, c'est un graphe weighted dont on ignore les poids --> pas besoin
    de réinstancier la moitié des objets du monde, il suffit d'en tenir compte dans l'algo et d'ajouter 1 pour
    chaque station au lieu d'ajouter le poids du segment de ligne
* inverser le chemin lors du pathFinding (actuellement on récupère le chemin cible -> source)
* graph.Graph lignes 54 et suivantes : ligne 3B et 7B traitées comme si elles faisaient partie de la 3 et de la 7 : bof bof
* Refactoring graph.Graph / Pathfinding :
  * Faire de la classe graph.Graph une factory pour construire le graphe à partir d'un chemin de fichier
  * avoir les méthodes de pathfinding sur la classe graph
* extraire et refactoriser la méthode addEdge sous forme d'un addEdgeBetweenNodes(int from, int to, String ligne, String type)
* enum EdgeType { ligne, correspondance } ?

## Pour lancer le projet

1) Ajouter les deux bibliothèque et les importer dans les classes:
      import org.json.*;
      import com.peertopark.java.geocalc.*;
 
2) Dans la classe Main, lors de l'instanciation de l'objet graph.Graph, lui donner en paramètre le chemin de réseaux.txt

3) C'est parti !
 
 
## Fonctionnement:
  
Le projet est composé d'un objet graph.Graph dont le constructeur s'occupe de remplir une Map des Nodes (stations de métro) qui le constituent.

Chaque graph.Node de cette Map possède une liste de graph.Edge, qui est remplis par le constructeur de graph.Graph. Les Nodes possèdent aussi toute sorte d'autre propriétées qui sont utiles par la suite.
