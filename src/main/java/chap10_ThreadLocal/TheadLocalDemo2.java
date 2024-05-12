package chap10_ThreadLocal;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 每个线程都有自己专属的本地变量副本
 * 需求：每个销售量独立，分灶吃饭
 * 存在隐患，内存大小是有限的，如果不remove()处理，可能会导致系统逻辑紊乱和内存泄露
 * @Author jys
 * @Date 2024-05-11 10:51
 */
//资源类
class House2{
    int saleCount=0;
    public synchronized void saleHouse(){
        ++saleCount;
    }
    //内部是匿名内部类 现在不这么些 用withInitial
    /*ThreadLocal<Integer> saleSize=new ThreadLocal<>(){
        //初始化为0
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };*/
    //与上面完全一样 since java8
    ThreadLocal<Integer> sale0=ThreadLocal.withInitial(()->0);
    public void sale0ByThreadLocal(){
        sale0.set(1+sale0.get());
    }
}
public class TheadLocalDemo2 {
    public static void main(String[] args) {
        House2 house2 = new House2();
        for (int i = 1; i <= 5; i++) {
             new Thread(()->{
                 int size=new Random().nextInt(5)+1;
                 try {
                     for (int j = 0; j < size; j++) {
                         house2.saleHouse();
                         house2.sale0ByThreadLocal();
                     }
                     System.out.println(Thread.currentThread().getName()+"\t"+"号销售卖出\t"+house2.sale0.get()+"套");
                 } finally {
                     /**
                      * @Description: 阿里巴巴手册对ThreadLocal使用的要求
                      * 1.必须回收自定义的ThreadLocal变量，尤其是在线程池尝尽修改，线程经常会被复用，如果不清理自定义的ThreadLocal变量，
                      * 可能会影响后续业务逻辑和造成内存泄露等问题，尽量在代理中使用try-finally块进行回收
                      * 调用remove()避免内存泄露
                      * @Author jys
                      * @Date 2024-05-11 11:16
                      */
                     house2.sale0.remove();
                 }
             },String.valueOf(i)).start();
         }
        try{
            TimeUnit.MILLISECONDS.sleep(300);} catch (InterruptedException e) {e.printStackTrace();}
        System.out.println("共卖出\t"+house2.saleCount+"\t套");
    }
}
