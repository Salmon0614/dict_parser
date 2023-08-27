package com.salmon.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析百度词库工具
 *
 * @author Salmon
 * @since 2023-08-27
 */
public class ParseBaiduDictUtils {

    /**
     * 解析词库文件(bdict)
     *
     * @param file 读取的文件
     * @return 返回所有词汇
     */
    public static List<String> parseData(File file) throws Exception {
        List<String> wordList = new ArrayList<>();
        //存储字节流
        ByteArrayOutputStream dataOut = new ByteArrayOutputStream();
        //读取文件
        FileChannel fileChannel = new RandomAccessFile(file, "r").getChannel();
        // 将文件通道中的数据传输到 ByteArrayOutputStream
        fileChannel.transferTo(0, fileChannel.size(), Channels.newChannel(dataOut));
        fileChannel.close();
        //获取字节数组
        ByteBuffer dataRawBytes = ByteBuffer.wrap(dataOut.toByteArray());
        //设置字节序为小端字节序
        dataRawBytes.order(ByteOrder.LITTLE_ENDIAN);
        byte[] buf = new byte[1024];
        byte[] pinyin = new byte[1024];
        //设置为字典数据的偏移位置，0x350为百度词典数据的起始偏移
        dataRawBytes.position(0x350);
        String word;
        while (dataRawBytes.position() < dataRawBytes.capacity()) {
            //得到词的字节长度
            int length = dataRawBytes.getShort();
            dataRawBytes.getShort();
            try {
                //拼音部分，这里不处理，2*length，对应拼音信息
                dataRawBytes.get(pinyin, 0, 2 * length);
                //unicode编码的中文，2*length，对应的被编码的信息
                dataRawBytes.get(buf, 0, 2 * length);
                //UTF-16小端序编码解析为字符串
                word = new String(buf, 0, 2 * length, StandardCharsets.UTF_16LE);
                wordList.add(word);
            } catch (Exception e) {
                return wordList;
            }
        }
        return wordList;
    }

}
