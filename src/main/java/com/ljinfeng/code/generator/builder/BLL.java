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
public class BLL implements ITemplate {

    private BLL() {
    }

    private ConfigBuilder config;

    /**
     * 转换输出BLL文件名称
     */
    private ConverterFileName converterBllFileName = (entityName -> "I" + entityName + Constant.BLL);

    /**
     * 转换输出BllImpl文件名称
     */
    private ConverterFileName converterBllImplFileName = (entityName -> entityName + Constant.BLL_IMPL);

    /**
     * 接口模板引擎路径
     */
    private final String bllTempleFilePath = "/templates/bll.java.btl";

    /**
     * 实现类模板引擎路径
     */
    private final String bllImplTemplateFilePath = "/templates/bllImpl.java.btl";

    @NotNull
    public ConverterFileName getConverterBllFileName() {
        return converterBllFileName;
    }

    @NotNull
    public ConverterFileName getConverterBllImplFileName() {
        return converterBllImplFileName;
    }

    @NotNull
    public String getBllTempleFilePath() {
        return bllTempleFilePath;
    }

    @NotNull
    public String getBllImplTemplateFilePath() {
        return bllImplTemplateFilePath;
    }

    @Override
    public @NotNull Map<String, Object> renderData(@NotNull TableInfo tableInfo) {

        String parentPath = config.getPackageConfig().getParent() + StringPool.DOT + Constant.BLL_PACKAGE;
        String implParentPath = parentPath + StringPool.DOT + Constant.BLL_IMPL_PACKAGE;

        return new HashMap<String, Object>(4, 1.0f) {{
            put("BLL", parentPath);
            put("BLLImpl", implParentPath);
            put("BLLName", getConverterBllFileName().convert(tableInfo.getEntityName()));
            put("BLLImplName", getConverterBllImplFileName().convert(tableInfo.getEntityName()));
        }};
    }

    public static class Builder extends BaseBuilder {

        private final BLL facade = new BLL();

        public Builder(@NotNull StrategyConfig strategyConfig, @NotNull ConfigBuilder config) {
            super(strategyConfig);
            facade.config = config;
        }

        @NotNull
        public BLL get() {
            return this.facade;
        }
    }

}
