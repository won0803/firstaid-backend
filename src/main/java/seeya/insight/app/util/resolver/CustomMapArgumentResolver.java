package seeya.insight.app.util.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import seeya.insight.app.util.CommandMap;

import java.util.Enumeration;
@Component
public class CustomMapArgumentResolver implements HandlerMethodArgumentResolver{
    protected Log log = LogFactory.getLog(this.getClass());
    // supportsParameter : Resolver가 적용가능하진 검사
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return CommandMap.class.isAssignableFrom(parameter.getParameterType());
    }

    // resolverArgument메소드는 파라미터와 기타정보를 받아 실제 객체를 반환한다
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        
        CommandMap commandMap = new CommandMap();
        
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Enumeration<?> enumeration = request.getParameterNames();

        String key      = null;
        String[] values = null;

        // 파라메터를 맵에 담기
        while(enumeration.hasMoreElements()){
            key = (String) enumeration.nextElement();
            values = request.getParameterValues(key);
            
            if (values != null){
                commandMap.put(key, (values.length > 1) ? values : values[0]);
            } // if value != null
        } // while

        HttpSession session = request.getSession();

        // 세션정보를 맵에 담기
        Enumeration<?> attEnum = session.getAttributeNames(); 
        while(attEnum.hasMoreElements()) { 
             key = (String) attEnum.nextElement(); 
             commandMap.put(key , session.getAttribute(key));
        }

        return commandMap;
    }
    
}
