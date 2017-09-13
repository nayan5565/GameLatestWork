package com.example.nayan.gameverson2.model;

/**
 * Created by NAYAN on 8/3/2016.
 */
public class MData {
    private int id;
    private int isSavePoint;
    private int levelId, sublevelId;

    public int getIsSavePoint() {
        return isSavePoint;
    }

    public void setIsSavePoint(int isSavePoint) {
        this.isSavePoint = isSavePoint;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public int getSublevelId() {
        return sublevelId;
    }

    public void setSublevelId(int sublevelId) {
        this.sublevelId = sublevelId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
