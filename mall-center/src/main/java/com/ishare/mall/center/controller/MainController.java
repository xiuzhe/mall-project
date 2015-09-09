package com.ishare.mall.center.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ishare.mall.center.controller.base.BaseController;
import com.ishare.mall.center.form.register.RegisterForm;
import com.ishare.mall.common.base.constant.uri.CenterURIConstant;
import com.ishare.mall.common.base.constant.view.CenterViewConstant;

/**
 * Created by ZhangZhaoxin on 2015/9/7.
 * Description :
 * Version 1.0
 */
@Controller
public class MainController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(MainController.class);

	public static Logger getLog() {
		return log;
	}
	/**
	 * 访问主页
	 * @return
	 */
    @RequestMapping(value = CenterURIConstant.Main.INDEX, method = RequestMethod.GET)
    public String main() {
        return CenterViewConstant.Main.MAIN;
    }
    /**
     * 访问注册页面
     * @return
     */
    @RequestMapping(value = CenterURIConstant.Main.REGISTER, method = RequestMethod.GET)
    public String register() {
        return CenterViewConstant.Main.REGISTER;
    }
    /**
     * 注册提交
     * @return
     */
    @RequestMapping(value = CenterURIConstant.Main.REGISTER, method = RequestMethod.POST)
    public String registerMember(@ModelAttribute("registerAttribute") RegisterForm registerForm) {
    			log.debug(registerForm.toString());
        return CenterViewConstant.Index.LOGIN;
    }
    /**
     * 访问找回密码页面
     * @return
     */
    @RequestMapping(value = CenterURIConstant.Main.FIND_PASSWORD, method = RequestMethod.GET)
    public String findPassword() {
        return CenterViewConstant.Main.FIND_PASSWORD;
    }
}
