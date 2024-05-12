package chap01_pre;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 创建线程的几种方法
 * 1.继承Thread类
 * 2.实现Runnable接口
 * 3.使用Callable和Future创建线程 有返回值
 * 4.使用线程池创建线程
 *
 * 线程池的7大参数
 *   (int corePoolSize,//核心线程数
 *   int maximumPoolSize,//最大线程数
 *   long keepAliveTime,//存活时间
 *   TimeUnit unit,//存活时间单位
 *   BlockingQueue<Runnable> workQueue,//任务队列  有界无界
 *   RejectedExecutionHandler handler)//拒绝策略
 * @Author jys
 * @Date 2024-05-11 15:16
 */
public class ThreadDemo {
    public static void main(String[] args) {
        //线程池的7个参数

        ThreadPoolExecutor poolExecutor=new ThreadPoolExecutor(1,1,2, TimeUnit.SECONDS,new ArrayBlockingQueue<>(10));
    }
}
