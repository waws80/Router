package pw.androidthanatos.router;

import android.content.Intent;

/**
 * activituForResult回调
 * Created by liuxiongfei on 2017/10/31.
 */

public  interface ResultCallBack {
     void next(int resultCode, Intent data);

}
