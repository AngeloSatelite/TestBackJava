package com.santander.gestaogastos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.santander.gestaogastos.domain.Categoria;

public interface CategoriaRepositorio extends JpaRepository<Categoria, Integer> {

}
