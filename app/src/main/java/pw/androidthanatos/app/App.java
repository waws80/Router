package pw.androidthanatos.app;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import pw.androidthanatos.annotation.Module;
import pw.androidthanatos.annotation.Modules;
import pw.androidthanatos.router.Request;
import pw.androidthanatos.router.Router;
import pw.androidthanatos.router.RouterIntecepter;


/**
 * App
 * @author liuxiongfei
 */

@Module("app")
@Modules({"app","module"})
public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Router.init(this.getApplicationContext());
        Router.debug(true);
        Router.addIntecepter(new RouterIntecepter() {
            @Override
            public Request chain(Request request) {
                return request;
            }

            @Override
            public void onLost(String msg) {
                Log.d("App", "onLost: "+msg);
            }

            @Override
            public void onSuccess() {
                Log.d("App", "onSuccess: ");
            }
        });
    }
}
