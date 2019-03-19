package com.leonds.blog.www.controller;

import com.leonds.blog.domain.dto.PostsQueryDto;
import com.leonds.blog.www.service.FrontPostsService;
import com.leonds.core.orm.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * @author Leon
 */
@Controller
public class HomeController {

    @Autowired
    private FrontPostsService frontPostsService;

    @GetMapping("/")
    public String index(PostsQueryDto postsQueryDto, Model model) {
        Page<Map<String, Object>> postsPage = frontPostsService.getPostsPage(postsQueryDto);
        model.addAttribute("posts", postsPage);
        return "index";
    }

    @GetMapping("/tags")
    public String tags() {
        return "tags";
    }

    @GetMapping("/item/{id}")
    public String item(@PathVariable(name = "id") String id, Model model, HttpServletRequest request) {
        Map<String, Object> posts = frontPostsService.getDetail(id);
        frontPostsService.addViewCount(id, getIp(request));
        model.addAttribute("posts", posts);
        return "postsDetail";
    }

    /**
     * 获取浏览器所在用户端的ip地址
     *
     * @param request
     * @return
     */
    private String getIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                try {
                    ipAddress = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException e) {
                    ;
                }
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) {
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }

        return ipAddress;
    }
}
