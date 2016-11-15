import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Math;

public class decisionTree{
	//attr_value will store attr_name, and a list of its values
	private static Map<String, ArrayList<String>> attr_value = new HashMap<String, ArrayList<String>>();
	//class_result will store values, "+" postive, "-" negative
	private static List<String> class_result = new ArrayList<String>();
	//attr_name will store all attr name, easy to be accessed by index
	private static List<String> attr_name = new ArrayList<String>();
	//attr_name_positivevalue will store attr name, and postive value and negative value
	private static Map<String, ArrayList<String>> attr_name_positivevalue = new HashMap<String, ArrayList<String>>();
	//array to store class value counts
	private static int[] class_count = new int[2];
	//root on decision tree
	private static TreeNode root;
	//leftchild of root, if exists
	private static TreeNode leftchild;
	//rightchild of root, if exists
	private static TreeNode rightchild;
	
	//main method
	public static void main(String[] args) throws IOException{
		geneTree(args[0]);
		displayTree();
		testTree(args[1]);
	}

	//method to calculate info gain
	public static double calInfoGain(int[] nosplit, int[] split_posi, int[] split_nega){
		//entropy when no split
		double nosplit_H = 0;
		//only when positive counts and negative counts not equal to 0, use formula to calculate entropy
		if(nosplit[0] != 0 && nosplit[1] != 0){
			double nosplit_p0 = (double)nosplit[0]/(nosplit[0] + nosplit[1]);
			double nosplit_p1 = (double)nosplit[1]/(nosplit[0] + nosplit[1]);
			nosplit_H = nosplit_p0 * Math.log(1/nosplit_p0) / Math.log(2) + nosplit_p1 * Math.log(1/nosplit_p1) / Math.log(2);
		}
		//when split, positive part portion and negative part portion
		double split_posi_portion = (((double)split_posi[0] + split_posi[1])) / (nosplit[0] + nosplit[1]);
		double split_nega_portion = (((double)split_nega[0] + split_nega[1])) / (nosplit[0] + nosplit[1]);
		//entropy of positive part
		double split_posi_H = 0;
		//only when positive counts and negative counts not equal to 0, use formula to calculate entropy
		if(split_posi[0] != 0 && split_posi[1] != 0){
			double split_posi_p0 = (double)split_posi[0]/(split_posi[0] + split_posi[1]);
			double split_posi_p1 = (double)split_posi[1]/(split_posi[0] + split_posi[1]);
			split_posi_H = split_posi_p0 * Math.log(1/split_posi_p0) / Math.log(2) + split_posi_p1 * Math.log(1/split_posi_p1) / Math.log(2);
		}
		//entropy of positive part
		double split_nega_H = 0;
		//only when positive counts and negative counts not equal to 0, use formula to calculate entropy
		if(split_nega[0] != 0 && split_nega[1] != 0){
			double split_nega_p0 = (double)split_nega[0]/(split_nega[0] + split_nega[1]);
			double split_nega_p1 = (double)split_nega[1]/(split_nega[0] + split_nega[1]);
			split_nega_H = split_nega_p0 * Math.log(1/split_nega_p0) / Math.log(2) + split_nega_p1 * Math.log(1/split_nega_p1) / Math.log(2);
		}
		//entropy after split
		double split_H = split_posi_portion * split_posi_H + split_nega_portion * split_nega_H;
		//return info gain
		return nosplit_H - split_H;
	}

	//method to generate the decision tree
	public static void geneTree(String filename) throws IOException{
		//read file line by line
		BufferedReader br = new BufferedReader(new FileReader(filename));
		//use the first line to get attr name
		String[] line = br.readLine().split(",");
		for(int i = 0;i < line.length - 1;i++){
			attr_name.add(line[i]);
			attr_value.put(line[i], new ArrayList<>());
		}
		String dataline = "";
		int length = line.length;
		String[] record = new String[length];
		while((dataline = br.readLine())!=null){
			record = dataline.split(",");
			//suppose all values of attr in the first line will be the positive value 
			if(attr_name_positivevalue.size() <= 0){
				for(int i = 0;i < length - 1;i++){
					ArrayList<String> values = new ArrayList<>();
					values.add(record[i]);
					attr_name_positivevalue.put(attr_name.get(i), values);
				}
			}
			//add current line attr name and value to map, postive value store "+", negative value store "-"
			for(int i = 0;i < length - 1;i++){
				if(record[i].equals(attr_name_positivevalue.get(attr_name.get(i)).get(0))){
					attr_value.get(attr_name.get(i)).add("+");
				}
				else{
					//also store the negative value string in attr_name_positivevalue
					if(attr_name_positivevalue.get(attr_name.get(i)).size() < 2){
						attr_name_positivevalue.get(attr_name.get(i)).add(record[i]);
					}
					attr_value.get(attr_name.get(i)).add("-");
				}
			}
			//if class value is "yes" or "A" or "democrat", store as "+", other wise store as "-"
			if(record[length - 1].equals("yes") || record[length - 1].equals("A") || record[length - 1].equals("democrat")){
				class_result.add("+");
				class_count[0] += 1;
			}
			else{
				class_result.add("-");
				class_count[1] += 1;
			}
		}
		br.close();

		//get the root
		//when root value is postive "+", the count of poistive class and negative class[+/-]
		int[] root_posi_count = null;
		//when root value is negative "-", the count of poistive class and negative class[+/-]
		int[] root_nega_count = null;
		double root_max_gain = 0;
		root = null;
		for(String eachattr: attr_value.keySet()){
			root_posi_count = new int[2];
			root_nega_count = new int[2];
			for(int i = 0;i < attr_value.get(eachattr).size();i++){
				if(attr_value.get(eachattr).get(i).equals("+") && class_result.get(i).equals("+")){
					root_posi_count[0] += 1;
				}
				else if(attr_value.get(eachattr).get(i).equals("+") && class_result.get(i).equals("-")){
					root_posi_count[1] += 1;
				}
				else if(attr_value.get(eachattr).get(i).equals("-") && class_result.get(i).equals("+")){
					root_nega_count[0] += 1;
				}
				else{
					root_nega_count[1] += 1;
				}
			}
			//get max info gain
			double infoGain = calInfoGain(class_count, root_posi_count, root_nega_count);
			if(infoGain > root_max_gain){
				root_max_gain = infoGain;
				root = new TreeNode(eachattr, root_posi_count, root_nega_count);
			}
		}
		root_posi_count = root.posi_count;
		root_nega_count = root.nega_count;

		//get leftchild if possible
		//when goes to root positive part, and leftchild be positive "+", the count of poistive class and negative class[+/-]
		int[] leftchild_posi_count = null;
		//when goes to root positive part, and leftchild be negative "-", the count of poistive class and negative class[+/-]
		int[] leftchild_nega_count = null;
		double leftchild_max_gain = 0;
		leftchild = null;
		for(String eachattr: attr_value.keySet()){
			if(!eachattr.equals(root.attr_name)){
				leftchild_posi_count = new int[2];
				leftchild_nega_count = new int[2];
				for(int i = 0;i < attr_value.get(eachattr).size();i++){
					if(attr_value.get(root.attr_name).get(i).equals("+")){
						if(attr_value.get(eachattr).get(i).equals("+") && class_result.get(i).equals("+")){
							leftchild_posi_count[0] += 1;
						}
						else if(attr_value.get(eachattr).get(i).equals("+") && class_result.get(i).equals("-")){
							leftchild_posi_count[1] += 1;
						}
						else if(attr_value.get(eachattr).get(i).equals("-") && class_result.get(i).equals("+")){
							leftchild_nega_count[0] += 1;
						}
						else{
							leftchild_nega_count[1] += 1;
						}
					}
				}
				//get max info gain
				double left_infoGain = calInfoGain(root_posi_count, leftchild_posi_count, leftchild_nega_count);
				if(left_infoGain >= 0.1 && left_infoGain > leftchild_max_gain){
					leftchild_max_gain = left_infoGain;
					leftchild = new TreeNode(eachattr, leftchild_posi_count, leftchild_nega_count);
				}
			}
		}

		//get rightchild if possible
		//when goes to root negative part, and rightchild be positive "+", the count of poistive class and negative class[+/-]
		int[] rightchild_posi_count = null;
		//when goes to root negative part, and rightchild be negative "-", the count of poistive class and negative class[+/-]
		int[] rightchild_nega_count = null;
		double rightchild_max_gain = 0;
		rightchild = null;
		for(String eachattr: attr_value.keySet()){
			if(!eachattr.equals(root.attr_name)){
				rightchild_posi_count = new int[2];
				rightchild_nega_count = new int[2];
				for(int i = 0;i < attr_value.get(eachattr).size();i++){
					if(attr_value.get(root.attr_name).get(i).equals("-")){
						if(attr_value.get(eachattr).get(i).equals("+") && class_result.get(i).equals("+")){
							rightchild_posi_count[0] += 1;
						}
						else if(attr_value.get(eachattr).get(i).equals("+") && class_result.get(i).equals("-")){
							rightchild_posi_count[1] += 1;
						}
						else if(attr_value.get(eachattr).get(i).equals("-") && class_result.get(i).equals("+")){
							rightchild_nega_count[0] += 1;
						}
						else{
							rightchild_nega_count[1] += 1;
						}
					}
				}
				//get max info gain
				double right_infoGain = calInfoGain(root_nega_count, rightchild_posi_count, rightchild_nega_count);
				if(right_infoGain >= 0.1 && right_infoGain > rightchild_max_gain){
					rightchild_max_gain = right_infoGain;
					rightchild = new TreeNode(eachattr, rightchild_posi_count, rightchild_nega_count);
				}
			}
		}
	}

	//method to display tree and error
	public static void displayTree(){
		System.out.println("[" + class_count[0] + "+/" + class_count[1] + "-]");
		//display root postive part
		System.out.println(root.attr_name.trim() + "= " + attr_name_positivevalue.get(root.attr_name).get(0) + ": [" + root.posi_count[0] + "+/" + root.posi_count[1] + "-]");
		//if leftchild exists, then display leftchild positive part and negative part
		if(leftchild != null){
			System.out.println("| " + leftchild.attr_name.trim() + "= " + attr_name_positivevalue.get(leftchild.attr_name).get(0) + ": [" + leftchild.posi_count[0] + "+/" + leftchild.posi_count[1] + "-]");
			System.out.println("| " + leftchild.attr_name.trim() + "= " + attr_name_positivevalue.get(leftchild.attr_name).get(1) + ": [" + leftchild.nega_count[0] + "+/" + leftchild.nega_count[1] + "-]");
		}
		//display root negative part
		System.out.println(root.attr_name.trim() + "= " + attr_name_positivevalue.get(root.attr_name).get(1) + ": [" + root.nega_count[0] + "+/" + root.nega_count[1] + "-]");
		//if rightchild exists, then display rightchild positive part and negative part
		if(rightchild != null){
			System.out.println("| " + rightchild.attr_name.trim() + "= " + attr_name_positivevalue.get(rightchild.attr_name).get(0) + ": [" + rightchild.posi_count[0] + "+/" + rightchild.posi_count[1] + "-]");
			System.out.println("| " + rightchild.attr_name.trim() + "= " + attr_name_positivevalue.get(rightchild.attr_name).get(1) + ": [" + rightchild.nega_count[0] + "+/" + rightchild.nega_count[1] + "-]");
		}
		//minority, not major vote part
		double minority = 0;
		//find to the leaves if leftchild and rightchild exists
		//get the smaller number in its count[] array, add them all will be the minority
		if(root.posi_count[0] != 0 && root.posi_count[1] != 0){
			if(leftchild != null){
				minority += leftchild.posi_count[0] < leftchild.posi_count[1] ? leftchild.posi_count[0] : leftchild.posi_count[1];
				minority += leftchild.nega_count[0] < leftchild.nega_count[1] ? leftchild.nega_count[0] : leftchild.nega_count[1];
			}
			else{
				minority += root.posi_count[0] < root.posi_count[1] ? root.posi_count[0] : root.posi_count[1];
			}
		}
		if(root.nega_count[0] != 0 && root.nega_count[1] != 0){
			if(rightchild != null){
				minority += rightchild.posi_count[0] < rightchild.posi_count[1] ? rightchild.posi_count[0] : rightchild.posi_count[1];
				minority += rightchild.nega_count[0] < rightchild.nega_count[1] ? rightchild.nega_count[0] : rightchild.nega_count[1];
			}
			else{
				minority += root.nega_count[0] < root.nega_count[1] ? root.nega_count[0] : root.nega_count[1];
			}
		}
		System.out.println("error(train): " + minority/(class_count[0] + class_count[1]));
	}

	//method to test decision tree
	public static void testTree(String filename) throws IOException{
		//read file
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = br.readLine();
		int length = 0;
		String[] record = null;
		int minority = 0;
		//real result
		String result = "";
		//predict result(vote by majority)
		String predicresult = "";
		int totalline = 0;
		while((line = br.readLine())!=null){
			totalline += 1;
			record = line.split(",");
			length = record.length;
			//store real result as "+" for positive, "-" for negative
			if(record[length - 1].equals("yes") || record[length - 1].equals("A") || record[length - 1].equals("democrat")){
				result = "+";
			}
			else{
				result = "-";
			}
			for(int i = 0;i < length - 1;i++){
				//find the root attr value
				if(attr_name.get(i).equals(root.attr_name)){
					//go for root.posi_count
					if(record[i].equals(attr_name_positivevalue.get(root.attr_name).get(0))){
						//if leftchild exist
						if(leftchild != null){
							//find leftchild attr value
							for(int j = 0;j < length - 1;j++){
								if(attr_name.get(j).equals(leftchild.attr_name)){
									//go for leftchild.posi_count
									if(record[j].equals(attr_name_positivevalue.get(leftchild.attr_name).get(0))){
										//store predictresult as "+" for postive, "-" for negative
										if(leftchild.posi_count[0] > leftchild.posi_count[1]){
											predicresult = "+";
										}
										else{
											predicresult = "-";
										}
									}
									//go for leftchild.nega_count
									else{
										//store predictresult as "+" for postive, "-" for negative
										if(leftchild.nega_count[0] > leftchild.nega_count[1]){
											predicresult = "+";
										}
										else{
											predicresult = "-";
										}
									}
									break;
								}
							}
						}
						//no leftchild
						else{
							//store predictresult as "+" for postive, "-" for negative
							if(root.posi_count[0] > root.posi_count[1]){
								predicresult = "+";
							}
							else{
								predicresult = "-";
							}
						}
					}
					//go for root.nega_count
					else{
						//if rightchild exists
						if(rightchild != null){
							//find rightchild attr value
							for(int j = 0;j < length - 1;j++){
								if(attr_name.get(j).equals(rightchild.attr_name)){
									//go for rightchild.posi_count
									if(record[j].equals(attr_name_positivevalue.get(rightchild.attr_name).get(0))){
										//store predictresult as "+" for postive, "-" for negative
										if(rightchild.posi_count[0] > rightchild.posi_count[1]){
											predicresult = "+";
										}
										else{
											predicresult = "-";
										}
									}
									//go for rightchild.nega_count
									else{
										//store predictresult as "+" for postive, "-" for negative
										if(rightchild.nega_count[0] > rightchild.nega_count[1]){
											predicresult = "+";
										}
										else{
											predicresult = "-";
										}
									}
									break;
								}
							}
						}
						//no rightchild
						else{
							//store predictresult as "+" for postive, "-" for negative
							if(root.nega_count[0] > root.nega_count[1]){
								predicresult = "+";
							}
							else{
								predicresult = "-";
							}
						}
					}
					//if predict result not equal result, then it is one minority or one error
					if(!predicresult.equals(result)){
						minority += 1;
					}
					break;
				}
			}
			//reset
			result = "";
			predicresult = "";
		}
		br.close();
		System.out.println("error(test): " + (double)minority/totalline);
	}

	
}

//class of decision tree node
class TreeNode{
	//attr name
	String attr_name;
	//when attr is postive, count of class[+/-]
	int[] posi_count;
	//when attr is negative, count of class[+/-]
	int[] nega_count;
	//rightchild, leftchild
	TreeNode left;
	TreeNode right;
	public TreeNode(String attr_name, int[] posi_count, int[] nega_count){
		this.attr_name = attr_name;
		this.posi_count = posi_count;
		this.nega_count = nega_count;
		left = null;
		right = null;
	}
}