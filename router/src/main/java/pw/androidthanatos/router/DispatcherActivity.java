package pw.androidthanatos.router;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * 路由调度器
 * 只处理外部调用activity请求
 * @author liuxiongfei
 */
public final class DispatcherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RouterLog.d("外部请求正在启动app。。。。。");
        Intent intent = getIntent();
        Uri uri = intent.getData();
        RouterLog.d(uri.toString());
        String path = ""+uri.getPath().replace("/","");
        Request.Builder builder = new Request.Builder(this.getApplicationContext()).path(path);
        for (String name:uri.getQueryParameterNames()) {
            String value = uri.getQueryParameter(name);
            RouterLog.d("key: "+name+"    value: "+value);
            builder.addString(name,value);
        }
        Request request = builder.build();
        Router.getInstance().newCall(request).enqueue(new RouterCallBack() {
            @Override
            public void next(Response response) {
                //不开启debug模式不会显示
                RouterLog.d(response.toString());
            }
        });
        finish();
    }

}
