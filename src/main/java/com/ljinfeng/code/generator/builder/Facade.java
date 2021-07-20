package com.ljinfeng.code.generator.builder;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.ITemplate;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.BaseBuilder;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.function.ConverterFileName;
import com.ljinfeng.code.generator.constant.Constant;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ljinfeng@linewell.com
 * @date 2021-07-20
 */
public class Facade implements ITemplate {

    private Facade() {
    }

    private ConfigBuilder config;

    /**
     * 转换输出Facade文件名称
     */
    private ConverterFileName converterFacadeFileName = (entityName -> "I" + entityName + Constant.FACADE);

    /**
     * 转换输出FacadeImpl文件名称
     */
    private ConverterFileName converterFacadeImplFileName = (entityName -> entityName + Constant.FACADE_IMPL);

    /**
     * 接口模板引擎路径
     */
    private final String facadeTempleFilePath = "/templates/facade.java.btl";

    /**
     * 实现类模板引擎路径
     */
    private final String facadeImplTemplateFilePath = "/templates/facadeImpl.java.btl";

    @NotNull
    public ConverterFileName getConverterFacadeFileName() {
        return converterFacadeFileName;
    }

    @NotNull
    public ConverterFileName getConverterFacadeImplFileName() {
        return converterFacadeImplFileName;
    }

    @NotNull
    public String getFacadeTempleFilePath() {
        return facadeTempleFilePath;
    }

    @NotNull
    public String getFacadeImplTemplateFilePath() {
        return facadeImplTemplateFilePath;
    }

    @Override
    public @NotNull Map<String, Object> renderData(@NotNull TableInfo tableInfo) {

        String parentPath = config.getPackageConfig().getParent() + StringPool.DOT + Constant.FACADE_PACKAGE;
        String implParentPath = parentPath + StringPool.DOT + Constant.FACADE_IMPL_PACKAGE;

        return new HashMap<String, Object>(4, 1.0f) {{
            put("Facade", parentPath);
            put("FacadeImpl", implParentPath);
            put("FacadeName", getConverterFacadeFileName().convert(tableInfo.getEntityName()));
            put("FacadeImplName", getConverterFacadeImplFileName().convert(tableInfo.getEntityName()));
        }};
    }

    public static class Builder extends BaseBuilder {

        private final Facade facade = new Facade();

        public Builder(@NotNull StrategyConfig strategyConfig, @NotNull ConfigBuilder config) {
            super(strategyConfig);
            facade.config = config;
        }

        @NotNull
        public Facade get() {
            return this.facade;
        }
    }

}
