package com.dysen.common_library.views.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import com.dysen.common_library.R;

import java.util.List;

/**
 * 数字键盘。
 *
 */
public class NumberKeyboardView extends KeyboardView implements KeyboardView.OnKeyboardActionListener {
    //删除
    private static final int KEYCODE_DELECT= -101;
    //清空
    private static final int KEYCODE_CLEAR= -103;
    //确认
    private static final int KEYCODE_CONFIRM= -102;
    //隐藏
    private static final int KEYCODE_HIDE= -104;
    //向上
    private static final int KEYCODE_UPPER= -105;
    //向下
    private static final int KEYCODE_DOWN= -106;
    //无效按键
    private static final int KEYCODE_INVALID= -180;
    private Canvas mCanvas;
    private Context mContext;
    private IOnKeyboardListener mOnKeyboardListener;

    public NumberKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public NumberKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mContext=context;
        // 设置软键盘按键的布局
        Keyboard keyboard = new Keyboard(context, R.xml.keyboard_number);
        setKeyboard(keyboard);
        setEnabled(true);
        setPreviewEnabled(false); // 设置按键没有点击放大镜显示的效果
        setOnKeyboardActionListener(this);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas=canvas;

    }
    public void setConfirmKey(final String str){
        // 遍历所有的按键
        Keyboard keyboard = getKeyboard();
        if (keyboard != null && keyboard.getKeys() != null && keyboard.getKeys().size() > 0) {
            List<Keyboard.Key> keys = getKeyboard().getKeys();
            for (Keyboard.Key key : keys) {
                // 如果是确认按键，重画按键的背景
                if (key.codes[0] == KEYCODE_CONFIRM) {
                    key.label = str;
                    //key.onReleased(false);
                }
            }
        }
        setKeyboard(keyboard);
    }
    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        // 处理按键的点击事件
        // 点击了删除按键
        if (primaryCode == KEYCODE_DELECT) {
            if (mOnKeyboardListener != null)
                mOnKeyboardListener.onDeleteKeyEvent();
        }
        //点击了清空键
        else if (primaryCode == KEYCODE_CLEAR) {
            if (mOnKeyboardListener != null){
                mOnKeyboardListener.onClearKeyEvent();
            }

        }
        //点击了隐藏键
        else if (primaryCode == KEYCODE_HIDE) {
            if (mOnKeyboardListener != null){
                mOnKeyboardListener.onHideKeyEvent();
            }

        }
        //点击了确认键
        else if (primaryCode == KEYCODE_CONFIRM) {
            if (mOnKeyboardListener != null)
                mOnKeyboardListener.onConfirmKeyEvent();
        }
        //点击了向上键
        else if (primaryCode == KEYCODE_UPPER) {
            if (mOnKeyboardListener != null){
                mOnKeyboardListener.onUpperKeyEvent();
            }

        }
        //点击了向下键
        else if (primaryCode == KEYCODE_DOWN) {
            if (mOnKeyboardListener != null){
                mOnKeyboardListener.onDownKeyEvent();
            }

        }
        //点击了无效键
        else if (primaryCode == KEYCODE_INVALID) {
            if (mOnKeyboardListener != null){
                mOnKeyboardListener.onInvalidKeyEvent();
            }

        }
        // 点击了数字按键
        else {
            if (mOnKeyboardListener != null) {
                mOnKeyboardListener.onInsertKeyEvent(Character.toString((char) primaryCode));
            }
        }
    }


    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
    /**
     * 设置键盘的监听事件。
     *
     * @param listener 监听事件
     */
    public void setIOnKeyboardListener(IOnKeyboardListener listener) {
        this.mOnKeyboardListener = listener;
    }

    /**
     * 键盘的监听事件。
     */
    public interface IOnKeyboardListener {

        /**
         * 点击数字按键。
         *
         * @param text 输入的数字
         */
        void onInsertKeyEvent(String text);

        /**
         * 点击了删除按键。
         */
        void onDeleteKeyEvent();
        /**
         * 点击了确认按键。
         */
        void onConfirmKeyEvent();
        /**
         * 点击了隐藏按键。
         */
        void onHideKeyEvent();
        /**
         * 点击了清空按键。
         */
        void onClearKeyEvent();
        /**
         * 点击了向上按键。
         */
        void onUpperKeyEvent();
        /**
         * 点击了向下按键。
         */
        void onDownKeyEvent();
        /**
         * 点击了无效按键。
         */
        void onInvalidKeyEvent();
    }
}
