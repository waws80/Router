package pw.androidthanatos.router;

import android.app.Activity;

import java.util.concurrent.ConcurrentHashMap;

/**
 * RouterTabs
 *
 * @author liuxiongfei
 *         2017/10/28
 */

public final class RouterTabs {

    private static final ConcurrentHashMap<String, Class<? extends Activity>> ROUTABLE_TABS
            = new ConcurrentHashMap<>();

    /**
     * 将activity 和其别名 添加到 路由表中
     * @param alias 别名
     * @param target 目标activity
     */
    public static void map(String alias, Class<? extends Activity> target){
        ROUTABLE_TABS.put(alias, target);
    }

    /**
     * 获取路由表
     * @return 返回路由表
     */
    public static ConcurrentHashMap<String, Class<? extends Activity>> tabs(){
        return ROUTABLE_TABS;
    }


    public static void clearMap(){
        ROUTABLE_TABS.clear();
    }

}
