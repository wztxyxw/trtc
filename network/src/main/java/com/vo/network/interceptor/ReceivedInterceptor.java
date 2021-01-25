package com.vo.network.interceptor;

import com.chuangdu.library.util.SP;
import com.vo.network.RetrofitRequestTool;

import java.io.IOException;

import io.reactivex.Flowable;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author sc
 */
public class ReceivedInterceptor implements Interceptor {

    private final SP mSP;

    public ReceivedInterceptor(SP sp) {
        mSP = sp;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            final StringBuffer cookieBuffer = new StringBuffer();
            Flowable.fromIterable(originalResponse.headers("Set-Cookie"))
                    .map(s -> {
                        // JSESSIONID=aaaQElHouiqmGh-oaQCtv; path=/
                        String[] cookieArray = s.split(";");
                        return cookieArray[0];
                    })
                    .subscribe(cookie -> {
                        RetrofitRequestTool.addHeader(mSP, "Cookie", cookie);
                    });
        }
        return originalResponse;
    }
}
