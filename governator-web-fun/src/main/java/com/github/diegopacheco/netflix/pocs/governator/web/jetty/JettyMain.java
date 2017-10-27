package com.github.diegopacheco.netflix.pocs.governator.web.jetty;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.servlet.ServletModule;
import com.google.inject.util.Modules;
import com.netflix.governator.InjectorBuilder;
import com.netflix.governator.LifecycleInjector;
import com.netflix.governator.ShutdownHookModule;
import com.netflix.governator.guice.jetty.DefaultJettyConfig;
import com.netflix.governator.guice.jetty.JettyConfig;
import com.netflix.governator.guice.jetty.JettyModule;

public class JettyMain {
	
	@Singleton
	@WebServlet
	static class MyServlet extends HttpServlet{
		private static final long serialVersionUID = 1L;
		public MyServlet() {}
		
		@Override
		public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			resp.getOutputStream().write("Ok".getBytes());
			resp.getOutputStream().flush();
		}
	}
	
	static class SampleServletModule extends ServletModule {
	     @Override
	     protected void configureServlets() {
	       serve("/").with(MyServlet.class);
	     }
	}
	
	public static void main(String[] args) throws Throwable {
		
		LifecycleInjector injector = InjectorBuilder.fromModules(
				  new SampleServletModule(),
				  new ShutdownHookModule(),
	              Modules.override(new JettyModule())
                    .with(new AbstractModule() {
                         protected void configure() {}
                         @Provides
                         JettyConfig getConfig() {
                             return new DefaultJettyConfig().setPort(9090);
                         }
                     })
		 ).createInjector();
		System.out.println(injector);
                
	}
	
}
