package pw.androidthanatos.module1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pw.androidthanatos.annotation.Path;


@Path("moduleSecond")
public class ModuleSecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_second);
    }
}
