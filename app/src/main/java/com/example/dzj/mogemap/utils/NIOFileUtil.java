package com.example.dzj.mogemap.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * @author: dzj
 * @date: 2020/6/9 14:09
 */
public class NIOFileUtil {

    public static void read(String filePath, int allocate, String charSet) throws IOException {
        RandomAccessFile access = new RandomAccessFile(filePath, "r");

        //FileInputStream inputStream = new FileInputStream(this.file);
        FileChannel channel = access.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(allocate);

        CharBuffer charBuffer = CharBuffer.allocate(allocate);
        Charset charset = Charset.forName(charSet);
        CharsetDecoder decoder = charset.newDecoder();
        int length = channel.read(byteBuffer);
        while (length != -1) {
            byteBuffer.flip();
            decoder.decode(byteBuffer, charBuffer, true);
            charBuffer.flip();
            System.out.println(charBuffer.toString());
            // 清空缓存
            byteBuffer.clear();
            charBuffer.clear();
            // 再次读取文本内容
            length = channel.read(byteBuffer);
        }
        channel.close();
        if (access != null) {
            access.close();
        }
    }

    public static void write(String filePath, String content, int allocate, String charSet) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        // FileOutputStream outputStream = new FileOutputStream(this.file); //文件内容覆盖模式 --不推荐
        FileOutputStream outputStream = new FileOutputStream(filePath, true); //文件内容追加模式--推荐
        FileChannel channel = outputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(allocate);
        byteBuffer.put(content.getBytes(charSet));
        byteBuffer.flip();//读取模式转换为写入模式
        channel.write(byteBuffer);
        channel.close();
        if(outputStream != null){
            outputStream.close();
        }
    }

    /**
     * nio事实现文件拷贝
     * @param source
     * @param target
     * @param allocate
     * @throws IOException
     */
    public static void nioCpoy(String source, String target, int allocate) throws IOException{
        ByteBuffer byteBuffer = ByteBuffer.allocate(allocate);
        FileInputStream inputStream = new FileInputStream(source);
        FileChannel inChannel = inputStream.getChannel();

        FileOutputStream outputStream = new FileOutputStream(target);
        FileChannel outChannel = outputStream.getChannel();

        int length = inChannel.read(byteBuffer);
        while(length != -1){
            byteBuffer.flip();//读取模式转换写入模式
            outChannel.write(byteBuffer);
            byteBuffer.clear(); //清空缓存，等待下次写入
            // 再次读取文本内容
            length = inChannel.read(byteBuffer);
        }
        outputStream.close();
        outChannel.close();
        inputStream.close();
        inChannel.close();
    }
}
