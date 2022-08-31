import java.util.ArrayList;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
	private final Digraph graph;
	
	public SAP(Digraph G) {
		graph = new Digraph(G.V());
		for(int i=0;i<G.V();i++) {
			for(int j: G.adj(i)) {  //Makes the graph immutable
				graph.addEdge(i, j);
			}
		}
	}
	
	public int length(int v, int w) {
		BreadthFirstDirectedPaths V = new BreadthFirstDirectedPaths(graph,v);
		BreadthFirstDirectedPaths W = new BreadthFirstDirectedPaths(graph,w);
		int length = Integer.MAX_VALUE;
		for(int i=0;i<graph.V();i++) { //Check if the path exists with all the vertices
			if(V.hasPathTo(i)&&W.hasPathTo(i)) {
				int l = V.distTo(i)+W.distTo(i); //If path exists distance is calculated and if it is less than the best distance update
				if(l<length) length = l;
			}
			if(length==0) return length;
		}
		if(length==Integer.MAX_VALUE) return -1;
		return length;
	}
	
	public int ancestor(int v, int w) {
		BreadthFirstDirectedPaths V = new BreadthFirstDirectedPaths(graph,v);
		BreadthFirstDirectedPaths W = new BreadthFirstDirectedPaths(graph,w);
		int length = Integer.MAX_VALUE;
		int ancestor=-1;
		for(int i=0;i<graph.V();i++) {
			if(V.hasPathTo(i)&&W.hasPathTo(i)) {
				int l = V.distTo(i)+W.distTo(i);
				if(l<length) {
					length = l;
					ancestor = i;
				}
			}
			if(length==0) return ancestor;
		}
		if(length==Integer.MAX_VALUE) return -1;
		return ancestor;
	}
	
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		if(v==null || w==null) throw new IllegalArgumentException();
		ArrayList<Integer> V = new ArrayList<Integer>(),W = new ArrayList<Integer>();
		for(Object i:v) {
			if(i==null||(int) i<0) throw new IllegalArgumentException();
			V.add((int)i);
		}
		for(Object i:w) {
			if(i==null||(int) i<0) throw new IllegalArgumentException();
			W.add((int)i);
		}
		BreadthFirstDirectedPaths[] pathV = new BreadthFirstDirectedPaths[V.size()], pathW = new BreadthFirstDirectedPaths[W.size()];
		//BFS are cached to save time
		int Count=0;
		for(int i:V) pathV[Count++] = new BreadthFirstDirectedPaths(graph,i);
		Count =0;
		for(int i:W) pathW[Count++] = new BreadthFirstDirectedPaths(graph,i);
		Count=0;
		ArrayList<Integer> vertices = new ArrayList<Integer>();
		int sizeV = V.size(),sizeW = W.size();
		for(int i=0;i<graph.V();i++) {
			Count =0;
			for(int j=0;j<sizeW;j++) {
				if(pathW[j].hasPathTo(i))Count++;
				if(Count>0) {
					vertices.add(i);
					break;
				}
			}
		}
		Count =0;
		int length = Integer.MAX_VALUE;
		for(int i:vertices) {
			for(int j=0;j<sizeV;j++) {
				if(!pathV[j].hasPathTo(i)) continue;
				for(int k=0;k<sizeW;k++) {
					if(!pathW[k].hasPathTo(i)) continue;
					int l = pathV[j].distTo(i) + pathW[k].distTo(i);
					if(l<length) length =l;
					if(length==0) return length;
				}
			}
		}
		if(length==Integer.MAX_VALUE) return -1;
		return length;
	}
	
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		if(v==null||w==null) throw new IllegalArgumentException();
		ArrayList<Integer> V = new ArrayList<Integer>(),W = new ArrayList<Integer>(), vertices = new ArrayList<Integer>();
		for(Object i:v) {
			if(i==null||(int) i<0) throw new IllegalArgumentException();
			V.add((int)i);
		}
		for(Object i:w) {
			if(i==null||(int) i<0) throw new IllegalArgumentException();
			W.add((int)i);
		}
		BreadthFirstDirectedPaths[] pathV = new BreadthFirstDirectedPaths[V.size()], pathW = new BreadthFirstDirectedPaths[W.size()];
		int Count=0;
		for(int i:V) pathV[Count++] = new BreadthFirstDirectedPaths(graph,i);
		Count =0;
		for(int i:W) pathW[Count++] = new BreadthFirstDirectedPaths(graph,i);
		int sizeV = V.size(),sizeW = W.size();
		for(int i=0;i<graph.V();i++) {
			Count =0;
			for(int j=0;j<sizeW;j++) {
				if(pathW[j].hasPathTo(i))Count++;
				if(Count>0) {
					vertices.add(i);
					break;
				}
			}
		}
		int length = Integer.MAX_VALUE;
		int ancestor = -1;
		for(int i:vertices) {
			for(int j=0;j<V.size();j++) {
				if(!pathV[j].hasPathTo(i)) continue;
				for(int k=0;k<W.size();k++) {
					if(!pathW[k].hasPathTo(i)) continue;
					int l = pathV[j].distTo(i) + pathW[k].distTo(i);
					if(l<length) {
						length =l;
						ancestor = i;
					}
					if(length==0) return ancestor;
				}
			}
		}
		if(length==Integer.MAX_VALUE) return -1;
		return ancestor;
	}
	
	public static void main(String[] args) {
	}
}
