package pools;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class PreparedOrderQueue {

    private volatile int[] preparedOrderQueue;
    private volatile int orderCount=0;
    private volatile int queueHead=0;
    private volatile int queueCount=0;
    // Create a lock so that only a producer or consumer can access the bounded buffer at any single time.
    private final Lock lock = new ReentrantLock(false);
    Condition full = lock.newCondition();
    Condition empty = lock.newCondition();


    






    public PreparedOrderQueue(int size ){
        this.preparedOrderQueue = new int[size];
        
    }

    public void putOrder(){
        // Producer cannot add order if the queue is full
        // use the lock to access shared buffer and order count
        lock.lock();
        try{
        while(orderCount==this.preparedOrderQueue.length)
        {
            full.await();   
                }
        int avail = (queueHead + orderCount)% preparedOrderQueue.length;
        preparedOrderQueue[avail] = orderCount;
        orderCount ++;
        // The buffer is guranteed not to be empty and should signal to the ones that are waiting
        // on the empty condition.
        empty.signal();
        

    }
    catch(InterruptedException e){
        Thread.currentThread().interrupt();
    }
    finally{
        lock.unlock();
    }


    }

    public int getOrder(){
            // Waiter cannot serve if the order queue is empty
        int orderId;
        lock.lock();
        try{
        while(orderCount==0)
        {
            // 
            empty.await();
        }
        orderId = preparedOrderQueue[queueHead];
        queueHead = (queueHead +1) % preparedOrderQueue.length;
        orderCount --;
        // The buffer is guranteed not to be full and we can then signal for the 
        // threads waiting on the full condition ah
        full.signal();
        return orderId;

        

    }
    catch(InterruptedException e){
        Thread.currentThread().interrupt();
        // return -1 if there is an error with the thread
        return -1;
    }
    finally{
        lock.unlock();
    }
  
    }




    
}
