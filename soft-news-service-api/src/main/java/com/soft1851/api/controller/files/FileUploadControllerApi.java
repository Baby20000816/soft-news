package com.soft1851.api.controller.files;

import com.soft1851.result.GraceResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "文件上传controller",tags = {"文件上传controller"})
@RequestMapping("fs")
public interface FileUploadControllerApi {
//    @ApiOperation(value = "上传多图",notes = "上传多图",httpMethod = "POST")
//    @GetMapping("/readInGridFS")
//    GraceResult doAliImageReview(String pendingImageUrl) throws  Exception;

    @ApiOperation(value = "上传用户头像",notes = "上传用户头像",httpMethod = "POST")
    @PostMapping("uploadFace")
    GraceResult uploadFace(@RequestParam String userId, MultipartFile file) throws Exception;

    @ApiOperation(value = "上传多图",notes = "上传多图",httpMethod = "POST")
    @PostMapping("/uploadSomeFiles")
    GraceResult uploadSomeFiles(@RequestParam String userId,MultipartFile[] files) throws  Exception;

    @ApiOperation(value = "管理员人脸入库",notes = "管理员人脸入库",httpMethod = "POST")
    @PostMapping("uploadToGridFS")
    GraceResult uploadToGridFs(@RequestParam String username, @RequestParam(value = "file") MultipartFile multipartFile) throws Exception;


    @ApiOperation(value = "从gridFS中读取文件",notes = "从gridFS中读取文件",httpMethod = "GET")
    @GetMapping("readInGridFS")
    GraceResult readInGridFs(@RequestParam String faceId, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
