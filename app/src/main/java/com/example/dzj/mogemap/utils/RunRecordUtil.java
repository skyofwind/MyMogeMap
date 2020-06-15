package com.example.dzj.mogemap.utils;

import android.annotation.SuppressLint;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: dzj
 * @date: 2020/6/9 14:35
 */
public class RunRecordUtil {

    public static void saveInLocal(String json) throws IOException {
        saveInLocal(json, getRunRecordName(), "UTF-8");
    }

    public static void saveInLocal(String json, String fileName, String charSet) throws IOException {
        File file = new File(FileUtil.FILE_PATH);
        FileUtil.judeDirExists(file);
        file = new File(FileUtil.RUN_RECORD_PATH);
        FileUtil.judeDirExists(file);
        String path = FileUtil.RUN_RECORD_PATH + "/" + "run_record_" + fileName + ".json";
        NIOFileUtil.write(path, json, 1024, charSet);
    }

    public static String getRunRecordName(Date date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String name = simpleDateFormat.format(date);
        return name;
    }

    public static String getRunRecordName() {
        Date date = new Date(System.currentTimeMillis());
        return getRunRecordName(date);
    }
}
