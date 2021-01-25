package com.chuangdu.pad.models.req;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author sc
 * @since 2020-04-15
 */
public class BaseRequest {

    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<String, Object>();
        // 将json字符串转换成jsonObject
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(this));

//        Gson gson = new Gson();
//
//        String mapJson =  gson.toJson(this);
//
//        data = gson.fromJson(mapJson, Map.class);

        Iterator it = jsonObject.keySet().iterator();
        // 遍历jsonObject数据，添加到Map对象
        while (it.hasNext()) {
            String key = String.valueOf(it.next());
            Object value = jsonObject.get(key);
            if (null != value) {
                data.put(key, value);
            }
        }
        return data;
    }
}
