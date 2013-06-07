package com.example.android.apis.animation;

import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

/**
 * 形状提供者,一个数据结构,它持有的形状和各种属性,可用于定义如何绘制形状
 */
public class ShapeHolder {

    private float x = 0;
    private float y = 0;
    private ShapeDrawable shape;//图片
    private int color;
    private RadialGradient gradient;//一个指定圆心和半径的径向渐变
    private float alpha = 1f;//透明度
    private Paint paint;//画笔

    public ShapeHolder(ShapeDrawable s) {
        setShape(s);
    }

    /******************************************/
    /*      下面都是对私有字段的get\set方法      */
    /******************************************/

    public void setPaint(Paint value) {
        this.paint = value;
    }

    public Paint getPaint(){
        return paint;
    }

    public void setX(float value) {
        this.x = value;
    }

    public float getX() {
        return this.x;
    }

    public void setY(float value) {
        this.y = value;
    }

    public float getY() {
        return this.y;
    }


    public ShapeDrawable getShape() {
        return shape;
    }

    public void setShape(ShapeDrawable shape) {
        this.shape = shape;
    }

    public int getColor() {
        return color;
    }

    /**
     * 设置Paint的着色
     * @param color
     */
    public void setColor(int color) {
        shape.getPaint().setColor(color);
        this.color = color;
    }

    public RadialGradient getGradient() {
        return gradient;
    }

    public void setGradient(RadialGradient gradient) {
        this.gradient = gradient;
    }

    public float getAlpha() {
        return alpha;
    }

    /**
     * 设置shape的透明 度
     * @param alpha 范围应该为[0,1]
     */
    public void setAlpha(float alpha) {
        this.alpha = alpha;
        shape.setAlpha((int)(alpha * 255f + .5f));//
    }

    /**
     * 取得Shape的宽度
     * @return
     */
    public float getWidth() {
        return shape.getShape().getWidth();
    }

    /**
     * 设置ShapeDrawable中的Shape的宽度
     * @param width
     */
    public void setWidth(float width){
        Shape s = shape.getShape();
        s.resize(width,s.getHeight());
    }

    /**
     * 取得Shape的高度
     * @return
     */
    public float getHeight() {
        return shape.getShape().getHeight();
    }

    /**
     * 设置ShapeDrawable中的Shape的高度
     * @param height
     */
    public void setHeight(float height) {
        Shape s = shape.getShape();
        s.resize(s.getWidth(),height);
    }
}
