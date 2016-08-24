package ki.baratu.api.server;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ki.baratu.api.EMF;

public class ProcuraAlerta {

	@SuppressWarnings({ "unchecked", "unused" })
	private List<Alerta> getListOfAlertas() {

		EntityManager mgr = null;
		List<Alerta> alertas = null;

		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from Alerta as Alerta");

			alertas = (List<Alerta>) query.getResultList();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Alerta obj : alertas);
		} finally {
			mgr.close();
		}

		return alertas;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private List<Produto> getListOfProdutos() {

		EntityManager mgr = null;
		List<Produto> produtos = null;

		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from Produto as Produto");

			produtos = (List<Produto>) query.getResultList();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Produto obj : produtos);
		} finally {
			mgr.close();
		}
		
		return produtos;
	}
	
	private Produto getProdutoById(Long id) {
		EntityManager mgr = getEntityManager();
		Produto produto = null;
		try {
			produto = mgr.find(Produto.class, id);
		} finally {
			mgr.close();
		}
		return produto;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private List<Produto> getProdutosByName(String nome) {
	
		EntityManager mgr = null;
		List<Produto> produtos = null;

		try {
			mgr = getEntityManager();
			Query query;
			
			if(nome != null) {
				query = mgr.createQuery("select p from Produto p where p.nome = :nome", Produto.class);
				query.setParameter("nome", nome);		
				produtos = (List<Produto>) query.getResultList();
				
				// Tight loop for fetching all entities from datastore and accomodate
				// for lazy fetch.
				for (Produto obj : produtos);
			}
		} finally {
			mgr.close();
		}
		
		return produtos;
	}

	void findAlertaPorIdProduto() {

		List<Alerta> alertas = getListOfAlertas();
		
		// A procura por alerta so deve ocorrer se houverem alertas cadastrados.
		if(alertas.size() > 0) {
			
			for(Alerta alerta : alertas) {
				
				Long id = alerta.getIdProduto();
				Double valor = alerta.getValorAlerta();
				
				Produto produto = getProdutoById(id);
				
				// Se pesquisa retorna um produto, então verificar preço.
				if(produto != null) {
					
					// Se preço cadastrado for menor do que o do alerta, então usuário deve ser alertado.
					if(produto.getPreco() <= valor) {
						
						//TODO:
						// Alertar cliente!!!!!!!!!
						
						
					}
				}
			}
		}
	}
	
	void findAlertaPorNomeProduto() {

		List<Alerta> alertas = getListOfAlertas();
		
		// A procura por alerta so deve ocorrer se houverem alertas.
		if(alertas.size() > 0) {
			
			for(Alerta alerta : alertas) {
				
				String nome = alerta.getNomeProduto();
				Double valor = alerta.getValorAlerta();
				
				List<Produto> produtos = getProdutosByName(nome);
				
				// Se pesquisa retorna um produto, então verificar preço.
				if(produtos != null) {
					
					// Se preço cadastrado for menor do que o do alerta, então usuário deve ser alertado.
					
					for(Produto produto : produtos) {
						
						if(produto.getPreco() <= valor) {
							
							//TODO:
							// Alertar cliente!!!!!!!!!
							
							
						}
					}
				}
			}
		}
	}

	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}

}
