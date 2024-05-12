package chap05_LockSupportAndThreadInterrupt;

import java.util.concurrent.TimeUnit;

/**
 * @Description: 睡觉被叫醒继续睡
 * @Author jys
 * @Date 2024-05-11 17:02
 */
public class InterruptDemo3 {
    public static void main(String[] args) {
        Thread t1=new Thread(()->{
            while (true){
                if(Thread.currentThread().isInterrupted()){
                    System.out.println(Thread.currentThread().getName()+"\t中断标识："+Thread.currentThread().isInterrupted()+"\t程序停止");
                    break;
                }
                try {
                    Thread.sleep(200);//模拟业务
                } catch (InterruptedException e) {
                    //为啥要 再停止一次 停止程序，否则会死循环
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
                System.out.println("demo03");
            }
        },"t1");
        t1.start();
        try{
            TimeUnit.MILLISECONDS.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        new Thread(()->{
            t1.interrupt();
        },"t2").start();
    }
}
