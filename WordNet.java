import edu.princeton.cs.algs4.In;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;

public class WordNet {
	
	private final Digraph graph;
	private final Map<String,ArrayList<Integer>> table = new HashMap<String,ArrayList<Integer>>(); 
	//This stores nouns with their ids, some nouns are repeated thus the arrayList
	private final Map<Integer,Integer> bin = new HashMap<Integer,Integer>();
	//This hashes noun ids to an index(0-the size of the table) the index are the vertices of the graph
	private final Map<Integer,String> reverse = new HashMap<Integer,String>();
	//Returns the noun for a given id used in sap function
	private final SAP x; //Sap is cached once to save time
	
	public WordNet(String synsets, String hypernyms) {
		String[] line, line2;
		int id;
		if(synsets == null || hypernyms == null) throw new IllegalArgumentException("Null Argument!");
		In Synet = new In(synsets);
		int index =0;
		ArrayList<Integer> ids;
		while(Synet.hasNextLine()) {
			line = Synet.readLine().split(","); // This separates the various fields of the input
			line2 = line[1].split(" ");  //Incase there are more than one noun in a synset
			id = Integer.parseInt(line[0]);
			reverse.put(id, line[1]);
			for(String i:line2) {
				if(table.containsKey(i)) { //if the noun is repeated
					ids = table.get(i);
					ids.add(id);
				}
				else {
					ids = new ArrayList<Integer>();
					ids.add(id);
					table.put(i, ids);
				}
			}
			bin.put(id, index++); //An index is alotted to the synset
		}
		graph = new Digraph(index);
		In Hypernyms = new In(hypernyms);
		while(Hypernyms.hasNextLine()) {
			line = Hypernyms.readLine().split(",");
			int root = bin.get(Integer.parseInt(line[0]));
			for(int i=1;i<line.length;i++) {
				int edge = bin.get(Integer.parseInt(line[i]));
				graph.addEdge(root, edge);
			}// Adds edges between the hypernyms the hashtables allow the indexes to be accessed
		}
		Hypernyms.close();
		DirectedCycle check = new DirectedCycle(graph);
		if(check.hasCycle()) throw new IllegalArgumentException("Not a valid DAG");
		int rooted =0;
		for(int i=0; i<graph.V();i++) if(!graph.adj(i).iterator().hasNext()) rooted++;
		if(rooted!=1) throw new IllegalArgumentException("Not a rooted DAG");
		x = new SAP(graph);
	}
	
	public Iterable<String> nouns(){
		return table.keySet();
	}
	
	public boolean isNoun(String word) {
		if(word==null) throw new IllegalArgumentException();
		return table.containsKey(word);
	}
	
	
	public int distance(String nounA, String nounB) {
		if(!isNoun(nounA)||!isNoun(nounB)) throw new IllegalArgumentException();
		ArrayList<Integer> A,B,V,W;
		V = table.get(nounA); //These get the ids of the nouns
		W = table.get(nounB);
		A = new ArrayList<Integer>(V.size());
		B = new ArrayList<Integer>(W.size());
		for(int i:V) A.add(bin.get(i));//These stores the indexes
		for(int i:W) B.add(bin.get(i));
		return x.length(A, B); //The indexes are passes as the arguments
	}
	
	public String sap(String nounA, String nounB) {
		if(!isNoun(nounA)||!isNoun(nounB)) throw new IllegalArgumentException();
		ArrayList<Integer> A,B,V,W;
		V = table.get(nounA);
		W = table.get(nounB);
		A = new ArrayList<Integer>(V.size());
		B = new ArrayList<Integer>(W.size());
		for(int i:V) A.add(bin.get(i));
		for(int i:W) B.add(bin.get(i));
		int Ancestor = x.ancestor(A, B);
		return reverse.get(Ancestor);
	}

	public static void main(String[] args) {
	}
}
