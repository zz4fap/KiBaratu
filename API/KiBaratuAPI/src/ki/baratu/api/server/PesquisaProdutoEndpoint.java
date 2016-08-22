package o.que.tem.de.barato.aqui.server;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JPACursorHelper;

import o.que.tem.de.barato.aqui.EMF;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Api(name = "pesquisaprodutoendpoint", namespace = @ApiNamespace(ownerDomain = "tah.oque", ownerName = "tah.oque", packagePath = "barato.aqui.server"))
public class PesquisaProdutoEndpoint {
	
	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "searchProduto")
	public CollectionResponse<Produto> listProduto(@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit, @Nullable @Named("tipo") String tipo, @Nullable @Named("nome") String nome, @Nullable @Named("marca") String marca) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<Produto> execute = null;

		try {
			mgr = getEntityManager();
			Query query;
			
			if(tipo!=null && nome!=null && marca!=null) {
				query = mgr.createQuery("select p from Produto p where p.tipo = :tipo And p.nome = :nome And p.marca = :marca",Produto.class);
				query.setParameter("tipo", tipo);	
				query.setParameter("nome", nome);
				query.setParameter("marca", marca);				
			} else if(tipo!=null) {
				query = mgr.createQuery("select p from Produto p where p.tipo = :tipo",Produto.class);
				query.setParameter("tipo", tipo);
			} else if(nome!=null)  {
				query = mgr.createQuery("select p from Produto p where p.nome = :nome",Produto.class);
				query.setParameter("nome", nome);
			} else if(marca!=null) {
				query = mgr.createQuery("select p from Produto p where p.marca = :marca",Produto.class);
				query.setParameter("marca", marca);
			} else {
				query = mgr.createQuery("select from Produto as Produto");
			}	
			
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<Produto>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Produto obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Produto>builder().setItems(execute).setNextPageToken(cursorString).build();
	}
	
	private boolean containsProduto(Produto produto) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			Produto item = mgr.find(Produto.class, produto.getId());
			if (item == null) {
				contains = false;
			}
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}

}
