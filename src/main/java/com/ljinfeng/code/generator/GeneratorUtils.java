package com.ljinfeng.code.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import com.ljinfeng.code.generator.engine.CustomTemplateEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动生成工具类
 *
 * @author ljinfeng@linewell.com
 * @date 2021-07-19
 */
public class GeneratorUtils {

    public static void main(String[] args) {

        GeneratorUtils generatorUtils = new GeneratorUtils();
        generatorUtils.generator();
    }

    public void generator() {

        AutoGenerator autoGenerator = new AutoGenerator(this.initDataSourceConfig());
        //全局配置
        autoGenerator.global(this.initGlobalConfig());
        //策略配置
        autoGenerator.strategy(this.initStrategyConfig());
        //包配置
        autoGenerator.packageInfo(this.initPackageConfig());
        //自定义配置
        autoGenerator.injection(this.initInjectionConfig());
        //自定义模板引擎
        autoGenerator.execute(new CustomTemplateEngine());
    }

    private DataSourceConfig initDataSourceConfig() {

        String url = "jdbc:mysql://192.168.203.17:3306/xtyzt?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC";
        String username = "root";
        String password = "Ajo^iURC#r1830";

        //默认mysql，根据url链接自动切换库类型
        return new DataSourceConfig.Builder(url, username, password)
                .build();
    }

    private GlobalConfig initGlobalConfig() {

        return new GlobalConfig.Builder()
                //生成文件本地位置
                .outputDir("F://code-generator")
                //覆盖
                .fileOverride()
                //开启swagger
                .enableSwagger()
                .author("ljinfeng")
                .build();
    }

    private StrategyConfig initStrategyConfig() {

        //生成表单
        List<String> tableList = new ArrayList<String>() {{
            add("xt_camera");
        }};

        StrategyConfig.Builder builder = new StrategyConfig.Builder();
        builder.entityBuilder().naming(NamingStrategy.underline_to_camel);
        //全局主键类型
        builder.entityBuilder().idType(IdType.INPUT);
        //开启lombok
        builder.entityBuilder().enableLombok();
        //开启生成serialVersionUID
        builder.entityBuilder().enableSerialVersionUID();
        //开启生成实体时生成字段注解（开启swagger时不会生成注解）
        builder.entityBuilder().enableTableFieldAnnotation();
        //开启生成@RestController控制器
        builder.controllerBuilder().enableRestStyle();
        builder.addInclude(tableList.toArray(new String[0]));
        //生成文件去除表前缀
        builder.addTablePrefix("xt_");

        return builder.build();
    }

    private PackageConfig initPackageConfig() {

        return new PackageConfig.Builder()
                //父包名
                .parent("com.ljinfeng.code.generator")
                //模块名
                .moduleName("autogenerator")
                .build();
    }

    private InjectionConfig initInjectionConfig() {

        return new InjectionConfig.Builder()
                .build();
    }

}
