package com.ofilm.android.hozonlauncher;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ButtonLayout extends LinearLayout implements View.OnClickListener{

    private Button btn1,btn2,btn3,btn4,btn5,btn6;
    private static String TAG="ButtonLayout";


    public ButtonLayout(Context context) {

        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.button_layout, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(layout, params);
        Button btn1=(Button)findViewById(R.id.button1);
        Button btn2=(Button)findViewById(R.id.button2);
        Button btn3=(Button)findViewById(R.id.button3);
        Button btn4=(Button)findViewById(R.id.button4);
        Button btn5=(Button)findViewById(R.id.button5);
        Button btn6=(Button)findViewById(R.id.button6);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);


    }



    @Override
    public void onClick(View v) {
        Button btn1=(Button)findViewById(R.id.button1);
        Button btn2=(Button)findViewById(R.id.button2);
        Button btn3=(Button)findViewById(R.id.button3);
        Button btn4=(Button)findViewById(R.id.button4);
        Button btn5=(Button)findViewById(R.id.button5);
        Button btn6=(Button)findViewById(R.id.button6);
        switch (v.getId()){
            case R.id.button1:
                Log.d(TAG,"button1");
                if (btn1.getText().equals("ON")){
                    btn1.setText("OFF");
                    btn1.setBackgroundColor(Color.GRAY);
                }
                else if (btn1.getText().equals("OFF")){
                    btn1.setText("ON");
                    btn1.setBackgroundColor(Color.parseColor("#4F9796"));
                }
                break;
            case R.id.button2:
                if (btn2.getText().equals("ON")){
                    btn2.setText("OFF");
                    btn2.setBackgroundColor(Color.GRAY);
                }
                else if (btn2.getText().equals("OFF")){
                    btn2.setText("ON");
                    btn2.setBackgroundColor(Color.parseColor("#4F9796"));
                }
                break;
            case R.id.button3:
                if (btn3.getText().equals("ON")){
                    btn3.setText("OFF");
                    btn3.setBackgroundColor(Color.GRAY);
                }
                else if (btn3.getText().equals("OFF")){
                    btn3.setText("ON");
                    btn3.setBackgroundColor(Color.parseColor("#4F9796"));
                }
                break;
            case R.id.button4:
                if (btn4.getText().equals("ON")){
                    btn4.setText("OFF");
                    btn4.setBackgroundColor(Color.GRAY);
                }
                else if (btn4.getText().equals("OFF")){
                    btn4.setText("ON");
                    btn4.setBackgroundColor(Color.parseColor("#4F9796"));
                }
                break;

            case R.id.button5:
                if (btn5.getText().equals("ON")){
                    btn5.setText("OFF");
                    btn5.setBackgroundColor(Color.GRAY);
                }
                else if (btn5.getText().equals("OFF")){
                    btn5.setText("ON");
                    btn5.setBackgroundColor(Color.parseColor("#4F9796"));
                }
                break;
            case R.id.button6:
                if (btn6.getText().equals("ON")){
                    btn6.setText("OFF");
                    btn6.setBackgroundColor(Color.GRAY);
                }
                else if (btn6.getText().equals("OFF")){
                    btn6.setText("ON");
                    btn6.setBackgroundColor(Color.parseColor("#4F9796"));
                }
                break;

        }
    }
}
