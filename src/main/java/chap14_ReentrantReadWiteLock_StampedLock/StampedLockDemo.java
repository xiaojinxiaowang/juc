package chap14_ReentrantReadWiteLock_StampedLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * @Description: 邮戳锁（票据锁 版本锁）：比读写锁更快的锁-->解决锁饥饿问题；可重入读写锁的优化，戳记标签
 * 读的过程也允许写锁介入
 * 在获取乐观读锁后，需要对结果进行校验
 * 对短的只读代码段，使用乐观模式通常可以减少争用并提高吞吐量
 * 重点：是不可重入锁，如果一个线程已经持有了写锁，再去获取写锁就会造成死锁
 * 获取锁：返回邮戳 0表示失败，其余表示成功
 * 释放锁：必须和成功获取锁时邮戳一致
 * 三种模式：Reading 读模式悲观；  Writing 写模式；Optimistic reading 乐观读模式--无所机制，类似数据库乐观锁 支持读写并发，假如被修改再升级为悲观读模式
 * 传统模式：可以当传统读写锁
 * stampedLock.validate(stamp)  stampedLock.tryOptimisticRead(
 * 缺点：不支持重入 不支持条件变量 不要有中断操作 不要调用interrupt()方法
 * @Author jys
 * @Date 2024-05-10 14:15
 */
public class StampedLockDemo {
    static int NUMBER=728;
    static StampedLock stampedLock=new StampedLock();
    public void write(){
        //戳记
        long stamp = stampedLock.writeLock();
        System.out.println(Thread.currentThread().getName()+"\t"+"当前邮戳为："+stamp);
        System.out.println(Thread.currentThread().getName()+"\t"+"写线程准备修改");
        try {
            NUMBER=NUMBER+10;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            stampedLock.unlockWrite(stamp);
            System.out.println(Thread.currentThread().getName()+"\t"+"当前邮戳为："+stamp);
        }
        System.out.println(Thread.currentThread().getName()+"\t"+"写线程修改完成，将NUMBER修改为："+NUMBER);
    }
    //悲观读 传统 写未完成不能读
    public void read(){
        long stamp = stampedLock.readLock();
        System.out.println(Thread.currentThread().getName()+"\t"+"当前邮戳为："+stamp);
        System.out.println(Thread.currentThread().getName()+"\t"+"读线程准备就绪，4s后继续");
        for (int i = 0; i < 4; i++) {
            try{TimeUnit.MILLISECONDS.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
            System.out.println(Thread.currentThread().getName()+"\t"+"正在读取中...");
        }
        try{
            int result=NUMBER;
            System.out.println(Thread.currentThread().getName()+"\t"+"获得成员变量值："+result);
            System.out.println("写线程没有修改成功，读锁时候写锁无法介入，传统读写互斥");
        }finally {
            stampedLock.unlockRead(stamp);
            System.out.println(Thread.currentThread().getName()+"\t"+"当前邮戳为："+stamp);
        }
    }
    //乐观读 读的过程允许获取写锁介入
    public void tryOptimisticRead(){
        //乐观读
        long stamp = stampedLock.tryOptimisticRead();
        int result=NUMBER;
        //间隔四秒 乐观认为其他线程修改过NUMBER值
       ;
        System.out.println("4s前 stampedLock.validate()为：true无修改，false有修改"+"\t"+stampedLock.validate(stamp));
        for (int i = 0; i < 4; i++) {
            try{TimeUnit.MILLISECONDS.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
            System.out.println(Thread.currentThread().getName()+"\t"+"正在读取..."+i+
                    "秒后stampedLock.validate()为：true无修改，false有修改|||||"+"\t"+stampedLock.validate(stamp));
        }
        //重点
        if(!stampedLock.validate(stamp)){
            System.out.println("有写操作 被修改");
            stamp = stampedLock.readLock();
            try {
                System.out.println("从乐观读升级为悲观读");
                result=NUMBER;
                System.out.println("重新悲观读后result为："+result);
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        System.out.println(Thread.currentThread().getName()+"\t"+"最终值："+result);
    }

    public static void main(String[] args) {
        StampedLockDemo resource = new StampedLockDemo();
        //传统版
       /* new Thread(()->{
            resource.read();
        },"readThread").start();
        try{TimeUnit.MILLISECONDS.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"\t"+"come in...");
            resource.write();
        },"writeThread").start();*/
        new Thread(()->{
            resource.tryOptimisticRead();
        },"readThread").start();
        //暂停之后，读过程可以写介入
        //try{TimeUnit.MILLISECONDS.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
        //没有修改
        try{TimeUnit.SECONDS.sleep(6);} catch (InterruptedException e) {e.printStackTrace();}
        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"\t"+"come in...");
            resource.write();
        },"writeThread").start();
    }
}
