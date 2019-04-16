package com.kuan.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@RestController
public class wechatController {
    @RequestMapping(value = "/upload",method = { RequestMethod.POST,RequestMethod.GET})
    public ModelAndView uploadImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MultipartHttpServletRequest req =(MultipartHttpServletRequest)request;
        MultipartFile multipartFile =  req.getFile("file");
        String realPath = "E:/image";
        try {
            File dir = new File(realPath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file  =  new File(realPath,"animal.jpg");
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        ModelAndView mv = new ModelAndView();
        mv.setViewName("aiLoad");
        return mv;
    }
}
