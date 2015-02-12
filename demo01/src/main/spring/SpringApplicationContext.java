package main.spring;

import java.io.IOException;

import org.apache.xbean.spring.context.ResourceXmlApplicationContext;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;

/**
 * 这个类覆写spring中ResourceXmlApplicationContext类的getClassLoader()方法
 * 
 * 来定制加载spring的类加载�?
 * 
 * @author li.zhang 2014-9-2
 * 
 * 
 * <bean id="dataSource" class="${oracle.datasource.class}" destroy-method="close" depends-on="propConfigurer">
        <property name="driverClassName" value="${server.db.driver}" />
        <property name="url" value="${server.db.cache.url}" />
        <property name="username" value="${server.db.cache.username}" />
        <property name="password" value="${server.db.cache.password}" />
        <property name="maxWait" value="4" />
        <property name="maxActive" value="15" />
        <property name="validationQuery" value="select 1 from dual" />
        <property name="initialSize" value="6" />
        <property name="minIdle" value="2" />
        <property name="maxIdle" value="4" />
        <property name="testOnBorrow" value="true" />
        <property name="testOnReturn" value="true" />
        <property name="testWhileIdle" value="true" />
        
        <!-- 释放连接. -->
        <property name="removeAbandoned" value="false" />
        <property name="removeAbandonedTimeout" value="3000" />
        <property name="logAbandoned" value="true" />
        <property name="timeBetweenEvictionRunsMillis" value="60000" />
        <property name="minEvictableIdleTimeMillis" value="60000" />
    </bean>
          这个bean配置的class="${oracle.datasource.class}"这个属性的初始化时机是个关注点...
 * 
 */
public class SpringApplicationContext extends ResourceXmlApplicationContext
{
    public SpringApplicationContext(Resource resource)
    {
        super(resource);
    }

    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory)
        throws IOException
    {
        //CustXmlBeanDefinitionReader这句是我们的定制点..
        XmlBeanDefinitionReader bdReader = new XmlBeanDefinitionReader(beanFactory);
        //XmlBeanDefinitionReader bdReader = new CustXmlBeanDefinitionReader(beanFactory);
        
        bdReader.setResourceLoader(this);
        bdReader.setEntityResolver(new ResourceEntityResolver(this));
        initBeanDefinitionReader(bdReader);
        loadBeanDefinitions(bdReader);
    }

    public ClassLoader getClassLoader()
    {
        ClassLoader cl = getClass().getClassLoader();
        return cl;
    }
}
