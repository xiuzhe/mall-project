package com.ishare.mall.crawler.controller;

import com.google.common.collect.Maps;
import com.ishare.mall.crawler.site.jd.model.JDCategory;
import com.ishare.mall.crawler.site.jd.model.JDProduct;
import com.ishare.mall.crawler.site.jd.processor.JDCategoryPageProcessor;
import com.ishare.mall.crawler.site.jd.processor.JDPageProcessor;
import com.ishare.mall.crawler.site.jd.repository.JDCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class IndexController {

    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    JDCategoryRepository jdCategoryRepository;

    @Autowired
    JDCategoryPageProcessor categoryPageProcessor;

    @Autowired
    JDPageProcessor pageProcessor;

    @RequestMapping("/")
    public String index(Model model) {
        return "dashboard";
    }

    @RequestMapping("/search")
    public String search(Model model) {
        return "search";
    }

    @RequestMapping("/category")
    public String category(Model model) {
        return "category";
    }

    @RequestMapping("/page")
    public String page(Model model) {
        return "page";
    }

    @RequestMapping(value = "init")
    public String init() {
        log.debug("初始化");

        return "index";
    }

    @RequestMapping(value = "fetchAll", method = RequestMethod.GET)
    public
    @ResponseBody
    Object fetchAll() {
        Map<String, Object> result = Maps.newHashMap();
        boolean bln = false;
        try {


            bln = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.put("success", bln);
        return result;
    }

    @RequestMapping(value = "fetchList", method = RequestMethod.POST)
    public
    @ResponseBody
    Object fetchList(@RequestParam("link") String url) {
        Map<String, Object> result = Maps.newHashMap();
        boolean bln = false;
        try {

            bln = true;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "error");
        }

        result.put("success", bln);
        return result;
    }

    @RequestMapping(value = "fetchPage")
    public
    @ResponseBody
    Object fetchPage(@RequestParam("link") String url) {
        Map<String, Object> result = Maps.newHashMap();
        boolean bln = false;
        try {
            bln = true;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "error");
        }

        result.put("success", bln);
        return result;
    }

    @RequestMapping(value = "product/{id}")
    public String page(@PathVariable(value = "id") long id, Model model) {

        JDProduct product = new JDProduct();//jdProductRepository.findOne(id);
        model.addAttribute("product", product);

        return "product";
    }

    @RequestMapping(value = "dashboard", method = RequestMethod.GET)
    public String dashboard(Model model) {

        return "dashboard";
    }

    @RequestMapping(value = "data.json", method = RequestMethod.GET)
    public
    @ResponseBody
    Object data(
            @RequestParam(value = "start", defaultValue = "0") int start,
            @RequestParam(value = "length", defaultValue = "10") int length,
            @RequestParam(value = "search[value]", defaultValue = "") String search) {
        log.debug("search: {}", search);

        Map<String, Object> data = Maps.newHashMap();
        Page<JDCategory> page = jdCategoryRepository.findAll(new PageRequest(start / length, length));
        data.put("recordsTotal", page.getTotalElements());
        data.put("recordsFiltered", page.getTotalElements());
        data.put("data", page.getContent());
        return data;
    }

    @RequestMapping(value = "jdCategory")
    public String jdCategory() {

        return "jd/category";
    }
}
