package com.yupi.yuoj.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 腾讯云对象存储客户端
 *

 */
@Configuration
@ConfigurationProperties(prefix = "cos.client")
@Data
public class CosClientConfig {

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * 区域
     */
    private String region;

    /**
     * 桶名
     */
    private String bucket;
    
    /**
     * COS 访问地址（可选，如果不配置则自动生成）
     * 格式：https://{bucket}.cos.{region}.myqcloud.com
     * 如果配置了自定义域名，请填写自定义域名
     */
    private String host;

    /**
     * 获取 COS 访问地址
     * 如果配置了 host，则使用配置的值
     * 否则自动生成：https://{bucket}.cos.{region}.myqcloud.com
     */
    public String getCosHost() {
        if (host != null && !host.isEmpty() && !host.equals("xxx")) {
            return host;
        }
        // 自动生成 COS 访问地址
        return String.format("https://%s.cos.%s.myqcloud.com", bucket, region);
    }

    @Bean
    public COSClient cosClient() {
        // 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
        // 设置bucket的区域, COS地域的简称请参照 https://www.qcloud.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        // 生成cos客户端
        return new COSClient(cred, clientConfig);
    }
}