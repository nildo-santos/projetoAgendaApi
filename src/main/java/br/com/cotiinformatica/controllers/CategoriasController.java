package br.com.cotiinformatica.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cotiinformatica.dtos.CategoriaResponseDto;
import br.com.cotiinformatica.repositories.CategoriaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/categorias")
@Tag(name = "Controle de categorias", 
     description = "Serviços para gerenciamento de dados de categorias.")
public class CategoriasController {

	//Instanciando o repositório de categorias de forma automática
	@Autowired CategoriaRepository categoriaRepository;
	@Autowired ModelMapper mapper;
	
	@GetMapping
	@Operation(summary = "Consulta de categorias", 
	  	   description = "Retorna todas as categorias cadastradas no sistema.")
	public List<CategoriaResponseDto> get() {
		
		//consultar as categorias cadastradas no banco de dados		
		var categorias = categoriaRepository.findAll();
				
		//copiando os dados de uma lista de categorias (entidade) 
		//para uma lista da classe CategoriaResponseDto
		return categorias //lista com as categorias do banco de dados
				.stream() //percorrendo cada categoria
				.map(categoria -> mapper.map(categoria, CategoriaResponseDto.class)) //copiando para o DTO
				.collect(Collectors.toList()); //retornando a lista de DTOs
	}
}






