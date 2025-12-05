package com.yupi.yuoj.controller;

import cn.hutool.core.io.FileUtil;
import com.yupi.yuoj.common.BaseResponse;
import com.yupi.yuoj.common.ErrorCode;
import com.yupi.yuoj.common.ResultUtils;
import com.yupi.yuoj.constant.FileConstant;
import com.yupi.yuoj.exception.BusinessException;
import com.yupi.yuoj.config.CosClientConfig;
import com.yupi.yuoj.manager.CosManager;
import com.yupi.yuoj.model.dto.file.UploadFileRequest;
import com.yupi.yuoj.model.entity.User;
import com.yupi.yuoj.model.enums.FileUploadBizEnum;
import com.yupi.yuoj.service.UserService;
import java.io.File;
import java.util.Arrays;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件接口
 *

 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Resource
    private UserService userService;

    @Resource
    private CosManager cosManager;
    
    @Resource
    private CosClientConfig cosClientConfig;

    /**
     * 文件上传
     *
     * @param multipartFile
     * @param uploadFileRequest
     * @param request
     * @return
     */
    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(@RequestPart("file") MultipartFile multipartFile,
            UploadFileRequest uploadFileRequest, HttpServletRequest request) {
        log.info("收到文件上传请求, biz: {}, 文件名: {}, 文件大小: {} bytes", 
                uploadFileRequest != null ? uploadFileRequest.getBiz() : "null",
                multipartFile != null ? multipartFile.getOriginalFilename() : "null",
                multipartFile != null ? multipartFile.getSize() : 0);
        
        String biz = uploadFileRequest != null ? uploadFileRequest.getBiz() : null;
        if (biz == null || biz.isEmpty()) {
            log.error("biz 参数为空");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "biz 参数不能为空");
        }
        
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        if (fileUploadBizEnum == null) {
            log.error("无效的 biz 参数: {}", biz);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的 biz 参数: " + biz);
        }
        
        validFile(multipartFile, fileUploadBizEnum);
        User loginUser = userService.getLoginUser(request);
        log.info("用户信息: userId={}, userName={}", loginUser.getId(), loginUser.getUserName());
        // 文件目录：根据业务、用户来划分
        String uuid = RandomStringUtils.randomAlphanumeric(8);
        String filename = uuid + "-" + multipartFile.getOriginalFilename();
        String filepath = String.format("/%s/%s/%s", fileUploadBizEnum.getValue(), loginUser.getId(), filename);
        File file = null;
        try {
            // 上传文件
            // 注意：createTempFile 的第一个参数不能包含路径分隔符，所以使用 uuid 作为前缀
            file = File.createTempFile("upload_" + uuid, null);
            log.info("创建临时文件成功: {}", file.getAbsolutePath());
            
            multipartFile.transferTo(file);
            log.info("文件已转移到临时文件: {}, 大小: {} bytes", file.getAbsolutePath(), file.length());
            
            try {
                cosManager.putObject(filepath, file);
                log.info("文件已上传到COS: {}", filepath);
            } catch (Exception cosException) {
                log.error("COS上传失败: {}", cosException.getMessage(), cosException);
                throw cosException; // 重新抛出，让外层捕获并返回详细错误信息
            }
            
            // 返回可访问地址（优先使用配置的 host，否则使用常量）
            String cosHost = cosClientConfig != null ? cosClientConfig.getCosHost() : FileConstant.COS_HOST;
            String fileUrl = cosHost + filepath;
            log.info("文件访问地址: {}", fileUrl);
            return ResultUtils.success(fileUrl);
        } catch (Exception e) {
            log.error("file upload error, filepath = " + filepath, e);
            // 返回更详细的错误信息，帮助调试
            String errorMessage = "上传失败: " + e.getMessage();
            if (e.getCause() != null) {
                errorMessage += " (原因: " + e.getCause().getMessage() + ")";
            }
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
                if (!delete) {
                    log.error("file delete error, filepath = {}", filepath);
                }
            }
        }
    }

    /**
     * 校验文件
     *
     * @param multipartFile
     * @param fileUploadBizEnum 业务类型
     */
    private void validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        final long ONE_M = 1024 * 1024L;
        if (FileUploadBizEnum.USER_AVATAR.equals(fileUploadBizEnum)) {
            if (fileSize > ONE_M) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 1M");
            }
            if (!Arrays.asList("jpeg", "jpg", "svg", "png", "webp").contains(fileSuffix)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
            }
        }
    }
}
