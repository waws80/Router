package pw.androidthanatos.router;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * RouterThreadPool 线程池
 *
 * @author liuxiongfei
 *         2017/10/31
 */
public final class RouterThreadPool {

    private Handler handler = new Handler(Looper.getMainLooper());

    private ExecutorService service =
            new ThreadPoolExecutor(2,2,0L,TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(),new ThreadPoolExecutor.CallerRunsPolicy());

    private static class Build{
        private static volatile RouterThreadPool POOL = new RouterThreadPool();
    }

    static RouterThreadPool init(){
        return Build.POOL;
    }

    void exe(Runnable runnable){
        service.execute(runnable);
    }

    Handler handle(){
        return handler;
    }

    public void destory(){
        handler = null;
        service.shutdown();
        service = null;
    }
}
