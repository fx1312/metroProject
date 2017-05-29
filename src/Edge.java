public class Edge {
    private Node nodeFrom;
    private Node nodeTo;
    int weight = -1; //Longueur en temps, calculer à l'aide de la lat et la lng.
    int line; //Ligne à laquelle appartient cette liaison.
    private String type;

    Edge(Node nodeFrom, Node nodeTo, int line, String type) {
        this.nodeTo = nodeTo;
        this.nodeFrom = nodeFrom;
        this.line = line;
        this.type = type;
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

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
