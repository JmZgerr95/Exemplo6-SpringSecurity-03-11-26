package br.senac.sp.tads.dsw.exemplo6.controller;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import br.senac.sp.tads.dsw.exemplo6.dto.ProdutoRequestDTO;
import br.senac.sp.tads.dsw.exemplo6.model.Categoria;
import br.senac.sp.tads.dsw.exemplo6.repository.CategoriaRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional 
class ProdutoControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private ObjectMapper objectMapper;

    private Categoria categoriaPadrao;

    @BeforeEach 
    void setUp() {
        Categoria cat = new Categoria();
        cat.setNome("Eletrônicos");
        categoriaPadrao = categoriaRepository.save(cat);
    }

    @Test 
    @WithMockUser (roles = "ADMIN") // Simula um usuário auteticado como ADMIN
    void deveCriarProdutoComSucesso() throws Exception {
        ProdutoRequestDTO dto = new ProdutoRequestDTO(
                "Smartphone",
                new BigDecimal("2500.00"),
                categoriaPadrao.getId()
        );
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Smartphone"))
                .andExpect(jsonPath("$.nomeCategoria").value("Eletrônicos"));
    }
}

