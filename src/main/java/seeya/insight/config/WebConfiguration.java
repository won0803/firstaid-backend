package seeya.insight.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import seeya.insight.app.util.resolver.CustomMapArgumentResolver;
import seeya.insight.config.interceptor.LoggerInterceptor;

import java.util.List;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    /*
        WebMvcConfigurer : 해당 인터페이스를 구현하면 @EnableWebMvc의 자동 설정을 베이스로 가져가며,
                           개발자가 원하는 설정까지 추가할 수 있다는 장점이 있다. (Override 가능)
     */

    /*
        addInterceptors
        - 애플리케이션 내에 인터셉터를 등록함.
        - 이 과정에서 excludePathPatterns()를 이용하면 메서드의 인자로 전달하는 주소(URI)와 경로(Path)는 인터셉터 호출에서 제외시킴.
        - 여기서 해당 메서드는 resources의 모든 정적(static) 파일을 무시(ignore)하겠다는 의미로 사용됨.
        - 반대의 경우로 addPathPatterns()가 있으며
           excludePathPatterns()가 주소와 경로를 인터셉터 호출에서 제외한다면
           addPathPatterns()는 인터셉터를 호출하는 주소와 경로를 추가하는 개념.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggerInterceptor()).excludePathPatterns("/assets/**");
    }

    // modify by lhk 2023.04.05 - commandMap 사용을 위한 resolver 설정 시작
    @Autowired
    private CustomMapArgumentResolver customMapArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(customMapArgumentResolver);
    }
    // modify by lhk 2023.04.05 - commandMap 사용을 위한 resolver 설정 끝


    /*
        jsonView 빈네임 설정을 통해, jsonView가 리턴될 때, JSON형태의 데이터로 매핑

        ex) public ModelAndView XXXXX(CommandMap commandMap, HttpServletRequest request) throws Exception {
                ModelAndView mv = null;

                mv = new ModelAndView("jsonView");

                ....

                return mv;
            }
     */
    @Bean(name="jsonView")
    public MappingJackson2JsonView jsonView() {
        return new MappingJackson2JsonView();
    }

}
