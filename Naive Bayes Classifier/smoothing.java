import java.io.*;
import java.util.*;

public class smoothing{
	public static void main(String[] args) throws IOException {
		String trainSet = args[0];
		String testSet = args[1];
		double q = Double.parseDouble(args[2]);
		HashMap<String, Integer> conWords = new HashMap<>();
		HashMap<String, Integer> libWords = new HashMap<>();
		HashSet<String> vocabulary = new HashSet<>();
		HashMap<String, Double> conWordsProb = new HashMap<>();
		HashMap<String, Double> libWordsProb = new HashMap<>();
		double conProb = 0;
		double libProb = 0;
		int conCount = 0;
		int libCount = 0;
		int conWordsCount = 0;
		int libWordsCount = 0;
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		String eachfile = "";
		BufferedReader br1 = null;
		String newline = "";
		while((eachfile = br.readLine()) != null){
			br1 = new BufferedReader(new FileReader(eachfile));
			if(eachfile.startsWith("con")){
				conCount += 1;
				while((newline = br1.readLine()) != null){
					newline = newline.toLowerCase();
					vocabulary.add(newline);
					conWords.put(newline, conWords.getOrDefault(newline, 0) + 1);
					conWordsCount += 1;
				}
			}
			else{
				libCount += 1;
				while((newline = br1.readLine()) != null){
					newline = newline.toLowerCase();
					vocabulary.add(newline);
					libWords.put(newline, libWords.getOrDefault(newline, 0) + 1);
					libWordsCount += 1;
				}
			}
		}
		//conservative blog probability
		conProb = (double)conCount / (conCount + libCount);
		//liberal blog probability
		libProb = (double)libCount / (conCount + libCount);
		//words with probability in conservative blog
		for(Map.Entry<String, Integer> each: conWords.entrySet()){
			conWordsProb.put(each.getKey(), (double)(each.getValue() + q) / (conWordsCount + q * vocabulary.size()));
		}
		//words with probability in liberal blog
		for(Map.Entry<String, Integer> each: libWords.entrySet()){
			libWordsProb.put(each.getKey(), (double)(each.getValue() + q) / (libWordsCount + q * vocabulary.size()));
		}
		
		int totalcount = 0;
		int rightcount = 0;
		double conProduct = 0.0;
		double libProduct = 0.0;
		br = new BufferedReader(new FileReader(args[1]));
		while((eachfile = br.readLine()) != null){
			totalcount += 1;
			br1 = new BufferedReader(new FileReader(eachfile));
			conProduct = Math.log(conProb);
			libProduct = Math.log(libProb);
			while((newline = br1.readLine()) != null){
				newline = newline.toLowerCase();
				if(conWordsProb.containsKey(newline) && !libWordsProb.containsKey(newline)){
					conProduct += Math.log(conWordsProb.get(newline));
					libProduct += Math.log((double)q / (libWordsCount + q * vocabulary.size()));
				}
				else if(!conWordsProb.containsKey(newline) && libWordsProb.containsKey(newline)){
					conProduct += Math.log((double)q / (conWordsCount + q * vocabulary.size()));
					libProduct += Math.log(libWordsProb.get(newline));
				}
				else if(conWordsProb.containsKey(newline) && libWordsProb.containsKey(newline)){
					conProduct += Math.log(conWordsProb.get(newline));
					libProduct += Math.log(libWordsProb.get(newline));
				}
				else{
					conProduct *= 1;
					libProduct *= 1;
				}
			}
			if(conProduct > libProduct){
				System.out.println("C");
				if(eachfile.startsWith("con")){
					rightcount += 1;
				}
			}
			else{
				System.out.println("L");
				if(eachfile.startsWith("lib")){
					rightcount += 1;
				}
			}
		}
		System.out.printf("Accuracy: %.04f\n", (float)rightcount / totalcount);
	}
}