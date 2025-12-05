package com.yupi.yuoj.manager;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.yupi.yuoj.config.CosClientConfig;
import java.io.File;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Cos 对象存储操作
 *

 */
@Component
@Slf4j
public class CosManager {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;

    /**
     * 上传对象
     *
     * @param key 唯一键
     * @param localFilePath 本地文件路径
     * @return
     */
    public PutObjectResult putObject(String key, String localFilePath) {
        return putObject(key, new File(localFilePath));
    }

    /**
     * 上传对象
     *
     * @param key 唯一键
     * @param file 文件
     * @return
     */
    public PutObjectResult putObject(String key, File file) {
        // 验证配置
        if (cosClientConfig == null || 
            cosClientConfig.getBucket() == null || 
            cosClientConfig.getBucket().equals("xxx") ||
            cosClientConfig.getBucket().isEmpty()) {
            throw new RuntimeException("COS 配置未正确设置，请检查 application.yml 中的 cos.client.bucket 配置");
        }
        
        if (cosClient == null) {
            throw new RuntimeException("COS 客户端未初始化，请检查 COS 配置");
        }
        
        try {
            log.info("准备上传文件到COS, bucket: {}, key: {}", cosClientConfig.getBucket(), key);
            PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key, file);
            PutObjectResult result = cosClient.putObject(putObjectRequest);
            log.info("文件上传到COS成功, key: {}, ETag: {}", key, result.getETag());
            return result;
        } catch (CosServiceException e) {
            // COS 服务端异常
            log.error("COS 服务端异常: {}", e.getMessage(), e);
            throw new RuntimeException("COS 上传失败: " + e.getErrorMessage() + " (错误码: " + e.getErrorCode() + ", 请求ID: " + e.getRequestId() + ")", e);
        } catch (CosClientException e) {
            // COS 客户端异常
            log.error("COS 客户端异常: {}", e.getMessage(), e);
            throw new RuntimeException("COS 连接失败: " + e.getMessage() + "。请检查网络连接和 COS 配置（accessKey, secretKey, region, bucket）", e);
        } catch (Exception e) {
            log.error("文件上传异常: {}", e.getMessage(), e);
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }
}
