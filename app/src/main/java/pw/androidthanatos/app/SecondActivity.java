package pw.androidthanatos.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import pw.androidthanatos.annotation.Path;

@Path("second")
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        TextView tv = (TextView) findViewById(R.id.tv);
        String id = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");
        tv.setText("id:"+id+"\nname:"+name);
        Intent intent = new Intent();
        intent.putExtra("tag","RouterFragment 666");
        setResult(666,intent);
    }
}
