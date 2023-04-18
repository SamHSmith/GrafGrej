// PROG2 VT2023, Inlämningsuppgift, del 1
// Grupp 100
// Sam Smith sasm7798
// Marcus Berngarn mabe1838

import java.util.*;

public class ListGraph<T> implements Graph<T> {

	/* For nodes without links. */
	public ArrayList<T> allNodes = new ArrayList<T>();
	
	/* Links SOA */
	public ArrayList<T> nodesOne = new ArrayList<T>();
	public ArrayList<T> nodesTwo = new ArrayList<T>();
	public ArrayList<String> names = new ArrayList<String>();
	public ArrayList<Integer> weights = new ArrayList<Integer>();
	
	public void add(T node)
	{
		allNodes.add(node);
	}
	
	public void connect(T node1, T node2, String name, int weight)
	{
		if (weight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }
	    if (!allNodes.contains(node1)) {
	        throw new NoSuchElementException();
	    }
	    if (!allNodes.contains(node2)) {
	        throw new NoSuchElementException();
	    }
		for(int i = 0; i < nodesOne.size(); i++)
		{
			if(nodesOne.get(i) == node1 && nodesTwo.get(i) == node2)
			{
		        throw new IllegalStateException();
			}
			if(nodesOne.get(i) == node2 && nodesTwo.get(i) == node1)
			{
		        throw new IllegalStateException();
			}
		}


		nodesOne.add(node1);
		nodesTwo.add(node2);
		names.add(name);
		weights.add(weight);

		if(!allNodes.contains(node1))
		{ allNodes.add(node1); }
		if(!allNodes.contains(node2))
		{ allNodes.add(node2); }
	}
	
	public void setConnectionWeight(T node1, T node2, int weight)
	{
		if (weight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }
	    if (!allNodes.contains(node1)) {
	        throw new NoSuchElementException("Node1 not found in the graph");
	    }

	    if (!allNodes.contains(node2)) {
	        throw new NoSuchElementException("Node2 not found in the graph");
	    }
		
		for(int i = 0; i < nodesOne.size(); i++)
		{
			if(nodesOne.get(i) == node1 && nodesTwo.get(i) == node2)
			{
				weights.set(i, weight);
				return;
			}
			if(nodesOne.get(i) == node2 && nodesTwo.get(i) == node1)
			{
				weights.set(i, weight);
				return;
			}
		}
	}
	
	public int getConnectionWeight(T node1, T node2)
	{
		for(int i = 0; i < nodesOne.size(); i++)
		{
			if(nodesOne.get(i) == node1 && nodesTwo.get(i) == node2)
			{
				return weights.get(i);
			}
			if(nodesOne.get(i) == node2 && nodesTwo.get(i) == node1)
			{
				return weights.get(i);
			}
		}
		return -1;
	}
	
	public Set<T> getNodes()
	{
		return new HashSet<T>(allNodes);
	}
	
	public Collection<Edge<T>> getEdgesFrom(T node)
	{
	    if (!allNodes.contains(node)) {
	        throw new NoSuchElementException();
	    }
		ArrayList<Edge<T>> ret = new ArrayList<Edge<T>>();
		for(int i = 0; i < nodesOne.size(); i++)
		{
			if(nodesOne.get(i) == node)
			{
				ret.add(new Edge<T>(nodesOne.get(i), nodesTwo.get(i), names.get(i), this));
			}
			if(nodesTwo.get(i) == node)
			{
				ret.add(new Edge<T>(nodesTwo.get(i), nodesOne.get(i), names.get(i), this));
			}
		}
		return ret;
	}
	
	public Edge<T> getEdgeBetween(T node1, T node2)
	{
		
		if(!allNodes.contains(node1))
		{
			throw new NoSuchElementException();
		}
		if(!allNodes.contains(node2))
		{
			throw new NoSuchElementException();
		}
		
		for(int i = 0; i < nodesOne.size(); i++)
		{
			if(nodesOne.get(i) == node1 && nodesTwo.get(i) == node2)
			{
				return new Edge<T>(nodesOne.get(i), nodesTwo.get(i), names.get(i), this);
			}
			if(nodesTwo.get(i) == node1 && nodesOne.get(i) == node2)
			{
				return new Edge<T>(nodesTwo.get(i), nodesOne.get(i), names.get(i), this);
			}
		}
		return null;
	}
	
	public void disconnect(T node1, T node2) {
	    if (!allNodes.contains(node1)) {
	        throw new NoSuchElementException();
	    }

	    if (!allNodes.contains(node2)) {
	        throw new NoSuchElementException();
	    }

	    boolean connectionFound = false;

	    for (int i = 0; i < nodesOne.size(); i++) {
	        if (nodesOne.get(i).equals(node1) && nodesTwo.get(i).equals(node2) ||
	            nodesOne.get(i).equals(node2) && nodesTwo.get(i).equals(node1)) {
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
		if (!allNodes.contains(node)) {
	        throw new NoSuchElementException();
	    }
		
		ArrayList<Integer> toRemove = new ArrayList<Integer>();
		for(int i = 0; i < nodesOne.size(); i++)
		{
			if(nodesOne.get(i) == node || nodesTwo.get(i) == node)
			{
				toRemove.add(i);
			}
		}
		for(int index : toRemove)
		{
			nuke_soa_connection(index);
		}
		allNodes.remove(node);
	}
	
	private void nuke_soa_connection(int i)
	{
		nodesOne.remove(i);
		nodesTwo.remove(i);
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
			return null; // bör throwa?
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
	    if (!allNodes.contains(from) || !allNodes.contains(to)) {
	        return null;
	    }

		ArrayList<ArrayList<T>> potentialPaths = new ArrayList<ArrayList<T>>();
		{
			ArrayList<T> start = new ArrayList<T>();
			start.add(from);
			potentialPaths.add(start);
		}
		while(true)
		{
			ArrayList<ArrayList<T>> newPaths = new ArrayList<ArrayList<T>>();
				
			for(ArrayList<T> path : potentialPaths)
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
					newPaths.add(np);
				}
			}
			
			if(newPaths.size() == 0)
			{ return null; }
			potentialPaths = newPaths;
		}
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		for(T node : allNodes)
		{
			builder.append(node + "\n");
		}
		for(int i = 0; i < nodesOne.size(); i++)
		{
			T a = nodesOne.get(i);
			T b = nodesTwo.get(i);
			int weight = weights.get(i);
			builder.append("till " + b + " med " + a + " -> " + b + " tar " + weight + "\n");
			builder.append("till " + a + " med " + b + " -> " + a + " tar " + weight + "\n");
		}
		return builder.toString();
	}
}
