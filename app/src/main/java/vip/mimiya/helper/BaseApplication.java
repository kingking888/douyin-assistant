package vip.mimiya.helper;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NavUtils;

import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";
    private ClipboardManager clipboardManager;
    private boolean status = false;
    private String lastPasteString;
    //private FloatWindow.B floatWindows;
    private EditText postEditText;

    @Override
    public void onCreate() {
        super.onCreate();
        clipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                ClipData clipData = clipboardManager.getPrimaryClip();
                String pasteString = "";
                if (clipData != null && clipData.getItemCount() > 0) {
                    CharSequence text = clipData.getItemAt(0).getText();
                    pasteString = text.toString();
                }
                if (!status) return;
                if (TextUtils.isEmpty(pasteString)) return;

                String pattern = "(https://v.douyin.com).*/";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(pasteString);
                String shareUrl = null;
                if (m.find()) {
                    shareUrl = m.group(0);
                    postShareUrl(shareUrl);
                    Toast.makeText(BaseApplication.this, "收到任务:[" + pasteString + "]", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(BaseApplication.this, "NO MATCH!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        LayoutInflater inflater = LayoutInflater.from(this);
        View floatLayout = inflater.inflate(R.layout.input_layout, null);

        postEditText = floatLayout.findViewById(R.id.post_editText);
        Button postButton = floatLayout.findViewById(R.id.post_button);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FloatWindow.get().addFocus();
                postEditText.setEnabled(true);
                postEditText.setFocusable(true);
                postEditText.setFocusableInTouchMode(true);
                postEditText.requestFocus();
                postEditText.findFocus();

                String sharp_url = postEditText.getText().toString();
                postEditText.setText("aaa");
                handler.sendEmptyMessage(101);

            }
        });

        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.icon);

        FloatWindow.with(getApplicationContext()).setView(floatLayout)
                //.setWidth(150)                               //设置控件宽高
                .setHeight(Screen.width, 0.2f)
                .setX(0)                                   //设置控件初始位置
                .setY(Screen.height, 0.1f)
                .setDesktopShow(true)                        //桌面显示
                .setViewStateListener(mViewStateListener)    //监听悬浮控件状态改变
                .setPermissionListener(mPermissionListener)  //监听权限申请结果
                .build();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status) ((ImageView) v).setImageResource(R.drawable.icon);
                else ((ImageView) v).setImageResource(R.drawable.icon1);
                status = !status;

                ClipData clipData = clipboardManager.getPrimaryClip();
                String pasteString = "";
                if (clipData != null && clipData.getItemCount() > 0) {
                    CharSequence text = clipData.getItemAt(0).getText();
                    pasteString = text.toString();
                }
                // if (!status) return;
                if (TextUtils.isEmpty(pasteString)) return;

                clipboardManager.setPrimaryClip(ClipData.newPlainText("aaa", "bbb"));

                if (!TextUtils.isEmpty(lastPasteString) && lastPasteString.equals(pasteString))
                    return;


                lastPasteString = pasteString;

                String pattern = "(https://v.douyin.com).*/";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(pasteString);

                String shareUrl = null;
                if (m.find()) {
                    shareUrl = m.group(0);
                    postShareUrl(shareUrl);
                    Toast.makeText(BaseApplication.this, "收到任务:[" + pasteString + "]", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(BaseApplication.this, "NO MATCH!", Toast.LENGTH_SHORT).show();
                }
//                ClipData clipData = clipboardManager.getPrimaryClip();
//                String pasteString = "";
//                if (clipData != null && clipData.getItemCount() > 0) {
//                    CharSequence text = clipData.getItemAt(0).getText();
//                    pasteString = text.toString();
//                }
//                if (TextUtils.isEmpty(pasteString)) return;
//                Toast.makeText(BaseApplication.this, "收到任务:[" + pasteString + "]", Toast.LENGTH_LONG).show();

                //TODO POST
            }
        });
    }

    private void postShareUrl(final String url) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaType JSON = MediaType.get("application/json; charset=utf-8");
                OkHttpClient client = new OkHttpClient();
                String json = String.format("{\"url\":\"%s\"}", url);
                RequestBody body = RequestBody.create(json, JSON);
                Request request = new Request.Builder()
                        .url("http://************:5015/api/caches/video/clone/" + String.valueOf(System.currentTimeMillis()))
                        .post(body)
                        .build();
                Message msg = handler.obtainMessage(100);
                try {
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();
                    System.out.println(result);
                    msg.obj = result;
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    msg.obj = e.getMessage();
                    handler.sendMessage(msg);
                }
            }
        }).start();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    String result = (String) msg.obj;
                    Toast.makeText(BaseApplication.this, result, Toast.LENGTH_LONG).show();
                    break;
                case 101:
                    try {
                        ClipData clipData = clipboardManager.getPrimaryClip();

                        String pasteString = "";
                        if (clipData != null && clipData.getItemCount() > 0) {
                            CharSequence text = clipData.getItemAt(0).getText();
                            pasteString = text.toString();
                        }
                        // if (!status) return;
                        if (TextUtils.isEmpty(pasteString)) return;

                        clipboardManager.setPrimaryClip(ClipData.newPlainText("aaa", "" + System.currentTimeMillis()));

                        if (!TextUtils.isEmpty(lastPasteString) && lastPasteString.equals(pasteString))
                            return;


                        lastPasteString = pasteString;

                        String pattern = "(https://v.douyin.com).*/";
                        Pattern r = Pattern.compile(pattern);
                        Matcher m = r.matcher(pasteString);

                        String shareUrl = null;
                        if (m.find()) {
                            shareUrl = m.group(0);
                            postShareUrl(shareUrl);
                            Toast.makeText(BaseApplication.this, "收到任务:[" + pasteString + "]", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(BaseApplication.this, "NO MATCH!", Toast.LENGTH_SHORT).show();
                        }
                    } finally {
                        postEditText.setText("");
                        postEditText.clearFocus();
                        postEditText.setEnabled(false);
                        FloatWindow.get().clearFocus();
                    }
                    break;
                default:
                    break;
            }
        }
    };
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
