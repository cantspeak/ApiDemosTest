package com.example.android.apis.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import com.example.android.apis.R;

import java.util.ArrayList;

/**
 * Created by root on 13-7-12.
 */
public class AnimationCloning2 extends Activity{
    ArrayList<ShapeHolder> balls = new ArrayList<ShapeHolder>();
    AnimatorSet animation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_cloning);
        LinearLayout container = (LinearLayout)findViewById(R.id.container);
        final MyAnimationView animView = new MyAnimationView(this);
        container.addView(animView);
        Button starter = (Button) findViewById(R.id.startButton);
        starter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                animView.startAnimation();
            }
        });
    }

    public class MyAnimationView extends View implements ValueAnimator.AnimatorUpdateListener{

        public MyAnimationView(Context context) {
            super(context);
        }

        private ShapeHolder addBall(float x,float y){
           /* OvalShape circle = new OvalShape();
            circle.resize(50*mDensity,50*mDensity);
            ShapeDrawable drawable = new ShapeDrawable(circle);
            ShapeHolder shapeHolder = new ShapeHolder(drawable);
            shapeHolder.setX(x-25f);
            shapeHolder.setY(y-25f);
            shapeHolder.setPaint(drawable.getPaint());
            balls.add(shapeHolder);
            return shapeHolder;
            */return null;
        }

        public void startAnimation(){
            onCreateAnimator();
            animation.start();
        }

        private void onCreateAnimator() {
            if(animation == null){
                ObjectAnimator anim1 = ObjectAnimator.ofFloat(balls.get(0),"y",
                        0f,getHeight()).setDuration(500);
                ObjectAnimator anim2 = anim1.clone();
                anim2.setTarget(balls.get(1));

                anim1.addUpdateListener(this);
                ShapeHolder ball2 = balls.get(2);
                ObjectAnimator animDown = ObjectAnimator.ofFloat(ball2,"y",
                        0f,getHeight()-ball2.getHeight()).setDuration(500);
                ObjectAnimator animUp = ObjectAnimator.ofFloat(ball2,"y",
                        getHeight()-ball2.getHeight(),0f).setDuration(500);//所在ｖｉｅｗ的高-自身的高
                animDown.setInterpolator(new AccelerateInterpolator());
                animUp.setInterpolator(new DecelerateInterpolator());
                //给第三个球设置动画
                AnimatorSet s1 = new AnimatorSet();
                s1.playSequentially(animDown,animUp);
                animDown.addUpdateListener(this);

                AnimatorSet s2 = (AnimatorSet)s1.clone();

            }
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {

        }
    }
}
