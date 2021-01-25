package com.vo.network;

/**
 * 数据返回的基础模型
 *
 */
public class BaseResponse<T> {

    /**
     * "返回的提示语"
     */

    public String message;

    /**
     * 当errCode为非0时 查找错误信息时使用
     */

    public Integer status;

    public T data;

    private int integerValue;

    public int getIntegerValue() {
        return integerValue;
    }

    public void setIntegerValue(int integerValue) {
        this.integerValue = integerValue;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String msg) {
        this.message = msg;
    }

    public Integer getErrCode() {
        if (status == null) {
            status = -1;
        }
        return status;
    }

    public void setErrCode(Integer errCode) {
        this.status = errCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return ErrorCode.isSuccess(status);
    }


    public static BaseResponse create(BaseResponse responseBean) {
        BaseResponse bean = new BaseResponse();
        bean.setMsg(responseBean.message);
        bean.setErrCode(responseBean.status);
        bean.setIntegerValue(responseBean.integerValue);
        return bean;
    }
}