package main;

import main.spring.SpringApplicationContext;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

public class TestInit2
{
    public static void main(String[] args)
    {
        ClassLoader loader = TestInit2.class.getClassLoader();
        Resource resource = new UrlResource(loader.getResource("db.xml"));
        ApplicationContext appContext = new SpringApplicationContext(resource);
        
        BasicDataSource ds = (BasicDataSource)appContext.getBean("dataSource");
        System.out.println(ds.getDriverClassName());
        
    }
}
