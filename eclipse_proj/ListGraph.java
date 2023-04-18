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
		if (weight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }
	    if (!all_nodes.contains(node1)) {
	        throw new NoSuchElementException();
	    }
	    if (!all_nodes.contains(node2)) {
	        throw new NoSuchElementException();
	    }
		for(int i = 0; i < nodes_one.size(); i++)
		{
			if(nodes_one.get(i) == node1 && nodes_two.get(i) == node2)
			{
		        throw new IllegalStateException();
			}
			if(nodes_one.get(i) == node2 && nodes_two.get(i) == node1)
			{
		        throw new IllegalStateException();
			}
		}


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
		if (weight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }
	    if (!all_nodes.contains(node1)) {
	        throw new NoSuchElementException("Node1 not found in the graph");
	    }

	    if (!all_nodes.contains(node2)) {
	        throw new NoSuchElementException("Node2 not found in the graph");
	    }
		
		for(int i = 0; i < nodes_one.size(); i++)
		{
			if(nodes_one.get(i) == node1 && nodes_two.get(i) == node2)
			{
				weights.set(i, weight);
				return;
			}
			if(nodes_one.get(i) == node2 && nodes_two.get(i) == node1)
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
			if(nodes_one.get(i) == node2 && nodes_two.get(i) == node1)
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
	    if (!all_nodes.contains(node)) {
	        throw new NoSuchElementException();
	    }
		ArrayList<Edge<T>> ret = new ArrayList<Edge<T>>();
		for(int i = 0; i < nodes_one.size(); i++)
		{
			if(nodes_one.get(i) == node)
			{
				ret.add(new Edge<T>(nodes_one.get(i), nodes_two.get(i), names.get(i), this));
			}
			if(nodes_two.get(i) == node)
			{
				ret.add(new Edge<T>(nodes_two.get(i), nodes_one.get(i), names.get(i), this));
			}
		}
		return ret;
	}
	
	public Edge<T> getEdgeBetween(T node1, T node2)
	{
		
		if(!all_nodes.contains(node1))
		{
			throw new NoSuchElementException();
		}
		if(!all_nodes.contains(node2))
		{
			throw new NoSuchElementException();
		}
		
		for(int i = 0; i < nodes_one.size(); i++)
		{
			if(nodes_one.get(i) == node1 && nodes_two.get(i) == node2)
			{
				return new Edge<T>(nodes_one.get(i), nodes_two.get(i), names.get(i), this);
			}
			if(nodes_two.get(i) == node1 && nodes_one.get(i) == node2)
			{
				return new Edge<T>(nodes_two.get(i), nodes_one.get(i), names.get(i), this);
			}
		}
		return null;
	}
	
	public void disconnect(T node1, T node2) {
	    if (!all_nodes.contains(node1)) {
	        throw new NoSuchElementException();
	    }

	    if (!all_nodes.contains(node2)) {
	        throw new NoSuchElementException();
	    }

	    boolean connectionFound = false;

	    for (int i = 0; i < nodes_one.size(); i++) {
	        if ((nodes_one.get(i).equals(node1) && nodes_two.get(i).equals(node2)) ||
	            (nodes_one.get(i).equals(node2) && nodes_two.get(i).equals(node1))) {
	            nuke_soa_connection(i);
	            connectionFound = true;
	            break;
	        }
	    }

	    if (!connectionFound) {
	        throw new IllegalStateException();
	    }
	}
	
	public void remove(T node)
	{
		if (!all_nodes.contains(node)) {
	        throw new NoSuchElementException();
	    }
		
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
		all_nodes.remove(node);
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
		return search_path(from, to) != null;
	}
	
	public List<Edge<T>> getPath(T from, T to)
	{
		ArrayList<T> nodes = search_path(from, to);
		if(nodes == null)
		{
			return null; // bör throwa
		}
		
		ArrayList<Edge<T>> edges = new ArrayList<Edge<T>>();
		T prev = nodes.get(0);
		for(int i = 1; i < nodes.size(); i++)
		{
			T current = nodes.get(i);
			edges.add(this.getEdgeBetween(prev, current));
			prev = current;
		}
		return edges;
	}
	
	private ArrayList<T> search_path(T from, T to)
	{
	    if (!all_nodes.contains(from) || !all_nodes.contains(to)) {
	        return null;
	    }

		ArrayList<ArrayList<T>> potential_paths = new ArrayList<ArrayList<T>>();
		{
			ArrayList<T> start = new ArrayList<T>();
			start.add(from);
			potential_paths.add(start);
		}
		while(true)
		{
			ArrayList<ArrayList<T>> new_paths = new ArrayList<ArrayList<T>>();
				
			for(ArrayList<T> path : potential_paths)
			{
				T last = path.get(path.size()-1);
				if(last == to)
				{ return path; }
				
				for(Edge<T> edge : this.getEdgesFrom(last))
				{
					if(path.contains(edge.getDestination()))
					{ continue; }
					ArrayList<T> np = new ArrayList<T>(path);
					np.add(edge.getDestination());
					new_paths.add(np);
				}
			}
			
			if(new_paths.size() == 0)
			{ return null; }
			potential_paths = new_paths;
		}
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		for(T node : all_nodes)
		{
			builder.append(node + "\n");
		}
		for(int i = 0; i < nodes_one.size(); i++)
		{
			T a = nodes_one.get(i);
			T b = nodes_two.get(i);
			int weight = weights.get(i);
			builder.append("till " + b + " med " + a + " -> " + b + " tar " + weight + "\n");
			builder.append("till " + a + " med " + b + " -> " + a + " tar " + weight + "\n");
		}
		return builder.toString();
	}
}
