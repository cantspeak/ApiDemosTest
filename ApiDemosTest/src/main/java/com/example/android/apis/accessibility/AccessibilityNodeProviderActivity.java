package com.example.android.apis.accessibility;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import com.example.android.apis.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 未完成
 */
public class AccessibilityNodeProviderActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accessibility_node_provider);
    }

    public static class VirtualSubtreeRootView extends View {

        /**画笔对象为画子view*/
        private final Paint mPaint = new Paint();

        /**Temporary ractangle to minimize object createion.*/
        private final Rect mTempRect = new Rect();

        /**Handle to the system accessibility service.*/
        private final AccessibilityManager mAccessibilityManager;

        /**The virtual children of this view.*/
        private final List<VirtualView> mChildren = new ArrayList<VirtualView>();

        private AccessibilityNodeProvider mAccessibilityNodeProvider;

        public VirtualSubtreeRootView(Context context, AttributeSet attrs) {
            super(context, attrs);
            mAccessibilityManager = (AccessibilityManager)context.getSystemService(
                    Service.ACCESSIBILITY_SERVICE
            );
            createVirtualChildren();
        }

        private void createVirtualChildren() {

            VirtualView firstChild = new VirtualView(0, new Rect(0,0,150,150) , Color.RED,
                    "Virtual view 1");
            VirtualView secondChild = new VirtualView(0, new Rect(0,0,150,150) , Color.RED,
                    "Virtual view 2");
            VirtualView thirdChild = new VirtualView(0, new Rect(0,0,150,150) , Color.RED,
                    "Virtual view 3");

        }

        private boolean onHoverVirtualView(VirtualView virtualView,MotionEvent event) {
            //徘徊动作分配能被已知的任何适当的方法实现.
            final int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_HOVER_ENTER: {
                    sendAccessibilityEventForVirtualView(virtualView,
                            AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
                }break;
                case MotionEvent.ACTION_HOVER_EXIT:{
                    sendAccessibilityEventForVirtualView(virtualView,
                            AccessibilityEvent.TYPE_VIEW_HOVER_EXIT);
                }
            }
            return true;
        }

        /**
         *
         * @param virtualView
         * @param eventType
         */
        private void sendAccessibilityEventForVirtualView(VirtualView virtualView, int eventType) {

            if(mAccessibilityManager.isTouchExplorationEnabled()) {
                AccessibilityEvent event = AccessibilityEvent.obtain(eventType);
                event.setPackageName(getContext().getPackageName());
                event.setClassName(virtualView.getClass().getName());
                event.setSource(VirtualSubtreeRootView.this,virtualView.mId);
                event.getText().add(virtualView.mText);
                getParent().requestSendAccessibilityEvent(VirtualSubtreeRootView.this,event);

            }
        }

        /**
         * Represents a virtual view.
         * 一个存在的视图,给定所有的参数
         */
        private class VirtualView {
            public static final int ALPHA_SELECTED = 255;
            public static final int ALPHA_NOT_SELECTED = 127;

            public final int mId;
            public final int mColor;
            public final Rect mBounds;
            public final String mText;
            public int mAlpha;

            public VirtualView(int id,Rect bounds,int color,String text){
                mId = id;
                mColor = color;
                mBounds = bounds;
                mText =text;
                mAlpha = ALPHA_NOT_SELECTED;
            }
        }

        /**
         * 在这个View的所有的VirtualView中查找指定id的VirtualView
         * @param id 指定VirtualView的id
         * @return 返回指定id的VirtualView,如果不存在返回null
         */
        private VirtualView findVirtualViewById(int id) {
            //这个view的所的VirtualView中查找指定id的
            List<VirtualView> children = mChildren;
            final int childCount = children.size();
            for (int i = 0; i < childCount; i++) {
                VirtualView child = children.get(i);
                if(child.mId == id) {
                    return child;
                }
            }
            return null;
        }


        /**
         * 可访问性Node提供者,如果使用系统的UI组件,但自定义的视图,需要实现这个接口(AccessibilityNoderProvider)
         * question:
         * 1.what is VirtualView?
         * 2.AccessibilityNoderProvider做什么用的?
         */
        private class VirtualDescendantsProvider extends AccessibilityNodeProvider{

            @Override
            public AccessibilityNodeInfo createAccessibilityNodeInfo(int virtualViewId) {
                AccessibilityNodeInfo info = null;
                if(virtualViewId == View.NO_ID) {
                    info = AccessibilityNodeInfo.obtain(VirtualSubtreeRootView.this);
                    onInitializeAccessibilityNodeInfo(info);
                    //Add the virtual childen of the root view.添加根视图的孩子
                    List<VirtualView> children = mChildren;
                    final int childCount = children.size();
                    for (int i = 0;i < childCount;i++) {
                        VirtualView child = children.get(i);
                        info.addChild(VirtualSubtreeRootView.this,child.mId);
                    }
                } else {
                    //Find the view that corresponds to the fiven id.
                    VirtualView virtualView = findVirtualViewById(virtualViewId);
                    if (virtualView == null) {
                        return null;
                    }
                    // Obtain and initialize an AccessibilityNodeInfo with
                    //给虚拟view获得并初始化一个AccessibilityNodeInfo信息
                    // information about the virtual view.
                    info = AccessibilityNodeInfo.obtain();
                    info.addAction(AccessibilityNodeInfo.ACTION_SELECT);
                    info.addAction(AccessibilityNodeInfo.ACTION_CLEAR_SELECTION);
                    info.setPackageName(getContext().getPackageName());
                    info.setClassName(virtualView.getClass().getName());
                    info.setSource(VirtualSubtreeRootView.this,virtualViewId);
                    info.setParent(VirtualSubtreeRootView.this);
                    info.setText(virtualView.mText);
                }

                return info;
            }

            @Override
            public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText (String searched,
                                                                               int virtualViewId) {
                if (TextUtils.isEmpty(searched)) {
                    return Collections.emptyList();
                }
                String searchedLowerCase = searched.toLowerCase();
                List<AccessibilityNodeInfo> result = null;
                if (virtualViewId == View.NO_ID) {
                    List<VirtualView> children = mChildren;
                    final int childCount = children.size();
                    for(int i = 0;i < childCount; i++) {
                        VirtualView child = children.get(i);
                    }
                }
                return null;
            }
        }

    }
}
