package com.xh.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.StringTokenizer;

public class CheatActivity extends Activity {
    private static final String EXTRA_ANSWER_IS_TRUE="com.xh.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN="com.xh.geoquiz.answer_shown";
    private static final String TAG="CheatActivity";
    private boolean mAnswerIsTrue;
    private boolean mAnswerIsShown=false;

    private TextView mAnswerTextView;
    private Button mShowAnswer;
    private TextView mAPITextView;

    public static Intent newIntent(Context packageContext,boolean isAnswerTrue){
        Intent i=new Intent(packageContext,CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, isAnswerTrue);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        mAnswerTextView= (TextView) findViewById(R.id.answerTextView);
        mAPITextView= (TextView) findViewById(R.id.APITextView);
        //显示所运行的sdk api版本
        mAPITextView.setText("API Level "+Build.VERSION.SDK_INT);

       //恢复屏幕翻转前的是否显示答案
        if(savedInstanceState!=null){
            mAnswerIsShown=savedInstanceState.getBoolean(EXTRA_ANSWER_SHOWN,false);
        }

        mAnswerIsTrue=getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);
        //如果翻转屏幕前已经显示了答案，翻转后，保持答案显示在屏幕上，并传递给QuizActivity
        if(mAnswerIsShown){
            ShowAnswer(mAnswerIsTrue);
            setAnswerShownResult(mAnswerIsShown);
        }

        mShowAnswer= (Button) findViewById(R.id.showAnswerButton);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAnswer(mAnswerIsTrue);
                //标记答案已显示，即代表已经作弊
                mAnswerIsShown = true;
                setAnswerShownResult(mAnswerIsShown);

                //高版本sdk上显示答案有动画效果
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                    int cx=mShowAnswer.getWidth()/2;
                    int cy=mShowAnswer.getHeight()/2;
                    float radius=mShowAnswer.getWidth();
                    Animator anim= ViewAnimationUtils
                            .createCircularReveal(mShowAnswer,cx,cy,radius,0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswer.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                }else{
                    mShowAnswer.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

   //将显示正确答案的过程封装成此函数
    public void ShowAnswer(Boolean mAnswerIsTrue){
        if(mAnswerIsTrue){
            mAnswerTextView.setText(R.string.true_button);
        }
        else{
            mAnswerTextView.setText(R.string.false_button);
        }
    }

   //将CheatActivity中作弊的记录返回给QuizActivity
    private  void setAnswerShownResult(boolean isAnswerShown){
        Intent data =new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown);
        setResult(RESULT_OK, data);
    }

    //解析出是否作弊，返回给QuizActivity
    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN,false);
    }

    //将mAnswerIsShown保存在savedInstanceState中
    // 避免屏幕翻转导致Activity被摧毁后重新创建，始终初始化mAnswerIsShown=false,消除作弊记录
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putBoolean(EXTRA_ANSWER_SHOWN,mAnswerIsShown);
    }

}




