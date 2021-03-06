package com.example.nayan.gameverson2.tools;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nayan.gameverson2.R;
import com.example.nayan.gameverson2.activity.GameActivity;
import com.example.nayan.gameverson2.activity.MainActivity;
import com.example.nayan.gameverson2.activity.SubLevelActivity;
import com.example.nayan.gameverson2.model.MAllContent;
import com.example.nayan.gameverson2.model.MData;
import com.example.nayan.gameverson2.model.MLock;
import com.example.nayan.gameverson2.model.MWords;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import static com.example.nayan.gameverson2.activity.SubLevelActivity.mSubLevels;

/**
 * Created by NAYAN on 8/20/2016.
 */
public class GameLogic {
    public static Dialog dialog;
    private static GameLogic gameLogic;
    private int previousId, count, counter, clickCount, matchWinCount, previousType, gameWinCount, previousPoint, presentPoint, bestPoint, idPrevious, oneClick;
    private ArrayList<MAllContent> list;
    private int countPoint;
    private ArrayList<MWords> list2;
    private SharedPreferences preferences;
    private Context context;
    private MLock mLock = new MLock();
    MData mData=new MData();
    private Handler handler = new Handler();
    private RecyclerView.Adapter gameAdapter;
    private MAllContent previousMcontents = new MAllContent();
    private RecyclerView recyclerView;
    private LinearLayout changeColor;


    private GameLogic() {

    }

    public static GameLogic getInstance(Context context1) {
        gameLogic = new GameLogic();
        gameLogic.context = context1;

        return gameLogic;

    }

    public void callData(ArrayList<MAllContent> list, RecyclerView.Adapter adapter) {
        this.list = list;

        clickCount = getMin() - 1;
        this.gameAdapter = adapter;
    }

    public void setLevel(MAllContent mContents) {
        this.previousMcontents = mContents;

    }

    public void saveDb() {
        MLock lock = new MLock();
        lock.setLevel_id(Global.levelId);
        lock.setSub_level_id(Global.subLevelId);
        lock.setBestPoint(Utils.bestPoint);
        lock.setTotal_pont(Global.totalPoint);
        lock.setPopup(Global.popUp);
        lock.setIsSavePoint(Global.isSavePoint);
        lock.setUnlockNextLevel(1);

        DatabaseHelper db = new DatabaseHelper(context);
        db.addLockData(lock);
        if (Global.SUB_INDEX_POSITION >= SubLevelActivity.mSubLevels.size() - 1) {
            Utils.toastMassage(context, "no more level");

            return;

        } else {

            Log.e("LOGIC", "id:" + SubLevelActivity.mSubLevels.get(Global.SUB_INDEX_POSITION + 1).getLid());
            lock = db.getLocalData(Global.levelId, SubLevelActivity.mSubLevels.get(Global.SUB_INDEX_POSITION + 1).getLid());
            lock.setLevel_id(Global.levelId);
            lock.setSub_level_id(SubLevelActivity.mSubLevels.get(Global.SUB_INDEX_POSITION + 1).getLid());
            Log.e("LOGIC", "sid:" + lock.getSub_level_id());
            lock.setUnlockNextLevel(1);
            db.addLockData(lock);
        }


        //next sublevel unlock


    }

    public int getMin() {
        if (list == null || list.size() < 1) {
            Log.e("ERR", "no data");
            return 0;
        }
        int min = list.get(0).getMid();
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).getMid() < min) {
                min = list.get(i).getMid();
            }
        }
        return min;
    }

    public void textClick(final MAllContent mContents, int pos, final int listSize, final View view, TextView view2, final ImageView imageView) {
        counter++;
        oneClick++;
        countPoint++;

        Log.e("textClick", "is" + countPoint);
        //don't work if mid !=1 at first time because first time click count=1
        if (oneClick > 1) {
            Log.e("textClick", "click :" + oneClick);
            return;
        }
        if (mContents.getMatch() == 1) {
            oneClick = 0;
            Log.e("textClick", "click23 :" + oneClick);
            return;
        }

        if (mContents.getMid() == clickCount + 1) {

            list.get(pos).setMatch(1);
            String mSound = "msound";
            if (Global.levelId == 1) {
                mSound = MainActivity.sounds;
            } else if (Global.levelId == 2) {
                mSound = MainActivity.sounds;
            } else if (Global.levelId == 3) {
                mSound = MainActivity.sounds;
            } else if (Global.levelId == 4) {
                mSound = MainActivity.sounds;
            }
            Utils.PlaySound(mSound + File.separator + mContents.getAud());
            //clickcount store present mid
            flipAnimation(view);
            imageView.setImageResource(R.drawable.green_panel);
            clickCount = mContents.getMid();
            count++;
            Log.e("textClick", "step3");

        } else {
            //Utils.getSound(context, R.raw.fail);
            shakeAnimation(view);
            imageView.setImageResource(R.drawable.red_panel);
            Log.e("textClick", "step4");
        }

        if (count == listSize) {


            savePoint(listSize);


            isSavePoint();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    resetList(listSize);
                }
            }, 1000);
            GameActivity.getInstance().txtTotalPoint.setText(Global.totalPoint + "");

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    resetList(listSize);
                    dialogShowForLevelClear(listSize);
                }
            }, 1500);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    Utils.getSound(context, R.raw.shuffle);
                }
            }, 500);

        }


    }

    public void isSavePoint() {
        DatabaseHelper db = new DatabaseHelper(context);

        mData = db.getIsSavePoint(Global.levelId, Global.subLevelId);
        mData.setLevelId(Global.levelId);
        mData.setSublevelId(Global.subLevelId);
        mData.setIsSavePoint(1);
        db.isPointSave(mData);
        Global.isSavePoint = mLock.getIsSavePoint();
        Log.e("isSaveP", " is " + mData.getIsSavePoint());
    }

    public void forLevel2(final View itemView, final MAllContent mContents, final int listSize, TextView textView, int pos, final ImageView imageView) {
        counter++;
        oneClick++;
        countPoint++;
        if (oneClick > 1) {
            return;
        }
        if (mContents.getMatch() == 1) {
            Utils.toastMassage(context, "Matched");
            Log.e("s", ":1");
            return;
        }
        list.get(pos).setMatch(1);
        if (previousId == 0) {
            mContents.setMatch(1);
            previousMcontents = mContents;
            previousId = mContents.getMid();
            flipAnimation(itemView);

            Log.e("s", ":3");
            return;
        }

        if (previousId == mContents.getMid()) {
            Utils.toastMassage(context, "Same Clicked");

            Log.e("s", ":2");
            return;

        } else {

            Log.e("s", ":4");
            if (mContents.getPresentType() == previousMcontents.getPresentType()) {
                mContents.setMatch(1);
                matchWinCount++;
                Utils.toastMassage(context, "Match");

                if (matchWinCount == listSize / 2) {
                    savePoint(listSize);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            resetList(listSize);
                        }
                    }, 1000);
                    GameActivity.getInstance().txtTotalPoint.setText(Global.totalPoint + "");

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialogShowForLevelClear(listSize);
                        }
                    }, 1500);
                    gameWinCount++;
                }

            } else {
                previousMcontents.setMatch(0);
                mContents.setMatch(0);
                imageView.setImageResource(R.drawable.red_panel);
                Toast.makeText(context, "wrong", Toast.LENGTH_SHORT).show();
            }
            previousId = 0;
            previousMcontents = mContents;
            flipAnimation(itemView);
        }
    }

    public void imageClick(final MAllContent mImage, int pos, final int listSize, final View view, final ImageView imageView) {
        Log.e("Loge", "present id ::" + mImage.getPresentId());
        Log.e("position", "pos" + pos);
        countPoint++;
        counter++;
        oneClick++;
        Log.e("ANIM", "click prev:" + oneClick);
        if (oneClick > 1) {
            return;
        }


        if (previousId == mImage.getMid() || count > 1 || mImage.getMatch() == 1) {
            Log.e("previous type", "same: " + mImage.getPresentType());
            Log.e("click over 1", "count: " + count);
            Log.e("mid", "pt mid :" + mImage.getMid());
            Log.e("mid", "pv mid :" + previousId);
//            shakeAnimation(view);
            oneClick = 0;
            Utils.toastMassage(context, "Same click");
            return;
        }
//        clickCount++;

        list.get(pos).setMatch(1);
        String mSound = "msound";
        mSound = MainActivity.sounds;
//        if (Global.levelId == 1) {
//
//        } else if (Global.levelId == 2) {
//            mSound = MainActivity.sounds;
//        } else if (Global.levelId == 3) {
//            mSound = MainActivity.sounds;
//        } else if (Global.levelId == 4) {
//            mSound = MainActivity.sounds;
//        }
        Utils.PlaySound(mSound + File.separator + mImage.getAud());

        flipAnimation(view);
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                gameAdapter.notifyDataSetChanged();
////                textView.setBackgroundColor(0xff888888);
//            }
//        }, 400);

        count++;


        Log.e("click", "count: " + count);
//        Utils.getSound(context, R.raw.click);
        if (count == 2) {

            if (previousType == mImage.getPresentType()) {
                Log.e("log", "match win count : " + matchWinCount);
                Log.e("previous id", "MID : " + previousId);

                Utils.PlaySound(mSound + File.separator + mImage.getAud());
                matchWinCount++;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

//                        Utils.getSound(context, R.raw.match2);
                        count = 0;


                    }
                }, 800);


                if (matchWinCount == listSize / 2) {
                    savePoint(listSize);
                    isSavePoint();
//                    resetList(listSize);
//                    textView.setBackgroundColor(0);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            resetList(listSize);
                            dialogShowForLevelClear(listSize);
                        }
                    }, 1500);
                    GameActivity.getInstance().txtTotalPoint.setText(Global.totalPoint + "");
//                    VungleAdManager.getInstance(context).play();


                    gameWinCount++;
                    Log.e("log", "game over : " + gameWinCount);
                }

                return;
            } else {
//                shakeAnimation(view);
//                textView.setBackgroundColor(0xffff0000);


                final int previous = previousId;

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        Utils.getSound(context, R.raw.fail);
                        for (int i = 0; i < listSize; i++) {
                            if (list.get(i).getMid() == previous || list.get(i).getMid() == mImage.getMid()) {
                                list.get(i).setMatch(0);
                                gameAdapter.notifyDataSetChanged();
                                imageView.setImageResource(R.drawable.red_panel);
//                                flipAnimation2(view);
//
                            }
                        }
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                gameAdapter.notifyDataSetChanged();
//                            }
//                        }, 400);

//                        textView.setBackgroundColor(0);
                        count = 0;
                    }
                }, 1000);
                previousId = 0;
                previousType = 0;
                return;
            }
        }
        previousId = mImage.getMid();
        previousType = mImage.getPresentType();
//        textView2 = textView;
//        view1=view;
    }

    public void sequentialPlay() {
//        if (Global.SORTING_LIST.get(0) == mAllContents.get(pos).getMid()) {
//            Global.SORTING_LIST.remove(0);
//            Log.e("sort", " correct ");
//        } else {
//            Log.e("sort", " wrong ");
//        }
    }

    public void dialogShowForLevelClear(final int listSize) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_level_cleared);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        changeColor = (LinearLayout) dialog.findViewById(R.id.dia_LenearLayout);
        TextView txtClear = (TextView) dialog.findViewById(R.id.dia_level_clear);
        final ImageView txtPoint = (ImageView) dialog.findViewById(R.id.txtLevelPoint);
//        final TextView txtBestPoint = (TextView) dialog.findViewById(R.id.txtLevelBestPoint);
        final TextView txtScore = (TextView) dialog.findViewById(R.id.txtLevelScore);
        ImageView imgLevelMenu = (ImageView) dialog.findViewById(R.id.imgLevelMenu);
        ImageView imgFacebook = (ImageView) dialog.findViewById(R.id.imgFacebook);

        Utils.setFont(context, "carterone", txtClear);
        imgLevelMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                            Intent intent = new Intent(context, SubLevelActivity.class);
//                            intent.putExtra("id", Global.levelId);
//                            intent.putExtra("name", Global.levelName);
//                            context.startActivity(intent);
                ((Activity) context).finish();
                dialog.dismiss();


                Utils.toastMassage(context, "Return Game Level");
            }
        });
        if (Global.levelId == 1) {
            Utils.setFont(context, "carterone", txtScore);
            Utils.changeUIcolor(context, Global.uriBangla, changeColor);
        } else if (Global.levelId == 2) {
            Utils.setFont(context, "carterone", txtScore);
            Utils.changeUIcolor(context, Global.uriOngko, changeColor);
        } else if (Global.levelId == 3) {
            Utils.setFont(context, "carterone", txtScore);
            Utils.changeUIcolor(context, Global.uriEnglish, changeColor);
        } else if (Global.levelId == 4) {
            Utils.setFont(context, "carterone", txtScore);
            Utils.changeUIcolor(context, Global.uriMath, changeColor);
        }

        ImageView imgReload = (ImageView) dialog.findViewById(R.id.btnLevelReload);
        imgReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                GameLogic.getInstance(context).resetList(listSize);
//                imageView.setImageResource(R.drawable.yellow_panel);

                Log.e("totalpoint", "tpoint is" + Global.totalPoint);
//                GameActivity.getInstance().refresh(Global.SUB_INDEX_POSITION);
                GameActivity.getInstance().getIsSaveDataFromDb(Global.levelId, Global.subLevelId);
                resetList(listSize);
                dialog.dismiss();
            }


        });
        ImageView imgNextLevel = (ImageView) dialog.findViewById(R.id.imgLevelForward);
        imgNextLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Global.SUB_INDEX_POSITION >= mSubLevels.size() - 1) {
                    Utils.toastMassage(context, "Level Finished ");
                    GameActivity.getInstance().finish();

                    return;

                } else {
                    Global.SUB_INDEX_POSITION = Global.SUB_INDEX_POSITION + 1;

//                    Global.CONTENT=Global.CONTENT;
                    mSubLevels.get(Global.SUB_INDEX_POSITION).setUnlockNextLevel(1);
                    Global.subLevelId = mSubLevels.get(Global.SUB_INDEX_POSITION).getLid();
                    Global.logic = mSubLevels.get(Global.SUB_INDEX_POSITION).getLogic();
                    Global.CONTENT = mSubLevels.get(Global.SUB_INDEX_POSITION).getContent();
                    GameActivity.getInstance().getIsSaveDataFromDb(Global.levelId, Global.subLevelId);

                    String start = DialogSoundOnOff.getPREF(context, Global.levelId + "");
                    String maxContent = Utils.getPREF(context, Global.levelId + "");
                    int s = Integer.valueOf(start);
                    int m = Integer.valueOf(maxContent);
                    Log.e("content", " start " + s);
                    Log.e("content", " maximum content " + m);
                    Log.e("content", " present " + Global.CONTENT);
                    if (Global.CONTENT > m) {
                        dialog.dismiss();
                        dialogShow(s, 0, Global.levelId);
                        return;
                    }
                    GameActivity.getInstance().refresh(Global.SUB_INDEX_POSITION, Global.CONTENT);
                }
                dialog.dismiss();
            }
        });
        imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/apple.fruit"));
                context.startActivity(browserIntent);
                dialog.dismiss();
            }
        });


//        txtBestPoint.setText("" + Utils.bestPoint);

        if (Global.isSavePoint == 0) {
            if (Global.levelId == 1) {
                txtScore.setText("Score :  " + Utils.convertNum(presentPoint + ""));
            }
            if (Global.levelId == 2) {
                txtScore.setText("Score :  " + Utils.convertNum(presentPoint + ""));
            }
            if (Global.levelId == 3) {
                txtScore.setText("Score :  " + presentPoint + "");
            }
            if (Global.levelId == 4) {
                txtScore.setText("Score :  " + presentPoint + "");
            }

            if (presentPoint == 50) {
                txtPoint.setImageResource(R.drawable.star_1);
            } else if (presentPoint == 75) {
                txtPoint.setImageResource(R.drawable.star_2);
            } else if (presentPoint == 100) {
                txtPoint.setImageResource(R.drawable.star_3);
            }

        }


//        txtScore.setText("Score :  " + presentPoint + "");

//        else txtPoint.setText(Utils.getIntToStar(0));
        dialog.show();
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

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void flipAnimation(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotationY", -180, 0);
        animator.setDuration(700);
        animator.start();
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                counter++;
                oneClick = 0;
                gameAdapter.notifyDataSetChanged();
                Log.e("ANIM", "click:" + oneClick);
            }
        }, 700);
    }

    public void flipAnimation2(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotationY", 0, -180);
        animator.setDuration(500);
        animator.start();
    }

    public void shakeAnimation(View v) {
        oneClick = 0;
        // Create shake effect from xml resource
        Animation shake = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.shaking);
        // View element to be shaken

        // Perform animation
        v.startAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                gameAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
//        gameAdapter.notifyDataSetChanged();
    }


    public void resetList(int listSize) {
        for (int i = 0; i < listSize; i++) {
//            list.get(i).setClick(Utils.IMAGE_OFF);
//            imageView.setImageResource(R.drawable.yellow_panel);
            list.get(i).setMatch(0);
        }
//        previousMcontents.setMatch(0);
//        MAllContent mContents = new MAllContent();
//        previousMcontents = mContents;
        Collections.shuffle(list);
        clickCount = getMin() - 1;
        matchWinCount = 0;
        previousType = 0;
        countPoint = 0;
        previousId = 0;
        count = 0;
        counter = 0;
        gameAdapter.notifyDataSetChanged();

    }

    private void resetList2(int listSize) {
        for (int i = 0; i < listSize; i++) {
            list.get(i).setMatch(0);
        }
        Collections.shuffle(list);
        matchWinCount = 0;
        previousType = 0;
        previousId = 0;
        counter = 0;
        gameAdapter.notifyDataSetChanged();

    }

    private void savePoint(int listSize) {
        if (Global.isSavePoint == 0) {
            presentPoint = pointCount(listSize);
            Global.totalPoint = Global.totalPoint + presentPoint;
            saveDb();
        }

//        addDb();

        if (presentPoint > Utils.bestPoint) {
            Utils.bestPoint = presentPoint;
            saveDb();
        }
    }

    private int pointCount(int listSize) {
        int point = 50;

        if (countPoint == listSize) {
            point = 100;
        } else if (countPoint > listSize && countPoint <= listSize + listSize / 2) {
            point = 75;
        }
        Log.e("pint", "point is" + point);
        return point;
    }

    public String showHistory() {

        Dialog dialog = new Dialog(context);
        dialog.setTitle("Status");
        dialog.setContentView(R.layout.row_d_best_point);
        TextView textView = (TextView) dialog.findViewById(R.id.txtBetPoint);
//        textView.setText("best point: " + previousMcontents.getBestpoint() + "\nWin count : " + previousMcontents.getLevelWinCount());
        TextView textView1 = (TextView) dialog.findViewById(R.id.txtTotalWin);
//        textView1.setText("no of total win: " + gameWinCount + "");
        dialog.show();
        return "";
    }

}
