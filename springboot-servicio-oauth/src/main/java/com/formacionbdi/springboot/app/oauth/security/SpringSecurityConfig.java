package com.formacionbdi.springboot.app.oauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.ldap.EmbeddedLdapServerContextSourceFactoryBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

	@Autowired
	private UserDetailsService usuarioService;

	@Configuration
	public class SecurityConfiguration {

		@Bean
		static BCryptPasswordEncoder passwordEncoder() {

			return new BCryptPasswordEncoder();
		}

		@Bean
		public EmbeddedLdapServerContextSourceFactoryBean contextSourceFactoryBean() {
			EmbeddedLdapServerContextSourceFactoryBean contextSourceFactoryBean = EmbeddedLdapServerContextSourceFactoryBean
					.fromEmbeddedLdapServer();
			contextSourceFactoryBean.setPort(0);
			return contextSourceFactoryBean;
		}

		@Bean
		public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
				throws Exception {
			return authenticationConfiguration.getAuthenticationManager();
		}

	}
}
