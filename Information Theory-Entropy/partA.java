import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class partA {
	//the final hypothesis
	private static String[] finalhypothesis = new String[9];
	public static void main(String[] args) throws IOException {
		//input space is: 2^9 = 512
		System.out.println(512);
		
		//concept space is: 2^512, through mathematics conversion
		//decimal digits = [512log(2) + 1] = 155
		//referenced url: http://www.exploringbinary.com/number-of-decimal-digits-in-a-binary-integer/
		System.out.println(155);
		
		//hypothesis space is: 3^9 + 1 = 19684
		System.out.println(19684);
		
		findS();
		devCal();
		testInput(args[0]);
	}

	public static void findS() throws IOException {
		//bufferedreader to read file
		BufferedReader br = new BufferedReader(new FileReader("9Cat-Train.labeled"));
		//bufferedwriter to write file
		BufferedWriter bw = new BufferedWriter(new FileWriter("partA4.txt"));
		//read to line
		String line = null;
		//used to parse read line
		String[] array = new String[10];
		int positivecount = 0;
		int negativecount = 0;
		//initial h0 with all attributes' value equal to null
		String[] outputline = new String[9];
		while((line = br.readLine())!=null){
			array = line.split("\t");
			//if is postive
			if(array[9].split(" ")[1].equals("high")){
				positivecount += 1;
				for(int i = 0;i <= 8;i++){
					//if outputline[i] is null then assign it current value
					if(outputline[i] == null){
						outputline[i] = array[i].split(" ")[1];
					}
					//else means it has value
					else{
						//if its value does not equal to current value, assign "?" to it
						if(!outputline[i].equals(array[i].split(" ")[1])){
							outputline[i] = "?";
						}
					}
				}
			}
			//ignore negative
			else{
				negativecount += 1;
			}
			//every 30 iteration, output the current conjunction to file
			if((positivecount + negativecount) % 30 == 0){
				String output = outputline[0];
				for(int i = 1;i <= 8;i++){
					output += "\t" + outputline[i];
				}
				bw.write(output + "\n");
			}
		}
		//get the final conjunction
		for(int i = 0;i < 9;i++){
			finalhypothesis[i] = outputline[i];
		}
		br.close();
		bw.close();
	}

	public static void devCal() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("9Cat-Dev.labeled"));
		String line = null;
		String[] array = new String[10];
		int totalcount = 0;
		int rightcount = 0;
		String labeledresult = "";
		while((line = br.readLine())!=null){
			//count total instances
			totalcount += 1;
			array = line.split("\t");
			//get labeled result
			labeledresult = array[9].split(" ")[1];
			//boolean value
			boolean flag = true;
			for(int i = 0;i < 9;i++){
				//ignore variable with value "?"
				if(finalhypothesis[i].equals("?")){
					continue;
				}
				else{
					//if consistent, flag = true
					if(finalhypothesis[i].equals(array[i].split(" ")[1])){
						flag = true;
					}
					//else flag = false, and break
					else{
						flag = false;
						break;
					}
				}
			}
			//if flag = true, and labeled result is positive, same with hypothesis, it is right count
			if(flag == true && labeledresult.equals("high")){
				rightcount += 1;
			}
			//or if flag = flase, and labeled result is negative, it is also right count
			if(flag == false && labeledresult.equals("low")){
				rightcount += 1;
			}
		}
		//calculation
		System.out.println((float)(totalcount - rightcount)/totalcount);
	}

	public static void testInput(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = null;
		String[] array = new String[9];
		String labeledresult = "";
		while((line = br.readLine())!=null){
			array = line.split("\t");
			boolean flag = true;
			for(int i = 0;i < 9;i++){
				//ignore variable with value "?"
				if(finalhypothesis[i].equals("?")){
					continue;
				}
				else{
					//if consistent, flag = true
					if(finalhypothesis[i].equals(array[i].split(" ")[1])){
						flag = true;
					}
					//else flag = false, and break
					else{
						flag = false;
						break;
					}
				}
			}
			//if flag = true, based on hypothesis it is high
			if(flag == true){
				System.out.println("high");
			}
			//else it is low
			else{
				System.out.println("low");
			}
		}
	}
}
