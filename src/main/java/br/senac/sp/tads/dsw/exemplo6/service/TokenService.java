package br.senac.sp.tads.dsw.exemplo6.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.senac.sp.tads.dsw.exemplo6.model.Usuario;

@Service 
public class TokenService {
    
    @Value ("${api.security.token.secret}") //link com a linha de código do resources
    private String secret;

    public String gerarToken(Usuario usuario){
        try{
            //Algoritmo de assinatura de criptografia
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    //Identifica quem emitiu o token
                    .withIssuer("API e-commerce Turma B")
                    // guarda o login do usuário dentro do token
                    .withSubject(usuario.getUsername())
                    // define a duração da validade 
                    .withExpiresAt(gerarDataExpiracao())
                    // assina digitalmente com o segredo definido em properties (resources)
                    .sign(algorithm);
        }catch(JWTCreationException e){
            throw new RuntimeException("Erro ao gerar tokenJWT", e);
        }    
    }

        private Instant gerarDataExpiracao() {
            return Instant.now().plus(2, ChronoUnit.HOURS);
        }

        public String validarToken(String token){
            try{
                Algorithm algorithm = Algorithm.HMAC256(token);
                return JWT.require(algorithm)
                .withIssuer("API e-commerce Turma B")
                .build()
                // Verifica se a assinatura é autentica e se não expirou
                .verify(token)
                // Devolve o login do usuário que estava guardado no token
                .getSubject();
            }catch(JWTVerificationException e){
                //Se o token for inválido, adulterado ou expirado, retorna vazio
                return "";
            }
        }
}
