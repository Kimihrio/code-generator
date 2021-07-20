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
public class DTO implements ITemplate {

    private DTO() {
    }

    private ConfigBuilder config;

    /**
     * 转换输出DTO文件名称
     */
    private ConverterFileName converterDtoFileName = (entityName -> entityName + Constant.DTO);

    /**
     * 模板引擎路径
     */
    private final String dtoTempleFilePath = "/templates/dto.java.btl";

    @NotNull
    public ConverterFileName getConverterDtoFileName() {
        return converterDtoFileName;
    }

    @NotNull
    public String getDtoTempleFilePath() {
        return dtoTempleFilePath;
    }

    @Override
    public @NotNull Map<String, Object> renderData(@NotNull TableInfo tableInfo) {

        String parentPath = config.getPackageConfig().getParent() + StringPool.DOT + Constant.DTO_PACKAGE;

        return new HashMap<String, Object>(2, 1.0f) {{
            put("Dto", parentPath);
            put("DtoName", getConverterDtoFileName().convert(tableInfo.getEntityName()));
        }};
    }

    public static class Builder extends BaseBuilder {

        private final DTO dto = new DTO();

        public Builder(@NotNull StrategyConfig strategyConfig, @NotNull ConfigBuilder config) {
            super(strategyConfig);
            dto.config = config;
        }

        @NotNull
        public DTO get() {
            return this.dto;
        }
    }

}
