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
public class Params implements ITemplate {

    private Params() {
    }

    private ConfigBuilder config;

    /**
     * 转换输出BLL文件名称
     */
    private ConverterFileName converterParamsFileName = (entityName -> entityName + Constant.PARAMS);

    /**
     * 模板引擎路径
     */
    private final String paramsTempleFilePath = "/templates/params.java.btl";

    @NotNull
    public ConverterFileName getConverterParamsFileName() {
        return converterParamsFileName;
    }

    @NotNull
    public String getParamsTempleFilePath() {
        return paramsTempleFilePath;
    }

    @Override
    public @NotNull Map<String, Object> renderData(@NotNull TableInfo tableInfo) {

        String parentPath = config.getPackageConfig().getParent() + StringPool.DOT + Constant.PARAMS_PACKAGE;

        return new HashMap<String, Object>(2, 1.0f) {{
            put("Params", parentPath);
            put("ParamsName", getConverterParamsFileName().convert(tableInfo.getEntityName()));
        }};
    }

    public static class Builder extends BaseBuilder {

        private final Params params = new Params();

        public Builder(@NotNull StrategyConfig strategyConfig, @NotNull ConfigBuilder config) {
            super(strategyConfig);
            params.config = config;
        }

        @NotNull
        public Params get() {
            return this.params;
        }
    }

}
