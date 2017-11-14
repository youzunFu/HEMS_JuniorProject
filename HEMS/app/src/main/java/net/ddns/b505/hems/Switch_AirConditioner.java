package net.ddns.b505.hems;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


/**
 * Created by RED on 2017/9/30.
 */

public class Switch_AirConditioner extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.air_switch);
        Button air4Btn= (Button) findViewById(R.id.air4EnterButton);
        Button air5leftBtn= (Button) findViewById(R.id.air505leftEnterButton);
        Button air5rightBtn= (Button) findViewById(R.id.air505rightEnterButton);

        air4Btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Switch_AirConditioner.this,AirControlActivity.class);
                startActivity(intent);
            }
        });

        air5leftBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Switch_AirConditioner.this,Air505leftControlActivity.class);
                startActivity(intent);
            }
        });

        air5rightBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Switch_AirConditioner.this,Air505rightControlActivity.class);
                startActivity(intent);
            }
        });


    }


}
