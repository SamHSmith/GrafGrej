import java.util.*;

public class ListGraph<T> implements Graph<T> {

	/* For nodes without links. */
	public ArrayList<T> all_nodes = new ArrayList<T>();
	
	/* Links SOA */
	public ArrayList<T> nodes_one = new ArrayList<T>();
	public ArrayList<T> nodes_two = new ArrayList<T>();
	public ArrayList<String> names = new ArrayList<String>();
	public ArrayList<Integer> weights = new ArrayList<Integer>();
	
	public void add(T node)
	{
		all_nodes.add(node);
	}
	
	public void connect(T node1, T node2, String name, int weight)
	{
		nodes_one.add(node1);
		nodes_two.add(node2);
		names.add(name);
		weights.add(weight);

		if(!all_nodes.contains(node1))
		{ all_nodes.add(node1); }
		if(!all_nodes.contains(node2))
		{ all_nodes.add(node2); }
	}
	
	public void setConnectionWeight(T node1, T node2, int weight)
	{
		for(int i = 0; i < nodes_one.size(); i++)
		{
			if(nodes_one.get(i) == node1 && nodes_two.get(i) == node2)
			{
				weights.set(i, weight);
				return;
			}
		}
	}
	
	public int getConnectionWeight(T node1, T node2)
	{
		for(int i = 0; i < nodes_one.size(); i++)
		{
			if(nodes_one.get(i) == node1 && nodes_two.get(i) == node2)
			{
				return weights.get(i);
			}
		}
		return -1;
	}
	
	public Set<T> getNodes()
	{
		return new HashSet<T>(all_nodes);
	}
	
	public Collection<Edge<T>> getEdgesFrom(T node)
	{
		ArrayList<Edge<T>> ret = new ArrayList<Edge<T>>();
		for(int i = 0; i < nodes_one.size(); i++)
		{
			if(nodes_one.get(i) == node)
			{
				ret.add(new Edge<T>(nodes_one.get(i), nodes_two.get(i), names.get(i), weights.get(i), this));
			}
		}
		return ret;
	}
	
	public Edge<T> getEdgeBetween(T node1, T node2)
	{
		for(int i = 0; i < nodes_one.size(); i++)
		{
			if(nodes_one.get(i) == node1 && nodes_two.get(i) == node2)
			{
				return new Edge<T>(nodes_one.get(i), nodes_two.get(i), names.get(i), weights.get(i), this);
			}
		}
		return null;
	}
	
	public void disconnect(T node1, T node2)
	{
		for(int i = 0; i < nodes_one.size(); i++)
		{
			if(nodes_one.get(i) == node1 && nodes_two.get(i) == node2)
			{
				nuke_soa_connection(i);
				return;
			}
		}
	}
	
	public void remove(T node)
	{
		ArrayList<Integer> to_remove = new ArrayList<Integer>();
		for(int i = 0; i < nodes_one.size(); i++)
		{
			if(nodes_one.get(i) == node || nodes_two.get(i) == node)
			{
				to_remove.add(i);
			}
		}
		for(int index : to_remove)
		{
			nuke_soa_connection(index);
		}
	}
	
	private void nuke_soa_connection(int i)
	{
		nodes_one.remove(i);
		nodes_two.remove(i);
		names.remove(i);
		weights.remove(i);
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
