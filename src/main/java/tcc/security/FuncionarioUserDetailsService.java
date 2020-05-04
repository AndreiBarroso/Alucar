/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcc.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tcc.domain.Funcionario;
import tcc.repository.FuncionarioRepositorio;

/**
 *
 * @author andrei
 */
@Service
public class FuncionarioUserDetailsService implements UserDetailsService {

    private final FuncionarioRepositorio repositorio;

    public FuncionarioUserDetailsService(FuncionarioRepositorio repo) {
        repositorio = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        Funcionario f = repositorio.retornaFuncionario(login);
        if (f == null) {
            return null;
        }
        return new FuncionarioUserDetails(f);
    }

}
