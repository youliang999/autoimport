package com.youliang.autoimport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories("com.youliang.autoimport")
public class AutoimportApplication {

    public static void main(String[] args) {
        try {

            ConfigurableApplicationContext context = SpringApplication.run(AutoimportApplication.class, args);
//            UploadOrgAddrInfoTask groupInfoDao = context.getBean(UploadOrgAddrInfoTask.class);
//            UploadOrgAddrInfoTask task = new UploadOrgAddrInfoTask();
//            groupInfoDao.upload();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw  e;
        }
    }

}
