/**
 *    Copyright 2006-2020 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.plugins;


import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

public class PaginationPlugin extends PluginAdapter {
    /**
     * 添加 分页 开始行数 和结束行数 属性
     */
    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        // add field, getter, setter for limit clause
        addProperty(topLevelClass, introspectedTable, "limitStart", FullyQualifiedJavaType.getIntInstance());
        addProperty(topLevelClass, introspectedTable, "limitEnd", FullyQualifiedJavaType.getIntInstance());
        addProperty(topLevelClass, introspectedTable, "groupByClause", FullyQualifiedJavaType.getStringInstance());
        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
    }

    /**
     * 给对应的实体 实体添加 属性字段
     */
    private void addProperty(TopLevelClass topLevelClass, IntrospectedTable introspectedTable, String name, FullyQualifiedJavaType fullyQualifiedJavaType) {
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        CommentGenerator commentGenerator = context.getCommentGenerator();
        Field field = new Field();
        field.setVisibility(JavaVisibility.PROTECTED);
        field.setType(fullyQualifiedJavaType);
        field.setName(name);
        //field.setInitializationString("-1");
        commentGenerator.addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);
        char c = name.charAt(0);
        String camel = Character.toUpperCase(c) + name.substring(1);
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(type);
        method.setName("set" + camel);
        method.addParameter(new Parameter(fullyQualifiedJavaType, name));
        method.addBodyLine("this." + name + "=" + name + ";");
        method.addBodyLine("return this;");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(fullyQualifiedJavaType);
        method.setName("get" + camel);
        method.addBodyLine("return " + name + ";");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
    }

    /**
     * 添加 映射 文件配置 limit 的配置
     */
    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
//		XmlElement isParameterPresenteElemen = (XmlElement) element.getElements();
        //设置 if 判断 节点
        //$NON-NLS-1$
        XmlElement limitElement = new XmlElement("if");
        //给 节点添加 条件运算符
        //$NON-NLS-1$ //$NON-NLS-2$
        limitElement.addAttribute(new Attribute("test", "limitEnd > 0"));
        //如果条件成立 就进行分页查询
        limitElement.addElement(new TextElement("limit #{limitStart,jdbcType=INTEGER}  , #{limitEnd,jdbcType=INTEGER}"));
        //添加节点到 配置文件中
        element.addElement(limitElement);
        //$NON-NLS-1$
        XmlElement groupbyElement = new XmlElement("if");
        //给 节点添加 条件运算符
        //$NON-NLS-1$ //$NON-NLS-2$
        groupbyElement.addAttribute(new Attribute("test", "groupByClause != null"));
        //如果条件成立 就进行分页查询
        groupbyElement.addElement(new TextElement("group by ${groupByClause}"));
        //添加节点到 配置文件中
        element.addElement(groupbyElement);

        return super.sqlMapUpdateByExampleWithoutBLOBsElementGenerated(element, introspectedTable);

    }

    /**
     * This plugin is always valid - no properties are required
     */
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

}
