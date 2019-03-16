package existv2.com.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import existv2.com.R;

public class BeforeGetStarted extends AppCompatActivity {

    Button btn_getstarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_get_started);

        btn_getstarted = (Button) findViewById(R.id.btn_getstarted);
        btn_getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BeforeGetStarted.this,SignIn.class));
            }
        });

    }
}
