//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2018.01.26 时间 03:02:16 PM CST 
//


package com.jack.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="scan-package" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="view">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="content-type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="prefix" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="suffix" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "scanPackage",
    "view"
})
@XmlRootElement(name = "beans")
public class Beans {

    @XmlElement(name = "scan-package", required = true)
    protected String scanPackage;
    @XmlElement(required = true)
    protected Beans.View view;

    /**
     * 获取scanPackage属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScanPackage() {
        return scanPackage;
    }

    /**
     * 设置scanPackage属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScanPackage(String value) {
        this.scanPackage = value;
    }

    /**
     * 获取view属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Beans.View }
     *     
     */
    public Beans.View getView() {
        return view;
    }

    /**
     * 设置view属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Beans.View }
     *     
     */
    public void setView(Beans.View value) {
        this.view = value;
    }


    /**
     * <p>anonymous complex type的 Java 类。
     * 
     * <p>以下模式片段指定包含在此类中的预期内容。
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="content-type" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="prefix" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="suffix" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "contentType",
        "prefix",
        "suffix"
    })
    public static class View {

        @XmlElement(name = "content-type", required = true)
        protected String contentType;
        @XmlElement(required = true)
        protected String prefix;
        @XmlElement(required = true)
        protected String suffix;

        /**
         * 获取contentType属性的值。
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getContentType() {
            return contentType;
        }

        /**
         * 设置contentType属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setContentType(String value) {
            this.contentType = value;
        }

        /**
         * 获取prefix属性的值。
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPrefix() {
            return prefix;
        }

        /**
         * 设置prefix属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPrefix(String value) {
            this.prefix = value;
        }

        /**
         * 获取suffix属性的值。
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSuffix() {
            return suffix;
        }

        /**
         * 设置suffix属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSuffix(String value) {
            this.suffix = value;
        }

    }

}
