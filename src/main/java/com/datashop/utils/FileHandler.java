package com.datashop.utils;


import com.datashop.exception.DatashopException;
import io.swagger.models.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;

/**
 * Created by rasir on 2018/6/19.
 */
@Component
public class FileHandler {
    @Value("${file.base-path}")
    private String basePath;

    @Value("${file.static-path}")
    private String staticPath;

    private static String root;

    private static String publicPath;


    @PostConstruct
    public void init(){
        this.root = this.basePath;
        this.publicPath = this.staticPath;
    }


    public static String saveFile(InputStream is, String fileName,String dirName) {
        OutputStream os = null;
        String absPath = root+publicPath+dirName;
        try{
            byte[] bytes = new byte[1024];
            int len;
            File tempFile = new File(absPath);
            if(!tempFile.exists()){
                tempFile.mkdir();
            }
            String filePath = absPath+ "/"+fileName;
            os = new FileOutputStream(filePath);
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes,0,len);
            }
            return publicPath + "/" + fileName;
        } catch (Exception e){
            throw new DatashopException(e.getMessage(),500);
        } finally {
            try {
                os.close();
                is.close();
            } catch (IOException e) {
                throw new DatashopException(e.getMessage(),500);
            }
        }

    }

    public static void removeFile(String filePath){
        String absPath = root+filePath;
        try{
            File file = new File(absPath);
            if(file.isFile() && file.exists()){
                file.delete();
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new DatashopException(e.getMessage(),500);
        }
    }
}
