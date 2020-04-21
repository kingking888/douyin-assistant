package vip.mimiya.helper;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;

public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";
    private ClipboardManager clipboard;

    @Override
    public void onCreate() {
        super.onCreate();
        clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.icon);

        FloatWindow
                .with(getApplicationContext())
                .setView(imageView)
                .setWidth(150)                               //设置控件宽高
                .setHeight(Screen.width, 0.2f)
                .setX(0)                                   //设置控件初始位置
                .setY(Screen.height, 0.3f)
                .setDesktopShow(true)                        //桌面显示
                .setViewStateListener(mViewStateListener)    //监听悬浮控件状态改变
                .setPermissionListener(mPermissionListener)  //监听权限申请结果
                .build();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clipData = clipboard.getPrimaryClip();
                String pasteString = "";
                if (clipData != null && clipData.getItemCount() > 0) {
                    CharSequence text = clipData.getItemAt(0).getText();
                    pasteString = text.toString();
                }
                if (TextUtils.isEmpty(pasteString)) return;
                Toast.makeText(BaseApplication.this, "收到任务:[" + pasteString + "]", Toast.LENGTH_LONG).show();

                //TODO POST
            }
        });
    }


    private PermissionListener mPermissionListener = new PermissionListener() {
        @Override
        public void onSuccess() {
            Log.d(TAG, "onSuccess");
        }

        @Override
        public void onFail() {
            Log.d(TAG, "onFail");
        }
    };


    private ViewStateListener mViewStateListener = new ViewStateListener() {
        @Override
        public void onPositionUpdate(int x, int y) {
            Log.d(TAG, "onPositionUpdate: x=" + x + " y=" + y);
        }

        @Override
        public void onShow() {
            Log.d(TAG, "onShow");
        }

        @Override
        public void onHide() {
            Log.d(TAG, "onHide");
        }

        @Override
        public void onDismiss() {
            Log.d(TAG, "onDismiss");
        }

        @Override
        public void onMoveAnimStart() {
            Log.d(TAG, "onMoveAnimStart");
        }

        @Override
        public void onMoveAnimEnd() {
            Log.d(TAG, "onMoveAnimEnd");
        }

        @Override
        public void onBackToDesktop() {
            Log.d(TAG, "onBackToDesktop");
        }
    };
}
