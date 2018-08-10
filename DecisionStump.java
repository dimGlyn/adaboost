import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by dimitris on 27-Jan-17.
 */
public class DecisionStump {

    /**
     * arrays with the possible values of each attribute
     */
    private static String [] buying  = {"vhigh", "high", "med", "low"};
    private static String [] maint = {"vhigh", "high", "med", "low"};
    private static String [] doors = {"2","3","4","5more"};
    private static String [] persons = {"2","4","more"};
    private static String [] lug_boot = {"small","med","big"};
    private static String [] safety = {"low","med","high"};

    //ArrayList that contains the arrays of the possible values of each feature
    private static ArrayList<String []> features;

    //array with possible classes classes
    static String [] acceptabillity = {"unacc","acc","good","vgood"};

    //Feature with maxIG(can be between 0 and 5 0-for buying 1-for maint etc.)
    private int bestFeatId;

    //Values of the bestFeat
    private ArrayList<String> bestFeatValues = new ArrayList<>();

    /*
    Most frequent classifications of the feature with max IG
    e.g the feat with max ig is safety. bestFeatClass[0] will
    be the most frequent class of the examples that have safety=low
     */
    private String [] bestFeatClass ;

    /**
     * Class constructor
     *
     * @param bestFeatId
     * @param bestFeatClass
     */
    public DecisionStump(int bestFeatId,String[]bestFeatClass){
        setBestFeatId(bestFeatId);
        this.setBestFeatValues(bestFeatId);
        this.setBestFeatClass(bestFeatClass);
    }

    /**
     * Finds the best split feature(calculates the information gain
     * of all the features and chooses the max) and the most
     * frequent class of its possible values and
     * returns it as a DecisionStump object
     *
     *
     * @param data
     * @param weights weight of each example
     * @return best DecisionStump
     */
    public static DecisionStump bestStump(ArrayList<Car> data, double[]weights){

        features = new ArrayList<>();

        features.add(buying);
        features.add(maint);
        features.add(doors);
        features.add(persons);
        features.add(lug_boot);
        features.add(safety);

        double H = entropy(data,weights);

        double [] ig = new double[features.size()];

        double maxIG = 0;
        int featWithMaxIG = 0;

        for (int i = 0; i < features.size(); i++) {

            ig[i] = calculateIG(data,weights, features.get(i), i, H);
            if (ig[i]>maxIG) {
                maxIG = ig[i];
                featWithMaxIG = i;
            }
        }

        String [] bestClasses = findBestClasses(data,weights,featWithMaxIG,features);

        return new DecisionStump(featWithMaxIG,bestClasses);
    }

    /**
     * Calculates and returns the information gain
     * of a specific feature
     *
     * @param data
     * @param weights
     * @param f array of values of a specific feature
     * @param fid id of the feature
     * @param H entropy
     * @return
     */
    public static double calculateIG(ArrayList<Car>data, double[]weights, String [] f, int fid, double H){
        //information gain
        double ig = H;

        double p;

        for (int i = 0; i < f.length; i++) {

            double HC = entropyConditional(data,weights,f,i,fid);

            p=0;

            for (int j = 0; j < data.size(); j++) {

                if (data.get(j).getAttributes()[fid].equals(f[i])){
                    p+=weights[j];
                }

            }

            ig-=p*HC;

        }

        return ig;
    }

    /**
     * Calculate the sumweight of each class in each
     * value of the feature and returns the maximums as array
     *
     * @param data
     * @param weights
     * @param fid
     * @param features
     * @return
     */
    public static String [] findBestClasses(ArrayList<Car>data, double[]weights, int fid, ArrayList<String[]>features){

        String [] values = features.get(fid);

        //array that will be returned. Contains the best classes
        //for each value
        String [] bestClasses = new String[values.length];

        //convert array to arraylist so we are able to use indexOf
        ArrayList<String> Classes = new ArrayList<>(Arrays.asList(acceptabillity));

        for (int i = 0; i < values.length; i++) {   //iterate for each value of the given feature

            double sumWeights[] = {0,0,0,0};    //the total weights for each seperate class

            for (int j = 0; j < data.size(); j++) { //iterate data set

                if (data.get(j).getAttributes()[fid].equals(values[i])) {

                    //Adds the weight of the example that has
                    //the current value(in the iteration) to
                    //the sum of the correct class
                    int correctIndexOfClass = Classes.indexOf(data.get(j).getAcceptabillity());

                    sumWeights[correctIndexOfClass] += weights[j];

                }
            }
            //Finds the max value and the index
            //of it in the array with the weights
            //and assigns the correct class to
            //the array with the best classes
            int indexOfMaxClass = 0;
            double max = 0;
            for (int j = 0; j < sumWeights.length; j++) {
                if (sumWeights[j]>max){
                    max = sumWeights[j];
                    indexOfMaxClass = j;
                }
            }
            bestClasses[i] = Classes.get(indexOfMaxClass);
        }



        return bestClasses;
    }


    /**
     * Calculates the entropy of the data set
     * taking into consideration the weights of
     * each example
     *
     * @param data
     * @param weights
     * @return
     */
    public static double entropy(ArrayList<Car> data, double[]weights){
        //array with the possibilities of each class
        double [] p = new double[acceptabillity.length];
        //Entropy H
        double H = 0;

        //iterates p to find the possibility of each class
        for (int i = 0; i < p.length; i++) {

            p[i] = 0;

            //iterates the dataset and finds the sum of the weights
            //of the examples of the wanted class
            for (int j = 0; j < data.size(); j++) {

                if (data.get(j).getAcceptabillity().equals(acceptabillity[i]))
                    p[i]+=weights[j];

            }

            if(p[i]!=0) {
                H -= p[i] * (Math.log(p[i]) / Math.log(2));   //division with Math.log(2) to get its logarithm with base 2
            }
        }


        return H;
    }

    /**
     * Calculates the entropy knowing
     * that the given attribute f has a
     * specific value
     * @param data
     * @param weights
     * @param f
     * @param x index of the specific value in array f
     * @param fid id of the given attribute
     * @return
     */
    public static double entropyConditional(ArrayList<Car> data, double[]weights, String []f, int x, int fid){

        double H = 0;

        //array that will have the probability of finding
        //an example that has a class c knowing its
        //attribute is f[x]
        double [] p = new double[acceptabillity.length];

        //iterate for eaach class
        for (int i = 0; i < acceptabillity.length; i++) {

            double num = 0,denum = 0;

            //iterate data set
            for (int j = 0; j < data.size(); j++) {


                if (data.get(j).getAttributes()[fid].equals(f[x])){
                    denum+=weights[j];
                    if (data.get(j).getAcceptabillity().equals(acceptabillity[i]))
                        num+=weights[j];
                }

            }

            p[i] = num/denum;

            if(p[i]!=0&&denum!=0)
                H -= p[i] * (Math.log(p[i]) / Math.log(2));

        }
        return H;
    }

    /**
     * Classifies the example according to
     * the most frequent class of the attribute
     * with the biggest IG
     *
     * @param example
     * @return the chosen class
     */
    public String classify(Car example){

        return bestFeatClass[bestFeatValues.indexOf(example.getAttributes()[bestFeatId])];
    }

    public void setBestFeatId(int id){
        this.bestFeatId = id;
    }

    public void setBestFeatClass(String[] bestFeatClass) {
        this.bestFeatClass = bestFeatClass;
    }

    /**
     * Inputs the values of the best
     * attribute in the ArrayList.
     * (so its easier to classify
     * using the method indexOf)
     *
     * @param bestFeatId
     */
    public void setBestFeatValues(int bestFeatId) {

        switch (bestFeatId){
            case 0:
                for(String x:buying)
                    this.bestFeatValues.add(x);
                break;
            case 1:
                for(String x:maint)
                    this.bestFeatValues.add(x);
                break;
            case 2:
                for(String x:doors)
                    this.bestFeatValues.add(x);
                break;
            case 3:
                for(String x:persons)
                    this.bestFeatValues.add(x);
                break;
            case 4:
                for(String x:lug_boot)
                    this.bestFeatValues.add(x);
                break;
            case 5:
                for(String x:safety)
                    this.bestFeatValues.add(x);
                break;

        }
    }
}
