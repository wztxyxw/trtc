package com.vo.network;

/**
 * @author sc
 */
public class RequestErrorException extends RuntimeException {

    public RequestErrorException(BaseResponse bean) {
        message = bean.getMsg();
        errCode = bean.getErrCode();
        integerValue = bean.getIntegerValue();
    }

    public RequestErrorException(int errCode, String msg) {
        this.errCode = errCode;
        this.message = msg;
    }

    private String message;

    private int errCode;

    private int httpStatus;

    private String data;

    private int integerValue;

    public int getIntegerValue() {
        return integerValue;
    }

    public void setIntegerValue(int integerValue) {
        this.integerValue = integerValue;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getErrCode() {
        return errCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "RequestErrorException{" +
                "message='" + message + '\'' +
                ", errCode=" + errCode +
                ", httpStatus=" + httpStatus +
                ", data='" + data + '\'' +
                '}';
    }
}