/**
 * @author fengyu .
 * @Date 2017-12-31 18:42
 */
public class Worker implements Runnable {
    
    @Override
    public void run() {
     for(int i=0;i<1000;i++) {
         doWork();
     }      
    }
    private static void doWork() {}
    public static void main(String[] args) {
        Worker worker = new Worker();
        new Thread(worker).start();
        new Thread(()->{//无参方式
            for (int i = 0; i < 1000; i++) {
                doWork();
            }
        }).start();
    }
}
