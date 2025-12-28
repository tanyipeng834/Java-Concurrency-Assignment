package staff;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import logger.Logger;
import pools.OrderPlacementQueue;
import pools.PreparedOrderQueue;

public class Chef implements Runnable {
    
    private volatile static int ordersCooked=0;
    static Lock lock = new ReentrantLock(false);
    OrderPlacementQueue orderQueue;
    PreparedOrderQueue preparedOrderQueue;
    // Total number of Orders
    private int Orders;
    private int id;
    private int preparationOrderTime;





    public Chef(int id, OrderPlacementQueue orderQueue, PreparedOrderQueue preparedQueue, int Orders, int preparationOrderTime){
        this.id = id;
        this.Orders = Orders;

        this.orderQueue = orderQueue;
        this.preparedOrderQueue = preparedQueue;
        this.preparationOrderTime = preparationOrderTime;
    }

    @Override
    public void run(){
        
        while (true){
            // obtain the order id from the order queue but this may block infinitely 
            // if all orders have been served and 
            
            lock.lock();



            try{
            if(ordersCooked>=Orders){
                break;
            }
            ordersCooked++;
        }

        finally{
            lock.unlock();
        }
        // getOrder only if not all orders
        int orderId = orderQueue.getOrder();
        
            try{
            Thread.sleep(preparationOrderTime);}
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
             long ts = System.currentTimeMillis();
            String logLine = String.format(
    "[%d] Chef %d: Order Prepared - Order %d",
    ts,
    this.id,
    orderId);
    Logger.log(logLine);
    preparedOrderQueue.putOrder(orderId);
          

        }
            
            

            // do the put order outside lock cause if i hold the lock and i put into sleep cos it is full, it will cause deadlock 
            // other threads cannot acquire the lock.
            // place the order id so that the waiter would know which order this belongs to.
            
        System.out.println("Chef Exits");
        }
        
        


    } 
    

    

