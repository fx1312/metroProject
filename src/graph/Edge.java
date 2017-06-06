package graph;

public class Edge {
    private Node nodeFrom;
    private Node nodeTo;
    private int weight = -1; //Longueur en mètres, calculer à l'aide de la lat et la lng.
    private String line; //Ligne à laquelle appartient cette liaison.
    private EdgeType type;

    private int betweenness = 0;

    Edge(Node nodeFrom, Node nodeTo, String line, EdgeType type) {
        this.nodeTo = nodeTo;
        this.nodeFrom = nodeFrom;
        this.line = line;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "nodeFrom=" + nodeFrom +
                ", nodeTo=" + nodeTo +
                ", weight=" + weight +
                ", line='" + line + '\'' +
                ", type=" + type +
                '}';
    }

    public Node getNodeFrom() {
        return nodeFrom;
    }

    public void setNodeFrom(Node nodeFrom) {
        this.nodeFrom = nodeFrom;
    }

    public Node getNodeTo() {
        return nodeTo;
    }

    public void setNodeTo(Node nodeTo) {
        this.nodeTo = nodeTo;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public EdgeType getType() {
        return type;
    }

    public void setType(EdgeType type) {
        this.type = type;
    }

    public int getBetweenness() {
        return betweenness;
    }

    public void setBetweenness(int betweenness) {
        this.betweenness = betweenness;
    }
}
