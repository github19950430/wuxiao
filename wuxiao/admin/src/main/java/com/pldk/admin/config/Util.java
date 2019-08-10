package com.pldk.admin.config;

import java.io.File;

public class Util {

    /**
     *  截取  / 出现第num次之后的字符串   删除指定目录下的文件  deletePath删除路径  + 删除的文件名
     * @param path
     * @param num
     * @return
     */
    public static Boolean Url(String path,String deletePath,Integer num){
        boolean delete = false;
        int i = 0;
        int s = 0;
        while (i++ < num) {
            s = path.indexOf("/", s + 1);
        }
        File file=new File(deletePath + "/" + path.substring(s + 1));
        if(file.exists()) {//判断是否文件存在
            delete = file.delete();  //执行删除
        }
        return delete;
    }
}
