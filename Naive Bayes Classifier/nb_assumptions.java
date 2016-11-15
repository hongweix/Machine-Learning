import java.io.*;
import java.util.*;

public class nb_assumptions{
	public static void main(String[] args) throws IOException {
		String trainSet = args[0];
		String testSet = args[1];
		HashMap<String, Integer> conWords = new HashMap<>();
		HashMap<String, Integer> libWords = new HashMap<>();
		HashMap<String, Double> conWordsProb = new HashMap<>();
		HashMap<String, Double> libWordsProb = new HashMap<>();
		double conProb = 0;
		double libProb = 0;
		int conCount = 0;
		int libCount = 0;
		int conWordsCount = 0;
		int libWordsCount = 0;
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		String newline = "";
		String[] record = null;
		while((newline = br.readLine()) != null){
			record = newline.split(" ");
			if(record[record.length-1].equals("1")){
				conCount += 1;
				for(int i = 0;i < record.length-1;i++){
					if(record[i].equals("1")){
						conWords.put("col" + i, conWords.getOrDefault("col" + i, 0) + 1);
					}
				}
			}
			else{
				libCount += 1;
				for(int i = 0;i < record.length-1;i++){
					if(record[i].equals("1")){
						libWords.put("col" + i, libWords.getOrDefault("col" + i, 0) + 1);
					}
				}
			}
		}

		// for(Map.Entry<String, Double> each: conWords.entrySet()){
		// 	System.out.println("hi there");
		// 	System.out.println(each.getKey() + " " + each.getValue());
		// }
		// for(Map.Entry<String, Double> each: libWords.entrySet()){
		// 	System.out.println("hi here");
		// 	System.out.println(each.getKey() + " " + each.getValue());
		// }

		//conservative blog probability
		conProb = (double)conCount / (conCount + libCount);
		//liberal blog probability
		libProb = (double)libCount / (conCount + libCount);
		//words with probability in conservative blog
		for(Map.Entry<String, Integer> each: conWords.entrySet()){
			conWordsProb.put(each.getKey(), (double)(each.getValue()) / (conCount));
			//conWordsProb.put(each.getKey(), (double)(each.getValue() + 1) / (conCount + 2));
		}
		//words with probability in liberal blog
		for(Map.Entry<String, Integer> each: libWords.entrySet()){
			libWordsProb.put(each.getKey(), (double)(each.getValue()) / (conCount));
			//libWordsProb.put(each.getKey(), (double)(each.getValue() + 1) / (libCount + 2));
		}
	
		double conProduct = 0.0;
		double libProduct = 0.0;
		br = new BufferedReader(new FileReader(args[1]));
		while((newline = br.readLine()) != null){
			//conProduct = conProb;
			conProduct = conProb;
			libProduct = libProb;
			record = newline.split(" ");
			for(int i = 0; i < record.length-1;i++){
				if(record[i].equals("1")){
					conProduct *= conWordsProb.get("col" + i);
					libProduct *= libWordsProb.get("col" + i);
				}
				else{
					conProduct *= (1.0 - conWordsProb.get("col" + i));
					libProduct *= (1.0 - libWordsProb.get("col" + i));
					//conProduct *= (double)1 / (conCount + 2);
				}
			}
			System.out.println(conProduct / (conProduct + libProduct));
		}
	}
}