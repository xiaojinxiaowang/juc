package chap10_ThreadLocal;

/**
 * @Description: 强引用不能被回收，造成内存泄露--人死灯灭
 * 弱引用之后不是万事大吉，还存在脏Entry key为null value一直存在一条引用链 （线程复用）--手动调用remove（）来删除--Entry中的value置为null
 * 实现数据隔离不在于ThreadLocal 而在于ThreadLocalMap
 * 最佳实践：
 * threadLocal
 * 修饰为static
 *
 * 总结：
 * ThreadLocal并不解决线程间共享数据的问题
 * 适用于变量在线程间隔离其在方法见共享的场景
 * 通过隐式的不同线程内创建独立势力副本避免了实例下线程安全问题
 * 每个线程持有一个只属于自己的专属Map并维护了ThreadLocal对象与具体实例的映射 该Map由于制备持有它的线程访问 不存在线程安全及锁的问题
 * ThreadLocalMap的Entry为弱引用 避免对象无法被回收的问题
 * 手动remove 剞劂脏Entry问题
 *
 * @Author jys
 * @Date 2024-05-11 14:47
 */
public class WeakReferenceDemo {
    static ThreadLocal threadLocal=null;
    ThreadLocal<Integer> sale0=ThreadLocal.withInitial(()->0);

    public static void main(String[] args) {
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set("jys");
        threadLocal.get();
    }
    public void test(){
       sale0.remove();
    }
}
