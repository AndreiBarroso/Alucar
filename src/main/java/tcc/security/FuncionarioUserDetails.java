/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcc.security;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tcc.domain.Funcionario;

/**
 * @author andrei
 */
public class FuncionarioUserDetails implements UserDetails {

    private final static GrantedAuthority GERENTE = new SimpleGrantedAuthority("GERENTE");
    private final static GrantedAuthority OPERADOR = new SimpleGrantedAuthority("OPERADOR");
    private Funcionario func;

    public FuncionarioUserDetails(Funcionario f) {
        func = f;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (func.isGerente()) {
            return Collections.singleton(GERENTE);

        } else {
            return Collections.singleton(OPERADOR);
        }
    }

    public boolean isGerente() {
        return func.isGerente();
    }

    @Override
    public String getPassword() {
        return func.getSenha();

    }

    @Override
    public String getUsername() {
        return func.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;

    }

}
