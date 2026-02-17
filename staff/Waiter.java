package staff;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import logger.Logger;

import pools.OrderPlacementQueue;
import pools.PreparedOrderQueue;

public class Waiter implements Runnable {
    private static int ordersTaken=0;
    private  static int ordersServed=0;
    static Lock lock = new ReentrantLock(false);
    OrderPlacementQueue orderQueue;
    PreparedOrderQueue preparedOrderQueue;
    // Total number of Orders
    private int Orders;
    private int id;
    private int placementOrderTime;
    private int serveOrderTime;





    public Waiter(int id, OrderPlacementQueue orderQueue, PreparedOrderQueue preparedQueue, int Orders, int placementOrderTime, int serveOrderTime){
        this.id = id;
        this.Orders = Orders;
        this.orderQueue = orderQueue;
        this.preparedOrderQueue = preparedQueue;
    }

    @Override
    public void run(){
        
        while (true){
            

       
             try{
            Thread.sleep(placementOrderTime);}
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
            

            
            
        
    

    lock.lock();
   

            try{
            if(ordersTaken>=Orders){
                break;
            }

             orderQueue.putOrder();
        long ts = System.currentTimeMillis();
            String logLine = String.format(
    "[%d] Waiter %d: Order Placed - Order %d",
    ts,
    this.id,
    ordersTaken);
    Logger.log(logLine);
            ordersTaken++;
          

        }
        finally{
            lock.unlock();
        }

        
            

        }

            


            
  

            

        
        
        while(true){
            
           
        
    
    try{
            Thread.sleep(serveOrderTime);
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
      

     lock.lock();
            try{
            if(ordersServed>=Orders){

                break;
            }
              long ts = System.currentTimeMillis();
    int currentOrderService = preparedOrderQueue.getOrder();
        String logLine = String.format(
    "[%d] Waiter %d: Order Served - Order %d",
    ts,
    this.id,
    currentOrderService);
    Logger.log(logLine);
            ordersServed++;
           
        }
        finally{
            lock.unlock();


        }

    



            
           
            
        }
    
            
            
        
    }


    } 
    

    
