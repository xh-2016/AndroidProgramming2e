package com.xh.geoquiz;

/**
 * Created by xh on 2016/10/10.
 */
public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public  Question(int textResId,boolean answerTrue){
        mTextResId=textResId;
        mAnswerTrue=answerTrue;
    }
}
