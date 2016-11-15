import java.io.*;
import java.util.*;

public class topwords{
	public static void main(String[] args) throws IOException{
		String trainSet = args[0];
		HashMap<String, Integer> conWords = new HashMap<>();
		HashMap<String, Integer> libWords = new HashMap<>();
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
					conWords.put(newline, conWords.getOrDefault(newline, 0) + 1);
					conWordsCount += 1;
				}
			}
			else{
				while((newline = br1.readLine()) != null){
					newline = newline.toLowerCase();
					vocabulary.add(newline);
					libWords.put(newline, libWords.getOrDefault(newline, 0) + 1);
					libWordsCount += 1;
				}
			}
		}
		
		ArrayList<Map.Entry<String, Integer>> conWordsList = new ArrayList<>(conWords.entrySet());
		ArrayList<Map.Entry<String, Integer>> libWordsList = new ArrayList<>(libWords.entrySet());
		
		Collections.sort(conWordsList, new Comparator<Map.Entry<String, Integer>>(){
			public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2){
				return entry2.getValue() - entry1.getValue();
			}
		});
		Collections.sort(libWordsList, new Comparator<Map.Entry<String, Integer>>(){
			public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2){
				return entry2.getValue() - entry1.getValue();
			}
		});
		
		for(int i = 0;i < 20;i++){
			System.out.printf(libWordsList.get(i).getKey() + " %.04f\n", (float)(libWordsList.get(i).getValue() + 1) / (libWordsCount + vocabulary.size()));
		}
		System.out.println();
		for(int i = 0;i < 20;i++){
			System.out.printf(conWordsList.get(i).getKey() + " %.04f\n", (float)(conWordsList.get(i).getValue() + 1) / (conWordsCount + vocabulary.size()));
		}
	}
}