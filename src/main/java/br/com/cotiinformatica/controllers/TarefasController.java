package br.com.cotiinformatica.controllers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cotiinformatica.dtos.TarefaRequestDto;
import br.com.cotiinformatica.dtos.TarefaResponseDto;
import br.com.cotiinformatica.entities.Tarefa;
import br.com.cotiinformatica.repositories.CategoriaRepository;
import br.com.cotiinformatica.repositories.TarefaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/tarefas")
@Tag(name = "Controle de tarefas", 
	 description = "Serviços para gerenciamento de dados de tarefas da agenda.")
public class TarefasController {

	//Instanciando atributos de forma automática
	@Autowired TarefaRepository tarefaRepository;
	@Autowired CategoriaRepository categoriaRepository;
	@Autowired ModelMapper mapper;
	
	@PostMapping
	@Operation(summary = "Cadastro de tarefas", 
	       description = "Cria uma nova tarefa no sistema.")
	public TarefaResponseDto post(@RequestBody @Valid TarefaRequestDto request) {

		//Buscar a categoria no banco de dados através do ID
		var categoria = categoriaRepository.findById(request.getCategoriaId())
						.orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada. Verifique o ID informado."));
				
		var tarefa = mapper.map(request, Tarefa.class); //copiando os dados do objeto request para a entidade
		tarefa.setId(UUID.randomUUID()); //gerando o id da tarefa (chave primária)
		tarefa.setCategoria(categoria); //associando a tarefa com a categoria
		
		//enviando o registro para ser gravado no banco através do repositório
		tarefaRepository.save(tarefa);
		
		//retornar os dados da tarefa cadastrada
		return mapper.map(tarefa, TarefaResponseDto.class);
	}
	
	@PutMapping("{id}")
	@Operation(summary = "Edição de tarefas", 
           description = "Atualiza uma tarefa existente no sistema.")
	public TarefaResponseDto put(@PathVariable UUID id, @RequestBody @Valid TarefaRequestDto request) {
		
		//Verificar se a tarefa existe no banco de dados
		if(!tarefaRepository.existsById(id))
			throw new IllegalArgumentException("Tarefa não encontrada para edição. Verifique o ID informado.");
		
		//Buscar a categoria no banco de dados através do ID
		var categoria = categoriaRepository.findById(request.getCategoriaId())
						.orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada. Verifique o ID informado."));
		
		var tarefa = mapper.map(request, Tarefa.class); //copiando os dados do objeto request para a entidade
		tarefa.setId(id); //preencher o id da tarefa enviado no PUT
		tarefa.setCategoria(categoria); //associando a tarefa com a categoria
		
		//enviando o registro para ser gravado no banco através do repositório
		tarefaRepository.save(tarefa);
		
		//retornar os dados da tarefa cadastrada
		return mapper.map(tarefa, TarefaResponseDto.class);
	}
	
	@DeleteMapping("{id}")
	@Operation(summary = "Exclusão de tarefas", 
           description = "Apaga uma tarefa no sistema.")
	public TarefaResponseDto delete(@PathVariable UUID id) {
		
		//consultar a tarefa no banco de dados através do ID informado
		var tarefa = tarefaRepository.findById(id)
						.orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada para exclusão. Verifique o ID informado."));
		
		//excluindo a tarefa
		tarefaRepository.delete(tarefa);
		
		//retornar os dados da tarefa cadastrada
		return mapper.map(tarefa, TarefaResponseDto.class);
	}
	
	@GetMapping
	@Operation(summary = "Consulta de tarefas", 
           description = "Retorna as tarefas cadastradas no sistema.")
	public List<TarefaResponseDto> get() {
		
		//consultar as tarefas cadastradas no banco de dados		
		var tarefas = tarefaRepository.findAll();
						
		//copiando os dados de uma lista de tarefas (entidade) 
		//para uma lista da classe TarefaResponseDto
		return tarefas //lista com as tarefas do banco de dados
				.stream() //percorrendo cada tarefa
				.map(tarefa -> mapper.map(tarefa, TarefaResponseDto.class)) //copiando para o DTO
				.collect(Collectors.toList()); //retornando a lista de DTOs		
	}
	
	@GetMapping("{id}")
	@Operation(summary = "Consulta de tarefa por ID", 
		   description = "Retorna 1 tarefa baseado no ID informado.")
	public TarefaResponseDto getById(@PathVariable UUID id) {
	
		//consultar 1 tarefa no banco de dados através do ID
		var tarefa = tarefaRepository.findById(id)
						.orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada."));
		
		//retornar os dados da tarefa
		return mapper.map(tarefa, TarefaResponseDto.class);
	}
}






