package com.market.util;

import android.app.Activity;
import android.widget.Toast;

public class ToastUtil {

    /**
     * 显示toast
     *
     * @param ctx
     * @param msg
     */
    public static void showToast(final Activity ctx, final String msg, final int duration) {
        // 判断是在子线程，还是主线程
        if ("main".equals(Thread.currentThread().getName())) {
            Toast.makeText(ctx, msg, duration).show();
        } else {
            // 子线程
            ctx.runOnUiThread(() -> Toast.makeText(ctx, msg, duration).show());
        }
    }
}
