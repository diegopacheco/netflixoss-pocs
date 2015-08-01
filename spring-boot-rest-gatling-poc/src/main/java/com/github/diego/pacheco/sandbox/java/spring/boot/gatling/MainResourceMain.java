package com.github.diego.pacheco.sandbox.java.spring.boot.gatling;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
@SuppressWarnings("deprecation")
public class MainResourceMain {

	@RequestMapping(value="/giveme",method=RequestMethod.POST)
    @ResponseBody
    String home(){
		 String result = "Hello World California :-) " + new Date().toString();
		 System.out.println(result);
         return result;
    }
	
	@RequestMapping("/token/{token}")
    @ResponseBody
    String receiveAndPrint(@PathVariable String token){
		String result = "I got your token: " + token;
		System.out.println(result);
        return result; 
    }
	
	@Bean
	FilterRegistrationBean responseFilter() {
		return new FilterRegistrationBean(new Filter() {
			public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
				HttpServletResponse response = (HttpServletResponse) res;
				response.setHeader("Access-Control-Allow-Methods","POST,GET,OPTIONS,DELETE");
				response.setHeader("Access-Control-Max-Age", Long.toString(60 * 60));
				response.setHeader("Token", "token-" + new Date().getMinutes() + "" + new Date().getSeconds() );
				chain.doFilter(req, res);
			}
			public void init(FilterConfig filterConfig) {}
			public void destroy() {}
		});
	}

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainResourceMain.class, args);
    }
}
