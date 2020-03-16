package tsc03018.dorothiii.apple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.InterstitialAd;

public class PopupActivity extends Activity {
    TextView textView_top;
    TextView textView_ques;
    Button button_yes;
    Button button_no;
    int kind;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup);

        textView_top = findViewById(R.id.textView_top);
        textView_ques = findViewById(R.id.textView_ques);

        button_yes = findViewById(R.id.button_yes);
        button_no = findViewById(R.id.button_no);

        //데이터 가져오기
        Intent intent = getIntent();
        String top = intent.getStringExtra("top");
        String ques = intent.getStringExtra("ques");
        textView_top.setText(top);
        textView_ques.setText(ques);
        kind = intent.getIntExtra("kind", 1);

        button_yes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                switch (kind){
                    case 1:
                        Intent next = new Intent(PopupActivity.this, SecondActivity.class);
                        startActivity(next);
                        finish();
                        break;
                    case 2:
                        Intent next2 = new Intent(PopupActivity.this, ThirdActivity.class);
                        startActivity(next2);
                        finish();
                        break;
                    case 3:
                        Intent next3 = new Intent(PopupActivity.this, FourthActivity.class);
                        startActivity(next3);
                        finish();
                        break;
                    case 4:
                        Intent next4 = new Intent(PopupActivity.this, MainActivity.class);
                        startActivity(next4);
                        finish();
                        break;
                }
            }
        });

        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                System.runFinalization();
                System.exit(0);
            }
        });


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

}
