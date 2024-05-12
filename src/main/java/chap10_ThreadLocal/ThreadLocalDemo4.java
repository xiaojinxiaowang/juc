package chap10_ThreadLocal;

/**
 * @Description: 源码解析
 * Thread ThreadLocal ThreadLocalMap三者的关系：
 * Thread里有ThreadLocal.ThreadLocalMap(ThreadLocal的静态内部类)属性
 * ThreadLocalMap里面有个静态内部类Entry继承弱引用
 * ThreadLocalMap实际上就是一个以threadLocal实例为key，任意对象为value的Entry对象
 * Thread自然人-ThreadLocal身份证-ThreadLocalMap身份证上的信息
 * @Author jys
 * @Date 2024-05-11 13:36
 */
public class ThreadLocalDemo4 {
    public static void main(String[] args) {
        Thread thread = new Thread();
    }
}
