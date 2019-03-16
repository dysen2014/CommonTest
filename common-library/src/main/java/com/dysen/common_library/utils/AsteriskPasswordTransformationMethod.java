package com.dysen.common_library.utils;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

/**
 * @package com.zsdk.exchange.util
 * @email dy.sen@qq.com
 * created by dysen on 2019/3/13 - 11:09
 * @info
 */
public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {


    public static AsteriskPasswordTransformationMethod getInstance() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private class PasswordCharSequence implements CharSequence {
        private CharSequence mSource;

        public PasswordCharSequence(CharSequence source) {
            mSource = source; // Store char sequence
        }

        public char charAt(int index) {
            return '*'; // This is the important part
        }

        public int length() {
            return mSource.length(); // Return default
        }

        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end); // Return default
        }
    }
}