package ki.baratu.api.server;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Alerta {
	
	@Id
	private Long id;
	private String nomeProduto; // Nome do produto ao qual o alerta está relacionado.
	private Long idProduto;		// ID do produto ao qual o alerta está relacionado.
	private Double valorAlerta; // Preço abaixo do qual o alerta deve ser enviado ao usuário.
	private String email;
	private String telefone;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	public Long getIdProduto() {
		return idProduto;
	}
	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}
	public Double getValorAlerta() {
		return valorAlerta;
	}
	public void setValorAlerta(Double valorAlerta) {
		this.valorAlerta = valorAlerta;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
}
