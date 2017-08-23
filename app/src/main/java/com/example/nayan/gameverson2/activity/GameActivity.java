package com.example.nayan.gameverson2.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nayan.gameverson2.R;
import com.example.nayan.gameverson2.adapter.GameAdapter;
import com.example.nayan.gameverson2.model.MAllContent;
import com.example.nayan.gameverson2.model.MLevel;
import com.example.nayan.gameverson2.model.MLock;
import com.example.nayan.gameverson2.model.MQuestions;
import com.example.nayan.gameverson2.model.MSubLevel;
import com.example.nayan.gameverson2.model.MWords;
import com.example.nayan.gameverson2.tools.DatabaseHelper;
import com.example.nayan.gameverson2.tools.Global;
import com.example.nayan.gameverson2.tools.MyGoogleAnalytics;
import com.example.nayan.gameverson2.tools.SpacesItemDecoration;
import com.example.nayan.gameverson2.tools.Utils;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by NAYAN on 11/24/2016.
 */
public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private static GameActivity gameActivity;
    private static MLevel mLevel;
    private static MAllContent mContents;
    public String subLevelName, how;
    public String parentName;
    public TextView txtName, txtTotalPoint, txtSubName;
    LinearLayout popUI;
    private MSubLevel mSubLevel = new MSubLevel();
    private ArrayList<MAllContent> imageArrayList1;
    private ArrayList<MWords> wordsList;
    private ImageView imgSetting, imageView, imgHelp;
    private RecyclerView recyclerView;
    //    private Context context;
    private GameAdapter gameAdapter;
    private DatabaseHelper database;
    private MQuestions mQuestions;
    private MLock mLock;

    //    private GameActivity(){
//
//    }
    public static GameActivity getInstance() {
        return gameActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game_activity);
//        VungleAdManager.getInstance(this).play();


        init();
        MyGoogleAnalytics.getInstance().setupAnalytics("Game Activity");
        getLocalData();
        prepareDisplay();

        getPopUp();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


    public void init() {
        mQuestions = new MQuestions();
        imgHelp = (ImageView) findViewById(R.id.imgHelp);
        imgHelp.setOnClickListener(this);
        gameActivity = this;
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(this);
        txtName = (TextView) findViewById(R.id.txtNameGame);
        txtSubName = (TextView) findViewById(R.id.txtNameGameSub);
        txtTotalPoint = (TextView) findViewById(R.id.txtTotalPoint);
        database = new DatabaseHelper(this);
        imgSetting = (ImageView) findViewById(R.id.imgseting);
        imgSetting.setOnClickListener(this);
        imageArrayList1 = new ArrayList<>();
        wordsList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.addItemDecoration(new SpacesItemDecoration(7));
        Global.subLevelName = getIntent().getStringExtra("subLevelName");
        Global.logic = getIntent().getIntExtra("SLogic", 0);
        subLevelName = Global.subLevelName;
        Global.parentLevelName = getIntent().getStringExtra("parentLevelName");
        parentName = Global.parentLevelName;
        Global.SUB_INDEX_POSITION = getIntent().getIntExtra("index", 0);
        Global.subLevelId = getIntent().getIntExtra("Sid", 0);
        Global.popUp = getIntent().getIntExtra("pop", 0);
        gameAdapter = new GameAdapter(this);

    }

    public void getPopUp() {
//        if (Global.popUp == 0)
            diaRulesOfPlay(database.getPopUp(Global.levelId, Global.subLevelId));
//        mSubLevel = new MSubLevel();
//        mSubLevel.setParentId(Global.levelId);
//        mSubLevel.setLid(Global.subLevelId);
//        mSubLevel.setIsPopUp(1);
//        database.addSubFromJsom(mSubLevel);


    }


    public void getLocalData() {


        mLock = database.getLocalData(Global.levelId, Global.subLevelId);

        Log.e("TEST", Global.levelId + ":" + Global.subLevelId + ":" + Global.totalPoint);

        if (Global.logic == 1) {

            if (Global.levelId == 1) {
                if (Global.subLevelId == 1) {
                    imageArrayList1 = database.getBanglaContentsContentsData(1);
                }
                if (Global.subLevelId == 4) {
                    imageArrayList1 = database.getBanglaContentsContentsData(2);
                }
                if (Global.subLevelId == 7) {
                    imageArrayList1 = database.getBanglaContentsContentsData(3);
                }
                if (Global.subLevelId == 10) {
                    imageArrayList1 = database.getBanglaContentsContentsData(4);
                }

            } else if (Global.levelId == 2) {
                if (Global.subLevelId == 13) {
                    imageArrayList1 = database.getBanglaMathContentsContentsData(1);
                }
                if (Global.subLevelId == 16) {
                    imageArrayList1 = database.getBanglaMathContentsContentsData(2);
                }
            } else if (Global.levelId == 3) {
                if (Global.subLevelId == 19) {
                    imageArrayList1 = database.getEnglishContentsContentsData(1);
                }
                if (Global.subLevelId == 22) {
                    imageArrayList1 = database.getEnglishContentsContentsData(2);
                }
                if (Global.subLevelId == 25) {
                    imageArrayList1 = database.getEnglishContentsContentsData(3);
                }
                if (Global.subLevelId == 28) {
                    imageArrayList1 = database.getEnglishContentsContentsData(4);
                }
                if (Global.subLevelId == 31) {
                    imageArrayList1 = database.getEnglishContentsContentsData(5);
                }

            } else if (Global.levelId == 4) {
                if (Global.subLevelId == 34) {
                    imageArrayList1 = database.getEnglishContentsContentsData(1);
                }
            }

            Collections.shuffle(imageArrayList1);
        }

//        if (Global.logic == 2) {
//
//            if (Global.levelId == 1) {
//                imageArrayList1 = database.getBanglaContentsContentsData(2);
//            } else if (Global.levelId == 2) {
//                imageArrayList1 = database.getBanglaMathContentsContentsData(2);
//            } else if (Global.levelId == 3) {
//                imageArrayList1 = database.getEnglishContentsContentsData(2);
//            } else if (Global.levelId == 4) {
//                imageArrayList1 = database.getMathContentsContentsData();
//            }
//
//            Collections.shuffle(imageArrayList1);
//        }
        else if (Global.logic == 2) {

            ArrayList<MAllContent> realAssets = new ArrayList<>();
            if (Global.levelId == 1) {
                if (Global.subLevelId == 2) {
                    realAssets = database.getBanglaContentsContentsData(1);
                }
                if (Global.subLevelId == 5) {
                    realAssets = database.getBanglaContentsContentsData(2);
                }
                if (Global.subLevelId == 8) {
                    realAssets = database.getBanglaContentsContentsData(3);
                }
                if (Global.subLevelId == 11) {
                    realAssets = database.getBanglaContentsContentsData(4);
                }

            } else if (Global.levelId == 2) {
                if (Global.subLevelId == 14) {
                    realAssets = database.getBanglaMathContentsContentsData(1);
                }
                if (Global.subLevelId == 17) {
                    realAssets = database.getBanglaMathContentsContentsData(2);
                }
            } else if (Global.levelId == 3) {
                if (Global.subLevelId == 20) {
                    realAssets = database.getEnglishContentsContentsData(1);
                }
                if (Global.subLevelId == 23) {
                    realAssets = database.getEnglishContentsContentsData(2);
                }
                if (Global.subLevelId == 26) {
                    realAssets = database.getEnglishContentsContentsData(3);
                }
                if (Global.subLevelId == 29) {
                    realAssets = database.getEnglishContentsContentsData(4);
                }
                if (Global.subLevelId == 32) {
                    realAssets = database.getEnglishContentsContentsData(5);
                }

            } else if (Global.levelId == 4) {
                if (Global.subLevelId == 35) {
                    realAssets = database.getEnglishContentsContentsData(1);
                }
            }

            imageArrayList1 = generatesTxtSen(realAssets);
            Collections.shuffle(imageArrayList1);
        } else if (Global.logic == 3) {

            if (Global.levelId == 1) {
                if (Global.subLevelId == 3) {
                    imageArrayList1 = database.getBanglaContentsContentsData(1);
                }
                if (Global.subLevelId == 6) {
                    imageArrayList1 = database.getBanglaContentsContentsData(2);
                }
                if (Global.subLevelId == 9) {
                    imageArrayList1 = database.getBanglaContentsContentsData(3);
                }
                if (Global.subLevelId == 12) {
                    imageArrayList1 = database.getBanglaContentsContentsData(4);
                }
            } else if (Global.levelId == 2) {
                if (Global.subLevelId == 15) {
                    imageArrayList1 = database.getBanglaMathContentsContentsData(1);
                }
                if (Global.subLevelId == 18) {
                    imageArrayList1 = database.getBanglaMathContentsContentsData(2);
                }
            } else if (Global.levelId == 3) {
                if (Global.subLevelId == 21) {
                    imageArrayList1 = database.getEnglishContentsContentsData(1);
                }
                if (Global.subLevelId == 24) {
                    imageArrayList1 = database.getEnglishContentsContentsData(2);
                }
                if (Global.subLevelId == 27) {
                    imageArrayList1 = database.getEnglishContentsContentsData(3);
                }
                if (Global.subLevelId == 30) {
                    imageArrayList1 = database.getEnglishContentsContentsData(4);
                }
                if (Global.subLevelId == 33) {
                    imageArrayList1 = database.getEnglishContentsContentsData(5);
                }
            } else if (Global.levelId == 4) {
                if (Global.subLevelId == 36) {
                    imageArrayList1 = database.getEnglishContentsContentsData(1);
                }
            }
//            Collections.shuffle(imageArrayList1);

        } else if (Global.logic == 4) {

            ArrayList<MAllContent> realAssets = new ArrayList<>();
            if (Global.levelId == 1) {
                realAssets = database.getBanglaContentsContentsData(4);
            } else if (Global.levelId == 2) {
                realAssets = database.getBanglaMathContentsContentsData(4);
            } else if (Global.levelId == 3) {
                realAssets = database.getEnglishContentsContentsData(4);
            } else if (Global.levelId == 4) {
                realAssets = database.getMathContentsContentsData();
            }

            imageArrayList1 = generatesTxtImg(realAssets);
            Collections.shuffle(imageArrayList1);
        }


    }

    public void diaRulesOfPlay(String s) {
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.game_instruction);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        popUI = (LinearLayout) dialog.findViewById(R.id.popUpUI);
        if (Global.levelId == 1) {
            Utils.changeUIcolor(this, Global.uriBangla, popUI);
//            txtLevelName.setTextColor(0xff00ff00);
        } else if (Global.levelId == 2) {
            Utils.changeUIcolor(this, Global.uriOngko, popUI);
//            txtLevelName.setTextColor(0xffffff00);
        } else if (Global.levelId == 3) {
            Utils.changeUIcolor(this, Global.uriEnglish, popUI);
        } else if (Global.levelId == 4) {
//            imageView.setImageResource(R.drawable.red_coins);
            Utils.changeUIcolor(this, Global.uriMath, popUI);
        }
        TextView txtRule = (TextView) dialog.findViewById(R.id.txtRules);
        txtRule.setText(s);
        Button close = (Button) dialog.findViewById(R.id.btnDismiss);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private ArrayList<MAllContent> generateAssets(ArrayList<MAllContent> realAssets) {
        int count = realAssets.size();
        ArrayList<MAllContent> tempAsset = new ArrayList<>();
        for (MAllContent mContents : realAssets) {
            tempAsset.add(mContents);
            count++;
            MAllContent asset1 = new MAllContent();
            asset1.setPresentType(mContents.getPresentType());
            asset1.setTxt(mContents.getTxt());
            asset1.setMid(count);
            tempAsset.add(asset1);
        }
        return tempAsset;
    }

    private ArrayList<MAllContent> generatesTxtSen(ArrayList<MAllContent> realTxtSen) {
//        int count = realTxtSen.size();
        int count = 550;
        ArrayList<MAllContent> tempTxtSen = new ArrayList<>();
        for (MAllContent mContents : realTxtSen) {
            tempTxtSen.add(mContents);
            count++;
            MAllContent contents = new MAllContent();
            contents.setSen(mContents.getSen());
            contents.setMid(count);
//            mContents.setCp();
//            contents.setPresentId(mContents.getPresentId());
            contents.setPresentType(mContents.getPresentType());
            tempTxtSen.add(contents);
        }
        return tempTxtSen;
    }

    private ArrayList<MAllContent> generatesTxtImg(ArrayList<MAllContent> realTxtSen) {
        int count = realTxtSen.size();
        ArrayList<MAllContent> tempTxtSen = new ArrayList<>();
        for (MAllContent mContents : realTxtSen) {
            tempTxtSen.add(mContents);
            count++;
            MAllContent contents = new MAllContent();
            contents.setImg(mContents.getImg());
            contents.setMid(count);
//            contents.setPresentId(mContents.getPresentId());
            contents.setAud(mContents.getAud());
            contents.setPresentType(mContents.getPresentType());
            tempTxtSen.add(contents);
        }
        return tempTxtSen;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    public void refresh(int index) {
        subLevelName = Global.parentName.get(index).getName();
        how = Global.parentName.get(index).getHowto();
        Log.e("sublevel name", "s  n :" + subLevelName);
        parentName = Global.parentName.get(index).getParentName();
        Log.e("index ", "posi =" + Global.SUB_INDEX_POSITION);
        getLocalData();
        prepareDisplay();
    }

    public void prepareDisplay() {
        Utils.setFont(this, "carterone", txtName, txtTotalPoint);
        Global.totalPoint = mLock.getTotal_pont();

        txtTotalPoint.setText(Global.totalPoint + "");
//        txtName.setText(parentName + "(" + subLevelName + ")");
        txtSubName.setText(" -" + subLevelName + "");

        int item = Utils.getScreenSize(this, 90);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(gameAdapter);
        gameAdapter.setData(imageArrayList1);

        if (Global.levelId == 1) {
            imageView.setImageResource(R.drawable.grren_coins);
            txtName.setBackgroundResource(R.drawable.bangla);


        } else if (Global.levelId == 2) {
            imageView.setImageResource(R.drawable.yellow_coins);
            txtName.setBackgroundResource(R.drawable.ongko);

        } else if (Global.levelId == 3) {
            imageView.setImageResource(R.drawable.red_coins);
            txtName.setBackgroundResource(R.drawable.english);

        } else if (Global.levelId == 4) {
            imageView.setImageResource(R.drawable.violet_coins);
            txtName.setBackgroundResource(R.drawable.math);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imgHelp || v.getId() == R.id.imageView) {

            diaRulesOfPlay(database.getPopUp(Global.levelId, Global.subLevelId));
        }
    }
}
