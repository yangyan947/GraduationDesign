package com.baidu.ueditor.upload;

import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.FileType;
import com.baidu.ueditor.define.State;
import com.example.util.ImageUtil;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BinaryUploader {

    public static final State save(HttpServletRequest request,
                                   Map<String, Object> conf) {

        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile multipartFile = multipartRequest.getFile(conf.get("fieldName").toString());

            String savePath = (String) conf.get("savePath");
            String originFileName = multipartFile.getOriginalFilename();
            String suffix = FileType.getSuffixByFilename(originFileName);

            originFileName = originFileName.substring(0, originFileName.length() - suffix.length());
            savePath = savePath + suffix;

            long maxSize = ((Long) conf.get("maxSize")).longValue();

            if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
                return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
            }

            savePath = PathFormat.parse(savePath, originFileName);

            State storageState = StorageManager.saveFileByInputStream(multipartFile.getInputStream(), savePath, maxSize);
            saveFile(multipartFile, (String) conf.get("rootPath"), savePath);

            if (storageState.isSuccess()) {
                storageState.putInfo("url", PathFormat.format(savePath));
                storageState.putInfo("type", suffix);
                storageState.putInfo("original", originFileName + suffix);
            }

            return storageState;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return new BaseState(false, AppInfo.IO_ERROR);
    }

    private static boolean validType(String type, String[] allowTypes) {
        List<String> list = Arrays.asList(allowTypes);

        return list.contains(type);
    }

    private static String saveFile(MultipartFile file, String root, String fileName) throws Exception {
        String fileType = file.getContentType().split("/")[1];

        FileOutputStream fos = null;
        fileName = root + fileName;
        String[] strs =  fileName.split("/");
        root =  fileName.split(strs[strs.length - 1])[0];
        try {
            InputStream fis = file.getInputStream();
            // 转换文件为png格式，并保存在同名目录下
            File files = new File(root);
            // 判断文件夹是否存在,如果不存在则创建文件夹
            if (!files.exists()) {
                files.mkdirs();
            }
            if (file.getContentType().split("/")[0].equals("image")) {
//				if (path.endsWith(separator))
//					fileName = path + "upload" + separator + uuid + ".png";
//				else
//					fileName = path + separator + "upload" + separator + uuid + ".png";
                fos = new FileOutputStream(fileName);
                ImageUtil.convertFormat(fis, fos, fileType, "png", 0, 0);
                fos.flush();
                fos.close();
            }
        } catch (Exception ex) {
            System.out.println("文件取出失败，错误信息: " + ex.getMessage());
            if (fos != null)
                fos.close();
            throw ex;
        }
        return "";
    }
}
