package com.cupagroup.controlcalidad.utils;

import android.widget.Adapter;

import androidx.room.ColumnInfo;

public class CustomListActiveEnsayo {

    @ColumnInfo(name = "id_ensayo")
    private Long mIdEnsayo;

    @ColumnInfo(name = "ref_comercial")
    private String mRefComercial;

    @ColumnInfo(name = "formatos")
    private String mFormatos;

    @ColumnInfo(name = "forma")
    private String mForma;

    @ColumnInfo(name = "comment")
    private String mComment;

    @ColumnInfo(name = "create_at")
    private String mCreatedAt;

    public CustomListActiveEnsayo(
            Long mIdEnsayo,
            String mRefComercial,
            String mFormatos,
            String mForma,
            String mComment,
            String mCreatedAt
    ) {
        this.mIdEnsayo = mIdEnsayo;
        this.mRefComercial = mRefComercial;
        this.mFormatos = mFormatos;
        this.mForma = mForma;
        this.mComment = mComment;
        this.mCreatedAt = mCreatedAt;
    }

    public Long deleteSerial(Adapter adapter, Object position) {
        return  getmIdEnsayo();
    }

    public Long getmIdEnsayo() {
        return mIdEnsayo;
    }
    public void setmIdEnsayo(Long mIdEnsayo) {
        this.mIdEnsayo = mIdEnsayo;
    }

    public String getmRefComercial() {
        return mRefComercial;
    }
    public void setmRefComercial(String mRefComercial) {
        this.mRefComercial = mRefComercial;
    }

    public String getmFormatos() {
        return mFormatos;
    }
    public void setmFormatos(String mFormatos) {
        this.mFormatos = mFormatos;
    }

    public String getmForma() {
        return mForma;
    }
    public void setmForma(String mForma) {
        this.mForma = mForma;
    }

    public String getmComment() {
        return mComment;
    }
    public void setmComment(String mComment) {
        this.mComment = mComment;
    }

    public String getmCreatedAt() {
        return mCreatedAt;
    }
    public void setmCreatedAt(String mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }
}
