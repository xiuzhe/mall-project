package com.ishare.mall.center.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baidu.ueditor.ActionEnter;
import com.ishare.mall.center.controller.base.BaseController;



/**
 * Created by liaochenglei on 2015/9/7.
 * Description :
 * Version 1.0
 */
@Controller
@RequestMapping(value = "/ueditor")
public class UploadController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(UploadController.class);

    public static Logger getLog() {
        return log;
    }
    
    @RequestMapping("/dispatch")
    public void config(HttpServletRequest request,  HttpServletResponse response, String action,HttpSession session) {
            String rootPath = request.getSession().getServletContext().getRealPath("/");
            try {
                    String exec = new ActionEnter(request, rootPath).exec();
                   JSONObject jsonObject = new JSONObject(exec);
                    String URL = jsonObject.getString("url");
                    String[] ab = URL.split("/");
                    System.out.println(ab[4]+ab[5].substring(0,9));
                    session.setAttribute("URL", exec);
                    PrintWriter writer = response.getWriter();
                    writer.write(exec);
                    writer.flush();
                    writer.close();
            } catch (IOException e) {
                    e.printStackTrace();
            }
            
    }
}
