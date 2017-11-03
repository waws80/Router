package pw.androidthanatos.app;

import android.os.Bundle;

import com.androidthanatos.dynamic.annotation.BundleOption;
import com.androidthanatos.dynamic.annotation.Path;
import com.androidthanatos.dynamic.annotation.QueryBundle;
import com.androidthanatos.dynamic.annotation.RequestCode;
import com.androidthanatos.dynamic.annotation.ResultCall;
import com.androidthanatos.dynamic.annotation.SkipIntecepter;

import pw.androidthanatos.router.Call;
import pw.androidthanatos.router.ResultCallBack;

/**
 * Created by liuxiongfei on 2017/11/1.
 */

public interface RouterService {

    @Path("second")
    @RequestCode(100)
    @SkipIntecepter
    Call tosecond(@QueryBundle Bundle query, @BundleOption Bundle option,
                  @ResultCall ResultCallBack callBack);
}
