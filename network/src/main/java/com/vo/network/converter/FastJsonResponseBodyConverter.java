package com.vo.network.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.chuangdu.library.util.LogUtil;
import com.chuangdu.library.util.RsaUtil;
import com.vo.network.ApiEntity;
import com.vo.network.RequestErrorException;
import com.vo.network.BaseResponse;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;


/**
 * @author sc
 */
public class FastJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private static final String TAG = FastJsonResponseBodyConverter.class.getSimpleName();
    private final Type type;

    FastJsonResponseBodyConverter(Type type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String s = value.string();

        // 获取 Api 实体
        if (type == ApiEntity.class) {
            return JSON.parseObject(s, type);
        }

        if (type == BaseResponse.class) {
            BaseResponse bean = JSON.parseObject(s, type);
            if (bean != null && bean.getSuccess()) {
                return (T) bean;
            } else {
                throw new RequestErrorException(bean);
            }
        }

        // 获取封装实体
        BaseResponse bean;
        try {
            bean = JSON.parseObject(s, new TypeReference<BaseResponse<String>>(){});
        } catch (Exception e) {
            bean = null;
            s = s.replace("\"", "");
        }
        if (bean == null) {
            String jsonData = null;
            if ("error".equals(s)) {
                return null;
            }
            try {
                jsonData = RsaUtil.decryptByPrivate(RsaUtil.decodeURL(s), RsaUtil.PRIVATE_KEY_DEFAULT);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            if (jsonData == null) {
                return null;
            }
            bean = JSON.parseObject(jsonData, BaseResponse.class);
        }
        // 通过解析出来的实体判断
        if (null != bean && bean.getSuccess()) {
            try {
                // bean已经解析成功，防止data字段里面解析错误，所以使用try-catch。
                return JSON.parseObject(s, type);
            } catch (Exception e) {
                bean.setMsg("数据错误");
                LogUtil.e(TAG, "network response: parseObject() 数据错误");
            }
        }
        throw new RequestErrorException(bean);
    }
}
