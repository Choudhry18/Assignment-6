import java.util.ArrayList;

public class Outcast {
	
	private WordNet x;
	
	public Outcast(WordNet wordnet) {
		x = wordnet;
	}
	
	public String outcast(String[] nouns) {
		ArrayList<ArrayList<Integer>> distances = new ArrayList<ArrayList<Integer>>(nouns.length-1);
		String out = "";
		int max = -1;
		for(int i=0;i<nouns.length;i++) {
			ArrayList<Integer> current = new ArrayList<Integer>(nouns.length-i); 
			int Tdistance=0;
			for(int j=0;j<nouns.length;j++) {
				if(i==j) continue;
				int distance;
				if(j<i) distance = distances.get(j).get((i-j)-1);
				else {
					distance = x.distance(nouns[i], nouns[j]);
					current.add(distance);
				}
				Tdistance += distance;
			}
			distances.add(current);
			if(Tdistance>max) {
				max = Tdistance;
				out = nouns[i];
			}
		}
		return out;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
