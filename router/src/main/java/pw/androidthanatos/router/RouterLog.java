package pw.androidthanatos.router;

import android.util.Log;

/**
 * RouterLog
 *
 * @author liuxiongfei
 *         2017/10/30
 */

public final class RouterLog {

    public static void d(String msg){
        if (Router.debug()){
            Log.d("Router",msg);
        }
    }
}
