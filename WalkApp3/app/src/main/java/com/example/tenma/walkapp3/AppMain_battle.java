package com.example.tenma.walkapp3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Tenma and yuuma on 2017/1/22.
 */

public class AppMain_battle extends AppCompatActivity {
    //imageボタンの画像切り替えで使う変数
    ImageButton battle, magic, check, escape;
    //ボタンが二回押されたかの判定で使う変数
    int buttonFlag = 0;
    TextView Hp;
    TextView My_Hp;
    MediaPlayer bgm;
    //      乱数
    Random r = new Random();
    int n = r.nextInt(1000) + 100;
    //    idの番地
    static int number;
    //    preference
    SharedPreferences data;
    SharedPreferences.Editor editor;
    private SoundPool soundPool,soundPool1,soundPool2;
    private int soundId,soundId1,soundId2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_battle);
        //data関連
        data = getSharedPreferences("ZENKAIDATA",MODE_PRIVATE);
        editor = data.edit();
        // SharedPreferencesよりデータを読み込む
        number =data.getInt("ZenkaiData", 1);

        //リソースファイルから再生
        bgm = MediaPlayer.create(this, R.raw.battle);
        bgm.start();
        bgm.setLooping(true);

//        画像ファイル
        ImageView iw = (ImageView)findViewById(R.id.npc1);
// ランダム画像のリソースの配列を宣言
        TypedArray typedArray =getResources().obtainTypedArray(R.array.tekicpu);
// ランダムな数値を設定
        int rand = (int)(Math.floor(Math.random() * 3));// ランダムで画像を選択する
        Drawable drawable = typedArray.getDrawable(rand);
            // ImageViewの画像の値を設定
            iw.setImageDrawable(drawable);
//        体力
        Hp = (TextView) findViewById(R.id.hp);
        Hp.setTextColor(Color.RED);
        ImageView Hp_Bar = (ImageView)findViewById(R.id.hpbar);
        Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.hp100);
        Hp_Bar.setImageBitmap(bmp1);
//        敵HP設定
        if (number>355){
            n = 100000;
            Hp.setText("HP" + "" + n );
        } else {
            Hp.setText("HP" + "" + n);
        }

        //        アニメーション
        findViewById(R.id.npc1).startAnimation(AnimationUtils.loadAnimation(this, R.anim.start));
        ImageButton ib1 = (ImageButton) findViewById(R.id.main_hukidashi2);
        ib1.setImageResource(R.drawable.main_hukidasi);
        findViewById(R.id.main_hukidashi2).setVisibility(View.INVISIBLE);

    }
    protected void onResume() {
        super.onResume();
        // 予め音声データを読み込む
        soundPool1 = new SoundPool(50, AudioManager.STREAM_MUSIC, 0);
        soundId1 = soundPool1.load(getApplicationContext(), R.raw.bun, 1);
        soundPool = new SoundPool(50, AudioManager.STREAM_MUSIC, 0);
        soundId = soundPool.load(getApplicationContext(), R.raw.mahou, 1);
        soundPool2 = new SoundPool(50, AudioManager.STREAM_MUSIC, 0);
        soundId2 = soundPool2.load(getApplicationContext(), R.raw.mahou, 1);

    }
    protected void localBgmStop() {
        bgm.pause();
        bgm.release();
        bgm = null;
    }

    public void StatusStart(View view) {
        soundPool2.play(soundId2, 1f, 1f, 0, 0, 1);    //音の大きさは0fから1fで調整できる
        Intent intent = new Intent(this, AppStatus.class);
        localBgmStop();
        //遷移先の画面を起動
        startActivity(intent);
    }

    public void LogStart(View view) {
        soundPool2.play(soundId2, 1f, 1f, 0, 0, 1);    //音の大きさは0fから1fで調整できる
        Intent intent = new Intent(this, AppLog.class);
        localBgmStop();
        //遷移先の画面を起動
        startActivity(intent);
    }
    public void onStop(){
        super.onStop();
//        localBgmStop();
    }

        //コマンド
    public void change(View view) {

        switch (view.getId()) {

            case R.id.main_battle:
                //矢印がどれも選択していない場合
                if (buttonFlag == 0) {
                    battle = (ImageButton) findViewById(R.id.main_battle);
                    battle.setImageResource(R.drawable.main_battle2);
                    buttonFlag = 1;

                    //矢印がたたかう以外選択していた場合
                } else if (buttonFlag != 1) {
                    battle = (ImageButton) findViewById(R.id.main_battle);
                    battle.setImageResource(R.drawable.main_battle2);
                    magic = (ImageButton) findViewById(R.id.main_magic);
                    magic.setImageResource(R.drawable.main_magic1);
                    check = (ImageButton) findViewById(R.id.main_check);
                    check.setImageResource(R.drawable.main_check1);
                    escape = (ImageButton) findViewById(R.id.main_escape);
                    escape.setImageResource(R.drawable.main_escape1);
                    buttonFlag = 1;
                    //矢印がたたかうを選択していた場合
                } else if (buttonFlag == 1) {
                    findViewById(R.id.npc1).startAnimation(AnimationUtils.loadAnimation(this, R.anim.yureru));
                    TextView tv = (TextView) findViewById(R.id.main_text);
                    //魔王最強になる(条件処理)
                    if (number > 355) {
                        soundPool1.play(soundId1, 1f, 1f, 0, 0, 1);    //音の大きさは0fから1fで調整できる
                        n -= 99999;
                        Hp.setText("HP" + "" + n);
                        tv.setText("かいしんのいちげき !てきに９９９９９のタ゛メーシ゛");

                    } else {
                        soundPool1.play(soundId1, 1f, 1f, 0, 0, 1);    //音の大きさは0fから1fで調整できる
                        tv.setText("まおう のこうけ゛き !てきに１００のタ゛メーシ゛");
                        n -= 100;
                        Hp.setText("HP" + "" + n);
//                        頭がまわらなくなってきた条件分岐何それおいしいの
                        if(n<=(n/100*75)) {
                            ImageView Hp_Bar = (ImageView)findViewById(R.id.hpbar);
                            Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.hp75);
                            Hp_Bar.setImageBitmap(bmp1);

                        }else if(n<=(n/100*50)) {
                            ImageView Hp_Bar = (ImageView)findViewById(R.id.hpbar);
                            Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.hp50);
                            Hp_Bar.setImageBitmap(bmp1);

                        }else if(n<=(n/100*25)) {
                            ImageView Hp_Bar = (ImageView)findViewById(R.id.hpbar);
                            Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.hp25);
                            Hp_Bar.setImageBitmap(bmp1);

                        }
                    }
                    tv.setTypeface(Typeface.createFromAsset(getAssets(), "DragonQuestFCIntact.ttf"));
                    tv.setTextColor(Color.WHITE);
                    tv.setTextSize(10);
                    findViewById(R.id.main_hukidashi2).setVisibility(View.VISIBLE);
                    findViewById(R.id.main_text).setVisibility(View.VISIBLE);

                    if (n <= 0) {
                        Intent intent = new Intent(this, AppMain_scenario.class);
                        localBgmStop();
                        //遷移先の画面を起動
                        startActivity(intent);

                    } else if (n <= 150) {
                        findViewById(R.id.npc1).startAnimation(AnimationUtils.loadAnimation(this, R.anim.hureeru));

                    }

                }


                break;

            case R.id.main_magic:

                //矢印がなにも選択していない場合
                if (buttonFlag == 0) {
                    //まほうが選ばれたらまほうに矢印
                    magic = (ImageButton) findViewById(R.id.main_magic);
                    magic.setImageResource(R.drawable.main_magic2);
                    buttonFlag = 2;
                    //矢印がまほう以外を選択していた場合
                } else if (buttonFlag != 2) {
                    magic = (ImageButton) findViewById(R.id.main_magic);
                    magic.setImageResource(R.drawable.main_magic2);
                    battle = (ImageButton) findViewById(R.id.main_battle);
                    battle.setImageResource(R.drawable.main_battle1);
                    check = (ImageButton) findViewById(R.id.main_check);
                    check.setImageResource(R.drawable.main_check1);
                    escape = (ImageButton) findViewById(R.id.main_escape);
                    escape.setImageResource(R.drawable.main_escape1);
                    buttonFlag = 2;
                    //矢印がまほうを選択していた場合
                } else if (buttonFlag == 2) {
                    soundPool.play(soundId, 1f, 1f, 0, 0, 1);    //音の大きさは0fから1fで調整できる
                    TextView tv = (TextView) findViewById(R.id.main_text);
                    findViewById(R.id.npc1).startAnimation(AnimationUtils.loadAnimation(this, R.anim.yureru));
                    tv.setTypeface(Typeface.createFromAsset(getAssets(), "DragonQuestFCIntact.ttf"));
                    tv.setTextColor(Color.WHITE);
                    tv.setTextSize(10);
                    findViewById(R.id.main_hukidashi2).setVisibility(View.VISIBLE);
                    findViewById(R.id.main_text).setVisibility(View.VISIBLE);
                    if (number > 355) {
                        tv.setText("めら をはなった てきに１１４９１４のタ゛メーシ゛");
                        n -= 99999;
                        Hp.setText("HP" + "" + n);
                    } else {
                        tv.setText("まおう は し゛ゅもん をはなった てきに１００のタ゛メーシ゛");
                        n -= 100;
                        Hp.setText("HP" + "" + n);
                        if(n<=100) {
                            ImageView Hp_Bar = (ImageView)findViewById(R.id.hpbar);
                            Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.hp100);
                            Hp_Bar.setImageBitmap(bmp1);

                        }else if(n<=75) {
                            ImageView Hp_Bar = (ImageView)findViewById(R.id.hpbar);
                            Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.hp75);
                            Hp_Bar.setImageBitmap(bmp1);

                        }else if(n<=50) {
                            ImageView Hp_Bar = (ImageView)findViewById(R.id.hpbar);
                            Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.hp50);
                            Hp_Bar.setImageBitmap(bmp1);

                        }else if(n<=25) {
                            ImageView Hp_Bar = (ImageView)findViewById(R.id.hpbar);
                            Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.hp25);
                            Hp_Bar.setImageBitmap(bmp1);

                        }
                    }
                    if (n <= 0) {
                        findViewById(R.id.npc1).startAnimation(AnimationUtils.loadAnimation(this, R.anim.hureeru));
                        Intent intent = new Intent(this, AppMain_scenario.class);
                        localBgmStop();
                        //遷移先の画面を起動
                        startActivity(intent);

                    } else if (n <= 150) {
                        findViewById(R.id.npc1).startAnimation(AnimationUtils.loadAnimation(this, R.anim.hureeru));

                    }


                }

                break;

            case R.id.main_check:

                //矢印がなにも選択していない場合
                if (buttonFlag == 0) {
                    //かくにんが選ばれたらかくにんに矢印
                    check = (ImageButton) findViewById(R.id.main_check);
                    check.setImageResource(R.drawable.main_check2);
                    buttonFlag = 3;

                    //矢印がかくにん以外を選択していた場合
                } else if (buttonFlag != 3) {
                    check = (ImageButton) findViewById(R.id.main_check);
                    check.setImageResource(R.drawable.main_check2);
                    battle = (ImageButton) findViewById(R.id.main_battle);
                    battle.setImageResource(R.drawable.main_battle1);
                    magic = (ImageButton) findViewById(R.id.main_magic);
                    magic.setImageResource(R.drawable.main_magic1);
                    escape = (ImageButton) findViewById(R.id.main_escape);
                    escape.setImageResource(R.drawable.main_escape1);
                    buttonFlag = 3;

                    //矢印がかくにんを選んでいた場合
                } else if (buttonFlag == 3) {
                    TextView tv = (TextView) findViewById(R.id.main_text);
                    tv.setText("いま の ホ゜イント は １００ ホ゜イント て゛す");
                    tv.setTypeface(Typeface.createFromAsset(getAssets(), "DragonQuestFCIntact.ttf"));
                    tv.setTextColor(Color.WHITE);
                    tv.setTextSize(10);
                    findViewById(R.id.main_hukidashi2).setVisibility(View.VISIBLE);
                    findViewById(R.id.main_text).setVisibility(View.VISIBLE);

                }
                break;

            case R.id.main_escape:

                //矢印がなにも選択していなかった場合
                if (buttonFlag == 0) {
                    //にげるが選ばれたらにげるに矢印
                    escape = (ImageButton) findViewById(R.id.main_escape);
                    escape.setImageResource(R.drawable.main_escape2);
                    buttonFlag = 4;
                    //矢印が逃げる以外を選択していた場合
                } else if (buttonFlag != 4) {
                    escape = (ImageButton) findViewById(R.id.main_escape);
                    escape.setImageResource(R.drawable.main_escape2);
                    battle = (ImageButton) findViewById(R.id.main_battle);
                    battle.setImageResource(R.drawable.main_battle1);
                    magic = (ImageButton) findViewById(R.id.main_magic);
                    magic.setImageResource(R.drawable.main_magic1);
                    check = (ImageButton) findViewById(R.id.main_check);
                    check.setImageResource(R.drawable.main_check1);
                    buttonFlag = 4;

                    //矢印が逃げるを選択していた場合
                } else if (buttonFlag == 4) {
                    TextView tv = (TextView) findViewById(R.id.main_text);
                    tv.setText("テ゛フ゛すき゛て にけ゛られない ！！" +
                            "てきはHPをかいふくした");
                    tv.setTypeface(Typeface.createFromAsset(getAssets(), "DragonQuestFCIntact.ttf"));
                    tv.setTextColor(Color.WHITE);
                    tv.setTextSize(10);
                    findViewById(R.id.main_hukidashi2).setVisibility(View.VISIBLE);
                    findViewById(R.id.main_text).setVisibility(View.VISIBLE);
                    n = +1000;
                    Hp.setText("HP" + "" + n);
                    findViewById(R.id.npc1).startAnimation(AnimationUtils.loadAnimation(this, R.anim.kaihuku));
                }

                break;

            case R.id.main_hukidashi2:
                findViewById(R.id.main_hukidashi2).setVisibility(View.INVISIBLE);
                findViewById(R.id.main_text).setVisibility(View.INVISIBLE);
                //矢印の初期化
                battle = (ImageButton) findViewById(R.id.main_battle);
                battle.setImageResource(R.drawable.main_battle1);
                magic = (ImageButton) findViewById(R.id.main_magic);
                magic.setImageResource(R.drawable.main_magic1);
                check = (ImageButton) findViewById(R.id.main_check);
                check.setImageResource(R.drawable.main_check1);
                escape = (ImageButton) findViewById(R.id.main_escape);
                escape.setImageResource(R.drawable.main_escape1);
                buttonFlag = 0;
                break;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction()==KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    // ダイアログ表示など特定の処理を行いたい場合はここに記述
                    // 親クラスのdispatchKeyEvent()を呼び出さずにtrueを返す
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}