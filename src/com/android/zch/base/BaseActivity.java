package com.android.zch.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.android.zch.R;
import com.android.zch.wifi.WifiHelper;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**   
* @Title: BaseActivity.java 
* @Package com.android.touchjet.base 
* @Description: TODO
* @author zch:qw8shop@gmail.com  
* @date 2015-1-29 下午1:21:36 
* @version V1.0   
*/
public abstract class BaseActivity extends FragmentActivity {
    protected static final String GlobalSharedName = "LocalUserInfo"; // SharedPreferences文件名
    protected static LinkedList<BaseActivity> queue = new LinkedList<BaseActivity>();// 打开的activity队列
    protected WifiHelper wifiHelper;

    private static int notificationMediaplayerID;
    private static SoundPool notificationMediaplayer;
    private static Vibrator notificationVibrator;
    
    

    protected List<AsyncTask<Void, Void, Boolean>> mAsyncTasks = new ArrayList<AsyncTask<Void, Void, Boolean>>();

    /**
     * 屏幕的宽度、高度、密度
     */
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected float mDensity;

    protected Long exitTime = (long) 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        mDensity = metric.density;

//        if (!queue.contains(this)) {
//            queue.add(this);
//        }
//        if (notificationMediaplayer == null) {
//            notificationMediaplayer = new SoundPool(3,
//                    AudioManager.STREAM_SYSTEM, 5);
//            notificationMediaplayerID = notificationMediaplayer.load(this,
//                    R.raw.crystalring, 1);
//        }
//        if (notificationVibrator == null) {
//            notificationVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
//        }

    }

    // @Override
    // public void onBackPressed() { // 返回桌面
    // if (MainTabActivity.getIsTabActive()) {
    // Intent intent = new Intent(Intent.ACTION_MAIN);
    // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    // intent.addCategory(Intent.CATEGORY_HOME);
    // startActivity(intent);
    // }
    // else {
    // super.onBackPressed();
    // }
    // }

    @Override
    public void onBackPressed() { // 返回桌面
            System.out.println(System.currentTimeMillis() - exitTime);
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showShortToast(R.string.back_desk);
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
    }

    @Override
    protected void onDestroy() {
        clearAsyncTask();
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
//        queue.removeLast();
    }

    /** 初始化视图 **/
    protected abstract void initViews();

    /** 初始化事件 **/
    protected abstract void initEvents();

    protected void putAsyncTask(AsyncTask<Void, Void, Boolean> asyncTask) {
        mAsyncTasks.add(asyncTask.execute());
    }

    /** 清理异步处理事件 */
    protected void clearAsyncTask() {
        Iterator<AsyncTask<Void, Void, Boolean>> iterator = mAsyncTasks.iterator();
        while (iterator.hasNext()) {
            AsyncTask<Void, Void, Boolean> asyncTask = iterator.next();
            if (asyncTask != null && !asyncTask.isCancelled()) {
                asyncTask.cancel(true);
            }
        }
        mAsyncTasks.clear();
    }


//    protected void showLoadingDialog(String text) {
//        if (text != null) {
//            mLoadingDialog.setText(text);
//        }
//        mLoadingDialog.show();
//    }
//
//    protected void dismissLoadingDialog() {
//        if (mLoadingDialog.isShowing()) {
//            mLoadingDialog.dismiss();
//        }
//    }

    /** 短暂显示Toast提示(来自res) **/
    protected void showShortToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

    /** 短暂显示Toast提示(来自String) **/
    protected void showShortToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /** 长时间显示Toast提示(来自res) **/
    protected void showLongToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show();
    }

    /** 长时间显示Toast提示(来自String) **/
    protected void showLongToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

//    /** 显示自定义Toast提示(来自res) **/
//    protected void showCustomToast(int resId) {
//        View toastRoot = LayoutInflater.from(BaseActivity.this).inflate(
//                R.layout.common_toast, null);
//        ((HandyTextView) toastRoot.findViewById(R.id.toast_text)).setText(getString(resId));
//        Toast toast = new Toast(BaseActivity.this);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.setView(toastRoot);
//        toast.show();
//    }

//    /** 显示自定义Toast提示(来自String) **/
//    protected void showCustomToast(String text) {
//        View toastRoot = LayoutInflater.from(BaseActivity.this).inflate(
//                R.layout.common_toast, null);
//        ((HandyTextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
//        Toast toast = new Toast(BaseActivity.this);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.setView(toastRoot);
//        toast.show();
//    }

    /** Debug输出Log日志 **/
    protected void showLogDebug(String tag, String msg) {
        Log.d(tag, msg);
    }

    /** Info输出Log日志 **/
    protected void showLogInfo(String tag, String msg) {
        Log.i(tag, msg);
    }

    /** Error输出Log日志 **/
    protected void showLogError(String tag, String msg) {
        Log.e(tag, msg);
    }

    /** 通过Class跳转界面 **/
    protected void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }
    /** 含有Bundle通过Class跳转界面 **/
    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /** 通过Action跳转界面 **/
    protected void startActivity(String action) {
        startActivity(action, null);
    }

    /** 含有Bundle通过Action跳转界面 **/
    protected void startActivity(String action, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /** 含有标题和内容的对话框 **/
    protected AlertDialog showAlertDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
                                                               .setMessage(
                                                                       message)
                                                               .show();
        return alertDialog;
    }

    /** 含有标题、内容、两个按钮的对话框 **/
    protected AlertDialog showAlertDialog(String title, String message, String positiveText, DialogInterface.OnClickListener onPositiveClickListener, String negativeText, DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
                                                               .setMessage(
                                                                       message)
                                                               .setPositiveButton(
                                                                       positiveText,
                                                                       onPositiveClickListener)
                                                               .setNegativeButton(
                                                                       negativeText,
                                                                       onNegativeClickListener)
                                                               .show();
        return alertDialog;
    }

    /** 含有标题、内容、图标、两个按钮的对话框 **/
    protected AlertDialog showAlertDialog(String title, String message, int icon, String positiveText, DialogInterface.OnClickListener onPositiveClickListener, String negativeText, DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
                                                               .setMessage(
                                                                       message)
                                                               .setIcon(icon)
                                                               .setPositiveButton(
                                                                       positiveText,
                                                                       onPositiveClickListener)
                                                               .setNegativeButton(
                                                                       negativeText,
                                                                       onNegativeClickListener)
                                                               .show();
        return alertDialog;
    }

    /**
     * 消息处理
     * 
     * <p>
     * 相关子类需要重写此函数，以完成各自的UI更新
     * 
     * @param msg
     *            接收到的消息对象
     */
    public abstract void processMessage(android.os.Message msg);

    /**
     * 新消息提醒 - 包括声音提醒、振动提醒
     */
//    public static void playNotification() {
//        if (BaseApplication.getSoundFlag()) {
//            notificationMediaplayer.play(notificationMediaplayerID, 1, 1, 0, 0,
//                    1);
//        }
//        if (BaseApplication.getVibrateFlag()) {
//            notificationVibrator.vibrate(200);
//        }
//
//    }

//    public static void sendEmptyMessage(int what) {
//        handler.sendEmptyMessage(what);
//    }
//
//    public static void sendMessage(android.os.Message msg) {
//        handler.sendMessage(msg);
//    }

//    private static Handler handler = new Handler() {
//        @Override
//        public void handleMessage(android.os.Message msg) {
//            MainTabActivity.sendEmptyMessage(); // 更新Tab信息
//            if (queue.size() > 0)
//                queue.getLast().processMessage(msg);
//
//            playNotification(); // 新消息响提醒
//
//        }
//    };
}
