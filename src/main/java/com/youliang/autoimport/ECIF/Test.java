package com.youliang.autoimport.ECIF;

import org.apache.commons.net.ftp.FTPClient;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;

/**
 * Created by lhb on 2017-12-29.
 */
public class Test {
    private FTPClient ftpClient;
    private String fileName, strencoding;
    private String ip = "";        // 服务器IP地址
    private String userName = "";        // 用户名
    private String userPwd = "";        // 密码
    private int port = 21;      // 端口号
    private String path = "/a/b/";        // 读取文件的存放目录

    /**
     * init ftp servere
     */
    public Test() {
        this.reSet();
    }

    public void reSet() {
        // 以当前系统时间拼接文件名
        fileName = "test.txt";
        strencoding = "UTF-8";
        this.connectServer(ip, port, userName, userPwd, path);

    }

    public void connectServer(String ip, int port, String userName, String userPwd, String path) {
        ftpClient = new FTPClient();
        try {
            // 连接
            ftpClient.connect(ip, port);
            // 登录
            ftpClient.login(userName, userPwd);
            if (path != null && path.length() > 0) {
                // 跳转到指定目录
                ftpClient.changeWorkingDirectory(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeServer() {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

/*
    public List<String> getFileList(String path) {
        List<String> fileLists = new ArrayList<String>();
        // 获得指定目录下所有文件名
        FTPFile[] ftpFiles = null;
        try {
            ftpFiles = ftpClient.listFiles(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; ftpFiles != null && i < ftpFiles.length; i++) {
            FTPFile file = ftpFiles[i];
            if (file.isFile()) {
                fileLists.add(file.getName());
            }
        }
        return fileLists;
    }
*/

    public String readFile() throws ParseException {
        InputStream ins = null;
        StringBuilder builder = null;
        try {
            // 从服务器上读取指定的文件
            ins = ftpClient.retrieveFileStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins, strencoding));
            String line;
            builder = new StringBuilder(150);
            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
                builder.append(line);
            }
            reader.close();
            if (ins != null) {
                ins.close();
            }
            // 主动调用一次getReply()把接下来的226消费掉. 这样做是可以解决这个返回null问题
            ftpClient.getReply();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

/*
    public void deleteFile(String fileName) {
        try {
            ftpClient.deleteFile(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    public static void main(String[] args) {
        try {
            Test ftp = new Test();
            String str = null;
            str = ftp.readFile();
            System.out.println(str);
            ftp.closeServer();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}