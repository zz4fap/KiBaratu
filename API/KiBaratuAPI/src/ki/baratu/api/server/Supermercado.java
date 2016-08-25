package ki.baratu.api.server;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Supermercado {
	
	@Id
	private Long id;
	private String nome;
	private String unidade;
	private String endereco;
	private String localizacao;
	
	private Collection<Produto> produtos;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getUnidade() {
		return unidade;
	}
	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public String getLocalizacao() {
		return localizacao;
	}
	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}
	
	public void addProduto(Produto produto)
	{
		this.produtos.add(produto);
	}
	
	public void delProduto(Produto produto)
	{
		this.produtos.remove(produto);
	}

}
