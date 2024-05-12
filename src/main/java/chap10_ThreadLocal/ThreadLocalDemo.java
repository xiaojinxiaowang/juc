package chap10_ThreadLocal;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 群雄逐鹿起纷争  加锁
 * 需求：5个销售卖房子，集团只关心销售总量的统计数
 * @Author jys
 * @Date 2024-05-11 10:41
 */
//资源类
class House{
    int saleCount=0;
    public synchronized void saleHouse(){
        ++saleCount;
    }
}
public class ThreadLocalDemo {
    public static void main(String[] args) {
        House house = new House();
        for (int i = 1; i <= 5; i++) {
             new Thread(()->{
                 int size= new Random().nextInt(5)+1;
                 System.out.println(size);
                 for (int j = 0; j < size; j++) {
                     house.saleHouse();
                 }
             },String.valueOf(i)).start();
        }
        try{TimeUnit.MILLISECONDS.sleep(300);} catch (InterruptedException e) {e.printStackTrace();}
        System.out.println(Thread.currentThread().getName()+"\t"+"共计卖出\t"+house.saleCount+"\t套");

    }
}
