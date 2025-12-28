import java.io.File;
import java.io.FileNotFoundException;

import java.util.Scanner;

import pools.OrderPlacementQueue;
import pools.PreparedOrderQueue;
import staff.Chef;
import staff.Waiter;
public class Resturant{
    static OrderPlacementQueue orderPlacementQueue;
    static PreparedOrderQueue preparedOrderQueue;

    
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
        
        // This is to create the thread safe queue for both the order placement and prepared order queue.
        orderPlacementQueue = new OrderPlacementQueue(inputConfig.orderQueueSize);
        preparedOrderQueue= new PreparedOrderQueue(inputConfig.serveQueueSize);

        createWaiters(inputConfig.Waiters, inputConfig.placementOrderTime, inputConfig.preparationOrderTime, inputConfig.serveOrderTime, inputConfig.Orders);
        createChefs(inputConfig.Chefs,inputConfig.preparationOrderTime,inputConfig.Orders);




  

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

static void createWaiters(int noOfWaiters, int placementOrderTime, int preparationOrderTime, int serveOrderTime,int orders){
    // Create pool of waiters to execute tasks of order placement and serving
    for (int i =0;i<noOfWaiters;i++){
        Thread  waiterThread = new Thread(new Waiter(i,Resturant.orderPlacementQueue,Resturant.preparedOrderQueue,orders,placementOrderTime,serveOrderTime));
        waiterThread.start();
       
    }
    
    

}
static void createChefs(int noOfChefs,int preparationOrderTime,int Orders)
{
    for (int i =0;i<noOfChefs;i++){
        Thread chefThread = new Thread(new Chef(i, orderPlacementQueue, preparedOrderQueue, Orders,preparationOrderTime));
        chefThread.start();
        
    }
   

}



}


