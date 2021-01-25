package com.chuangdu.pad.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author sc
 */
public class IOUtil {

    public static void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }
}
