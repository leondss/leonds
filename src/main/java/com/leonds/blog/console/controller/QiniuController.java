package com.leonds.blog.console.controller;

import com.leonds.blog.console.service.SettingsService;
import com.leonds.blog.domain.enums.SettingsName;
import com.leonds.core.resp.Response;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Leon
 */
@RestController
@RequestMapping("/api/qiniu")
public class QiniuController {

    @Autowired
    private SettingsService settingsService;

    @GetMapping("/token")
    public Response list() {
        String bucket = settingsService.getValue(SettingsName.QINIU_BUCKET);
        String result = auth().uploadToken(bucket);
        return Response.ok(result).build();
    }


    private Auth auth() {
        String ACCESS_KEY = settingsService.getValue(SettingsName.QINIU_ACCESS_KEY);
        String SECRET_KEY = settingsService.getValue(SettingsName.QINIU_SECRET_KEY);
        return Auth.create(ACCESS_KEY, SECRET_KEY);
    }
}
