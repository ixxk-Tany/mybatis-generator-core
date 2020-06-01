/**
 *    Copyright ${license.git.copyrightYears} the original author or authors.
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
package org.mybatis.generator.internal;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class MyDefaultCommentGenerator extends DefaultCommentGenerator {
    //类上添加注释
    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addJavaDocLine("/**");
        String remark = introspectedTable.getRemarks();
        if (this.isNotBlank(remark)) {
            topLevelClass.addJavaDocLine(" * " + remark);
        }
        topLevelClass.addJavaDocLine(" * ");
        topLevelClass.addJavaDocLine(" * " + "@author " + getAuthor());
        topLevelClass.addJavaDocLine(" * " + "@date " + getDateContent());
        topLevelClass.addJavaDocLine(" */");
    }

    //属性上添加注释
    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        String remark = introspectedColumn.getRemarks();
        if (this.isNotBlank(remark)) {
            field.addJavaDocLine("/**");
            field.addJavaDocLine(" * " + remark);
            field.addJavaDocLine(" */");
        }
    }

    public boolean isNotBlank(final CharSequence cs){
        return !isBlank(cs);
    }

    public boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
