package com.ofilm.android.hozonlauncher.ApplicationItem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ofilm.android.hozonlauncher.ApplicationLayout;
import com.ofilm.android.hozonlauncher.MainActivity;
import com.ofilm.android.hozonlauncher.R;

import java.util.List;

/**
 * Create By rongxinglan IN 2018/10/18
 */

public class AppAdapter extends BaseAdapter {
    public Context context;
    private boolean isShowDelete;
    private int clickItemIndex = -1;
    private LayoutInflater mInflater;
   //  List<MyAppInfo> mList;

    //public static   List<MyAppInfo> myAppInfos = new ArrayList<MyAppInfo>();
    public AppAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<MyAppInfo> myAppInfos) {
        ApplicationLayout.mList = myAppInfos;
        notifyDataSetChanged();
    }

    public List<MyAppInfo> getData() {
        return ApplicationLayout.mList;
    }

    public void setShowDelete(boolean isShowDelete) {
        this.isShowDelete = isShowDelete;
        notifyDataSetChanged();
        Log.i("TAG", "setShowDelete  " + isShowDelete);
    }

    public boolean getShowDelete() {
        return isShowDelete;
    }

    public void setClickItemIndex(int position) {
        this.clickItemIndex = position;
    }

    @Override
    public int getCount() {
        return ApplicationLayout.mList.size();
    }

    @Override
    public Object getItem(int position) {
        return ApplicationLayout.mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            //使用View的对象ItemView与
            convertView = mInflater.inflate(R.layout.app_layout, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.app_Image);
            //点击应用图标时图标透明并作出响应
            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isShowDelete == false) {
                        AlphaAnimation anim_alpha = new AlphaAnimation(0,1);
                        anim_alpha.setDuration(1000);//动画时间
                        holder.icon .startAnimation(anim_alpha);//启动动画
                        // AppAdapter(MainActivity.this).setClickItemIndex(position);
                        Intent intent = new Intent();
                        intent = context.getPackageManager().getLaunchIntentForPackage(ApplicationLayout.mList.get(position).getPackageName());
                        try {
                            context.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
            /*holder.icon.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (isShowDelete) {
                        isShowDelete = false;
                    } else {
                        isShowDelete = true;
                    }
                    Log.d("TAG", "onItemLongClick");
                    setShowDelete(isShowDelete);
                    notifyDataSetChanged();
                    setClickItemIndex(position);
                    Log.i("TAG", "setClickItemIndex  " + position);
                    return false;
                }
            });*/
            holder.delete = (ImageView) convertView.findViewById(R.id.delete);
            holder.label = (TextView) convertView.findViewById(R.id.app_Text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.icon.setImageDrawable(ApplicationLayout.mList.get(position).getIcon());
       // Log.i("MyAppInfo", "getLabel:  "+ApplicationLayout.mList.get(position).getLabel().toString());
        holder.label.setText(ApplicationLayout.mList.get(position).getLabel().toString());
//            holder.icon.setClickable(false);
        if (isShowDelete == true) {
            holder.delete.setVisibility(View.VISIBLE);
            Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
            holder.icon.startAnimation(shake);
        } else {
            holder.delete.setVisibility(View.GONE);
            holder.icon.clearAnimation();
        }
        Log.i("TAG", "setVisibility  " + isShowDelete);
        if (holder.delete.getVisibility() == View.VISIBLE) {
            if (position == clickItemIndex) {
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除应用图标和名称
                        Log.d("TAG", "onClick" + clickItemIndex);
                        showDialog(position);
                        notifyDataSetChanged();
                    }
                });
            }
        }
        return convertView;
    }

    public void showDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示信息")
                .setCancelable(false)
                .setMessage("是否删除应用")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse("package:" + ApplicationLayout.mList.get(position).getPackageName());
                        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
                        context.startActivity(intent);
                        // MainActivity.delete(mList.get(i).getLabel());
                        ApplicationLayout.mList.remove(position);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        builder.show();
    }



public class ViewHolder {
    private ImageView icon;
    private ImageView delete;
    private TextView label;
}
}