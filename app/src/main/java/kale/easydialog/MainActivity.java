package kale.easydialog;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import kale.ui.view.dialog.EasyDialog;

/**
 * 关于更多对话框的设置请参考：http://www.cnblogs.com/tianzhijiexian/p/3867731.html
 */
public class MainActivity extends AppCompatActivity {

    public final String TAG = getClass().getSimpleName();

    private BottomSheetBehavior behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
    }

    private void setViews() {
        findViewById(R.id.jump_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyStyleActivity.class));
            }
        });

        // 得到 Bottom Sheet 的视图对象所对应的 BottomSheetBehavior 对象
        behavior = BottomSheetBehavior.from(findViewById(R.id.ll_sheet_root));
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    public EasyDialog easyDialog;

    public void simpleDialog(View v) {
        final android.support.v4.app.FragmentTransaction ft =
                getSupportFragmentManager().beginTransaction();

        EasyDialog.Builder builder = EasyDialog.builder(this);
        builder.setTitle("Title")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage(R.string.hello_world)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // 点空白处消失时才会触发！！！！
                        Log.d(TAG, "onCancel"); // onCancel - > onDismiss
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Log.d(TAG, "onDismiss");
                    }
                })
//                .setNeutralButton("know", null)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick ok");// 设置对话框上的按钮 ok->dismiss

                        ft.remove(easyDialog);
                        ft.addToBackStack(null);

                        EasyDialog.builder(MainActivity.this)
                                .setTitle("Stack Dialog")
                                .setMessage("Please press back button")
                                .build()
                                .show(ft, "stackDialog");
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick cancel");
                        dialog.dismiss(); // cancel -> dismiss
                    }
                })
                .setNeutralButton("ignore", null)
                .setCancelable(true);

        easyDialog = builder.build();
        easyDialog.showAllowingStateLoss(getSupportFragmentManager());

//        easyDialog.show(getSupportFragmentManager());
    }

    /**
     * 支持单选列表的对话框
     */
    public void singleChoiceDialog(View v) {
        EasyDialog dialog = EasyDialog.builder(this)
                .setTitle("Single Choice Dialog")
                .setSingleChoiceItems(new String[]{"Android", "ios", "wp"}, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        Log.d(TAG, "onItemClick pos = " + position);
                        dialog.dismiss();
                    }
                })// 设置单选列表的数据和监听
                .build();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), TAG);
    }

    /**
     * 支持多选列表的对话框
     */
    public void multiChoiceDialog(View v) {
        EasyDialog.builder(this)
                // 设置数据和默认选中的选项
                .setMultiChoiceItems(new String[]{"Android", "ios", "wp"}, new boolean[]{true, false, true},
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                Log.d(TAG, "onClick pos = " + which + " , isChecked = " + isChecked);
                            }
                        }) // 设置监听器
                .build().show(getSupportFragmentManager(), TAG);
    }

    private InputDialog dialog;

    /**
     * 可以输入文字的dialog，拥有自定义布局
     */
    public void inputDialog(View v) {
        dialog = new InputDialog.Builder(this)
                .setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.kale))
                .setInputText("", "hint")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface ignore, int which) {
                        String text = dialog.getInputTextEt().getText().toString();
                        if (!TextUtils.isEmpty(text)) {
                            Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .build();
        dialog.show(getSupportFragmentManager()); // 一个参数的show()
    }

    /**
     * 显示在顶部的dialog，背景透明
     */
    public void topDialog(View v) {
        TopDialog.Builder builder = TopDialog.builder(this, TopDialog.class);
        builder.setTitle("标题");
        builder.setNegativeButton("因为设置了透明背景,", null);
        builder.setPositiveButton("所以这里是透明的", null);
        builder.build().show(getSupportFragmentManager());
    }

    /**
     * 自定dialog的builder
     */
    public void customBuilderDialog(View v) {
        new CustomBuilderDialog.Builder(this)
                .setSomeArg(31)
                .setTitle("custom args dialog")
                .setMessage("message")
                .build()
                .show(getSupportFragmentManager());
    }

    /**
     * 从底部弹出的对话框
     */
    public void bottomDialog(View v) {
        BottomSheetDialog.Builder builder = BottomSheetDialog.builder(this, BottomSheetDialog.class);
        builder.setMessage("click me");
        builder.setIsBottomDialog(true);
        EasyDialog dialog = builder.build();
        dialog.show(getSupportFragmentManager(), "dialog");

        dialog.setCancelable(false); // 如果设置了，那么底部dialog就不支持手势关闭和空白处关闭
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                // 监听点空白处cancel的事件
                Toast.makeText(MainActivity.this, "cancel", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Toast.makeText(MainActivity.this, "dismiss", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void customBottomDialog02(View v) {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }
}
