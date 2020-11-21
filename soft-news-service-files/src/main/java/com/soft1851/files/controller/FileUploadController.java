package com.soft1851.files.controller;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.soft1851.api.controller.files.FileUploadControllerApi;
import com.soft1851.files.resource.FileResource;
import com.soft1851.files.service.UploadService;
import com.soft1851.result.GraceResult;
import com.soft1851.result.ResponseStatusEnum;
import com.soft1851.utils.extend.AliImageReviewUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileUploadController implements FileUploadControllerApi {
    private final UploadService uploadService;
    private final FileResource fileResource;
    private final AliImageReviewUtil aliImageReviewUtil;
    private final GridFsTemplate gridFsTemplate;
    @Override
    public GraceResult uploadFace(String userId, MultipartFile file) throws Exception {
        String path;
        if (file!=null){
            String fileName = file.getOriginalFilename();
            if (StringUtils.isNotBlank(fileName)){
                String[] fileNameArr = fileName.split("\\.");
                String suffix = fileNameArr[fileNameArr.length-1];
                if (!"png".equalsIgnoreCase(suffix) &&
                        !"jpg".equalsIgnoreCase(suffix) &&
                        !"jpeg".equalsIgnoreCase(suffix)
                ){return GraceResult.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);
                }path = uploadService.uploadFdfs(file,suffix);
            }else {
                return GraceResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
            }
        }else{
            return GraceResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }
        log.info("path="+path);
        String finalPath;
        if (StringUtils.isNotBlank(path)){
            finalPath = fileResource.getHost()+path;
        }else {
            return GraceResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
        return GraceResult.ok(doAliImageReview(finalPath));
    }

    public static final String FAILED_IMAGE_URL = "https://niit-soft.oss-cn-hangzhou.aliyuncs.com/avatar/failed.jpg";

    private String doAliImageReview(String pendingImageUrl) {
        log.info(pendingImageUrl);
        boolean result = false;
        try{
            result = aliImageReviewUtil.reviewImage(pendingImageUrl);
        }catch (Exception e){
            System.err.println("图片识别出错");
        }
        if (!result){
            return FAILED_IMAGE_URL;
        }
        return pendingImageUrl;
    }

    @Override
    public GraceResult uploadSomeFiles(String userId, MultipartFile[] files) throws Exception {
        List<String> imageUrlList = new ArrayList<>();
        if (files!=null&&files.length>0){
            for (MultipartFile file:files){
                String path;
                if (file!=null){
                    String fileName = file.getOriginalFilename();
                    if (StringUtils.isNotBlank(fileName)){
                        String[] fileNameArr = fileName.split("\\.");
                        String suffix = fileNameArr[fileNameArr.length-1];
                        if (!"png".equalsIgnoreCase(suffix) &&
                                !"jpg".equalsIgnoreCase(suffix) &&
                                !"jpeg".equalsIgnoreCase(suffix)
                        ){
                            continue;
                        }
                        path = uploadService.uploadOSS(file,userId,suffix);
                    }else {
                        continue;
                    }
                }else {
                    continue;
                }
                String finalPath;
                if (StringUtils.isNotBlank(path)){
                    finalPath = fileResource.getOssHost()+path;
                    imageUrlList.add(finalPath);
                }
            }
        }
        return GraceResult.ok(imageUrlList);
    }

    @Override
    public GraceResult uploadToGridFs(String username, MultipartFile multipartFile) {
        Map<String, String> metaData = new HashMap<>(4);
        InputStream is = null;
        try {
            is = multipartFile.getInputStream();
        }catch (IOException e){
            e.printStackTrace();
        }
        String fileName = multipartFile.getOriginalFilename();

        assert is != null;
        ObjectId objectId = gridFsTemplate.store(is, fileName, metaData);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return GraceResult.ok(objectId.toHexString());
    }

    @Override
    public GraceResult readInGridFs(String faceId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(faceId)));
        if (gridFSFile == null){
            throw  new RuntimeException("No file with id:"+faceId);
        }
        System.out.println(gridFSFile.getFilename());
        GridFsResource resource = gridFsTemplate.getResource(gridFSFile);
        InputStream inputStream;
        String content = null;
        byte[] bytes = new byte[(int) gridFSFile.getLength()];
        try {
            inputStream = resource.getInputStream();
            inputStream.read(bytes);
            inputStream.close();
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes);
            outputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return GraceResult.ok(new String(bytes));
    }
}
