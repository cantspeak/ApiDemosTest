package com.example.android.apis.accessibility;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import com.example.android.apis.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 13-6-5.
 */
public class AccessibilityNodeProviderActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.accessibility_node_provider);
//    }
//
//    public static class VirtualSubtreeRootView extends View {
//
//        /**画笔对象为画子view*/
//        private final Paint mPaint = new Paint();
//
//        /**Temporary ractangle to minimize object createion.*/
//        private final Rect mTempRect = new Rect();
//
//        /**Handle to the system accessibility service.*/
//        private final AccessibilityManager mAccessibilityManager;
//
//        /**The virtual children of this view.*/
//        private final List<VirtualView> mChildren = new ArrayList<VirtualView>();
//
//        private AccessibilityNodeProvider mAccessibilityNodeProvider;
//
//        public VirtualSubtreeRootView(Context context, AttributeSet attrs) {
//            super(context, attrs);
//            mAccessibilityManager = (AccessibilityManager)context.getSystemService(
//                    Service.ACCESSIBILITY_SERVICE
//            );
//            createVirtualChildren();
//        }
//
//        /**
//         * Represents a virtual view.
//         * 一个存在的视图,给定所有的参数
//         */
//        private class VirtualView {
//            public static final int ALPHA_SELECTED = 255;
//            public static final int ALPHA_NOT_SELECTED = 127;
//
//            public final int mId;
//            public final int mColor;
//            public final Rect mBounds;
//            public final String mText;
//            public int mAlpha;
//
//            public VirtualView(int id,Rect bounds,int color,String text){
//                mId = id;
//                mColor = color;
//                mBounds = bounds;
//                mText =text;
//                mAlpha = ALPHA_NOT_SELECTED;
//            }
//        }
//
//        /**
//         *
//         */
//        private class VirtualDescendantsProvider extends AccessibilityNodeProvider{
//
//            @Override
//            public AccessibilityNodeInfo createAccessibilityNodeInfo(int virtualViewId) {
//                AccessibilityNodeInfo info = null;
//                if(virtualViewId == View.NO_ID) {
//                    info = AccessibilityNodeInfo.obtain(VirtualSubtreeRootView.this);
//                    onInitializeAccessibilityNodeInfo(info);
//
//
//                }
//
//                return super.createAccessibilityNodeInfo(virtualViewId);
//            }
//        }
//
//    }
}
