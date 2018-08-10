import java.util.ArrayList;
import java.util.List;

/**
 * Created by dimitris on 27-Jan-17.
 */
public class Adaboost {

    private List<DecisionStump> model;
    private ArrayList<Double> assumptionWeights;

    public Adaboost(List<DecisionStump> model, ArrayList<Double>assumWeights){
        this.model = model;
        this.assumptionWeights = assumWeights;
    }

    /**
     * Adaboost training algorithm
     *
     * @param data
     * @param m number of iterations
     * @return  new  Adaboost object with the correct model
     */
    public static Adaboost train(ArrayList<Car> data,int m){

        int n = data.size();
        //initializes the weights, all are 1/n
        double weights[] = new double[n];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = (double)1/n;

        }
        List<DecisionStump> model = new ArrayList<>();
        DecisionStump stump;
        double error;
        ArrayList<Double> assumptionWeights = new ArrayList<>();
        String []labels = getLabels(data);
        for (int i = 0; i < m; i++) {
            stump = DecisionStump.bestStump(data,weights);
            model.add(stump);
            error = calculateError(stump, data, weights, labels);
            weights = updateWeights(data,labels,stump,weights,error);
            assumptionWeights.add(Math.log((1 - error) / error));
        }

        return new Adaboost(model,assumptionWeights);
    }

    /**
     * Classifies the example according to the
     * weighted majority of the decision stumps
     *
     * @param x
     * @return best category for the given example
     */
    public String classify(Car x){
        double []votes = {0,0,0,0};

        for (int i = 0; i < model.size(); i++) {
            String classification = model.get(i).classify(x);
            switch (classification){
                case "unacc":
                    votes[0]+=assumptionWeights.get(i);
                    break;
                case "acc":
                    votes[1]+=assumptionWeights.get(i);
                    break;
                case "good":
                    votes[2]+=assumptionWeights.get(i);
                    break;
                case "vgood":
                    votes[3]+=assumptionWeights.get(i);
                    break;
            }
        }

        int indexOfMax = 0;
        String majorityVote = "unacc";
        if(votes[1]>votes[indexOfMax]) {
            indexOfMax = 1;
            majorityVote = "acc";
        }
        if (votes[2]>votes[indexOfMax]){
            indexOfMax = 2;
            majorityVote = "good";
        }
        if (votes[3]>votes[indexOfMax]){
            majorityVote = "vgood";
        }

        return majorityVote;
    }

    /**
     * Returns the weighted error of the given decision stump
     *
     * @param stump
     * @param cars
     * @param weights
     * @param labels
     * @return
     */
    public static double calculateError(DecisionStump stump, ArrayList<Car> cars, double[] weights, String[] labels) {
        double error = 0;

        try {
            for (int i = 0; i < cars.size(); i++) {
                if (!stump.classify(cars.get(i)).equals(labels[i]))
                    error += weights[i];
            }
        }catch (NullPointerException npe){
            npe.printStackTrace();
        }
        return error;
    }
    
    /**
     * Updates the weights
     *
     * @param cars
     * @param labels
     * @param stump
     * @param weights
     * @param error
     * @return updated array with weights(after normalization)
     */
    public static double[]updateWeights(ArrayList<Car> cars, String[]labels,DecisionStump stump,
                                 double[]weights, double error){
        try {
            for (int i = 0; i < cars.size(); i++) {
                if (stump.classify(cars.get(i)).equals(labels[i]))
                    weights[i] = (weights[i] * error) / (1 - error);
            }
        }catch (NullPointerException npe){
            npe.printStackTrace();
        }
        return normalizeWeights(weights);
    }

    /**
     * Normalizing weights by dividing each one by their total sum
     *
     * @param weights
     * @return
     */
    public static double [] normalizeWeights(double[]weights){
        double total = 0;

        for (int i = 0; i < weights.length; i++)
            total += weights[i];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = weights[i]/total;
        }

        return weights;
    }

    /**
     * Gets the classes of all the examples
     *
     * @param data
     * @return array with each examples class
     */
    public static String[]getLabels(ArrayList<Car> data){
        String []labels = new String[data.size()];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = data.get(i).getAcceptabillity();
        }
        return labels;
    }

}
