package com.pldk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Calendar;

/*@RunWith(SpringRunner.class)
@SpringBootTest*/
public class BootApplicationTests {

    /*@Test
    public void contextLoads() {


    }*/
    public static void main(String[] args) {
        /*String path = "http://192.168.0.112/abc/qwe";
        int i = 0;
        int s = 0;
        while (i++ < 4) {
            s = path.indexOf("/", s + 1);
            System.out.println(s);
        }

        System.out.println(s);
        String substring = path.substring(s+1);
        System.out.println(substring);*/
        //只需给   源文件目录加文件名   和   目标文件目录  两个参数即可
        try {
            String ywjUrl = "F:\\javatask\\zhl.txt";    //源文件
            File file=new File(ywjUrl);//拿到源文件

            String mbwjUrl = "F:\\javatask\\zhlll\\"+file.getName(); // 目标目录 加文件名
            String mbwjMl = "F:\\javatask\\zhlll\\"; //目标文件目录

            File destFile = new File(mbwjMl);
            if (!destFile.exists()) {
                destFile.mkdirs();// 如果没有文件夹 去创建
            }

            File newfile = new File(mbwjUrl);//目标文件目录


            if (file.renameTo(newfile)) //源文件移动至目标文件目录
            {
                System.out.println("File is moved successful!");//输出移动成功
            }
            else
            {
                System.out.println("File is failed to move !");//输出移动失败
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
