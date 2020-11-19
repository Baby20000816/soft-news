package com.soft1851.files.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    String uploadFdfs(MultipartFile file,String fileExtName) throws Exception;
}
