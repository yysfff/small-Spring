package com.yu.springframework.test;

import cn.hutool.core.io.IoUtil;
import com.yu.springframework.aop.AdvisedSupport;
import com.yu.springframework.aop.TargetSource;
import com.yu.springframework.aop.aspectj.AspectJExpressionPointcut;
import com.yu.springframework.aop.framework.Cglib2AopProxy;
import com.yu.springframework.aop.framework.JdkDynamicAopProxy;
import com.yu.springframework.beans.BeansException;
import com.yu.springframework.beans.PropertyValue;
import com.yu.springframework.beans.PropertyValues;
import com.yu.springframework.beans.factory.config.BeanDefinition;
import com.yu.springframework.beans.factory.config.BeanReference;
import com.yu.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.yu.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import com.yu.springframework.context.support.ClassPathXmlApplicationContext;
import com.yu.springframework.core.io.DefaultResourceLoader;
import com.yu.springframework.core.io.Resource;
import com.yu.springframework.test.bean.IUserService;
import com.yu.springframework.test.bean.UserDao;
import com.yu.springframework.test.bean.UserService;
import com.yu.springframework.test.bean.UserServiceInterceptor;
import com.yu.springframework.test.event.CustomEvent;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import org.openjdk.jol.info.ClassLayout;
public class ApiTest {
    private DefaultResourceLoader resourceLoader;
    @Before
    public void init(){
        resourceLoader = new DefaultResourceLoader();
    }
    @Test
    public void test_classpath() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:important.properties");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
    }
    @Test
    public void test_file() throws IOException {
        Resource resource = resourceLoader.getResource("src/test/resources/important.properties");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
    }
    @Test
    public void test_url() throws IOException {
        Resource resource = resourceLoader.getResource("https://github.com/fuzhengwei/small-spring/important.properties");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
    }
    @Test
    public void test_xml() throws BeansException {
        //1、初始化BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //2、读取配置文件，注册bean
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:spring.xml");
        //3、获取bean对象调用方法
        UserService userService = beanFactory.getBean("userService", UserService.class);
        String s = userService.queryUserInfo();
        System.out.println(s);
    }
    @Test
    public void test_xml_1() throws BeansException {
        //1、初始化BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutdownHook();
        //2、获取bean对象调用方法
        UserService userService = applicationContext.getBean("userService", UserService.class);
        String result = userService.queryUserInfo();
        System.out.println(result);
//        System.out.println("ApplicationContextAware:"+userService.getApplicationContext());
//        System.out.println("BeanFactoryAware:"+userService.getBeanFactory());

    }
    @Test
    public void test_BeanFactory() throws BeansException {
       DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
       beanFactory.registerBeanDefinition("userDao", new BeanDefinition(UserDao.class));
       PropertyValues propertyValues = new PropertyValues();
       propertyValues.addPropertyValue(new PropertyValue("uId","10001"));
       propertyValues.addPropertyValue(new PropertyValue("userDao",new BeanReference("userDao")));
       BeanDefinition beanDefinition = new BeanDefinition(UserService.class,propertyValues);
       beanFactory.registerBeanDefinition("userService",beanDefinition);
       UserService userService = (UserService) beanFactory.getBean("userService", "spring");
       userService.queryUserInfo();
        System.out.println(userService.toString());
    }
    @Test
    public void test_hook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("close！")));
    }
    @Test
    public void test_prototype() throws BeansException {
        //1、初始化BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutdownHook();
        //2、获取bean对象调用方法
        UserService userService01 = applicationContext.getBean("userService", UserService.class);
        UserService userService02 = applicationContext.getBean("userService", UserService.class);
        System.out.println(userService01);
        System.out.println(userService02);
        System.out.println(userService01+"十六进制哈希："+Integer.toHexString(userService01.hashCode()));
        System.out.println(ClassLayout.parseInstance(userService01).toPrintable());
    }
    @Test
    public void test_factory_bean() throws BeansException {
        //1、初始化BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutdownHook();
        //2、获取bean对象调用方法
        UserService userService = applicationContext.getBean("userService", UserService.class);
        System.out.println(userService.queryUserInfo());
    }
    @Test
    public void test_event() throws BeansException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext, 1019129009086763L,"成功了！"));
        applicationContext.registerShutdownHook();
    }
    @Test
    public void test_aop() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* com.yu.springframework.test.bean.UserService.*(..))");
        Class<UserService> clazz = UserService.class;
        Method method = clazz.getDeclaredMethod("queryUserInfo");
        System.out.println(pointcut.matches(clazz));
        System.out.println(pointcut.matches(method,clazz));
    }
    @Test
    public void test_dynamic(){
        //目标对象
        IUserService userService = new UserService();
        //组装代理对象
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTargetSource(new TargetSource(userService));
        advisedSupport.setMethodInterceptor(new UserServiceInterceptor());
        advisedSupport.setMethodMatcher(new AspectJExpressionPointcut("execution(* com.yu.springframework.test.bean.IUserService.*(..))"));
        //代理对象(JdkDynamicAopProxy)
        IUserService proxy_jdk = (IUserService) new JdkDynamicAopProxy(advisedSupport).getProxy();
        //测试调用
        System.out.println("测试结果：" + proxy_jdk.queryUserInfo());
        //代理对象(cglib2AopProxy)
        IUserService proxy_cglib = (IUserService) new Cglib2AopProxy(advisedSupport).getProxy();
        //测试调用
        System.out.println("测试结果" + proxy_cglib.register("花花"));
    }
    @Test
    public void test_aop_1() throws BeansException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println("测试结果：" + userService.queryUserInfo());
    }
    @Test
    public void test_property() throws BeansException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-property.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println(userService);
    }
}
