//package tcc.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import tcc.security.FuncionarioUserDetailsService;
//
//@Configuration
//@EnableWebSecurity
//public class ConfiguraSeguranca extends WebSecurityConfigurerAdapter {
//
//    private FuncionarioUserDetailsService detailsService;
//
//    public ConfiguraSeguranca(FuncionarioUserDetailsService servico) {
//        detailsService = servico;
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable();
//        http.cors().disable();
//
//        http.authorizeRequests()
//                .antMatchers(HttpMethod.POST, "/funcionarios").hasAnyAuthority("GERENTE")
//                .antMatchers(HttpMethod.POST, "/planos").hasAnyAuthority("GERENTE")
//                .antMatchers(HttpMethod.POST, "/veiculos").hasAnyAuthority("GERENTE")
//                .anyRequest().hasAnyAuthority("GERENTE", "OPERADOR");
//
//        http.formLogin().loginPage("/login").permitAll();
//        http.logout().permitAll();
//
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/img/*");
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) {
//        auth.authenticationProvider(authenticationProvider());
//    }
//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(detailsService);
//        authProvider.setPasswordEncoder(encoder());
//        return authProvider;
//    }
//
//    @Bean
//    public PasswordEncoder encoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }
//
//}
