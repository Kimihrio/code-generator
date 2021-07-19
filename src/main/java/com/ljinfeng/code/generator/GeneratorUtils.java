package com.ljinfeng.code.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

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

        String url = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC";
        String username = "root";
        String password = "ljinfeng";

        //默认mysql，根据url链接自动切换库类型
        return new DataSourceConfig.Builder(url, username, password)
                .build();
    }

    private GlobalConfig initGlobalConfig() {

        return new GlobalConfig.Builder()
                //生成文件本地位置
                .outputDir("F://code-generator")
                .fileOverride()
                //开启swagger
                .enableSwagger()
                .author("ljinfeng")
                .build();
    }

    private StrategyConfig initStrategyConfig() {

        //生成表单
        List<String> tableList = new ArrayList<String>() {{
            add("am_devices_checklist");
        }};

        StrategyConfig.Builder builder = new StrategyConfig.Builder();
        builder.entityBuilder().naming(NamingStrategy.underline_to_camel);
        //开启lombok
        builder.entityBuilder().enableLombok();
        builder.addInclude(tableList.toArray(new String[0]));
        //生成文件去除表前缀
        builder.addTablePrefix("am_");

        return builder.build();
    }

    private PackageConfig initPackageConfig() {

        return new PackageConfig.Builder()
                .parent("com.ljinfeng.code.generator")
                .moduleName("autogenerator")
                .build();
    }

    private InjectionConfig initInjectionConfig() {

        return new InjectionConfig.Builder()
                .build();
    }

}
