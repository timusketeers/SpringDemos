package main.spring;

import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CustBeanDefinitionDocumentReader extends DefaultBeanDefinitionDocumentReader
{
    private String placeholderPrefix = "${";
    private String placeholderSuffix = "}";
    
    protected void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate)
    {
        if(delegate.isDefaultNamespace(root.getNamespaceURI()))
        {
            NodeList nl = root.getChildNodes();
            for(int i = 0; i < nl.getLength(); i++)
            {
                org.w3c.dom.Node node = nl.item(i);
                if(node instanceof Element)
                {
                    Element ele = (Element)node;
                    String namespaceUri = ele.getNamespaceURI();
                    if(delegate.isDefaultNamespace(namespaceUri))
                    {
                        //prepareParse是我们新增的定制�?.
                        prepareParse(ele);
                        
                        parseDefaultElement(ele, delegate);
                    }
                    else
                    {
                        delegate.parseCustomElement(ele);
                    }
                }
            }

        } else
        {
            delegate.parseCustomElement(root);
        }
    }
    
    protected void prepareParse(Element element)
    {
        String id = element.getAttribute("id");
        
        /**
         * 这里是对spring bean id 为fdsDataSource的bean xml配置，将其属性class做了转义处理..
         * 
         * 其实，这里应该是可以搞成�?��比较通用的东东的，这里写的比较具�?后面的哥们可以�?虑搞搞这�?..
         * 
         * 代码待进�?��做抽象上的�?�?..
         */
        if (null != id && "fdsDataSource".equals(id))
        {
            String className = element.getAttribute("class");
            int startIndex = className.indexOf(placeholderPrefix);
            
            StringBuffer buf = new StringBuffer(className);
            if (startIndex != -1)
            {
                int endIndex = buf.toString().indexOf(placeholderSuffix, startIndex + placeholderPrefix.length());
                if(endIndex != -1)
                {
                    String placeholder = buf.substring(startIndex + placeholderPrefix.length(), endIndex);
                    String propVal = resolvePlaceholder(placeholder);
                    if(propVal != null)
                    {
                        element.setAttribute("class", propVal);
                    }
                }
                else
                {
                    startIndex = -1;
                }
            }
        }
    }

    private String resolvePlaceholder(String propertyName)
    {
        String propertyValue = System.getProperty(propertyName);
        if (null == propertyValue || "".equals(propertyValue))
        {
            propertyValue = System.getenv(propertyName);
        }
        
        return propertyValue;
    }

    private void parseDefaultElement(Element ele, BeanDefinitionParserDelegate delegate)
    {
        if(DomUtils.nodeNameEquals(ele, "import"))
        {
            importBeanDefinitionResource(ele);
        }
        else if(DomUtils.nodeNameEquals(ele, "alias"))
        {
            processAliasRegistration(ele);
        }
        else if(DomUtils.nodeNameEquals(ele, "bean"))
        {
            processBeanDefinition(ele, delegate);
        }
    }
}
