package com.hbaccara.fma.config;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.hbaccara.fma.security.NoRedirectStrategy;
import com.hbaccara.fma.security.TokenAuthenticationFilter;
import com.hbaccara.fma.security.TokenAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(new AntPathRequestMatcher("/user/login"),
			new AntPathRequestMatcher("/user/register"));
	private static final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(PUBLIC_URLS);

	TokenAuthenticationProvider provider;

	SecurityConfig(TokenAuthenticationProvider provider) {
		super();
		this.provider = provider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(provider);
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().requestMatchers(PUBLIC_URLS);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(STATELESS).and().exceptionHandling()
				// this entry point handles when you request a protected page and you are not
				// yet authenticated
				.defaultAuthenticationEntryPointFor(forbiddenEntryPoint(), PROTECTED_URLS).and()
				.authenticationProvider(provider)
				.addFilterBefore(restAuthenticationFilter(), AnonymousAuthenticationFilter.class).authorizeRequests()
				.requestMatchers(PROTECTED_URLS).authenticated().and().csrf().disable().formLogin().disable()
				.httpBasic().disable().logout().disable();

		http.cors();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList(new String[] { "*" }));
		configuration
				.setAllowedMethods(Arrays.asList(new String[] { "HEAD", "GET", "POST", "PUT", "DELETE", "PATCH" }));
		// setAllowCredentials(true) is important, otherwise:
		// The value of the 'Access-Control-Allow-Origin' header in the response must
		// not be the wildcard '*' when the request's credentials mode is 'include'.
		configuration.setAllowCredentials(true);
		// setAllowedHeaders is important! Without it, OPTIONS preflight request
		// will fail with 403 Invalid CORS request
		configuration
				.setAllowedHeaders(Arrays.asList(new String[] { "Authorization", "Cache-Control", "Content-Type" }));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	TokenAuthenticationFilter restAuthenticationFilter() throws Exception {
		TokenAuthenticationFilter filter = new TokenAuthenticationFilter(PROTECTED_URLS);
		filter.setAuthenticationManager(authenticationManager());
		filter.setAuthenticationSuccessHandler(successHandler());
		return filter;
	}

	@Bean
	SimpleUrlAuthenticationSuccessHandler successHandler() {
		SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
		successHandler.setRedirectStrategy(new NoRedirectStrategy());
		return successHandler;
	}

	/**
	 * Disable Spring boot automatic filter registration.
	 */
	@Bean
	FilterRegistrationBean disableAutoRegistration(TokenAuthenticationFilter filter) {
		FilterRegistrationBean registration = new FilterRegistrationBean(filter);
		registration.setEnabled(false);
		return registration;
	}

	@Bean
	AuthenticationEntryPoint forbiddenEntryPoint() {
		return new HttpStatusEntryPoint(FORBIDDEN);
	}
}
