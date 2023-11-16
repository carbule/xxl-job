package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.service.ObsService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @ClassName ObsController
 * @Description
 * @Author chenyiqiang
 * @Date 2023/8/3 10:58
 * @Version 1.0
 */
@RestController
@RequestMapping("/obs")
public class ObsController {

    @Resource
    private ObsService obsService;

    /**
     * 上传公共文件
     *
     * @param fileCode 通过code查找bucket
     * @param file
     * @return
     */
    @PostMapping("/putPublicObject/{fileCode}")
    public R<?> putPublicObject(@PathVariable("fileCode") String fileCode, MultipartFile file) {
        String fileUrl = obsService.putPublicObject(fileCode, file);
        return R.success(fileUrl);
    }

    /**
     * 上传私有文件
     *
     * @param fileCode 通过code查找bucket
     * @param file
     * @return
     */
    @PostMapping("/putPrivateObject/{fileCode}")
    public R<?> putPrivateObject(@PathVariable("fileCode") String fileCode, MultipartFile file) {
        String fileName = obsService.putPrivateObject(fileCode, file);
        return R.success(fileName);
    }

    /**
     * 下载文件
     *
     * @param fileCode
     * @param fileName
     * @param response
     * @return
     */
    @GetMapping("/getObject/{fileCode}/{fileName}")
    public void getObject(@PathVariable("fileCode") String fileCode, @PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
        obsService.getObject(fileCode, fileName, response);
    }

    /**
     * 获取文件签名Url
     *
     * @param fileCode
     * @param fileName
     * @return
     */
    @GetMapping("/getSignedUrl/{fileCode}/{fileName}")
    public R<?> getSignedUrl(@PathVariable("fileCode") String fileCode, @PathVariable("fileName") String fileName) {
        String url = obsService.getSignedUrl(fileCode, fileName);
        return R.success(url);
    }

    /**
     * 删除文件
     *
     * @param fileCode 通过code查找bucket
     * @param fileName
     * @return
     */
    @DeleteMapping("/deleteObject/{fileCode}/{fileName}")
    public R<?> removeObject(@PathVariable("fileCode") String fileCode, @PathVariable("fileName") String fileName) {
        obsService.deleteObject(fileCode, fileName);
        return R.ok();
    }
}
