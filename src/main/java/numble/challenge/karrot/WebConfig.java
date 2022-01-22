package numble.challenge.karrot;

import lombok.RequiredArgsConstructor;
import numble.challenge.karrot.board.service.BoardService;
import numble.challenge.karrot.common.interceptor.LogInterceptor;
import numble.challenge.karrot.common.interceptor.LoginCheckInterceptor;
import numble.challenge.karrot.common.interceptor.MyBoardCheckInterceptor;
import numble.challenge.karrot.common.exception.resolver.CommonHandlerExceptionResolver;
import numble.challenge.karrot.member.service.MemberService;
import numble.challenge.karrot.member.utils.ClientIpUtils;
import numble.challenge.karrot.quartz.factory.AutowiringSpringBeanJobFactory;
import numble.challenge.karrot.quartz.listener.JobsListener;
import numble.challenge.karrot.quartz.listener.TriggersListener;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final BoardService boardService;
    private final MemberService memberService;
    private final ClientIpUtils ipUtils;

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(commonHandlerExceptionResolver());
    }

    public CommonHandlerExceptionResolver commonHandlerExceptionResolver() {
        return new CommonHandlerExceptionResolver();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/image/**");

        registry.addInterceptor(loginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/members/new", "/login", "/css/**",
                        "/image/**", "/images/**", "/*.ico", "/error",
                        "/members/send-mail", "/members/check-mail/**",
                        "/member/mail-not-check", "/**/not-verify-place");

        registry.addInterceptor(myBoardCheckInterceptor())
                .order(3)
                .addPathPatterns("/boards/**")
                .excludePathPatterns("/boards/new", "/boards/**/**",
                        "/boards/interest", "/boards/**/replies/**");

    }

    public LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }

    public LoginCheckInterceptor loginCheckInterceptor() {
        return new LoginCheckInterceptor(ipUtils, memberService);
    }

    public MyBoardCheckInterceptor myBoardCheckInterceptor() {
        return new MyBoardCheckInterceptor(boardService);
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(ApplicationContext applicationContext) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        schedulerFactoryBean.setJobFactory(jobFactory);

        schedulerFactoryBean.setApplicationContext(applicationContext);

        Properties properties = new Properties();
        properties.putAll(quartzProperties().getProperties());

        schedulerFactoryBean.setGlobalTriggerListeners(triggersListener());
        schedulerFactoryBean.setGlobalJobListeners(jobsListener());
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setQuartzProperties(properties);
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true);
        return schedulerFactoryBean;
    }

    public TriggersListener triggersListener() {
        return new TriggersListener();
    }

    public JobsListener jobsListener() {
        return new JobsListener();
    }

    public QuartzProperties quartzProperties() {
        return new QuartzProperties();
    }
}
