package numble.challenge.karrot.common.exception.resolver;

import lombok.extern.slf4j.Slf4j;
import numble.challenge.karrot.common.exception.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class CommonHandlerExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("error/exception-page");

            if (ex instanceof MemberNotExistException) {
                log.info("MemberHandlerExceptionResolver resolveException : 존재하지 않는 회원 아이디입니다.");
                mav.addObject("message", "존재하지 않는 회원 아이디입니다.");

                return mav;
            }

            if (ex instanceof BoardNotExistException) {
                log.info("BoardHandlerExceptionResolver resolveException : 존재하지 않는 게시글입니다.");
                mav.addObject("message", "존재하지 않는 게시글입니다.");

                return mav;
            }

            if (ex instanceof ReplyNotExistException) {
                log.info("ReplyNotExistException resolveException : 존재하지 않는 댓글입니다.");
                mav.addObject("message", "존재하지 않는 댓글입니다.");

                return mav;
            }

            if (ex instanceof IpIOException) {
                log.info("IpIOException resolveException : 잘못된 지역명 또는 인식하지 못하는 지역명입니다.");
                mav.addObject("message", "잘못된 지역명 또는 인식하지 못하는 지역명입니다.");

                return mav;
            }

            if (ex instanceof MailSendErrorException) {
                log.info("MailSendErrorException resolveException : 메일 전송에 실패했습니다.");
                mav.addObject("message", "메일 전송에 실패했습니다.");

                return mav;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
