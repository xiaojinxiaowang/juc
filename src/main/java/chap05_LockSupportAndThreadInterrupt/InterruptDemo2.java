package chap05_LockSupportAndThreadInterrupt;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 实例方法interrupt（）仅仅是设置线程的中断状态位置为true 不会停止线程
 * @Author jys
 * @Date 2024-05-11 16:41
 */
public class InterruptDemo2 {
    public static void main(String[] args) {
        Thread t1=new Thread(()->{
            for (int i = 0; i < 300; i++) {
                System.out.println("----"+i);
            }
            System.out.println("中断标识02："+Thread.currentThread().isInterrupted());//true
        },"t1");
        t1.start();
        System.out.println("中断标识00："+t1.isInterrupted());//false
        try{TimeUnit.MILLISECONDS.sleep(2);} catch (InterruptedException e) {e.printStackTrace();}
        t1.interrupt();
        System.out.println("中断标识01："+t1.isInterrupted());//true
        try{TimeUnit.MILLISECONDS.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
        System.out.println("中断标识03："+t1.isInterrupted());//true//todo：jdk17和jdk8是不是有区别？ from：jdk14  jdk8为false
    }
}
