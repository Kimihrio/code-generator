package com.ljinfeng.code.generator;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.BeetlTemplateEngine;
import com.sun.istack.internal.NotNull;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 自定义重写模板引擎
 *
 * @author ljinfeng@linewell.com
 * @date 2021-07-19
 */
public class CustomTemplateEngine extends BeetlTemplateEngine {

    @Override
    public @org.jetbrains.annotations.NotNull AbstractTemplateEngine batchOutput() {

        try {

            ConfigBuilder config = this.getConfigBuilder();
            List<TableInfo> tableInfoList = config.getTableInfoList();

            tableInfoList.forEach(tableInfo -> {

                Map<String, Object> objectMap = this.getObjectMap(config, tableInfo);
                Optional.ofNullable(config.getInjectionConfig())
                        .ifPresent(t -> t.beforeOutputFile(tableInfo, objectMap));

                // Mp.java
                outputEntity(tableInfo, objectMap);
                // mapper and xml
                outputMapper(tableInfo, objectMap);
                // service
                outputService(tableInfo, objectMap);
                // bll.java
                outputBll(tableInfo, objectMap, config);
                // facade.java
                outputFacade(tableInfo, objectMap, config);
                // MpController.java
                outputController(tableInfo, objectMap);

            });
        } catch (Exception e) {
            throw new RuntimeException("无法创建文件，请检查配置信息！", e);
        }

        return this;
    }

    /**
     * 输出业务层
     *
     * @param tableInfo
     * @param objectMap
     * @param config
     */
    private void outputBll(@NotNull TableInfo tableInfo, @NotNull Map<String, Object> objectMap, ConfigBuilder config) {

        String bllPackagePath = config.getPackageConfig().getParent() + StringPool.DOT + "bll";
        String interfaceName = "I" + tableInfo.getEntityName() + "BLL";
        String implName = tableInfo.getEntityName() + "BLL";

        objectMap.put("bllPackagePath", bllPackagePath);
        objectMap.put("bllImplPackagePath", bllPackagePath + StringPool.DOT + "impl");
        objectMap.put("bllName", interfaceName);
        objectMap.put("bllImplName", implName);

        String entityName = tableInfo.getEntityName();

        // IBLL.java
        String bllFileSource = config.getGlobalConfig().getOutputDir() + File.separator + bllPackagePath.replaceAll("\\.", "\\\\");
        //本地接口文件全路径
        String facadeFile = String.format((bllFileSource + File.separator + interfaceName + this.suffixJavaOrKt()), entityName);
        outputFile(new File(facadeFile), objectMap, "/templates/bll.java.btl");

        // BLL.java
        String bllImplFileSource = bllFileSource + File.separator + "impl" + File.separator;
        //本地接口实现类文件全路径
        String implFile = String.format((bllImplFileSource + File.separator + implName + this.suffixJavaOrKt()), entityName);
        outputFile(new File(implFile), objectMap, "/templates/bllImpl.java.btl");

    }

    /**
     * 输出门面层
     *
     * @param tableInfo
     * @param objectMap
     * @param config
     */
    private void outputFacade(@NotNull TableInfo tableInfo, @NotNull Map<String, Object> objectMap, ConfigBuilder config) {

        String facadePackagePath = config.getPackageConfig().getParent() + StringPool.DOT + "facade";
        String interfaceName = "I" + tableInfo.getEntityName() + "Facade";
        String implName = tableInfo.getEntityName() + "FacadeImpl";

        objectMap.put("facadePackagePath", facadePackagePath);
        objectMap.put("facadeImplPackagePath", facadePackagePath + StringPool.DOT + "impl");
        objectMap.put("facadeName", interfaceName);
        objectMap.put("facadeImplName", implName);

        String entityName = tableInfo.getEntityName();

        // IFacade.java
        String facadeFileSource = config.getGlobalConfig().getOutputDir() + File.separator + facadePackagePath.replaceAll("\\.", "\\\\");
        //本地接口文件全路径
        String facadeFile = String.format((facadeFileSource + File.separator + interfaceName + this.suffixJavaOrKt()), entityName);
        outputFile(new File(facadeFile), objectMap, "/templates/facade.java.btl");

        // FacadeImpl.java
        String facadeImplFileSource = facadeFileSource + File.separator + "impl" + File.separator;
        //本地接口实现类文件全路径
        String implFile = String.format((facadeImplFileSource + File.separator + implName + this.suffixJavaOrKt()), entityName);
        outputFile(new File(implFile), objectMap, "/templates/facadeImpl.java.btl");

    }

}
