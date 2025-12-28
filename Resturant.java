import java.io.File;
import java.io.FileNotFoundException;

import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
public class Resturant{

    static BlockingQueue<Integer> orderPlacementQueue;
    static BlockingQueue<Integer> preparedOrderQueue;
    static final int POISON =-1;
   
    static volatile int currentOrderNo;
    static volatile int currentServeNo;
    static volatile int currentCookNo;
    // LOCK FOR Shared object currentOrderNo
    static final Object ORDER_LOCK = new Object();
    // Lock for Shared object currentServeNo
    static final Object SERVE_LOCK = new Object();
    static final Object COOK_LOCK = new Object();
    static void prepareOrder(int preparationOrderTime, int chefId){
            try{
                int orderId = orderPlacementQueue.take();
                Thread.sleep(preparationOrderTime);

                preparedOrderQueue.put(orderId);
                 System.out.printf("Chef %d Order Prepared- Order %d\n",chefId,orderId);

                 synchronized(COOK_LOCK){
                    currentCookNo++;
                 }
                


            }

            catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }

        }
    static void takeOrder(int placementOrderTime, int waiterId){
        int orderId;
            try{
                Thread.sleep(placementOrderTime);

            }

            catch(InterruptedException e){
                
                Thread.currentThread().interrupt();

            }
            try{
                
                synchronized(ORDER_LOCK){
                    
                   orderId= currentOrderNo ++;

                }

                    orderPlacementQueue.put(orderId);
                    
                    System.out.printf("Waiter %d Order Placed - Order %d\n",waiterId,orderId);
            
            
           
            
            }
            catch(InterruptedException e){
                Thread.currentThread().interrupt();

            }
        }
         static void serveOrder(int serveOrderTime, int waiterId){
            try{
                int orderId = preparedOrderQueue.take();
                System.out.printf("Waiter %d Order Served - Order %d\n",waiterId,orderId);
                
                // Time taken to serve to the customer
                Thread.sleep(serveOrderTime);
                synchronized(SERVE_LOCK){

                currentServeNo ++;
                }


            }

            catch(InterruptedException e){
                
                Thread.currentThread().interrupt();

            }
         
      


        }


    static class ChefTask implements Runnable{
        private final int chefId;
        private final int preparationOrderTime;
        private BlockingQueue<Integer> preparedOrderQueue;
        private final int Orders;

        ChefTask( int chefId, int preparationOrderTime, int Orders)
        {
            this.chefId = chefId;
            this.preparationOrderTime = preparationOrderTime;
            this.preparedOrderQueue = Resturant.preparedOrderQueue;
            this.Orders = Orders;


        }

       
        public void run(){
            // given that there is nothing to prepare, it will just sleep and then wake up when signalled when there is something put in the queue.
           
          
            
            while (true){
                synchronized(COOK_LOCK){
                    if(currentCookNo>=this.Orders)break;
                }

              
            Resturant.prepareOrder(preparationOrderTime,this.chefId);
          
            
                
        }
        }




    }

    static class WaiterTask implements Runnable{
        private final int waiterId;
        private final int placementOrderTime;
        private final int serveOrderTime;
        private BlockingQueue<Integer> orderPlacementQueue;
        private BlockingQueue<Integer> preparedOrderQueue;
        private final int Orders;
        
        WaiterTask(int waiterId,int placementOrderTime,int serveOrderTime,int Orders){
            this.waiterId = waiterId;
            this.placementOrderTime = placementOrderTime;
            this.serveOrderTime = serveOrderTime;
            this.preparedOrderQueue = Resturant.preparedOrderQueue;
            this.orderPlacementQueue = Resturant.orderPlacementQueue;
            this.Orders = Orders;

        }

        // Only one waiter can take the order at any given time

         
            
           



        
        //
       

        @Override
        public void run()
        {
            // only terminate when all orders have been served, check condition variable when thread wakes up
            


             while (true){

                while(true){
                     synchronized (ORDER_LOCK) {
                            if (currentOrderNo >= Orders) {
                                break;}
                        Resturant.takeOrder(placementOrderTime,waiterId); // or inline the increment here
                    }
                }
              


               synchronized (SERVE_LOCK) {
            if (currentServeNo >= this.Orders) break;
                                                        }
               
                Resturant.serveOrder(serveOrderTime,waiterId);

               
               // break from the loop and exit the thread.
               
                                                    }
                

           
                   
                

           
      
          
                    
                    

            
           
            
           



        }

    }


   
    




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
        // Runnable task
        Thread waiter = new Thread(new WaiterTask(i,placementOrderTime,serveOrderTime,orders));
        waiter.start();
    }
    
    

}
static void createChefs(int noOfChefs,int preparationOrderTime,int Orders)
{
    for (int i =0;i<noOfChefs;i++){
        // Runnable task
        Thread chef = new Thread(new ChefTask(i, preparationOrderTime, Orders));
        chef.start();
    }
   

}



}


