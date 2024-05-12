package chap05_LockSupportAndThreadInterrupt;

/**
 * @Description: 当前线程是否被中断 返回一个boolean并清楚中断状态 第二次调用中断状态已被清除 返回false
 * @Author jys
 * @Date 2024-05-11 17:11
 */
public class InterruptDemo4 {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName()+"\t"+Thread.interrupted());//false
        System.out.println(Thread.currentThread().getName()+"\t"+Thread.interrupted());//false
        System.out.println("----1");
        Thread.currentThread().interrupt();
        System.out.println("----2");
        System.out.println(Thread.currentThread().getName()+"\t"+Thread.interrupted());//true
        System.out.println(Thread.currentThread().getName()+"\t"+Thread.interrupted());//false
    }
}
