import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class partB {
	//2d matrix, row represent input space, column represent hypothesis space 
	private static int[][] matrix = new int[16][65536];
	//map to store key, value
	private static HashMap<String, Integer> map = new HashMap<>();
	//inconsistent hypothesis space
	private static HashSet<Integer> inconsisHypo = new HashSet<>();
	//consistent hypothesis space
	private static HashSet<Integer> consisHypo = new HashSet<>();
	public static void main(String[] args) throws IOException {
		//input space is: 2^4 = 16
		System.out.println(16);
		//concept space is: 2^16 = 65536
		System.out.println(65536);
		allHypoSpace();
		listThenEliminate();
		testVote(args[0]);
	}

	public static void allHypoSpace(){
		//fill the matrix with all hypothesis
		//eg.   x     h0     h1     h2  ......  hn
		//     00     0      1      0           1
		//     01     0      0      1           1
		//     10     0      0      0           1
		//     11     0      0      0           1
		for(int i = 0;i < 65536;i++){
			int num = i;
			for(int j = 15;j >= 0;j--){
				if(Math.pow(2,j) <= num){
					matrix[j][i] = 1;
					num = num - (int)Math.pow(2,j);
				}
				else{
					matrix[j][i] = 0;
				}
			}
		}
	}

	public static void listThenEliminate() throws IOException { 
		//0 male 1 female
		map.put("Male",0);
		map.put("Female",1);
		//0 young 1 old
		map.put("Young",0);
		map.put("Old",1);
		//0 yes 1 no
		map.put("Yes",0);
		map.put("No",1);
		//1 high 0 low
		map.put("high",1);
		map.put("low",0);
		BufferedReader br = new BufferedReader(new FileReader("4Cat-Train.labeled"));
		String newline = null;
		String[] array = new String[5];
		int result = 0;
		int targetrow = 0;
		while((newline = br.readLine()) != null){
			array = newline.split("\t");
			result = map.get(array[4].split(" ")[1]);
			//calculate current instance match which row of input space
			for(int i = 0;i < 4;i++){
				targetrow += map.get(array[i].split(" ")[1]) * (int)Math.pow(2,i);
			}
			//for the target row, traverse all hypothesis
			for(int j = 0;j < 65536;j++){
				//if it is inconsistent, add current column (the hypothesis num) to inconsistent hypothesis space
				if(matrix[targetrow][j] != result){
					inconsisHypo.add(j);
				}
			}
			//reset target row to be 0
			targetrow = 0;
		}
		//get the consistent hypothesis space size
		System.out.println(65536 - inconsisHypo.size());
		//fill the consistent hypothesis space
		for(int k = 0;k < 65536;k++){
			if(!inconsisHypo.contains(k)){
				consisHypo.add(k);
			}
		}
	}

	public static void testVote(String filename) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String newline = null;
		String[] array = new String[5];
		int targetrow = 0;
		int highcount = 0;
		int lowcount = 0;
		while((newline = br.readLine()) != null){
			array = newline.split("\t");
			//calculate current instance match which row of input space
			for(int i = 0;i < 4;i++){
				targetrow += map.get(array[i].split(" ")[1]) * (int)Math.pow(2,i);
			}
			//search online usage of iterator, url: https://www.tutorialspoint.com/java/util/hashset_iterator.html
			//traverse all consistent hypothesis space and count vote for high or low
			Iterator it = consisHypo.iterator();
			while (it.hasNext()){
				if(matrix[targetrow][(int)it.next()] == 1){
					highcount += 1;
				}
				else{
					lowcount += 1;
				} 
   			}
   			System.out.println(highcount + " " + lowcount);
   			highcount = 0;
   			lowcount = 0;
   			targetrow = 0;
		}
	}
}