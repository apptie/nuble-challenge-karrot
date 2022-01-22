package numble.challenge.karrot.common.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.challenge.karrot.board.service.BoardService;
import numble.challenge.karrot.common.session.SessionConst;
import numble.challenge.karrot.login.form.LoginMemberForm;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
public class MyBoardCheckInterceptor implements HandlerInterceptor {

    private final BoardService boardService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        log.info("MyBoardCheckInterceptor requestURI={}", requestURI);

        HttpSession session = request.getSession(false);

        LoginMemberForm loginMember = (LoginMemberForm) session.getAttribute(SessionConst.LOGIN_MEMBER);

        long boardId = Long.parseLong(requestURI.split("/")[2]);
        long memberId = boardService.getMemberId(boardId);

        if (loginMember == null) {
            response.sendError(404);
            return false;
        }

        if (memberId == loginMember.getId()) {
            response.sendRedirect("/boards/" + boardId + "/my-detail");
            return false;
        }

        return true;
    }
}
