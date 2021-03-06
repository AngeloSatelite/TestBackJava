package com.santander.gestaogastos.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.santander.gestaogastos.exception.GastosException;
import com.santander.gestaogastos.repository.GastosRepositorio;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@Entity
public class Gasto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String descricao;
	private BigDecimal valor;
	
	@ManyToOne
	@Immutable
	@JoinColumn(name = "id_categoria")
 	private Categoria categoria;
	
	@ManyToOne
	@Immutable
 	@JoinColumn(name = "id_usuario")
 	private Usuario usuario;
 	
 	@DateTimeFormat
 	@Temporal(TemporalType.TIMESTAMP)
 	private Date data;
 	
 	@JsonIgnore
 	@Transient
 	private GastosRepositorio gastosRepositorio;
	
 	public Gasto(GastosRepositorio gastosRepositorio) {
 		this.gastosRepositorio = gastosRepositorio;
 	}
 	
 	public List<Gasto> listaGastos(){
 		return this.gastosRepositorio.findAll();
 	}
 	
 	public Gasto salvarGasto(Gasto gastoIn) throws GastosException {
 		
 		validate();
 		
 		return this.gastosRepositorio.save(gastoIn);
 	}
 	
	public Gasto pesquisarGasto(Integer id) throws GastosException {
		if (this.gastosRepositorio.findById(id).isPresent()) {
			return this.gastosRepositorio.findById(id).get();
		}else {
			return null;
		}
			
	}
	
	public void removeGasto(Gasto gastoIn) throws GastosException {
		
		this.gastosRepositorio.delete(gastoIn);
		
	}
	public List<String> buscarCategoriasDoCliente(Gasto gastoIn) {
		return this.gastosRepositorio.buscarCategoriasDoCliente(gastoIn.getUsuario().getId());
	}
	
	public List<Gasto> gastosDoClientePorData(String dataIn) throws ParseException {
		SimpleDateFormat formatador = new SimpleDateFormat("yyyy/MM/dd");
		List<Gasto> gastosDoClientePorData = this.gastosRepositorio.findByDate(formatador.parse(dataIn.replace("-", "/")));
 		
		return gastosDoClientePorData;
	}
	
	public void validate () throws GastosException {
		
		if (this.descricao == null && "".equals(this.descricao)) {
			throw new GastosException(" É necessário informar as descrição do gasto");
		}

	}


 
}
