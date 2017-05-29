import javafx.application.Application;
import javafx.stage.Stage;

public class Main {

    public static void main(String[] args) {
        Graph metro = new Graph("C:\\Users\\francoisxavier\\IdeaProjects\\metroProject\\data\\reseaux.txt");
        metro.printGraph();
        metro.dijkstra("Cluny-La Sorbonne", "Charonne");
    }
}
