package chap14_ReentrantReadWiteLock_StampedLock;
/**
 * @Description: ReentrantReadWriteLock读写锁demo
 * 一体两面，读写互斥，读写共享，读没有完成时其他线程写锁无法获得
 * @Author jys
 * @Date 2024-05-10 14:00
 */

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class MyResource{
    Map<String,String> map=new HashMap<>();
    Lock lock=new ReentrantLock();
    ReentrantReadWriteLock rwLock=new ReentrantReadWriteLock();
    public void read(String key){
        //lock.lock();
        rwLock.readLock().lock();
        try{
            System.out.println(Thread.currentThread().getName()+"\t"+"开始读取");
            String res = map.get(key);
            //try{TimeUnit.MILLISECONDS.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
            try{TimeUnit.MILLISECONDS.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
            System.out.println(Thread.currentThread().getName()+"\t"+"读取完成"+"\t"+"读取结果\t"+res);
        }finally {
            //lock.unlock();
            rwLock.readLock().unlock();
        }

    }
    public void write(String key,String value){
        //lock.lock();
        rwLock.writeLock().lock();
        try{
            System.out.println(Thread.currentThread().getName()+"\t"+"开始写入");
            map.put(key,value);
            try{TimeUnit.MILLISECONDS.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
            System.out.println(Thread.currentThread().getName()+"\t"+"写入完成");
        }finally {
            //lock.unlock();
            rwLock.writeLock().unlock();
        }

    }
}
public class ReentrantReadWriteLockDemo {
    public static void main(String[] args) {
        MyResource myResource = new MyResource();
        for (int i = 1; i <= 10; i++) {
            int finalI = i;
            new Thread(()->{
                myResource.write(finalI +"", finalI +"");
            },String.valueOf(i)).start();
        }
          for (int i = 1; i <= 10; i++) {
              int finalI = i;
              new Thread(()->{
                   myResource.read(finalI +"");
              },String.valueOf(i)).start();
          }
            try{TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
           for (int i = 1; i <= 3; i++) {
               int finalI = i;
               new Thread(()->{
                   myResource.write(finalI +"last", finalI +"last");
               },"新写锁线程"+i).start();
           }
    }
}
