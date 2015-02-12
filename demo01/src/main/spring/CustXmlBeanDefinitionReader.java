package main.spring;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

public class CustXmlBeanDefinitionReader extends XmlBeanDefinitionReader
{

    public CustXmlBeanDefinitionReader(BeanDefinitionRegistry beanFactory)
    {
        super(beanFactory);
        
        //这一句是我们的定制点.
        setDocumentReaderClass(CustBeanDefinitionDocumentReader.class);
    }
}
