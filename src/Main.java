import com.peertopark.java.geocalc.Coordinate;
import com.peertopark.java.geocalc.DegreeCoordinate;
import com.peertopark.java.geocalc.EarthCalc;
import com.peertopark.java.geocalc.Point;

public class Main {
    public static void main(String[] args) {
        Graph metro = new Graph("reseaux.json");
        Pathfinder pathfinder = new Pathfinder(metro);

        metro.printGraph();
        System.out.println();
        System.out.println();

        pathfinder.dijkstra("Nation", "Vavin");
    }

    public static void testGeocalcDistance() {
        // Javel, Paris
        Coordinate lat = new DegreeCoordinate(48.8461034637876);
        Coordinate lng = new DegreeCoordinate(2.27845497841475);
        Point javel = new Point(lat, lng);

        // Charles-Michel, Paris
        Coordinate lat1 = new DegreeCoordinate(48.846336);
        Coordinate lng1 = new DegreeCoordinate(2.2863324);
        Point charles = new Point(lat1, lng1);

        int distance = (int) EarthCalc.getHarvesineDistance(javel, charles); // in meters

        System.out.println("Distance: " + distance);
    }
}
