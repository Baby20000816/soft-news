package com.soft1851.files.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    String uploadFdfs(MultipartFile file,String fileExtName) throws Exception;

    public String uploadOSS(MultipartFile file,String userId,String fileExtName) throws Exception;
}
