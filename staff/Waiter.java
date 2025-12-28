package staff;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import logger.Logger;

import pools.OrderPlacementQueue;
import pools.PreparedOrderQueue;

public class Waiter implements Runnable {
    private volatile static int ordersTaken=0;
    private volatile static int ordersServed=0;
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
        int currentOrder;
        while (true){
            lock.lock();

            try{
            if(ordersTaken>=Orders){
                break;
            }
            currentOrder = ordersTaken++;
          

        }
        finally{
            lock.unlock();
        }

       
             try{
            Thread.sleep(placementOrderTime);}
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
            

            
            
            long ts = System.currentTimeMillis();
            String logLine = String.format(
    "[%d] Waiter %d: Order Placed - Order %d",
    ts,
    this.id,
    currentOrder);
    Logger.log(logLine);
    orderQueue.putOrder();

        
            

        }

            


            
  

            

        
        int currentOrderService;
        while(true){
            
            lock.lock();
            try{
            if(ordersServed>=Orders){
                
                break;
            }
            currentOrderService = ordersServed++;
           
        }
        finally{
            lock.unlock();


        }

        
    preparedOrderQueue.getOrder();
    try{
            Thread.sleep(serveOrderTime);
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
        long ts = System.currentTimeMillis();
        String logLine = String.format(
    "[%d] Waiter %d: Order Served - Order %d",
    ts,
    this.id,
    currentOrderService);
    Logger.log(logLine);
    



            
           
            
        }
        System.out.println("Waiter Exits");
            
            
        
    }


    } 
    

    

