package pw.androidthanatos.router;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

/**
 * 代理fragment全局捕获处理activityResult
 * Created by liuxiongfei on 2017/10/31.
 */

public class DelegateFragmentV4 extends Fragment {

    private int mRequestCode = -1;
    private ResultCallBack callBack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mRequestCode = bundle.getInt("code");
        RouterLog.d(" DelegateFragmentV4 : onStart:");
        Bundle option = bundle.getParcelable("option");
        Intent intent = bundle.getParcelable("intent");
        RouterLog.d("timeStep: "+bundle.getString("time"));
        if (option != null){
            startActivityForResult(intent,
                    mRequestCode,option);
        }else {
            startActivityForResult(intent,
                    mRequestCode);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        RouterLog.d(" DelegateFragmentV4 : onPause:");
    }

    @Override
    public void onStop() {
        super.onStop();
        RouterLog.d(" DelegateFragmentV4 : onStop:");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RouterLog.d(" DelegateFragmentV4 : onDestroy:");
    }

    public void setCallBack(ResultCallBack callBack) {
       this.callBack = callBack;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RouterLog.d("DelegateFragmentV4: onActivityResult:"+resultCode+"\ndata:"+data.toString());
        if (mRequestCode != -1 && mRequestCode == requestCode){
            callBack.next(resultCode, data);
        }
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.remove(this).commit();
    }

}
