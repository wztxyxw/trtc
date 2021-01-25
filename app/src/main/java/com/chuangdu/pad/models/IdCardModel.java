package com.chuangdu.pad.models;

import android.text.TextUtils;
import android.util.Base64;

/**
 * @author sc
 * @since 2020-10-19
 */
public class IdCardModel {
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

    //public String finger_info;
    public String path;
    public String fileUrl;
    public String picture_data;

    public IdCardModel(){

    }
    public IdCardModel(IdCardMsg msg){
        if (!TextUtils.isEmpty(msg.name)){
            this.name = msg.name.replaceAll(" ", "");
        }
        if (!TextUtils.isEmpty(msg.sex)){
            this.sex = msg.sex;
        }
        if (!TextUtils.isEmpty(msg.nation_str)){
            this.nation_str = msg.nation_str;
        }
        if (!TextUtils.isEmpty(msg.birth_year)){
            this.birth_year = msg.birth_year;
        }
        if (!TextUtils.isEmpty(msg.birth_month)){
            this.birth_month = msg.birth_month;
        }
        if (!TextUtils.isEmpty(msg.birth_day)){
            this.birth_day = msg.birth_day;
        }
        if (!TextUtils.isEmpty(msg.address)){
            this.address = msg.address.replaceAll(" ", "");
        }
        if (!TextUtils.isEmpty(msg.id_num)){
            this.id_num = msg.id_num;
        }
        if (!TextUtils.isEmpty(msg.sign_office)){
            this.sign_office = msg.sign_office.replaceAll(" ", "");
        }
        if (!TextUtils.isEmpty(msg.useful_s_date_year)){
            this.useful_s_date_year = msg.useful_s_date_year;
        }
        if (!TextUtils.isEmpty(msg.useful_s_date_month)){
            this.useful_s_date_month = msg.useful_s_date_month;
        }
        if (!TextUtils.isEmpty(msg.useful_s_date_day)){
            this.useful_s_date_day = msg.useful_s_date_day;
        }
        if (!TextUtils.isEmpty(msg.useful_e_date_year)){
            this.useful_e_date_year = msg.useful_e_date_year;
        }
        if (!TextUtils.isEmpty(msg.useful_e_date_month)){
            this.useful_e_date_month = msg.useful_e_date_month;
        }
        if (!TextUtils.isEmpty(msg.useful_e_date_day)){
            this.useful_e_date_day = msg.useful_e_date_day;
        }
        if (!TextUtils.isEmpty(msg.fileUrl)){
            this.fileUrl = msg.fileUrl;
        }
        if (!TextUtils.isEmpty(msg.path)){
            this.path = msg.path;
        }
        //this.picture_data = new String(msg.picture_data);
        this.picture_data = Base64.encodeToString(msg.picture_data, Base64.DEFAULT);
    }

    @Override
    public String toString() {
        return "IdCardModel{" +
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
                ", path='" + path + '\'' +
                '}';
    }


}
