package com.example.nayan.gameverson2.adapter;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nayan.gameverson2.R;
import com.example.nayan.gameverson2.activity.GameActivity;
import com.example.nayan.gameverson2.activity.MainActivity;
import com.example.nayan.gameverson2.model.MLock;
import com.example.nayan.gameverson2.model.MSubLevel;
import com.example.nayan.gameverson2.tools.DatabaseHelper;
import com.example.nayan.gameverson2.tools.DialogSoundOnOff;
import com.example.nayan.gameverson2.tools.FilesDownload;
import com.example.nayan.gameverson2.tools.Global;
import com.example.nayan.gameverson2.tools.Utils;

import java.util.ArrayList;

/**
 * Created by NAYAN on 11/24/2016.
 */
public class SubLevelAdapter extends RecyclerView.Adapter<SubLevelAdapter.MyViewHolder> {
    int one;
    private ArrayList<MSubLevel> mSubLevels;
    private MSubLevel mSubLevel = new MSubLevel();
    private MLock mLock = new MLock();
    private Context context;
    private LayoutInflater inflater;
    private DatabaseHelper db;
    private int count;
    private int subLevel;

    public SubLevelAdapter(Context context) {
        this.context = context;
        mSubLevels = new ArrayList<>();
        db = new DatabaseHelper(context);
        inflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<MSubLevel> mSubLevels) {
        this.mSubLevels = mSubLevels;

        Log.e("log", "setdata:" + mSubLevels.size());
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_image, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        mSubLevel = mSubLevels.get(position);
        holder.txtSubLevel.setText(mSubLevel.getName());

        if (mSubLevel.getBestPoint() == 100) {
            holder.txtPoint.setImageResource(R.drawable.s_star_3);
        } else if (mSubLevel.getBestPoint() == 75) {
            holder.txtPoint.setImageResource(R.drawable.s_star_2);
        } else if (mSubLevel.getBestPoint() == 50) {
            holder.txtPoint.setImageResource(R.drawable.s_star_1);
        }

        if (mSubLevel.getUnlockNextLevel() == 1) {
            holder.imgLock.setVisibility(View.GONE);
            holder.imgSub.setImageResource(R.drawable.sublevel_item_view);
        } else {
            holder.imgLock.setVisibility(View.VISIBLE);
            holder.imgSub.setImageResource(R.drawable.inactive_bg);
        }
        if (mSubLevel.getIsDownload() == 1) {
            holder.imgSub.setImageResource(R.drawable.sublevel_item_view);
        }

        holder.itemView.post(new Runnable() {
            @Override
            public void run() {

                int cellWidth = holder.itemView.getWidth();// this will give you cell width dynamically
                int cellHeight = holder.itemView.getHeight();// this will give you cell height dynamically

//                dynamicHeight.HeightChange(position, cellHeight); //call your iterface hear
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSubLevels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtSubLevel;
        ImageView imgLock, txtPoint, imgSub;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtSubLevel = (TextView) itemView.findViewById(R.id.txtLevel);
            txtPoint = (ImageView) itemView.findViewById(R.id.txtPoint);
            imgLock = (ImageView) itemView.findViewById(R.id.imgLock);
            imgSub = (ImageView) itemView.findViewById(R.id.imgSub);
            Utils.setFont(context, "carterone", txtSubLevel);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    mSubLevel = mSubLevels.get(getAdapterPosition());
                    Log.e("content", " is  " + mSubLevel.getContent());
                    Utils.bestPoint = mSubLevel.getBestPoint();
                    if (mSubLevel.getUnlockNextLevel() == 1) {
                        String start = DialogSoundOnOff.getPREF(context, Global.levelId + "");
                        String maxContent = Utils.getPREF(context, Global.levelId + "");
                        int s = Integer.valueOf(start);
                        int m = Integer.valueOf(maxContent);
                        Log.e("content", " start " + s);
                        Log.e("content", " max " + m);
                        if (mSubLevel.getContent() > m) {
                            dialogShow(s, getAdapterPosition(), Global.levelId);
                            return;
                        }
                        Intent intent = new Intent(context, GameActivity.class);
                        intent.putExtra("subLevelName", mSubLevel.getName());
                        intent.putExtra("index", getAdapterPosition());
                        intent.putExtra("Sid", mSubLevel.getLid());
                        intent.putExtra("pop", mSubLevel.getIsPopUp());
                        intent.putExtra("content", mSubLevel.getContent());
                        intent.putExtra("SLogic", mSubLevel.getLogic());
                        intent.putExtra("parentLevelName", mSubLevel.getParentName());
                        context.startActivity(intent);
                    }

                }
            });


        }
    }

    private void dialogShow(final int start, final int pos, final int level) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dia_download);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnOK = (Button) dialog.findViewById(R.id.btnYap);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnNop);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Global.levelId == 1) {
                    MainActivity.getInstance().banglaImage(start);
                    Log.e("content", " start bangla ");

                } else if (Global.levelId == 2) {
                    MainActivity.getInstance().ongkoImage(start);
                    Log.e("content", " start ongko ");
                } else if (Global.levelId == 3) {
                    MainActivity.getInstance().englishImage(start);
                    Log.e("content", " start english ");
                } else if (Global.levelId == 4) {
                    MainActivity.getInstance().mathImage(start);
                    Log.e("content", " start math ");
                }
//                MainActivity.getInstance().allCatagoryImage(start, level, context);
                FilesDownload filesDownload = FilesDownload.getInstance(context, MainActivity.bothImg);
                for (int i = 0; i < Global.URLS.size(); i++) {
                    filesDownload.addUrl(Global.IMAGE_URL + Global.URLS.get(i));

                }
                FilesDownload.getInstance(context, "").start();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GameActivity.class);
                intent.putExtra("subLevelName", mSubLevel.getName());
                intent.putExtra("index", pos);
                intent.putExtra("Sid", mSubLevel.getLid());
//                        intent.putExtra("pop", mSubLevel.getIsPopUp());
                intent.putExtra("content", mSubLevel.getContent());
                intent.putExtra("SLogic", mSubLevel.getLogic());
                intent.putExtra("parentLevelName", mSubLevel.getParentName());
                context.startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
