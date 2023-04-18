public class Edge<T> {
    
	private T from;
    private T to;
    private String name;
    private ListGraph<T> graph;

    public Edge(T from, T to, String name, ListGraph<T> graph) {
        this.from = from;
        this.to = to;
        this.name = name;
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
        graph.setConnectionWeight(this.from, this.to, weight);
    }

    public String getName() {
        return this.name;
    }

    public T getFrom() {
        return this.from;
    }

    @Override
    public String toString() {
        return "till " + to + " med " + from + " -> " + to + " tar " + this.getWeight();
    }

}
