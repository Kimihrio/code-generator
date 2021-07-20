package com.ljinfeng.code.generator.engine;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.BeetlTemplateEngine;
import com.ljinfeng.code.generator.builder.BLL;
import com.ljinfeng.code.generator.builder.DTO;
import com.ljinfeng.code.generator.builder.Facade;
import com.ljinfeng.code.generator.builder.Params;
import com.ljinfeng.code.generator.constant.Constant;
import org.jetbrains.annotations.NotNull;

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

    private Params params;

    private DTO dto;

    private BLL bll;

    private Facade facade;

    @Override
    public @NotNull Map<String, Object> getObjectMap(@NotNull ConfigBuilder config, @NotNull TableInfo tableInfo) {

        Map<String, Object> objectMap = super.getObjectMap(config, tableInfo);
        //params
        params = (new Params.Builder(config.getStrategyConfig(), config)).get();
        objectMap.put("params", params.renderData(tableInfo));
        //dto
        dto = (new DTO.Builder(config.getStrategyConfig(), config)).get();
        objectMap.put("dto", dto.renderData(tableInfo));
        //bll
        bll = (new BLL.Builder(config.getStrategyConfig(), config)).get();
        objectMap.put("bll", bll.renderData(tableInfo));
        //facade
        facade = (new Facade.Builder(config.getStrategyConfig(), config)).get();
        objectMap.put("facade", facade.renderData(tableInfo));

        return objectMap;
    }

    @Override
    public @NotNull AbstractTemplateEngine batchOutput() {

        try {

            ConfigBuilder config = this.getConfigBuilder();
            List<TableInfo> tableInfoList = config.getTableInfoList();

            tableInfoList.forEach(tableInfo -> {

                Map<String, Object> objectMap = this.getObjectMap(config, tableInfo);
                Optional.ofNullable(config.getInjectionConfig())
                        .ifPresent(t -> t.beforeOutputFile(tableInfo, objectMap));

                // Mp.java
                outputEntity(tableInfo, objectMap);
                // Params.java
                outputParams(tableInfo.getEntityName(), objectMap, config);
                // DTO.java
                outputDTO(tableInfo.getEntityName(), objectMap, config);
                // mapper and xml
                outputMapper(tableInfo, objectMap);
                // service
                outputService(tableInfo, objectMap);
                // bll.java
                outputBll(tableInfo.getEntityName(), objectMap, config);
                // facade.java
                outputFacade(tableInfo.getEntityName(), objectMap, config);
                // MpController.java
                outputController(tableInfo, objectMap);

            });
        } catch (Exception e) {
            throw new RuntimeException("无法创建文件，请检查配置信息！", e);
        }

        return this;
    }

    /**
     * 输出params文件
     *
     * @param entityName
     * @param objectMap
     * @param config
     */
    private void outputParams(@NotNull String entityName, @NotNull Map<String, Object> objectMap, @NotNull ConfigBuilder config) {

        if (params == null) {
            System.out.println("未初始化params配置信息");
            return;
        }

        //初始化路径
        this.initParamsConfig(config);

        String paramsPath = getPathInfo(Constant.PARAMS_PATH);
        String paramsName = params.getConverterParamsFileName().convert(entityName);
        if (StringUtils.isNotBlank(paramsPath) && StringUtils.isNotBlank(paramsName)) {
            String paramsFile = String.format((paramsPath + File.separator + paramsName + suffixJavaOrKt()), entityName);
            outputFile(new File(paramsFile), objectMap, params.getParamsTempleFilePath());
        }

    }

    /**
     * 初始化params配置
     *
     * @param config
     */
    private void initParamsConfig(ConfigBuilder config) {

        Map<String, String> pathInfo = getConfigBuilder().getPathInfo();

        //params
        String parentPath = config.getPackageConfig().getParent() + StringPool.DOT + Constant.PARAMS_PACKAGE;
        pathInfo.put(Constant.PARAMS_PATH, joinPath(config.getGlobalConfig().getOutputDir(), parentPath));
    }

    /**
     * 输出params文件
     *
     * @param entityName
     * @param objectMap
     * @param config
     */
    private void outputDTO(@NotNull String entityName, @NotNull Map<String, Object> objectMap, @NotNull ConfigBuilder config) {

        if (dto == null) {
            System.out.println("未初始化dto配置信息");
            return;
        }

        //初始化路径
        this.initDTOConfig(config);

        String dtoPath = getPathInfo(Constant.DTO_PATH);
        String dtoName = dto.getConverterDtoFileName().convert(entityName);
        if (StringUtils.isNotBlank(dtoPath) && StringUtils.isNotBlank(dtoName)) {
            String paramsFile = String.format((dtoPath + File.separator + dtoName + suffixJavaOrKt()), entityName);
            outputFile(new File(paramsFile), objectMap, dto.getDtoTempleFilePath());
        }

    }

    /**
     * 初始化params配置
     *
     * @param config
     */
    private void initDTOConfig(ConfigBuilder config) {

        Map<String, String> pathInfo = getConfigBuilder().getPathInfo();

        //params
        String parentPath = config.getPackageConfig().getParent() + StringPool.DOT + Constant.DTO_PACKAGE;
        pathInfo.put(Constant.DTO_PATH, joinPath(config.getGlobalConfig().getOutputDir(), parentPath));
    }

    /**
     * 输出bll文件
     *
     * @param entityName
     * @param objectMap
     * @param config
     */
    private void outputBll(@NotNull String entityName, @NotNull Map<String, Object> objectMap, @NotNull ConfigBuilder config) {

        if (bll == null) {
            System.out.println("未初始化bll配置信息");
            return;
        }

        //初始化路径
        this.initBllConfig(config);

        // IBll.java
        String facadePath = getPathInfo(Constant.BLL_PATH);
        String facadeName = bll.getConverterBllFileName().convert(entityName);
        if (StringUtils.isNotBlank(facadeName) && StringUtils.isNotBlank(facadePath)) {
            String facadeFile = String.format((facadePath + File.separator + facadeName + suffixJavaOrKt()), entityName);
            outputFile(new File(facadeFile), objectMap, bll.getBllTempleFilePath());
        }

        // BllImpl.java
        String facadeImplPath = getPathInfo(Constant.BLL_IMPL_PATH);
        String facadeImplName = bll.getConverterBllImplFileName().convert(entityName);
        if (StringUtils.isNotBlank(facadeImplName) && StringUtils.isNotBlank(facadeImplPath)) {
            String implFile = String.format((facadeImplPath + File.separator + facadeImplName + suffixJavaOrKt()), entityName);
            outputFile(new File(implFile), objectMap, bll.getBllImplTemplateFilePath());
        }

    }

    /**
     * 初始化bll配置
     *
     * @param config
     */
    private void initBllConfig(ConfigBuilder config) {

        Map<String, String> pathInfo = getConfigBuilder().getPathInfo();

        //bll
        String parentPath = config.getPackageConfig().getParent() + StringPool.DOT + Constant.BLL_PACKAGE;
        pathInfo.put(Constant.BLL_PATH, joinPath(config.getGlobalConfig().getOutputDir(), parentPath));
        //bllImpl
        String implParentPath = parentPath + StringPool.DOT + Constant.BLL_IMPL_PACKAGE;
        pathInfo.put(Constant.BLL_IMPL_PATH, joinPath(config.getGlobalConfig().getOutputDir(), implParentPath));
    }

    /**
     * 输出facade文件
     *
     * @param entityName
     * @param objectMap
     */
    private void outputFacade(@NotNull String entityName, @NotNull Map<String, Object> objectMap, ConfigBuilder config) {

        if (facade == null) {
            System.out.println("未初始化facade配置信息");
            return;
        }

        //初始化路径
        this.initFacadeConfig(config);

        // IFacade.java
        String facadePath = getPathInfo(Constant.FACADE_PATH);
        String facadeName = facade.getConverterFacadeFileName().convert(entityName);
        if (StringUtils.isNotBlank(facadeName) && StringUtils.isNotBlank(facadePath)) {
            String facadeFile = String.format((facadePath + File.separator + facadeName + suffixJavaOrKt()), entityName);
            outputFile(new File(facadeFile), objectMap, facade.getFacadeTempleFilePath());
        }

        // FacadeImpl.java
        String facadeImplPath = getPathInfo(Constant.FACADE_IMPL_PATH);
        String facadeImplName = facade.getConverterFacadeImplFileName().convert(entityName);
        if (StringUtils.isNotBlank(facadeImplName) && StringUtils.isNotBlank(facadeImplPath)) {
            String implFile = String.format((facadeImplPath + File.separator + facadeImplName + suffixJavaOrKt()), entityName);
            outputFile(new File(implFile), objectMap, facade.getFacadeImplTemplateFilePath());
        }
    }

    /**
     * 初始化facade配置
     *
     * @param config
     */
    private void initFacadeConfig(ConfigBuilder config) {

        Map<String, String> pathInfo = getConfigBuilder().getPathInfo();

        //facade
        String parentPath = config.getPackageConfig().getParent() + StringPool.DOT + Constant.FACADE_PACKAGE;
        pathInfo.put(Constant.FACADE_PATH, joinPath(config.getGlobalConfig().getOutputDir(), parentPath));
        //facadeImpl
        String implParentPath = parentPath + StringPool.DOT + Constant.FACADE_IMPL_PACKAGE;
        pathInfo.put(Constant.FACADE_IMPL_PATH, joinPath(config.getGlobalConfig().getOutputDir(), implParentPath));
    }

    /**
     * 连接路径字符串
     *
     * @param parentDir   路径常量字符串
     * @param packageName 包名
     * @return 连接后的路径
     */
    private String joinPath(String parentDir, String packageName) {

        if (StringUtils.isBlank(parentDir)) {
            parentDir = System.getProperty(ConstVal.JAVA_TMPDIR);
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
        return parentDir + packageName;
    }

}
