package pw.androidthanatos.app;

import android.app.ActivityOptions;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;


import com.squareup.okhttp.OkHttpClient;

import pw.androidthanatos.annotation.Path;
import pw.androidthanatos.router.Request;
import pw.androidthanatos.router.Response;
import pw.androidthanatos.router.ResultCallBack;
import pw.androidthanatos.router.Router;
import pw.androidthanatos.router.RouterCallBack;
import pw.androidthanatos.router.RouterLog;


@Path("main")
public class MainActivity extends AppCompatActivity {
    private Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView wv = (WebView) findViewById(R.id.wv);
        bt = (Button) findViewById(R.id.bt);
        wv.loadUrl("file:///android_asset/index.html");
    }

    public void second(View view){
        Request request = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            request = new Request.Builder(this)
                    .path("second")
                    .responseCode(100)
                    .resultCallBack(new ResultCallBack() {
                        @Override
                        public void next(int resultCode, Intent data) {
                            RouterLog.d("resultCode:"+resultCode+"\ndata:"+data.getStringExtra("tag"));
                        }
                    })
                    .addOption(ActivityOptions.makeSceneTransitionAnimation(this,bt,"share").toBundle())
                    .build();
        }
        Router.getInstance().skipIntecepter().newCall(request)
                .enqueue(new RouterCallBack() {
            @Override
            public void next(Response response) {
                RouterLog.d(response.toString());
            }
        });
    }

    public void module(View view){
        Request request = new Request.Builder(this).path("module").build();
        Router.getInstance().newCall(request).execute();
    }

    public void capture(View view){
        Request request = new Request.Builder(MediaStore.ACTION_IMAGE_CAPTURE,this).build();
        Router.getInstance().newCall(request).execute();
    }

    public void uri(View view){
       Response response = Router.getInstance().uri("https://www.waws.top/module?id=2&name=haha");
//        Response response = Router.getInstance().path("module");
        if (response.code == Response.RESPONSE_OK){
            RouterLog.d(response.toString());
        }
    }
}
