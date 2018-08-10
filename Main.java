import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by dimitris on 20-Jan-17.
 */
public class Main {

    public static void main(String[]args){

        ArrayList<Car> cars = readFile("src\\carData.txt");


        ArrayList<Car> test = new ArrayList<>();

        for (int i = 0; i < 500; i++) {
            double x =Math.random()*cars.size();
            test.add(cars.remove((int)x));
        }


        ArrayList<String> classes = new ArrayList<String>(Arrays.asList(DecisionStump.acceptabillity));

        for(double p = 0; p <= 1; p+= 0.05) {

            //precision corect/selected
            //recall correct/total

            int []selected = {0,0,0,0};
            int []correct = {0,0,0,0};
            int []total = {0,0,0,0};


            ArrayList<Car> training = new ArrayList<>(cars.subList(0,(int)(cars.size()*p)));

            Adaboost boosting = Adaboost.train(training, 500);

            double succ = 0;

            for (int i = 0; i < test.size(); i++) {

                total[classes.indexOf(test.get(i).getAcceptabillity())]++;

                selected[classes.indexOf(boosting.classify(test.get(i)))]++;

                if (boosting.classify(test.get(i)).equals(test.get(i).getAcceptabillity())) {
                    correct[classes.indexOf(boosting.classify(test.get(i)))]++;
                    succ++;
                }

            }

            System.out.println(Math.round(p * 20)+1+" : "+succ / (double)test.size());

            for (int j = 0; j < 2; j++) {
                if (selected[j]!=0) {
                    System.out.println("Class: " + classes.get(j) + " - -recall:" +  (double)correct[j] / (double)total[j] + " precision:" + (double)correct[j] / (double)selected[j]);
                }else
                    System.out.println("Class: " + classes.get(j) + " - -recall:" + (double)correct[j] / (double)total[j] + " precision:" + 0);
            }

        }

    }

    public static ArrayList<Car> readFile(String path){

        File file = new File(path);
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(file));
        }catch (Exception e){
            System.out.println(e.toString());
        }

        ArrayList<Car> cars = new ArrayList<>();

        try{
            String line = reader.readLine();

            while(line!=null){
                String attr[] = line.split(",");
                cars.add(new Car(attr[0],attr[1],attr[2],attr[3],attr[4],attr[5],attr[6]));
                line = reader.readLine();

            }

        }catch (Exception e){
            System.out.println(e.toString());
        }


        return cars;
    }


}
