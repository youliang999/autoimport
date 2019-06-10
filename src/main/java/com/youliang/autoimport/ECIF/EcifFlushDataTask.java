package com.youliang.autoimport.ECIF;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/task")
public class EcifFlushDataTask {
    private static final Logger log = LoggerFactory.getLogger(EcifFlushDataTask.class);

    @RequestMapping("/index")
    public ModelAndView index() {
        return new ModelAndView("file");
    }

    @GetMapping("/start")
    public String startTask() {
        log.info("ecif刷数任务开始执行...");
        return "";
    }

}
