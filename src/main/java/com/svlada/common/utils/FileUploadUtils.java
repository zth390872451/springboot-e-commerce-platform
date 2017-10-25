package com.svlada.common.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.svlada.common.utils.DateUtils.PART_DATE_FORMAT;

public class FileUploadUtils {

    /**
     * 保存文件路径：年/月/日/
     * @param
     * @Desc 上传文件
     */
    public static List<String> saveCommonFile(MultipartFile[] files, String uploadPath) {
        try {
            List<String> filePaths = new ArrayList<>();
            for (MultipartFile file : files){
                String tomcatPath = ApplicationSupport.getValue("tomcatPath");
                String path = tomcatPath + "/" + uploadPath + "/" +
                    DateUtils.getFormatDate(null, PART_DATE_FORMAT) + "/"
                    + UUID.randomUUID().toString()
                    +"/"+ file.getName() + "/";
                File dir = new File(path);
                if (!dir.exists()) {
                    if (!dir.mkdirs()) {
                        try {
                            throw new Exception("创建保存目录失败");
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                }
                String originalFilename = file.getOriginalFilename();
                String fileName = file.getName() + getFileSuffix(file.getOriginalFilename());
                File target = new File(dir, originalFilename);
                file.transferTo(target);
                String absolutePath = target.getAbsolutePath();
                String relativePath = absolutePath.replace(tomcatPath, "");
                filePaths.add(relativePath);
            }
            return filePaths;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取文件后缀
     * @param originalFileName
     * @return
     */
    public static String getFileSuffix(String originalFileName){
        int dot=originalFileName.lastIndexOf('.');
        if((dot>-1)&&(dot<originalFileName.length())){
            return originalFileName.substring(dot);
        }
        return originalFileName;
    }
}
