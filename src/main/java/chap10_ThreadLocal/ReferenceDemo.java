package chap10_ThreadLocal;

import java.lang.ref.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description: ThreadLocal由于弱引用导致的内存泄露问题
 * ThreadLocal为啥要用弱引用：
 * 什么是内存泄露？为什么要用弱引用？不用如何
 * 内存泄漏定义：不在会被使用的对象或者变量占用的内存不能被回收，就是内存泄露---占着茅斯不拉屎
 * 谁惹的祸？ThreadLocalMap，1.包装成了弱引用对象 2.专门定义了Entry继承弱引用对象
 * 强软弱虚引用 finalize（）过时
 * 强引用：最常见的即强引用，new 出来的，就算出现了OOM也不会对该对象进行回收，可达，不可被回收，强引用造成内存泄露的主要原因
 * 软引用：内存充足时不回收，不够时回收---用于对内存敏感的程序中 如高速缓存
 * 弱引用：不管内存够不够用，只要gc，便会被回收
 * 虚引用：必须和引用队列联合使用--被干掉前放到引用队列中 比finalize更灵活  get方法总是返回null 处理监控通知使用--被回收时通知
 * @Author jys
 * @Date 2024-05-11 13:52
 */
class MyObject{
    //finalize（）方法过时
    @Override
    protected void finalize() throws Throwable {
       // super.finalize();
        System.out.println("----invoke finalize method");
    }
}
public class ReferenceDemo {
    public static void main(String[] args) {
        //softReference();
        //weakReference();
        MyObject myObject = new MyObject();
        ReferenceQueue<MyObject> queue=new ReferenceQueue<>();
        PhantomReference<MyObject> phantomReference = new PhantomReference<>(myObject, queue);
        System.out.println(phantomReference.get());//null
        //jvm内存设置为10m
        List<byte[]> list=new ArrayList<>();
        new Thread(()->{
            while (true){
                list.add(new byte[1*1024*1024]);//500ms加1m内存
                try{TimeUnit.MILLISECONDS.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
                System.out.println(phantomReference.get()+"\t"+"list add ok");
            }
        },"t1").start();
        new Thread(()->{
            System.out.println("invoke t2");
            while (true){
                Reference<? extends MyObject> reference = queue.poll();
                if(reference!=null){
                    System.out.println("有虚对象加入了队列");
                    break;
                }
            }
            System.out.println("end t2" );
        },"t2").start();
    }

    /**
     * 弱引用：高速缓存  读取图片
     */
    private static void weakReference() {
        WeakReference<MyObject> weakReference = new WeakReference<>(new MyObject());
        System.out.println("gc before:"+weakReference.get());
        System.gc();
        try{TimeUnit.MILLISECONDS.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
        System.out.println("gc after:"+weakReference.get());
        //gc before:chap10_ThreadLocal.MyObject@6d311334
        //----invoke finalize method
        //gc after:null
    }

    /**
     * 软引用
     */
    private static void softReference() {
        //strongReference();  //ctrl+alt+m移出代码块
        SoftReference<MyObject> softReference = new SoftReference<>(new MyObject());
        //System.out.println("softReference = " + softReference.get());
        System.gc();
        try{TimeUnit.MILLISECONDS.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        System.out.println("gc after内存够用:"+softReference.get());

        //配置jvm内存： run-edit configuration---Modify options--add vm option（-Xms10m -Xmx10m）--配置内存为10m
        try {
            byte[] bytes = new byte[20 * 1024 * 1024];//20m对象
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("gc after内存不够:"+softReference.get());//null
        }

        //gc after内存够用:chap10_ThreadLocal.MyObject@6d311334
        //----invoke finalize method
        //gc after内存不够:null
        //Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
        //	at chap10_ThreadLocal.ReferenceDemo.main(ReferenceDemo.java:36)
    }

    /**
     * 强引用
     */
    private static void strongReference() {
        //强引用
        MyObject myObject = new MyObject();
        System.out.println("gc before:"+myObject);
        //指向空
        myObject=null;
        System.gc();//人工开启gc 一般不用
        System.out.println("gc after:"+myObject);
        //gc before:chap10_ThreadLocal.MyObject@6d311334
        //----invoke finalize method
        //gc after:null
    }
}
