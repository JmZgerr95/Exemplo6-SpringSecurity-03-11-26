package br.senac.sp.tads.dsw.exemplo6.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.senac.sp.tads.dsw.exemplo6.dto.CategoriaRequestDTO;
import br.senac.sp.tads.dsw.exemplo6.dto.CategoriaResponseDTO;
import br.senac.sp.tads.dsw.exemplo6.model.Categoria;
import br.senac.sp.tads.dsw.exemplo6.repository.CategoriaRepository;

@Service
public class CategoriaService {
    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public List<CategoriaResponseDTO> listar() {
        return repository.findAll().stream()
                .map(c -> new CategoriaResponseDTO(c.getId(), c.getNome())) //Vai mapear cada uma das entidades "c", que é a categoria, com os DTOs de resposta
                .toList();
    }

    public CategoriaResponseDTO criar(CategoriaRequestDTO dto) {
        Categoria c = new Categoria();
        c.setNome(dto.nome());
        c = repository.save(c);
        return new CategoriaResponseDTO(c.getId(), c.getNome());
    }

    // ... (Métodos buscarPorId, atualizar e apagar seguem a mesma lógica)
}
