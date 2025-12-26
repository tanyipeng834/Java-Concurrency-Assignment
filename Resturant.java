import java.io.File;
import java.io.FileNotFoundException;

import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
public class Resturant{

    static BlockingQueue<Integer> orderPlacementQueue;
    static BlockingQueue<Integer> preparedOrderQueue;
    static ThreadPoolExecutor chefPool;
    static ThreadPoolExecutor waiterPool;
    static int currentOrderNo;


   
    




    static final class Config{
        final int Chefs;
        final int Waiters;
        final int Orders;
        final int placementOrderTime;
        final int preparationOrderTime;
        final int serveOrderTime;
        final int orderQueueSize;
        final int serveQueueSize;

        Config(int Chefs, int Waiters, int Orders, int placementOrderTime, int preparationOrderTime,int serveOrderTime, int orderQueueSize, int serveQueueSize){
            this.Chefs = Chefs;
            this.Waiters=Waiters;
            this.Orders= Orders;
            
            this.placementOrderTime = placementOrderTime;
            this.preparationOrderTime = preparationOrderTime;
            this.serveOrderTime = serveOrderTime;
            this.orderQueueSize = orderQueueSize;
            this.serveQueueSize = serveQueueSize;

        }
       @Override
public String toString() {
    return "Config{" +
            "Chefs=" + Chefs +
            ", Waiters=" + Waiters +
            ", Orders=" + Orders +
            ", placementOrderTime=" + placementOrderTime +
            ", preparationOrderTime=" + preparationOrderTime +
            ", serveOrderTime=" + serveOrderTime +
            ", orderQueueSize=" + orderQueueSize +
            ", serveQueueSize=" + serveQueueSize +
            '}';
}



    }

    public static void main(String[] args) {
        Config inputConfig = readUserInput("config.txt");
        
        orderPlacementQueue = new ArrayBlockingQueue<>(inputConfig.orderQueueSize);
        preparedOrderQueue = new ArrayBlockingQueue<>(inputConfig.serveQueueSize);



  

}

static Config readUserInput(String fileName){
    
    // Create an static array for the config object values
    int[] configValues = new int[8];
    File userInputFile = new File(fileName);
    
    try(Scanner fileReader = new Scanner(userInputFile)){
        int i =0;
        while(fileReader.hasNextLine()){
            String line = fileReader.nextLine();
            // Remove comment
            int hash = line.indexOf("#");
            if (hash>=0){
                line = line.substring(0,hash);
            }
        line = line.trim();
        if (line.isEmpty()){
            System.err.println("Error: Empty Line is not allowed ");
            System.exit(1);

        }
        int option;

        try{
            
            option = Integer.parseInt(line);
            configValues[i] = option;
            i++;

            System.out.println(option);
            if (option<0) throw new NumberFormatException();
        }
        catch (NumberFormatException e ){
            System.err.println("Error: Not a valid Integer");
            System.exit(1);
        }

        }
       

    }
    catch(FileNotFoundException e ){
        System.out.println("File is not Config.txt");
    }


    return new Config(configValues[0], configValues[1], configValues[2], configValues[3], configValues[4], configValues[5],configValues[6], configValues[7]);


    




}

static void createWaiters(int noOfWaiters){
    // Create pool of waiters to execute tasks of order placement and serving
    waiterPool = new ThreadPoolExecutor(noOfWaiters,noOfWaiters,0L,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>());
    

}
static void createChefs(int noOfChefs)
{
    // Create tasks of chef pool which 
    chefPool = new ThreadPoolExecutor(noOfChefs, noOfChefs, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

}



}


