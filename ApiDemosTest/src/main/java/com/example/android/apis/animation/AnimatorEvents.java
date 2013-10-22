package com.example.android.apis.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.android.apis.R;

/**
 * Created by root on 13-9-5.
 */
public class AnimatorEvents extends Activity{
    private static final String DEBUG_ANIMATOREVENTS = "AnimatorEvents";
    TextView startText, repeatText, cancelText , endText;
    TextView startTextAnimator, repeatTextAnimator, cancelTextAnimator , endTextAnimator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animator_events);
        LinearLayout layout = (LinearLayout) findViewById(R.id.container);
        final MyAnimationView myView = new MyAnimationView(this);
        layout.addView(myView);

        startText = (TextView) findViewById(R.id.startText);
        Log.e(DEBUG_ANIMATOREVENTS,""+startText.getAlpha());
        startText.setAlpha(.5f);
        repeatText = (TextView) findViewById(R.id.repeatText);
        repeatText.setAlpha(.5f);
        cancelText = (TextView) findViewById(R.id.cancelText);
        cancelText.setAlpha(.5f);
        endText = (TextView) findViewById(R.id.endText);
        endText.setAlpha(.5f);

        startTextAnimator = (TextView) findViewById(R.id.startTextAnimator);
        startTextAnimator.setAlpha(.5f);
        repeatTextAnimator = (TextView) findViewById(R.id.repeatTextAnimator);
        repeatTextAnimator.setAlpha(.5f);
        cancelTextAnimator = (TextView) findViewById(R.id.cancelTextAnimator);
        cancelTextAnimator.setAlpha(.5f);
        endTextAnimator = (TextView) findViewById(R.id.endTextAnimator);
        endTextAnimator.setAlpha(.5f);

        final CheckBox endCB = (CheckBox)findViewById(R.id.endCB);
        Button playButton = (Button) findViewById(R.id.startButton);
        Button cancalButton = (Button) findViewById(R.id.cancelButton);
        Button endButton = (Button) findViewById(R.id.endButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myView.startAnimation(endCB.isChecked());
            }
        });

        cancalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myView.cancelAnimation();
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something
                myView.endAnimation();
            }
        });
    }

    class MyAnimationView extends View implements Animator.AnimatorListener,
    ValueAnimator.AnimatorUpdateListener{

        ShapeHolder ball = null;
        AnimatorSet animation = null;
        boolean endImmediately = false;

        public MyAnimationView(Context context) {
            super(context);
            ball = createBall(50,50);
        }

        public void createAnimation(){
            if (null == animation){
                ObjectAnimator yAnim = ObjectAnimator.ofFloat(ball, "y",
                        ball.getY(),getHeight()-50f).setDuration(1500);
                yAnim.setRepeatCount(0);
                yAnim.setRepeatMode(ValueAnimator.REVERSE);
                yAnim.setInterpolator(new AccelerateInterpolator(2f));
                yAnim.addUpdateListener(this);
                yAnim.addListener(this);

                ObjectAnimator xAnim = ObjectAnimator.ofFloat(ball,"x",
                        ball.getX(),ball.getX() + 301).setDuration(1500);
                xAnim.setStartDelay(0);
                xAnim.setRepeatCount(0);
                xAnim.setRepeatMode(ValueAnimator.REVERSE);
                xAnim.setInterpolator(new AccelerateInterpolator(2f));

                ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(ball,"alpha",1f,.5f).
                        setDuration(1000);
                AnimatorSet alphaSeq = new AnimatorSet();
                alphaSeq.play(alphaAnim);

                animation = new AnimatorSet();
                ((AnimatorSet)animation).playTogether(yAnim,xAnim);
                animation.addListener(this);
            }
        }

        public void startAnimation(boolean endImmediately){
            if (null != animation) {
                animation.removeAllListeners();
                animation.cancel();
            }
            this.endImmediately = endImmediately;
            startText.setAlpha(.5f);
            repeatText.setAlpha(.5f);
            cancelText.setAlpha(.5f);
            endText.setAlpha(.5f);
            startTextAnimator.setAlpha(.5f);
            repeatTextAnimator.setAlpha(.5f);
            cancelTextAnimator.setAlpha(.5f);
            endTextAnimator.setAlpha(.5f);
            createAnimation();
            animation.start();
        }

        public void cancelAnimation(){
//            if (null != animation) {
//                animation.removeAllListeners();
//                animation.end();
//            }
            createAnimation();
            animation.cancel();
        }

        public void endAnimation(){
//            if (null != animation) {
//                animation.removeAllListeners();
//                animation.end();
//            }
            createAnimation();
            animation.end();
        }

        @Override
        public void onAnimationStart(Animator animation) {
            if (animation instanceof AnimatorSet) {
                startText.setAlpha(1f);
            } else {
                startTextAnimator.setAlpha(1f);
            }
            if (endImmediately){
                animation.end();
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if(animation instanceof AnimatorSet) {
                endText.setAlpha(1f);
            } else {
                endTextAnimator.setAlpha(1f);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            if (animation instanceof AnimatorSet){
                cancelText.setAlpha(1f);
            } else {
                cancelTextAnimator.setAlpha(1f);
            }
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            if(animation instanceof AnimatorSet){
                repeatText.setAlpha(1f);
            }else{
                repeatTextAnimator.setAlpha(1f);
            }
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.save();
            canvas.translate(ball.getX(),ball.getY());
            ball.getShape().draw(canvas);
            canvas.restore();
        }



        public ShapeHolder createBall(float x, float y){
            OvalShape circle = new OvalShape();
            circle.resize(x, y);
            ShapeDrawable drawable = new ShapeDrawable(circle);
            ShapeHolder shapeHolder = new ShapeHolder(drawable);
            shapeHolder.setX(x - 25f);
            shapeHolder.setY(y - 25f);
            int red = (int) (Math.random()*255);
            int green = (int) (Math.random()*255);
            int blue = (int) (Math.random()*255);
            int color = 0xff000000 | red << 16 | green << 8 | blue ;
            Paint paint = drawable.getPaint();
            int darkColor = 0xff000000 | red /4 << 16 | green /4 << 8 | blue /4;
            RadialGradient gradient = new RadialGradient(37.5f, 12.5f,
                    50f,color,darkColor, Shader.TileMode.CLAMP);
            paint.setShader(gradient);
            shapeHolder.setPaint(paint);
            return shapeHolder;

        }
    }
}
