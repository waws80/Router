package pw.androidthanatos.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Call 执行界面跳转操作
 *
 * @author liuxiongfei
 *         2017/10/30
 */

public final class Call {

    private boolean mSkipIntecepter;
    private LinkedList<RouterIntecepter> mRouterIntecepters;
    private Request mRequest;
    private boolean canceled = false;
    private HashMap<String, Class<? extends Activity>> tabs = new HashMap<>();

    public Call(boolean skipIntecepter, @NonNull LinkedList<RouterIntecepter> routerIntecepters, @NonNull Request request) {
        this.mSkipIntecepter = skipIntecepter;
        this.mRouterIntecepters = routerIntecepters;
        this.mRequest = request;
    }

    /**
     * 取消请求
     */
    public void cancel(){
        canceled = true;
    }

    /**
     * 同步请求
     * @return 返回 response
     */
    public Response execute(){
       return getResponse();
    }

    /**
     * 异步请求
     * @param callBack 回调函数
     */
    public void enqueue(final RouterCallBack callBack){
        final RouterThreadPool pool = RouterThreadPool.init();
        pool.exe(new Runnable() {
            @Override
            public void run() {
                pool.handle().post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.next(getResponse());
                    }
                });
            }
        });
    }

    /**
     * 构建响应体
     * @return response
     */
    private Response getResponse() {
        if (canceled){
            if (mRequest != null){
                return new Response(403,mRequest.path(),mRequest.action(),"页面取消了访问");
            }else {
                return new Response(403,"","","页面取消了访问");
            }

        }
        if (!mSkipIntecepter){
            for (RouterIntecepter intecepter: mRouterIntecepters) {
                this.mRequest = intecepter.chain(this.mRequest);
                if (this.mRequest == null){
                    intecepter.onLost("path isEmpty  or  action isEmpty");
                    return new Response(404,"","","path isEmpty  or  action isEmpty");
                }
            }
        }

        this.tabs.putAll(RouterTabs.tabs());
        String path = mRequest.path();
        String action = mRequest.action();
        Intent intent = null;
        int code = 404;
        String msg = "";
        if (path != null && !path.isEmpty()) {
            Class<? extends Activity> target = this.tabs.get(path);
            if (target == null){
                msg = "目标页面没有找到! path = "+path;

                code = 404;
            }else {
                if (action != null && !action.isEmpty()){
                    intent = buildIntent(target,action);
                }else {
                    intent = buildIntent(target);
                }
            }

        }else if (action != null && !action.isEmpty()){
            intent = buildIntentNoTarget(action);
        }else {
            if (path == null || path.isEmpty()){
                msg = "path isEmpty";
                code = 404;
            }
            if (action == null || action.isEmpty()){
                msg = "action isEmpty";
                code = 404;
            }
            //throw new IllegalStateException("path isEmpty or action isEmpty");
        }

        if (intent != null){
            if (mRequest.flag() != -1){
                intent.addFlags(mRequest.flag());
            }
            Context context = mRequest.context();
            try {
                if (context instanceof Activity){
                    if (Build.VERSION.SDK_INT >= 21 && mRequest.option() != null){
                        if (mRequest.requestCode() != -1){
                            //处理startActivityForResult
                            buildActivityResult(intent);

                        }else {
                            context.startActivity(intent,mRequest.option());
                        }

                    }else {
                        if (mRequest.requestCode() != -1){
                            buildActivityResult(intent);
                            ((Activity) context).startActivityForResult(intent,mRequest.requestCode());
                        }else {
                            context.startActivity(intent);
                        }
                        int[] anis = mRequest.animator();
                        if (anis[0] > 0 && anis[1] > 0){
                            ((Activity) context).overridePendingTransition(anis[0],anis[1]);
                        }
                    }
                }else {
                    context.startActivity(intent);
                }
                code = 200;
                msg = "success";
            }catch (Exception e){
                code = 404;
                msg = "访问的页面不存在\n path = "+path+"\n action = "+action;
                RouterLog.d(e.toString());
            }
        }

        if (!mSkipIntecepter){
            for (RouterIntecepter intecepter: mRouterIntecepters) {
                if (code == 200){
                    intecepter.onSuccess();
                }else {
                    intecepter.onLost(msg);
                }
            }
        }
        RouterLog.d(msg);
        return buildResponse(code,path,action,msg);
    }

    /**
     * 构建response
     * @param code response code
     * @param path request path
     * @param action request action
     * @param msg 响应消息
     * @return response
     */
    private Response buildResponse(int code, String path, String action,String msg) {
        return new Response(code,path,action,msg);
    }

    /**
     * 构建通过action跳转的 intent
     * @param action 请求目标页面的action
     * @return intent
     */
    private Intent buildIntentNoTarget(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtras(mRequest.params());
        return intent;
    }

    /**
     * 构建 intent
     * @param target 目标页面activity.class
     * @return intent
     */
    private Intent buildIntent(Class<? extends Activity> target) {
       return buildIntent(target,null);
    }

    /**
     * 构建带action的 intent
     * @param target 目标页面activity.class
     * @param action 请求目标页面的action
     * @return intent
     */
    private Intent buildIntent(Class<? extends Activity> target, String action) {
        Context context = mRequest.context();
        Intent intent = new Intent();
        intent.setClass(context,target);
        if (action != null && !action.isEmpty()){
            intent.setAction(action);
        }
        intent.putExtras(mRequest.params());
        if (context instanceof Activity){
            //do Nothing
        }else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    /**
     * 执行 onActivityResult
     * @param intent intent
     */
    private void buildActivityResult(Intent intent){
        Activity activity = (Activity) mRequest.context();
        if (activity instanceof FragmentActivity){
            FragmentManager manager = ((FragmentActivity) activity).getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            //new DelegateFragmentV4();
            DelegateFragmentV4 fragment = FragmentFactory.getDefault().getV4();
            fragment.setCallBack(mRequest.callBack());
            fragment.setArguments(buildBund(intent));
            transaction.add(fragment,"RouterFragment").commit();

        }else {
            android.app.FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
            //new DelegateFragment();
            DelegateFragment fragment = FragmentFactory.getDefault().get();
            fragment.setCallBack(mRequest.callBack());
            fragment.setArguments(buildBund(intent));
            transaction.add(fragment,"RouterFragment").commit();
        }
    }

    /**
     * 构建bundle
     * @param intent intent
     * @return bundle
     */
    private Bundle buildBund(Intent intent){
        Bundle bundle = new Bundle();
        bundle.putInt("code",mRequest.requestCode());
        bundle.putString("timeStep",System.currentTimeMillis()+"");
        bundle.putParcelable("intent",intent);
        bundle.putParcelable("option",mRequest.option());
        return bundle;
    }
}
