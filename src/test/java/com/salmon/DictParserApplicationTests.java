package com.salmon;

import com.salmon.utils.ParseBaiduDictUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.List;

@SpringBootTest
class DictParserApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(DictParserApplication.class);


    @Test
    void contextLoads() throws Exception {
        String path = "dict/china_star.bdict";
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:/templates/" + path);
        List<String> dicts = ParseBaiduDictUtils.parseByFile(resource.getFile());
        log.info("dicts:{}", dicts);
    }

}
