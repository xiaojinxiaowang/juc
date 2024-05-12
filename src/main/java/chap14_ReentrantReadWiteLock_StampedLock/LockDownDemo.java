package chap14_ReentrantReadWiteLock_StampedLock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Description: 锁降级Demo
 * 锁降级：遵循获取写锁，获取读锁在释放写锁的次序，写锁可以降级为读锁
 * 读写锁可以在写锁未释放时获取读锁，反之，写锁不可以在读锁未释放时获取，造成锁饥饿
 * @Author jys
 * @Date 2024-05-10 14:06
 */
public class LockDownDemo {
    public static void main(String[] args) {
        ReentrantReadWriteLock rwLock=new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock=rwLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock=rwLock.writeLock();
        //常规，写锁释放后读
/*        writeLock.lock();
        System.out.println("写入");
        writeLock.unlock();
        readLock.lock();
        System.out.println("读取");
        readLock.unlock();*/
        //写锁未释放依然可以读取
     /*   writeLock.lock();
        System.out.println("写入");
        readLock.lock();
        System.out.println("读取");
        readLock.unlock();
        writeLock.unlock();*/
        //读锁未释放，不能升级为写锁，程序阻塞
   /*     readLock.lock();
        System.out.println("读取");
        writeLock.lock();
        System.out.println("写入");
        readLock.unlock();
        writeLock.unlock();*/

    }
}
