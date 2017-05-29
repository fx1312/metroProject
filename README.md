# metroProject
Repo qui rassemble les classes et les données nécéssaire au projet

 1) Ajouter les deux bibliothèque et les importer dans les classes:
      import org.json.*;
      import com.peertopark.java.geocalc.*;
 
 2) Dans la classe Main, lors de l'instanciation de l'objet Graph, lui donner en paramètre le chemin de réseaux.txt
 
 3) C'est partis.
 
 
Fonctionnement:
  
  Le projet est composé d'un objet Graph dont le constructeur s'occupe de remplir une Map des Nodes (stations de métro) qui le constitue.
  
  Chaque Node de cette Map possède une liste de Edge, qui est remplis par le constructeur de Graph. Les Nodes possèdent aussi toute sorte   d'autre propriétées qui sont utile par la suite.
