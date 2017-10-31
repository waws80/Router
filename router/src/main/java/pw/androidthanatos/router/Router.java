package pw.androidthanatos.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * Router
 * @author liuxiongfei
 * 2017/10/28
 */

@SuppressWarnings("unchecked")
public final class Router {

    private static WeakReference<Context> wrf;
    private static boolean init = false;
    private static LinkedList<RouterIntecepter> ROUTER_INTECEPTERS = new LinkedList<>();

    private boolean skipIntecepter = false;

    private static boolean DEBUG = false;

    /**
     * 初始化router 尽可能早的初始化
     */
    public static void init(Context context){
        wrf = new WeakReference<>(context);
        init = true;
        try {
            Class claz = Class.forName("pw.androidthanatos.router.RouterHelper");
            Method method = claz.getDeclaredMethod("merge");
            method.setAccessible(true);
            //合并路由表
            method.invoke(claz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否开启调试模式
     * @param debug debug
     */
    public static void debug(boolean debug){
        DEBUG = debug;
    }

    /**
     * 获取是否是调试模式
     * @return 返回是否开启debug模式
     */
    public static boolean debug(){
        return DEBUG;
    }

    private static class Builder{
        private static final Router INSTANCE = new Router();
    }

    /**
     * 获取router 实类对象
     * @return router
     */
    public static Router getInstance(){
        return Builder.INSTANCE;
    }

    /**
     * 添加拦截器
     * @param intecepter 拦截器
     */
    public static void addIntecepter(RouterIntecepter intecepter){
        ROUTER_INTECEPTERS.add(intecepter);
    }

    /**
     * 添加多个拦截器
     * @param routerIntecepters 拦截器列表
     */
    public static void addIntecepters(List<RouterIntecepter> routerIntecepters){
        ROUTER_INTECEPTERS.addAll(routerIntecepters);
    }

    /**
     * 执行跳转请求，是否跳过拦截器
     * @return router
     */
    public Router skipIntecepter(){
        this.skipIntecepter = true;
        return this;
    }

    /**
     * 构建call 请求执行类
     * @param request router请求体
     * @return call
     */
    public Call newCall(Request request){
       return new Call(skipIntecepter,ROUTER_INTECEPTERS,request);
    }


    /**
     * 通过path来进行页面跳转
     * @param path activity定义的path
     */
    public Response path(@NonNull String path){
        Request request = new Request.Builder(checkInit(wrf.get())).path(path).build();
       return newCall(request).execute();
    }

    /**
     * 通过action进行页面跳转
     * @param action activity通过action进行跳转
     */
    public Response action(@NonNull String action){
        return newCall(new Request.Builder(action,checkInit(wrf.get())).build()).execute();
    }

    /**
     * 通过uri进行页面跳转
     * @param uri uri
     */
    public  Response uri(@NonNull String uri){
      return uri(Uri.parse(uri));
    }

    /**
     * 通过 uri进行页面跳转
     * @param uri uri
     */
    public  Response uri(@NonNull Uri uri){
        String path = uri.getPath().replace("/","");
        Request.Builder builder = new Request.Builder(checkInit(wrf.get())).path(path);
        for (String name:uri.getQueryParameterNames()) {
            String value = uri.getQueryParameter(name);
            RouterLog.d("key: "+name+"    value: "+value);
            builder.addString(name,value);
        }
        Request request = builder.build();
        return newCall(request).execute();
    }

    /**
     * 检测是否初始化
     * @param context 上下文对象
     * @return 上下文
     */
    private static Context checkInit(Context context){
        if (wrf == null || !init){
            throw new NullPointerException("请先初始化 Router");
        }
        if (context instanceof Activity){
            context = context.getApplicationContext();
        }
        return context;
    }

    /**
     * 销毁Router
     */
    public static void onDestory(){
        if (wrf != null){
            wrf.clear();
            wrf = null;
        }
        ROUTER_INTECEPTERS.clear();
        RouterThreadPool.init().destory();
        RouterTabs.clearMap();
    }
}
