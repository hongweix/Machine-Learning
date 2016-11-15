import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.lang.Math;

public class inspect{
	public static void main(String[] args) throws IOException {
		//br to read the file
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		//map to store label and its occurence
		HashMap<String, Integer> lable_count = new HashMap<>();
		//line to store current value in br
		String line = null;
		//current lable
		String cur_lable = "";
		//length of a record
		int length = br.readLine().split(",").length;
		while((line = br.readLine())!= null){
			//current lable
			cur_lable = line.split(",")[length - 1];
			//if already in map, update by adding 1
			if(lable_count.containsKey(cur_lable)){
				lable_count.put(cur_lable, lable_count.get(cur_lable) + 1);
			}
			//else store it starting from 1
			else{
				lable_count.put(cur_lable, 1);
			}
		}
		//array to store lable name, will be easy to call by index
		String[] lable = new String[2];
		//array to store lable occurence, will be easy to call by index
		int[] count = new int[2];
		//major lable index
		int major_lable_index = -1;
		//major count
		int major_count = 0;
		int i = 0;
		for(String key: lable_count.keySet()){
			lable[i] = key;
			count[i] = lable_count.get(key);
			if(count[i] > major_count){
				major_lable_index = i;
				major_count = count[i];
			} 
			i += 1;
		}
		//calculate probability
		double prob_0 = (double)count[0]/(count[0] + count[1]);
		double prob_1 = (double)count[1]/(count[0] + count[1]);
		//calculate entropy
		double entropy = -prob_0 * Math.log(prob_0) / Math.log(2) - prob_1 * Math.log(prob_1) / Math.log(2);
		System.out.println("entropy: " + entropy);
		//error = 1 - majority/total
		double error = 1 - (double)count[major_lable_index] / (count[0] + count[1]);
		System.out.println("error: " + error);
	}
}
