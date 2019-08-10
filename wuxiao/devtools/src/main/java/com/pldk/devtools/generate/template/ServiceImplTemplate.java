package com.pldk.devtools.generate.template;

import com.pldk.common.data.PageSort;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.utils.ToolUtil;
import com.pldk.devtools.generate.domain.Generate;
import com.pldk.devtools.generate.enums.TierType;
import com.pldk.devtools.generate.utils.FileUtil;
import com.pldk.devtools.generate.utils.GenerateUtil;
import com.pldk.devtools.generate.utils.jAngel.JAngelContainer;
import com.pldk.devtools.generate.utils.jAngel.nodes.Document;
import com.pldk.devtools.generate.utils.jAngel.parser.Expression;
import com.pldk.devtools.generate.utils.parser.JavaParseUtil;

import java.nio.file.FileAlreadyExistsException;
import java.util.Set;

/**
 * @author Pldk
 * @date 2018/10/25
 */
public class ServiceImplTemplate {

    /**
     * 生成需要导入的包
     */
    private static Set<String> genImports(Generate generate) {
        JAngelContainer container = new JAngelContainer();
        container.importClass(JavaParseUtil.getPackage(generate, TierType.DOMAIN));
        container.importClass(JavaParseUtil.getPackage(generate, TierType.SERVICE));
        container.importClass(JavaParseUtil.getPackage(generate, TierType.DAO));
        container.importClass(StatusEnum.class);
        container.importClass(PageSort.class);
        return container.getImports();
    }

    /**
     * 生成类体
     */
    private static Document genClazzBody(Generate generate) {
        // 构建数据-模板表达式
        Expression expression = new Expression();
        expression.label("name", ToolUtil.lowerFirst(generate.getBasic().getTableEntity()));
        expression.label("entity", generate.getBasic().getTableEntity());
        String path = FileUtil.templatePath(ServiceImplTemplate.class);

        // 获取jAngel文档对象
        Document document = JavaParseUtil.document(path, expression, generate, TierType.SERVICE_IMPL);
        document.getContainer().importClass(genImports(generate));

        return document;
    }

    /**
     * 生成服务实现层模板
     */
    public static String generate(Generate generate) {
        // 生成文件
        String filePath = GenerateUtil.getJavaFilePath(generate, TierType.SERVICE_IMPL);
        try {
            Document document = genClazzBody(generate);
            GenerateUtil.generateFile(filePath, document.content());
        } catch (FileAlreadyExistsException e) {
            return GenerateUtil.fileExist(filePath);
        }
        return filePath;
    }
}
