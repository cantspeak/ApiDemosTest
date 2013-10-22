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
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import com.example.android.apis.R;

import java.util.ArrayList;

/**
 * Created by root on 13-9-3.
 */
public class AnimationSeeking extends Activity{
    private static final int DURATION = 1500;
    private SeekBar mSeekBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_seeking);
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        final MyAnimationView animationView = new MyAnimationView(this);
        container.addView(animationView);

        Button starter = (Button)container.findViewById(R.id.startButton);
        starter.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                animationView.startAnimation();
            }
        });

        mSeekBar = (SeekBar)findViewById(R.id.seekBar);
        mSeekBar.setMax(DURATION);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(animationView.getHeight() != 0) {
                    animationView.seek(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public class MyAnimationView extends View implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener{

        private static final int RED = 0xffFF8080;
        private static final int BLUE = 0xff8080FF;
        private static final int CYAN = 0xff80ffff;
        private static final int GREEN = 0xff80ff80;
        private static final float BALL_SIZE = 100f;

        public final ArrayList<ShapeHolder> balls = new ArrayList<ShapeHolder>();
        AnimatorSet animation = null;
        ValueAnimator bounceAnim = null;
        ShapeHolder ball = null;

        public MyAnimationView(Context context) {
            super(context);
            ball = addBall(200, 0);
        }

        public void startAnimation(){
            createAnimation();
            bounceAnim.start();
        }

        private void createAnimation() {
            if (bounceAnim == null) {
                bounceAnim = ObjectAnimator.ofFloat(ball,"y",
                        ball.getY(),getHeight()- BALL_SIZE).setDuration(1000);
                bounceAnim.setInterpolator(new BounceInterpolator());
                bounceAnim.addUpdateListener(this);
            }
        }

        public void seek(long seekTime){
            createAnimation();
            bounceAnim.setCurrentPlayTime(seekTime);

        }

        private ShapeHolder addBall(float x,float y){
            OvalShape circle = new OvalShape();
            circle.resize(MyAnimationView.BALL_SIZE,MyAnimationView.BALL_SIZE);
            ShapeDrawable drawable = new ShapeDrawable(circle);
            ShapeHolder shapeHolder = new ShapeHolder(drawable);
            shapeHolder.setX(x);
            shapeHolder.setY(y);
            int red = (int) (100 + Math.random()*155);
            int green = (int) (100 + Math.random()*155);
            int blue = (int) (100 + Math.random()*155);
            int color = 0xff000000 | red << 16 | green << 8 | blue;
            Paint paint  = drawable.getPaint();
            int darkColor = 0xff000000 | red / 4 << 16 | green / 4 << 8 | blue / 4;
            RadialGradient gradient = new RadialGradient(37.5f,12.5f,
                    50f,color,darkColor, Shader.TileMode.CLAMP);
            paint.setShader(gradient);
            shapeHolder.setPaint(paint);
            balls.add(shapeHolder);
            return shapeHolder;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.translate(ball.getX(),ball.getY());
            ball.getShape().draw(canvas);
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            balls.remove(((ObjectAnimator) animation).getTarget());

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            invalidate();
            //long playtime = bounceAnim.getCurrentPlayTime();
        }

    }
}
