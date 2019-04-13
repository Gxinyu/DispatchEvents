package com.example.dispatchevents.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.dispatchevents.OnVideoDragListener;
import com.example.dispatchevents.R;

/**
 * @author gexinyu
 */
public class DragViewGroup extends FrameLayout implements View.OnClickListener {

    private float mDownX;
    private float mDownY;
    private int mRootTopY = 0;
    private int mRootMeasuredWidth = 0;
    private int mRootMeasuredHeight = 0;
    private int minTouchSlop;//系统可以辨别的最小滑动距离
    private boolean mHasMeasuredParent;//测量一次（如果父类动态改变，去掉此判断）

    private Context mContext;
    private CardView cvParentView;
    private ImageView ivCancleView;

    private OnVideoDragListener onVideoDragListener;//拖动监听

    public void setOnVideoDragListener(OnVideoDragListener onVideoDragListener) {
        this.onVideoDragListener = onVideoDragListener;
    }


    public DragViewGroup(@NonNull Context context) {
        this(context, null);
    }

    public DragViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        minTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();

        //初始化布局视图
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_drag_viewgroup, this);
        cvParentView = view.findViewById(R.id.cv_parent);
        ImageView ivVideoIamge = view.findViewById(R.id.iv_video_iamge);
        ivCancleView = view.findViewById(R.id.iv_colse_tiny_video);
        cvParentView.setOnClickListener(this);
        ivCancleView.setOnClickListener(this);

        Glide.with(mContext)
                .load(R.mipmap.beauty)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(ivVideoIamge);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean interceptd = false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                interceptd = false;
                //测量按下位置
                mDownX = event.getX();
                mDownY = event.getY();
                //测量父类的位置和宽高
                if (!mHasMeasuredParent) {
                    ViewGroup mViewGroup = (ViewGroup) getParent();
                    if (mViewGroup != null) {
                        //获取父布局的高度
                        mRootMeasuredHeight = mViewGroup.getMeasuredHeight();
                        mRootMeasuredWidth = mViewGroup.getMeasuredWidth();
                        int top = mViewGroup.getTop();
                        //获取父布局顶点的坐标
                        mRootTopY = mViewGroup.getTop();;
                        mHasMeasuredParent = true;
                    }
                }

                break;

            case MotionEvent.ACTION_MOVE:
                //计算移动距离 判定是否滑动
                float dx = event.getX() - mDownX;
                float dy = event.getY() - mDownY;

                if (Math.abs(dx) > minTouchSlop || Math.abs(dy) > minTouchSlop) {
                    interceptd = true;
                } else {
                    interceptd = false;
                }

                break;

            case MotionEvent.ACTION_UP:
                interceptd = false;
                break;
        }

        return interceptd;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("TAG", "yhhhhhhhh");
                break;
            case MotionEvent.ACTION_MOVE:

                if (mDownX >= 0
                        && mDownY >= mRootTopY
                        && mDownX <= mRootMeasuredWidth
                        && mDownY <= (mRootMeasuredHeight + mRootTopY)) {

                    float dx = event.getX() - mDownX;
                    float dy = event.getY() - mDownY;

                    float ownX = getX();
                    //获取手指按下的距离与控件本身Y轴的距离
                    float ownY = getY();
                    //理论中X轴拖动的距离
                    float endX = ownX + dx;
                    //理论中Y轴拖动的距离
                    float endY = ownY + dy;
                    //X轴可以拖动的最大距离
                    float maxX = mRootMeasuredWidth - getWidth();
                    //Y轴可以拖动的最大距离
                    float maxY = mRootMeasuredHeight - getHeight();
                    //X轴边界限制
                    endX = endX < 0 ? 0 : endX > maxX ? maxX : endX;
                    //Y轴边界限制
                    endY = endY < 0 ? 0 : endY > maxY ? maxY : endY;
                    //开始移动
                    setX(endX);
                    setY(endY);
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        super.onTouchEvent(event);

        return true;
    }

    @Override
    public void onClick(View view) {
        if (view == cvParentView) {
            //点击进入房间
            if (onVideoDragListener != null) {
                onVideoDragListener.onJoinRoom("");
            }
        } else if (view == ivCancleView) {
            //关闭房间
            if (onVideoDragListener != null) {
                onVideoDragListener.onCloseRoom("");
            }
        }
    }

}
