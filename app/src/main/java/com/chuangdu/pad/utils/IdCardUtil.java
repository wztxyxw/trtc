package com.chuangdu.pad.utils;

import android.app.Activity;
import android.graphics.BitmapFactory;

import com.chuangdu.pad.models.IdCardMsg;
import com.sdt.Sdtapi;

import cn.ywho.api.decode.DecodeWlt;

/**
 * @author sc
 * @since 2020-10-19
 */
public class IdCardUtil {

    Sdtapi sdta;

    public interface CallBack{
        void success();
        void error(String msg);
    }
    /*民族列表*/
    String [] nation = {"汉", "蒙古", "回", "藏", "维吾尔", "苗", "彝", "壮", "布依", "朝鲜",
            "满", "侗", "瑶", "白", "土家", "哈尼", "哈萨克", "傣", "黎", "傈僳",
            "佤", "畲", "高山", "拉祜", "水", "东乡", "纳西", "景颇", "克尔克孜", "土",
            "达斡尔", "仫佬", "羌", "布朗", "撒拉", "毛南", "仡佬", "锡伯", "阿昌", "普米",
            "塔吉克", "怒", "乌兹别克", "俄罗斯", "鄂温克", "德昂", "保安", "裕固", "京", "塔塔尔",
            "独龙", "鄂伦春", "赫哲", "门巴", "珞巴", "基诺"
    };

    IdCardUtil instance;

    public IdCardUtil(){

    }

    public IdCardUtil(Activity activity, CallBack callBack){

        init(activity, callBack);
    }


    public IdCardUtil getInstance(){

        return instance;

    }


    public void init(Activity activity, CallBack callBack) {
        try {
            sdta = new Sdtapi(activity);
            callBack.success();
        } catch (Exception e) {
            e.printStackTrace();
            if(e.getCause() == null) {
                //USB设备异常或无连接，应用程序即将关闭。

                callBack.error("USB设备异常或无连接，应用程序即将关闭。");
            } else {
                //USB设备未授权，需要确认授权

                callBack.error("USB设备未授权，弹出请求授权窗口后，请点击\"确定\"继续");
            }
        }
    }

    public String samId(){
        char [] puSAMID = new char[36];
        int ret = sdta.SDT_GetSAMIDToStr( puSAMID);

//        if(ret == 0x90)
//            ViewRe.setText(puSAMID, 0, puSAMID.length);
//        else
//        {
//            String show = "错误:" + String.format("0x%02x", ret);
//            ViewRe.setText(show);
//
//        }

        if(ret == 0x90) {
            return new String(puSAMID);
        } else {
            return "";
        }
    }

    public IdCardMsg readIdCard(){
        int ret;
        String show = "";

        // 开始读卡时间
        Long timeStart = System.currentTimeMillis();

        sdta.SDT_StartFindIDCard();//寻找身份证
        sdta.SDT_SelectIDCard();//选取身份证

        IdCardMsg msg = new IdCardMsg();//身份证信息对象，存储身份证上的文字信息

        ret = ReadFullMsgToStr(msg);

        // 结束读卡时间
//        Long timeFinish = System.currentTimeMillis();
//
//        if(ret == 0x90) {
//            show =   "姓名:" + msg.name + '\n'
//                    + "性别:" + msg.sex + '\n'
//                    + "民族:" + msg.nation_str + "族" + '\n'
//                    + "出生日期:" + msg.birth_year + "-" + msg.birth_month + "-" + msg.birth_day + '\n'
//                    + "住址:" + msg.address + '\n'
//                    + "身份证号码:" + msg.id_num + '\n'
//                    + "签发机关:" + msg.sign_office + '\n'
//                    + "有效期起始日期:" + msg.useful_s_date_year + "-" + msg.useful_s_date_month + "-" + msg.useful_s_date_day + '\n'
//                    + "有效期截止日期:" + msg.useful_e_date_year + "-" + msg.useful_e_date_month + "-" + msg.useful_e_date_day + '\n';
//
//        } else {
//            show = "读基本信息失败:" + String.format("0x%02x", ret);
//        }

//        ViewRe.setText(show);
//        showPicture(msg.picture_info);
////                ivImage.setImageBitmap(msg.picture_info);
//
//        ViewRe.append("\r\n读卡时间：" + String.valueOf(timeFinish - timeStart) + "ms");
//
//        ViewRe.append("\r\n指纹信息：" + msg.finger_info);
        if(ret == 0x90) {
            return msg;
        } else {
            return null;
        }

    }
    // 读取身份证中所有信息
    public int ReadFullMsgToStr(IdCardMsg msg)
    {
        int ret;
        // 文字信息长度
        int []puiCHMsgLen = new int[1];
        // 照片长度信息
        int []puiPHMsgLen = new int[1];
        // 指纹信息长度
        int [] puiFPMsgLen = new int[1];

        // 文字信息
        byte [] pucCHMsg = new byte[256];
        // 照片信息
        byte [] pucPHMsg = new byte[1024];
        byte[] bmp = new byte[14 + 40 + 308 * 126];
        // 指纹信息
        byte [] pucFPMsg = new byte[1024];

        //sdtapi中标准接口，输出字节格式的信息。
        ret = sdta.SDT_ReadBaseFPMsg(pucCHMsg, puiCHMsgLen, pucPHMsg, puiPHMsgLen, pucFPMsg, puiFPMsgLen);

        if(ret == 0x90)
        {
            try
            {
                char [] pucCHMsgStr = new char[128];

                // 将读取的身份证中的信息字节，解码成可阅读的文字
                DecodeByte(pucCHMsg, pucCHMsgStr);
                // 将信息解析到msg中
                PareseItem(pucCHMsgStr, msg);
                // 照片信息
                DecodeWlt.hxgc_Wlt2Bmp(pucPHMsg, bmp, 708);

                msg.picture_info = BitmapFactory.decodeByteArray(bmp, 0, bmp.length);

                msg.picture_data = bmp;

//                msg.picture_info = DecodeWlt.hxgc_Wlt2Bmp2(pucPHMsg);

//                msg.picture_info = BitmapFactory.decodeByteArray(pucPHMsg, 0, bmp.length);

                // 指纹信息
                msg.finger_info = bytesToHexString(pucFPMsg);
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }

        return ret;
    }
    //字节解码函数
    public void DecodeByte(byte[] msg, char []msg_str) throws Exception
    {
        byte[] newmsg = new byte[msg.length + 2];

        newmsg[0] = (byte) 0xff;
        newmsg[1] = (byte) 0xfe;

        for(int i = 0; i < msg.length; i++) {
            newmsg[i + 2] = msg[i];
        }

        String s = new String(newmsg, "UTF-16");
        for(int i = 0; i < s.toCharArray().length; i++) {
            msg_str[i] = s.toCharArray()[i];
        }


    }

    //分段信息提取
    public void  PareseItem(char []pucCHMsgStr, IdCardMsg msg)
    {
        msg.name = String.copyValueOf(pucCHMsgStr, 0, 15);
        String sex_code = String.copyValueOf(pucCHMsgStr, 15, 1);

        if(sex_code.equals("1")) {
            msg.sex = "男";
        } else if(sex_code.equals("2")) {
            msg.sex = "女";
        } else if(sex_code.equals("0")) {
            msg.sex = "未知";
        } else if (sex_code.equals("9")) {
            msg.sex = "未说明";
        }

        String nation_code = String.copyValueOf(pucCHMsgStr, 16, 2);
        msg.nation_str = nation[Integer.valueOf(nation_code) - 1];


        msg.birth_year = String.copyValueOf(pucCHMsgStr, 18, 4);
        msg.birth_month = String.copyValueOf(pucCHMsgStr, 22, 2);
        msg.birth_day = String.copyValueOf(pucCHMsgStr, 24, 2);
        msg.address = String.copyValueOf(pucCHMsgStr, 26, 35);
        msg.id_num = String.copyValueOf(pucCHMsgStr, 61, 18);
        msg.sign_office = String.copyValueOf(pucCHMsgStr, 79, 15);

        msg.useful_s_date_year = String.copyValueOf(pucCHMsgStr, 94, 4);
        msg.useful_s_date_month = String.copyValueOf(pucCHMsgStr, 98, 2);
        msg.useful_s_date_day = String.copyValueOf(pucCHMsgStr, 100, 2);

        msg.useful_e_date_year = String.copyValueOf(pucCHMsgStr, 102, 4);
        msg.useful_e_date_month = String.copyValueOf(pucCHMsgStr, 106, 2);
        msg.useful_e_date_day = String.copyValueOf(pucCHMsgStr, 108, 2);

    }

    public String bytesToHexString(byte[] src)
    {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0)
        {
            return null;
        }
        for (int i = 0; i < src.length; i++)
        {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2)
            {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }
}
