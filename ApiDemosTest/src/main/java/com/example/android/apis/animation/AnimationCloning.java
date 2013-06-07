package com.example.android.apis.animation;

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
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import com.example.android.apis.R;

import java.util.ArrayList;

/**
 * 创建四个球,并模拟下落动画
 */
public class AnimationCloning extends Activity{


    /**
     * 覆盖父类的onCreate方法,并初始化一些参数
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_cloning);
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        final MyAnimationView animView = new MyAnimationView(this);
        container.addView(animView);
        Button starter = (Button) findViewById(R.id.startButton);
        starter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animView.startAnimation();
            }
        });


    }

    /**
     * 自定义一个动画View,并实现ValueAnimator.AnimatorUpdateListener监听
     */
    public class MyAnimationView extends View implements ValueAnimator.AnimatorUpdateListener {

        public final ArrayList<ShapeHolder> balls = new ArrayList<ShapeHolder>();//存放所有Shape对象,这里是四个球.
        AnimatorSet animation = null;//通过实现playTogether()orplaySequentially()方法,可以实现动画的回调
        private float mDensity;//密度


        /**
         * 构造方法
         * @param context
         */
        public MyAnimationView(Context context) {
            super(context);
            mDensity = getContext().getResources().getDisplayMetrics().density;//逻辑密度

            //创建四个自定义的形状
            ShapeHolder ball0 = addBall(50f,25f);
            ShapeHolder ball1 = addBall(150f,25f);
            ShapeHolder ball2 = addBall(250f,25f);
            ShapeHolder ball3 = addBall(250f,25f);

        }

        /**************************************************************/
        /******************   重点在这里,创建动画    ********************/
        /**************************************************************/
        private void createAnimation() {
            //如果动画还没有定义
            if( animation == null) {
                /*第一个动画*/
                //把Y属性从开始位置,移动到
                ObjectAnimator anim1 = ObjectAnimator.ofFloat(
                        balls.get(0),//第一个球
                        "y",//y坐标
                        0f,//原来的位置
                        getHeight() - balls.get(0).getHeight()//所在View的高 - 球的高
                ).setDuration(500);//把指定的属性,从开始值变到结束值
                /*第二个动画*/
                ObjectAnimator anim2 = anim1.clone();//anim2是克隆的一个anim1的副本
                anim2.setTarget(balls.get(1));

                anim1.addUpdateListener(this);
                /*第三个动画的资源*/
                ShapeHolder ball2 = balls.get(2);
                ObjectAnimator animDown = ObjectAnimator.ofFloat(
                        ball2,
                        "y",
                        0f,
                        getHeight()- ball2.getHeight()
                ).setDuration(500);
                ObjectAnimator animUp = ObjectAnimator.ofFloat(
                        ball2,
                        "y",
                        getHeight()- ball2.getHeight(),
                        0f
                ).setDuration(500);
                animDown.setInterpolator(new DecelerateInterpolator());

                AnimatorSet s1 = new AnimatorSet();
                s1.playSequentially(animDown,animUp);//前一个动画结束就放后一个动画
                animDown.addUpdateListener(this);
                animUp.addUpdateListener(this);
                AnimatorSet s2 = (AnimatorSet)s1.clone();
                s2.setTarget(balls.get(3));

                animation = new AnimatorSet();
                animation.playTogether(anim1,anim2,s1);//设置AnimatorSet播放所有动画在同一时间.
                animation.playSequentially(s1,s2);//顺序播放所有动画
            }
        }

        /**
         * 这个是创建形状的
         * @param x
         * @param y
         * @return
         */
        private ShapeHolder addBall(float x,float y) {
            OvalShape circle = new OvalShape();
            circle.resize(50f*mDensity,50f*mDensity);
            ShapeDrawable drawable = new ShapeDrawable(circle);
            ShapeHolder shapeHolder = new ShapeHolder(drawable);
            shapeHolder.setX(x - 25f);
            shapeHolder.setY(y- 25f);
            int red = (int)(100 + Math.random() * 155);
            int green = (int)(100 + Math.random() * 155);
            int blue = (int)(100 + Math.random() * 155);
            int color = 0xff000000 | red << 16 | green << 8 | blue;
            Paint paint = drawable.getPaint();
            int darkColor = 0xff000000 | red/4 << 16 | green/4 << 8 | blue/4;
            RadialGradient gradient = new RadialGradient(37.5f,12.5f,50f,color,darkColor, Shader.TileMode.CLAMP);
            paint.setShader(gradient);
            shapeHolder.setPaint(paint);
            balls.add(shapeHolder);
            return shapeHolder;
        }

        @Override
        protected void onDraw(Canvas canvas){
            for (int i = 0;i< balls.size();i++) {
                ShapeHolder shapeHolder = balls.get(i);
                canvas.save();
                canvas.translate(shapeHolder.getX(), shapeHolder.getY());
                shapeHolder.getShape().draw(canvas);
                canvas.restore();
            }
        }

        public void startAnimation() {
            createAnimation();
            animation.start();
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            invalidate();
        }
    }
}
