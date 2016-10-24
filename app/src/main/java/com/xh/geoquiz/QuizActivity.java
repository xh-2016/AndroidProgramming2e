package com.xh.geoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG="QuizActivity";
    private static final String KEY_INDEX="index";
    private static final int REQUEST_CODE_CHEAT=0;
    private static final String KEY_IS_CHEATER="cheater";
    private boolean mIsCheater;
    private int cheatId;//记录作弊的问题id

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPervButton;
    private Button mCheatButtom;
    private TextView mQuestionTextView;

    private  Question[] mQuestionBank=new Question[]{
            new Question(R.string.question_oceans,true),
            new Question(R.string.question_mideast,false),
            new Question(R.string.question_africa,false),
            new Question(R.string.question_americas,true),
            new Question(R.string.question_asia,false),
    };

    private int mCurrentIndex=0;

    private void updateQuestion(){
//        Log.d(TAG,"Updating question text for question #"+mCurrentIndex,new Exception());
        int question=mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    //检查答案是否正确
    private void checkAnswer(boolean userPressedTrue){
        boolean isAnswerTrue=mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId=0;

        //判断如果有人作弊，并且当前题号等于作弊题号，即为作弊
        if(mIsCheater && cheatId == mCurrentIndex){
            messageResId=R.string.judgment_toast;
        }else{
            if(userPressedTrue==isAnswerTrue){
                messageResId=R.string.correct_toast;
            }
            else{
                messageResId=R.string.incorrect_toast;
            }
        }
        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Buddle) called");
        setContentView(R.layout.activity_quiz);

        //savedInsatanceState保存屏幕翻转前的QuestionId及是否作弊的记录
        if(savedInstanceState!=null){
            mCurrentIndex=savedInstanceState.getInt(KEY_INDEX, 0);
            //记录作弊的题号
            cheatId=savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater=savedInstanceState.getBoolean(KEY_IS_CHEATER,false);
        }

        mQuestionTextView= (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mTrueButton= (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        mFalseButton= (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        //cheat answer
        mCheatButtom= (Button) findViewById(R.id.cheat_button);
        mCheatButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start CheatActivity
               boolean answerIsTrue=mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i=CheatActivity.newIntent(QuizActivity.this,answerIsTrue);
                //startActivity(i);
                startActivityForResult(i,REQUEST_CODE_CHEAT);
            }
        });

        mPervButton= (ImageButton) findViewById(R.id.prev_button);
        mPervButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex=(mCurrentIndex-1+mQuestionBank.length)%mQuestionBank.length;
                //mIsCheater=false;
                updateQuestion();
            }
        });

        mNextButton= (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex=(mCurrentIndex+1) % mQuestionBank.length;
                //mIsCheater=false;
                updateQuestion();
            }
        });
        updateQuestion();
    }

    @Override
    protected  void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode!=RESULT_OK){
            return;
        }
        if(requestCode==REQUEST_CODE_CHEAT){
            if(data==null){
                return;
            }
            mIsCheater=CheatActivity.wasAnswerShown(data);
        }
    }

    //将mCurrentIndex、mIsCheater保存在savedInstanceState中
    // 避免屏幕翻转导致Activity被摧毁后重新创建，始终初始化mCurrentIndex=0、消除cheat记录
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(KEY_IS_CHEATER,mIsCheater);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
   public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}
