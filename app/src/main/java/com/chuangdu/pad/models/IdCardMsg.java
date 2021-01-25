package com.chuangdu.pad.models;

import android.graphics.Bitmap;

/**
 * @author sc
 * @since 2020-10-19
 */
public class IdCardMsg {
    public String name;
    public String sex;
    public String nation_str;


    public String birth_year ;
    public String birth_month ;
    public String birth_day ;
    public String address ;
    public String id_num ;
    public String sign_office;

    public String useful_s_date_year ;
    public String useful_s_date_month ;
    public String useful_s_date_day ;

    public String useful_e_date_year ;
    public String useful_e_date_month;
    public String useful_e_date_day ;

    public byte[] picture_data;
    public Bitmap picture_info;
    public String finger_info;
    public String path;
    public String fileUrl;

    @Override
    public String toString() {
        return "IdCardMsg{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", nation_str='" + nation_str + '\'' +
                ", birth_year='" + birth_year + '\'' +
                ", birth_month='" + birth_month + '\'' +
                ", birth_day='" + birth_day + '\'' +
                ", address='" + address + '\'' +
                ", id_num='" + id_num + '\'' +
                ", sign_office='" + sign_office + '\'' +
                ", useful_s_date_year='" + useful_s_date_year + '\'' +
                ", useful_s_date_month='" + useful_s_date_month + '\'' +
                ", useful_s_date_day='" + useful_s_date_day + '\'' +
                ", useful_e_date_year='" + useful_e_date_year + '\'' +
                ", useful_e_date_month='" + useful_e_date_month + '\'' +
                ", useful_e_date_day='" + useful_e_date_day + '\'' +
                ", picture_info=" +
                ", finger_info='" + finger_info + '\'' +
                ", path='" + path + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                '}';
    }


}
