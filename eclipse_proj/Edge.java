public class Edge<T> {
    
	private T from;
    private T to;
    private String name;
    private int weight;
    private ListGraph<T> graph;

    public Edge(T from, T to, String name, int weight, ListGraph<T> graph) {
        if (weight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }

        this.from = from;
        this.to = to;
        this.name = name;
        this.weight = weight;
        this.graph = graph;
    }

    public T getDestination() {
        return this.to;
    }

    public int getWeight() {
        return graph.getConnectionWeight(from, to);
    }

    public void setWeight(int weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }
        this.weight = weight;
        graph.setConnectionWeight(this.from, this.to, this.weight);
    }

    public String getName() {
        return this.name;
    }

    public T getFrom() {
        return this.from;
    }

    @Override
    public String toString() {
        return "Edge from " + from + " to " + to;
    }

}
