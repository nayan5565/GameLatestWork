package com.example.nayan.gameverson2.tools;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.nayan.gameverson2.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Nayan on 3/23/2017.
 */

public class FilesDownload {
    private static FilesDownload instance;
    private Context context;
    private String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "testDownload";
    private long total = 0, downloaded;
    private ProgressDialog dialog;
    private ArrayList<String> urls;
    private ArrayList<String> dirs;

    public FilesDownload() {
    }

    public static FilesDownload getInstance(Context context, String dir) {
        if (instance == null) instance = new FilesDownload();
        instance.dir = dir;
        instance.context = context;
        return instance;
    }
//    public static FilesDownload newInstance(Context context, String image) {
//        instance = new FilesDownload();
//        instance.image = image;
//        instance.context = context;
//        return instance;
//    }

    public void start() {
        if (urls != null && urls.size() > 0)
            new MyDownload().execute();
    }

    public FilesDownload addUrl(String url) {
        if (urls == null) urls = new ArrayList<>();
        if (dirs == null) dirs = new ArrayList<>();
        if (!urls.contains(url) && !isFileExists(url)) {
            urls.add(url);
            dirs.add(dir);
        }

        return instance;
    }

    private boolean isFileExists(String _url) {
        String fileName = _url.substring(_url.lastIndexOf("/") + 1);
        File file = new File(dir + File.separator + fileName);
        File fDir = new File(dir);
        if (!fDir.isDirectory())
            fDir.mkdirs();
        return file.exists();
    }

    private class MyDownload extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            Log.e(dir, "start");
            dialog = ProgressDialog.show(context, null, "Downloading...");
//            dialog = new ProgressDialog(context);
//            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            dialog.setTitle("Downloading...");


//            dialog.setMax(100);
//            dialog.setProgress(0);
            dialog.show();

            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            for (int i = 0; i < urls.size(); i++)
                totalFileSize(urls.get(i));

            Log.e("TEST", "Total Size :" + total + ":" + urls.size());
            for (int i = 0; i < urls.size(); i++)
                download(urls.get(i), i);


            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dialog.setMessage("Downloading " + values[0] + " %");
            Log.e("DOWNL", values[0] + " %");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Log.e(dir, "stop");
//            Utils.savePref(MyApplication.getAppContext(), "2", Global.MAXIMUM_CONTENT_BANGLA + "");
//            Utils.savePref(MyApplication.getAppContext(), "2", Global.MAXIMUM_CONTENT_ONGKO + "");
//            Utils.savePref(MyApplication.getAppContext(), "2", Global.MAXIMUM_CONTENT_ENGLISH + "");
//            Utils.savePref(MyApplication.getAppContext(), "2", Global.MAXIMUM_CONTENT_MATH + "");
            DialogSoundOnOff.savePref(context, "1", Global.startDownBan + "");
//            DialogSoundOnOff.savePref(MyApplication.getAppContext(), "1", Global.startDownBan + "");
            DialogSoundOnOff.savePref(context, "2", Global.startDownOngk + "");
//            DialogSoundOnOff.savePref(MyApplication.getAppContext(), "2", Global.startDownOngk + "");
            DialogSoundOnOff.savePref(context, "3", Global.startDownEng + "");
//            DialogSoundOnOff.savePref(MyApplication.getAppContext(), "3", Global.startDownEng + "");
            DialogSoundOnOff.savePref(context, "4", Global.startDownMath + "");
//            DialogSoundOnOff.savePref(MyApplication.getAppContext(), "4", Global.startDownMath + "");
            dialog.dismiss();

            if (result) {
            }

        }

        private void download(String _url, int dirPos) {
            int count = 0;
            try {
                URL url = new URL(_url);
                // downlod the file
                InputStream input = new BufferedInputStream(url.openStream());

                String fileName = _url.substring(_url.lastIndexOf("/") + 1);
                OutputStream output = new FileOutputStream(dirs.get(dirPos) + File.separator + fileName);

                byte data[] = new byte[1024];

                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);

                    // publishing the progress....
                    downloaded += count;
                    publishProgress((int) (downloaded * 100 / total));

                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Utils.log("fail", "fail" + e);
            }
        }

        private void totalFileSize(String _url) {
            try {
                URL url = new URL(_url);
                URLConnection conn = url.openConnection();
                conn.setConnectTimeout(3000);
                conn.connect();
                total += conn.getContentLength();
            } catch (Exception ex) {

            }


        }

    }
}
