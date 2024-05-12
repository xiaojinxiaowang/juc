package chap05_LockSupportAndThreadInterrupt;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Description: 线程中断 ：以下三个的区别
 * interrupt：中断此线程 实例方法 没有返回值 仅仅是设置线程的中断状态为true，发起一个协商而不会立刻停止线程
 * interrupted：i++测试当前线程是否已被中断 静态方法 判断线程是否被中断并清楚当前中断状态  返回当前线程的中断状态，测试当前线程是否已被中断  将当前线程的中断状态清零重新设置为false，清除线程的中断状态
 * isInterrupted：测试此线程是否已被中断 静态方法 判断当前线程是否被中断
 * 如何中断一个线程？interrupt  wait notify 等待唤醒机制
 * 中断标识为true 是不是线程就立刻停止了？
 * thread.interrupted()的理解
 * 如何停止中断运行中的线程？1.volatile可见性变量实现2.AtomicBoolean3.Thread类自带的中断api
 * 什么是中断机制？
 * 线程应该由自己决定何时停止  java中没有办法立即停止线程
 * 中断标识协商机制，中断过程程序员自己实现 true表示中断 false表示未中断
 * // 线程被中断时执行的操作，例如资源清理、记录日志等
 * 底层：interrupt0
 * @Author jys
 * @Date 2024-05-11 16:10
 */
public class InterruptDemo {
    static volatile boolean isStop=false;
    static AtomicBoolean atomicBoolean=new AtomicBoolean(false);
    public static void main(String[] args) {
        //interruptByInterrupt();
        //interruptByAtomicBoolean();
        //interruptByVolatile();
    }

    private static void interruptByInterrupt() {
        Thread t1=new Thread(()->{
            while (true){
                if(Thread.currentThread().isInterrupted()){
                    System.out.println(Thread.currentThread().getName()+"\tisInterrupted()被修改为true，程序停止"+"");
                    break;
                }
                System.out.println("isInterrupted()");
            }
        },"t1");
        t1.start();
        try{TimeUnit.MILLISECONDS.sleep(20);} catch (InterruptedException e) {e.printStackTrace();}
       /* new Thread(()->{
            t1.interrupt();
        },"t2").start();*/
        t1.interrupt();
    }

    //通过AtomicBoolean中断线程
    private static void interruptByAtomicBoolean() {
        //interruptByVolatile();
        new Thread(()->{
            while (true){
                if(atomicBoolean.get()){
                    System.out.println(Thread.currentThread().getName()+"\tatomicBoolean被修改为true，程序停止"+"");
                    break;
                }
                System.out.println("atomicBoolean");
            }
        },"t1").start();
        try{TimeUnit.MILLISECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
        new Thread(()->{
            atomicBoolean.set(true);
        },"t2").start();
    }

    //通过volatile变量中断线程
    private static void interruptByVolatile() {
        new Thread(()->{
            while (true){
                if(isStop){
                    System.out.println(Thread.currentThread().getName()+"\tisStop被修改为true，程序停止"+"");
                    break;
                }
                System.out.println("volatile");
            }
        },"t1").start();
        try{
            TimeUnit.MILLISECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
        new Thread(()->{
            isStop=true;
        },"t2").start();
    }
}
