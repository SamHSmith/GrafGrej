import java.util.*;

public class ListGraph<T> implements Graph<T> {
	public void add(T node)
	{
		
	}
	
	public void connect(T node1, T node2, String name, int weight)
	{
		
	}
	
	public void setConnectionWeight(T node1, T node2, int weight)
	{
		
	}
	
	public Set<T> getNodes()
	{
		return new HashSet<T>();
	}
	
	public Collection<Edge<T>> getEdgesFrom(T node)
	{
		return new ArrayList<Edge<T>>();
	}
	
	public Edge<T> getEdgeBetween(T noed1, T node2)
	{
		return new Edge<T>(null, null, "bla", 5);
	}
	
	public void disconnect(T node1, T node2)
	{
		
	}
	
	public void remove(T node)
	{
		
	}
	
	public boolean pathExists(T from, T to)
	{
		return false;
	}
	
	public List<Edge<T>> getPath(T from, T to)
	{
		return new ArrayList<Edge<T>>();
	}
}
