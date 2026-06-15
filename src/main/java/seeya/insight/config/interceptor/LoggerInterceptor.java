package seeya.insight.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Log4j2
public class LoggerInterceptor implements HandlerInterceptor {
//    @Resource(name="loginService")
//    private LoginService loginService;

    /*
      - 컨트롤러의 메서드에 매핑된 특정 URI가 호출됐을 때 실행되는 메서드
      - 컨트롤러를 경유(접근)하기 직전에 실행되는 메서드
      - 사용자가 어떠한 기능을 수행했는지 파악하기 위해, 해당 메서드(기능)와 매핑된 URI 정보가 로그로 출력되도록 처리
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("======================================          START         ======================================");
            log.debug(" Request URI \t:  " + request.getRequestURI());
        }

        String requestURI = request.getRequestURI();

        boolean isCustomLoginURI = "/LoginProcess".equals(requestURI);
//        boolean isSSOLoginURI = "/index.do".equals(requestURI) || "/indexRaon.do".equals(requestURI) || "/AForm/test.do".equals(requestURI) || "/HIRA/getTotalWorkStateStat.do".equals(requestURI) || "/HIRA/manualUpdateEmployeeDB.do".equals(requestURI);

//        if ( !(isSSOLoginURI || isCustomLoginURI) ) {
        if ( !isCustomLoginURI ) {
            if(request.getSession().getAttribute("strMyID") == null || request.getSession().getAttribute("strMyID") == "" ){
                if (isCustomLoginURI) {
                    // 로그인 화면을 통해 로그인하는 경우
                    response.sendRedirect("/");
                } else {
                    // SSO를 사용하는 경우 - 운영서버
//                    String result = loginService.loginProcessSSO(request, request.getSession(), null);
//                    if (result.equals("SUCCESS") || result.equals("WORK_PAGE")) {
//                    } else {
//                        response.sendRedirect("/");
//                        return false;
//                    }
                }
            }
        }


        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /*
        - 컨트롤러를 경유(접근) 한 후, 즉 화면(View)으로 결과를 전달하기 전에 실행되는 메서드
        - preHandle()과는 반대로 요청(Request)의 끝을 알리는 로그가 콘솔에 출력되도록 처리
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("======================================    END(postHandle)    ======================================");
        }
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /*
        - Controller에서 요청이 다 마무리되고, View로 Rendering이 다 끝나면 처리
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("======================================     END(afterCompletion)     ======================================\n");
        }
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
