package chap10_ThreadLocal;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description: 阿里巴巴手册对ThreadLocal使用的要求
 * 1.必须回收自定义的ThreadLocal变量，尤其是在线程池尝尽修改，线程经常会被复用，如果不清理自定义的ThreadLocal变量，
 * 可能会影响后续业务逻辑和造成内存泄露等问题，尽量在代理中使用try-finally块进行回收
 * 调用remove()避免内存泄露
 *
 * 总结：每个thread内有自己的实例副本且该副本只有当前线程自己使用 其他thread不可访问 不存在多线程见共享问题
 * 统一设置初始值 每个线程对这个值的修改都是各自线程相互独立的
 * 加入synchronized或者lock控制资源的访问顺序  人手一份 不用抢
 * @Author jys
 * @Date 2024-05-11 11:16
 */
//资源类
class MyData{
    ThreadLocal<Integer> threadLocal=ThreadLocal.withInitial(()->0);
    public void add(){
        threadLocal.set(1+threadLocal.get());
    }
}
public class ThreadLocalDemo3 {
    public static void main(String[] args) {
        MyData myData = new MyData();
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        try {
            //10个请求打进线程池
            for (int i = 0; i < 10; i++) {
                threadPool.submit(()->{
                    try {
                        Integer before = myData.threadLocal.get();
                        myData.add();
                        Integer after = myData.threadLocal.get();
                        System.out.println(Thread.currentThread().getName()+"\t"+"before:"+before+"\t"+"after:"+after);
                    } finally {
                        myData.threadLocal.remove();
                    }
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }
    }
}
