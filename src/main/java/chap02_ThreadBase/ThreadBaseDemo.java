package chap02_ThreadBase;

/**
 * @Description:
 * @Author jys
 * @Date 2024-05-11 15:14
 */
public class ThreadBaseDemo {
    public static void main(String[] args) {
        new Thread(()->{},"t1").start();//start0（）
    }
}
