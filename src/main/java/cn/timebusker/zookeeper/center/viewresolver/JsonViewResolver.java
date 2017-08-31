package cn.timebusker.zookeeper.center.viewresolver;

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

public class JsonViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String arg0, Locale arg1) throws Exception {
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        //便于阅读的排版
        view.setPrettyPrint(true);
        view.setExtractValueFromSingleKeyModel(true);
        return view;
    }

}

