package br.senac.sp.tads.dsw.exemplo6.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.senac.sp.tads.dsw.exemplo6.repository.UsuarioRepository;
import br.senac.sp.tads.dsw.exemplo6.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component 
public class SecurityFilter extends OncePerRequestFilter  {
    
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    public SecurityFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                var token = this.recuperarToken(request);

                if (token != null) {
                    //Valida a assinatura e validade do token, extraindo o login
                    var login = tokenService.validarToken(token);
                    if(!login.isBlank()){
                        //Buscar o usuário no banco para carregar suas permissões (Roles)
                        UserDetails usuario = usuarioRepository.findByLogin(login).orElse(null);
                    if(usuario != null){
                        // Autentica o usuário no contexto do Spring apenas durante esta requisição
                        var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
    }
    // Segue o fluxo (se não havia token válido, o Spring bloqueará logo adiante caso a ota seja privada)
    filterChain.doFilter(request, response);
}

    private String recuperarToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer")){
            return null;
        }
        return authHeader;
    }

}
