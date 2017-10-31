package pw.androidthanatos.router;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * 代理fragment全局捕获处理activityResult
 * Created by liuxiongfei on 2017/10/31.
 */

public class DelegateFragment extends Fragment {

    private int mRequestCode = -1;
    private ResultCallBack callBack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mRequestCode = bundle.getInt("code");
        RouterLog.d(" DelegateFragment : onStart:");
        Bundle option = bundle.getParcelable("option");
        Intent intent = bundle.getParcelable("intent");
        RouterLog.d("timeStep: "+bundle.getString("time"));
        if (option != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivityForResult(intent,
                        mRequestCode,option);
            }
            else {
                throw new IllegalStateException("暂不支持4.1以下版本使用 android.app.Fragment.startActivityForResult 请使用v4包下的fragment");
            }
        }else {
            startActivityForResult(intent, mRequestCode);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        RouterLog.d(" DelegateFragment : onPause:");
    }



    public void setCallBack(ResultCallBack callBack) {
       this.callBack = callBack;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RouterLog.d("DelegateFragment: onActivityResult:"+resultCode+"\ndata:"+data.toString());
        if (mRequestCode != -1 && mRequestCode == requestCode){
            callBack.next(resultCode, data);
        }
        FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
        transaction.remove(this).commit();
    }

}
