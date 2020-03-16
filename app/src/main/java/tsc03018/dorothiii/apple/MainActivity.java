package tsc03018.dorothiii.apple;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;


public class MainActivity extends Activity {
    int count = 20; // 유닛개수
    ArrayList total = new ArrayList();
    int score = count;
//    int x;
//    int y;
//    int ax = 5; // x축에 대한 가속도
//    int ay = 5 ;// y축에 대한 가속도
    int width ;//현재화면 해상도저장변수
    int height ; //해상도저장변수
    MyView m;
    Intent popup;



    //---------------------------------------------------------------------------------------
    class MyHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            //현재 메소드내의 코드는 UI Thread에 의해서 실행됨
            //ui thread에서 화면갱신가능 >
            //현재 메서드안에서 화면갱신이 가능


            for(int i = 0; i < total.size(); i++){

                Unit u = (Unit)total.get(i);

                if(u.x > width-100 || u.x < 0){ // x축 기준으로 화면 좌/우측으로 벗어남
                    u.ax *= -1;
                }

                if(u.y > height-150 || u.y < 0){ // y축 기준으로 화면 위/아래쪽으로 벗어남
                    u.ay *= -1;
                }

                u.x += u.ax;
                u.y += u.ay;
            }

            m.invalidate();
            sendEmptyMessageDelayed(0,5);


        }
    }
//--------------------------------------------------------------------------------------
private int cntTime= 30;
    Paint time;
//--------------------------------------------------------------------------------------

    class MyView extends View { // ui의 최상위 클래스는 view클래스임!!
        Bitmap image;
        MyHandler myHandler;

        public MyView(Context context) {
            super(context);
            setBackgroundResource(R.drawable.back);



            image = BitmapFactory.decodeResource(getResources(), R.drawable.apple);


            myHandler = new MyHandler();
            //직접호출안됨
            myHandler.sendEmptyMessage(0);// 콜백으로 handlerMessage호출됨


            //total.size말고 count 넣어주는 이유 > 총 100개를 만들거기때문에
            for(int i = 0; i<count; i++){
                Unit u = new Unit();
                u.x = (int) (Math.random()* width)/2;
                u.y= (int) (Math.random()* height)/2;
                u.ax=(int) (Math.random()* 10)+1;
                u.ay = (int) (Math.random()* 10)+1;
                u.image = image;
                u.health= 500;
                total.add(u);
            }

        }

        @Override
        protected void onDraw(Canvas canvas) { // canvas > 화면에 그림을 그려주는 메서드
            //time = new Paint();
            Paint level = new Paint();
            Paint apple = new Paint();
            time = new Paint();


            level.setColor(Color.WHITE);
            level.setTextSize(40);
            level.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));

            apple.setColor(Color.WHITE);
            apple.setTextSize(40);
            apple.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));

            time.setColor(Color.WHITE);
            time.setTextSize(150);
            time.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
            //canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.back),0,0,null);

            for(int i = 0; i < total.size(); i++){
                Unit u = (Unit)total.get(i);
                if(u.health > 0) {
                    canvas.drawBitmap(u.image, u.x, u.y, null); // 화면에 그려주는 코드
                }
            }
            //canvas.drawText("", 300, 100, time);

            if(cntTime > 0 && total.size() > 0) {
                autoCountHandler.postDelayed(autoCountRunnable, 1000);
            } else {
                onPause();
            }
            int pointX =  width / 8 ;
            int pointY =  height/13;
            int timeY =  height/10;
            int timeX = width/2-75;
            int appleY = height/10;
            canvas.drawText("Level 1", pointX , pointY, level);
            canvas.drawText(cntTime+"", timeX , timeY, time);
            canvas.drawText("Apple "+ total.size(), pointX, appleY, apple);

        }
    }

//----------------------------------------------------------------------------------------

    private Handler autoCountHandler = new Handler();
    private Runnable autoCountRunnable = new Runnable() {
        @Override
        public void run() {
            cntTime--;
            if(cntTime < 0){
            } else {
                if(autoCountHandler!= null){
                    autoCountHandler.removeCallbacks(autoCountRunnable);
                }
            }
        }
    };

    @Override
    public void onPause(){
        super.onPause();
        if(autoCountHandler != null){
            autoCountHandler.removeCallbacks(autoCountRunnable);
        }
    }

//----------------------------------------------------------------------------------------


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //터치한 위치값을 얻어와야함

        Bitmap apple = BitmapFactory.decodeResource(getResources(), R.drawable.apple);


        int imageX = apple.getWidth();
        int imageY = apple.getHeight();

        int x2 = (int) event.getX()-imageX; // 터치한 위치의 좌표값
        int y2 = (int) event.getY()-imageY; // 터치한 위치의 좌표값
        int action = event.getAction();

        final Vibrator  vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        for(int i = 0; i<total.size();i++){

            Unit u = (Unit) total.get(i);

            //자바에서 수학
            double b = Math.sqrt(Math.pow(u.x - x2, 2) + Math.pow(u.y - y2, 2));
            if (b < 100 && action == MotionEvent.ACTION_DOWN) {
                u.health -= 200;

                if (u.health <= 300 && u.health > 100) {
                    vibrator.vibrate(200);
                    u.image = BitmapFactory.decodeResource(getResources(), R.drawable.apple_1);
                } else if (u.health <= 100 && u.health > 0) {
                    vibrator.vibrate(200);
                    u.image = BitmapFactory.decodeResource(getResources(), R.drawable.apple_2);
                } else {
                    total.remove(u);
                    vibrator.vibrate(200);
                    score -= 1;
                }
            }
        }
        popup = new Intent(MainActivity.this, PopupActivity.class);
        if(total.size() < 1){
            suc = new Success();
            timeHandler.post(suc);
        }


        return super.onTouchEvent(event);
    }

    Handler timeHandler = new Handler();
    Success suc;
    Fail fail;
    //------------------------------------------------------------------------------------------
    class Success implements Runnable{
        @Override
        public void run(){
            popup.putExtra("top","Level 1 clear");
            popup.putExtra("ques","Level 2에 도전하시겠어요?");
            popup.putExtra("kind",1);
            startActivity(popup);
        }
    }
    class Fail implements Runnable{
        @Override
        public void run(){
            popup.putExtra("top", "Level 1 fail");
            popup.putExtra("ques", "Level 1에 다시 도전하시겠어요?");
            popup.putExtra("kind", 4);
            startActivity(popup);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //노티바 제거
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //디스플레이화면의 크기를 얻어오기 위한 코드
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();

        m = new MyView(this);

        setContentView(m);

        popup = new Intent(MainActivity.this, PopupActivity.class);
        fail = new Fail();
        timeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    if(suc != null){
                        timeHandler.removeCallbacks(fail);
                    } else {
                        timeHandler.post(fail);
                    }
            }
        },30000);

        //광고추가
//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-8655885767935937/7368518004");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        if (mInterstitialAd.isLoaded()) {
//            mInterstitialAd.show();
//        } else {
//            Log.d("TAG", "The interstitial wasn't loaded yet.");
//        }
    }



    @Override
    protected void onStop() {
        super.onStop();
    }

    long backBtnTime = 0;
    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if(0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }
        else {
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
        }


    }
}
