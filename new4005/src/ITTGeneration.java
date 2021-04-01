import java.util.*;

public class ITTGeneration {

    private double[] decimalArray;
    private Random rand;
    private double lambda;
    private int arraySize;

    public ITTGeneration(int seed, double userLambda, int userArraySize){

        lambda = userLambda;
        arraySize = userArraySize;

        //generate new rand based on seed
        if(seed == 0){
            rand = new Random();
        }else{
            rand = new Random(seed);
        }

        //init array of numbers from 0 to 1
        decimalArray = new double[arraySize];

        for (int i = 0; i < arraySize; i++){
            decimalArray[i] = rand.nextDouble();
        }

        //transform each element using ITT
        for (int i = 0; i < arraySize; i++){
            decimalArray[i] = (-1 / lambda) * Math.log(1 - decimalArray[i]);
        }
    }

    public double[] getDecArray(){
        return decimalArray;
    }

    public void printArray(){
        String result = "";
        for(double d:decimalArray){
            result += d + " ";
        }
        System.out.println(result);
    }
}
