import java.io.*;
import java.util.*;
//refered the idea from "http://fantasticinblur.iteye.com/blog/1465497" 
//for construct the matrix and do calculation and adjust weight
public class NN_education{
	private double[] inputLayer = new double[5];
	private double[] hiddenLayer = new double[6];
	private double[] hiddenDelta = new double[6];
	private double[][] inputhiddenWeight = new double[5][6];
	private double[][] inputhiddenPreWeight = new double[5][6];
	private double[] hiddenoutputWeight = new double[6];
	private double[] hiddenoutputPreWeight = new double[6];
	private Random rand = new Random();
	private double learningRate = 0.25;
	private double momentum = 0.9;
	private double sumError = 10000000.0;
	private double outputLayer = 0;
	private double targetValue = 0;
	private double outputDelta = 0;

	public NN_education(){
		for(int i = 0;i < inputhiddenWeight.length;i++){
			for(int j = 0;j < inputhiddenWeight[0].length;j++){
				inputhiddenWeight[i][j] = rand.nextDouble() - 0.5;
			}
		}
		for(int i = 0;i < hiddenoutputWeight.length;i++){
				hiddenoutputWeight[i] = rand.nextDouble()- 0.5;
		}	
	}

	public void feedforward(){
		for(int i = 0;i < hiddenLayer.length;i++){
			double sum = 0;
			for(int j = 0;j < inputLayer.length;j++){
				sum += inputLayer[j] * inputhiddenWeight[j][i];
			}
			hiddenLayer[i] = 1.0 / (1.0 + Math.exp(-sum));
		}
		double sum = 0;
		for(int j = 0;j < hiddenLayer.length;j++){
			sum += hiddenLayer[j] * hiddenoutputWeight[j];
		}
		outputLayer = 1.0 / (1.0 + Math.exp(-sum));
	}

	public void calculateError(){
		outputDelta = (targetValue - outputLayer) * outputLayer * (1.0 - outputLayer);
		sumError += (targetValue - outputLayer) * (targetValue - outputLayer);
		for(int i = 0;i < hiddenDelta.length;i++){ 
			hiddenDelta[i] = hiddenLayer[i] * (1.0 - hiddenLayer[i]) * hiddenoutputWeight[i] * outputDelta;
		}
	}

	public void adjustWeight(){
		for(int j = 0;j < hiddenLayer.length;j++){
			double adjustweight = momentum * hiddenoutputPreWeight[j] + learningRate * outputDelta * hiddenLayer[j];
			hiddenoutputWeight[j] += adjustweight;
			hiddenoutputPreWeight[j] = adjustweight;
		}
		for(int i = 0;i < hiddenDelta.length;i++){
			for(int j = 0;j < inputLayer.length;j++){
				double adjustweight = momentum * inputhiddenPreWeight[j][i] + learningRate * hiddenDelta[i] * inputLayer[j];
				inputhiddenWeight[j][i] += adjustweight;
				inputhiddenPreWeight[j][i] = adjustweight;
			}
		}
	}

	public static void main(String[] args) throws IOException{
		NN_education nnm = new NN_education();
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		br.readLine();
		String newline = "";
		String[] variables = null;
		ArrayList<double[]> data = new ArrayList<>();
		while((newline = br.readLine())!=null){
			variables = newline.split(",");
			int i = 0;
			double[] record = new double[6];
			for(String each: variables){
				record[i] = Double.parseDouble(each)/100;
				i += 1;
			}
			data.add(record);
		}
		br.close();

		double minError = nnm.sumError;
		while(nnm.sumError > 0.3){
			//set sumError to be 0 each loop
			nnm.sumError = 0;
			//get sumError for all data
			for(int i = 0;i < data.size();i++){
				double[] record = data.get(i);
				nnm.inputLayer = new double[]{record[0],record[1],record[2],record[3],record[4]};
				nnm.targetValue = record[5];
				nnm.feedforward();
				nnm.calculateError();
				nnm.adjustWeight();
			}
			//if decreasing then continue loop
			if(nnm.sumError <= minError){
				System.out.println(nnm.sumError);
				minError = nnm.sumError;
			}
			//if increasing then stop, find the local or global minimum
			else{
				break;
			}
		}
		System.out.println("TRAINING COMPLETED! NOW PREDICTING.");
		br = new BufferedReader(new FileReader(args[1]));
		br.readLine();
		while((newline = br.readLine())!=null){
			variables = newline.split(",");
			int i = 0;
			double[] record = new double[5];
			for(String each: variables){
				record[i] = Double.parseDouble(each)/100;
				i += 1;
			}
			nnm.inputLayer = record;
			nnm.feedforward();
			System.out.println(nnm.outputLayer*100);
		}
		br.close();
	}
}