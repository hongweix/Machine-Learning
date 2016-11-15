import java.io.*;
import java.util.*;

public class topwordsLogOdds{
	public static void main(String[] args) throws IOException{
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		String trainSet = args[0];
		HashMap<String, Double> conWords = new HashMap<>();
		HashMap<String, Double> libWords = new HashMap<>();
		HashSet<String> vocabulary = new HashSet<>();
		int conWordsCount = 0;
		int libWordsCount = 0;
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		String eachfile = "";
		BufferedReader br1 = null;
		String newline = "";
		while((eachfile = br.readLine()) != null){
			br1 = new BufferedReader(new FileReader(eachfile));
			if(eachfile.startsWith("con")){
				while((newline = br1.readLine()) != null){
					newline = newline.toLowerCase();
					vocabulary.add(newline);
					conWords.put(newline, conWords.getOrDefault(newline, 0.0) + 1.0);
					conWordsCount += 1;
				}
			}
			else{
				while((newline = br1.readLine()) != null){
					newline = newline.toLowerCase();
					vocabulary.add(newline);
					libWords.put(newline, libWords.getOrDefault(newline, 0.0) + 1.0);
					libWordsCount += 1;
				}
			}
		}
		
		ArrayList<Map.Entry<String, Double>> conWordsList = new ArrayList<>();
		for(Map.Entry<String, Double> entry: conWords.entrySet()){
			double conWordProb = (double)(entry.getValue() + 1) / (conWordsCount + vocabulary.size());
			double libWordProb = 0.0;
			if(libWords.containsKey(entry.getKey())){
				libWordProb = (double)(libWords.get(entry.getKey()) + 1) / (libWordsCount + vocabulary.size());
			}
			else{
				libWordProb = (double)1 / (libWordsCount + vocabulary.size());
			}
			Map.Entry<String,Double> newentry = new AbstractMap.SimpleEntry<String, Double>(entry.getKey(), Math.log(conWordProb) - Math.log(libWordProb));
			conWordsList.add(newentry);
		}
		ArrayList<Map.Entry<String, Double>> libWordsList = new ArrayList<>();
		for(Map.Entry<String, Double> entry: libWords.entrySet()){
			double libWordProb = (double)(entry.getValue() + 1) / (libWordsCount + vocabulary.size());
			double conWordProb = 0.0;
			if(conWords.containsKey(entry.getKey())){
				conWordProb = (double)(conWords.get(entry.getKey()) + 1) / (conWordsCount + vocabulary.size());
			}
			else{
				conWordProb = (double)1 / (conWordsCount + vocabulary.size());
			}
			Map.Entry<String,Double> newentry = new AbstractMap.SimpleEntry<String, Double>(entry.getKey(), Math.log(libWordProb) - Math.log(conWordProb));
			libWordsList.add(newentry);
		}

		Collections.sort(libWordsList, new Comparator<Map.Entry<String, Double>>(){
			public int compare(Map.Entry<String, Double> entry3, Map.Entry<String, Double> entry4){
				if(entry4.getValue() > entry3.getValue()){
					return 1;
				}
				else if(entry4.getValue() == entry3.getValue()){
					return 0;
				}
				else{
					return -1;
				}
			}
		});

		Collections.sort(conWordsList, new Comparator<Map.Entry<String, Double>>(){
			public int compare(Map.Entry<String, Double> entry1, Map.Entry<String, Double> entry2){
				if(entry2.getValue() > entry1.getValue()){
					return 1;
				}
				else if(entry2.getValue() == entry1.getValue()){
					return 0;
				}
				else{
					return -1;
				}
			}
		});
		
		for(int i = 0;i < 20;i++){
			System.out.printf(libWordsList.get(i).getKey() + " %.04f\n", (float)((double)libWordsList.get(i).getValue()));
		}
		System.out.println();
		for(int i = 0;i < 20;i++){
			System.out.printf(conWordsList.get(i).getKey() + " %.04f\n", (float)((double)conWordsList.get(i).getValue()));
		}
	}
}