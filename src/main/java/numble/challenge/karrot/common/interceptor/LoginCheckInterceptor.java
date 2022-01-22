package numble.challenge.karrot.common.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.challenge.karrot.common.session.SessionConst;
import numble.challenge.karrot.login.form.LoginMemberForm;
import numble.challenge.karrot.member.service.MemberService;
import numble.challenge.karrot.member.utils.ClientIpUtils;
import numble.challenge.karrot.member.utils.MemberStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final ClientIpUtils ipUtils;
    private final MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        log.info("LoginCheckInterceptor={}", requestURI);

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            log.info("LoginInterceptor not login");

            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false;
        }

        LoginMemberForm form = (LoginMemberForm) session.getAttribute(SessionConst.LOGIN_MEMBER);

        if (form.getStatus() == MemberStatus.인증오류) {
            session.invalidate();
            response.sendRedirect("/members/not-verify-place");
            return false;
        }

        LocalDateTime now = LocalDateTime.now();

        if (ChronoUnit.DAYS.between(form.getVerifyDate(), now) > 30) {
            boolean checkPlace = ipUtils.checkPlace(ipUtils.getClientIp(request), form.getPlace());
            memberService.checkPlace(form.getId(), checkPlace, now);
            if (!checkPlace) {
                session.invalidate();
                response.sendRedirect("/members/not-verify-place");
                return false;
            }
        }

        return true;
    }
}
