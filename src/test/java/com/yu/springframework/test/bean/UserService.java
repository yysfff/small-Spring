package com.yu.springframework.test.bean;

import com.yu.springframework.beans.BeansException;
import com.yu.springframework.beans.factory.*;
import com.yu.springframework.beans.factory.annotation.Autowired;
import com.yu.springframework.beans.factory.annotation.Value;
import com.yu.springframework.context.ApplicationContext;
import com.yu.springframework.context.ApplicationContextAware;
import com.yu.springframework.stereotype.Component;

import java.util.Random;

//public class UserService implements InitializingBean, DisposableBean, BeanNameAware, BeanClassLoaderAware,
//        ApplicationContextAware, BeanFactoryAware {
//
//    private ApplicationContext applicationContext;
//    private BeanFactory beanFactory;
//    private String uId;
//
//    private IUserDao userDao;
//    private String location;
//    private String company;
//
//    public void setLocation(String location) {
//        this.location = location;
//    }
//
//    public void setCompany(String company) {
//        this.company = company;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public String getCompany() {
//        return company;
//    }
//
//    public String queryUserInfo() {
//        return userDao.queryUserName(uId)+ "," + company + "," + location;
//    }
//
//    public String getuId() {
//        return uId;
//    }
//
//    public void setuId(String uId) {
//        this.uId = uId;
//    }
//
//
//    @Override
//    public void destroy() {
//        System.out.println("执行：UserService.destroy");
//    }
//
//    @Override
//    public void afterPropertiesSet() {
//        System.out.println("执行：UserService.afterPropertiesSet");
//    }
//
//    @Override
//    public void setBeanClassLoader(ClassLoader classLoader) {
//        System.out.println("ClassLoader: " + classLoader);
//    }
//
//    @Override
//    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
//        this.beanFactory = beanFactory;
//    }
//
//    @Override
//    public void setBeanName(String name) {
//        System.out.println("Bean Name is: " + name);
//    }
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.applicationContext = applicationContext;
//    }
//
//    public ApplicationContext getApplicationContext() {
//        return applicationContext;
//    }
//
//    public BeanFactory getBeanFactory() {
//        return beanFactory;
//    }
//}
//@Component("userService")
public class UserService implements IUserService {
//    @Value("${token}")
    private String token;
    @Autowired
    private UserDao userDao;
    public String queryUserInfo() {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "小傅哥，100001，深圳";
    }

    public String register(String userName) {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "注册用户：" + userName + " success！";
    }

    @Override
    public String toString() {
        return "UserService{" +
                "token='" + token + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
