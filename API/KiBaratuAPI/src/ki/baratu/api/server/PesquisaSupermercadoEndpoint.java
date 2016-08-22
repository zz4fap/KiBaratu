package o.que.tem.de.barato.aqui.server;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JPACursorHelper;

import o.que.tem.de.barato.aqui.EMF;

@Api(name = "pesquisasupermercadoendpoint", namespace = @ApiNamespace(ownerDomain = "tah.oque", ownerName = "tah.oque", packagePath = "barato.aqui.server"))
public class PesquisaSupermercadoEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "searchSupermercado")
	public CollectionResponse<Supermercado> listSupermercado(@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit, @Nullable @Named("nome") String nome) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<Supermercado> execute = null;

		try {
			mgr = getEntityManager();
			Query query;
			
			if(nome!=null) {
				query = mgr.createQuery("select p from Supermercado p where p.nome = :nome",Supermercado.class);
				query.setParameter("nome", nome);	
			} else {
				query = mgr.createQuery("select from Supermercado as Supermercado");
			}
			
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<Supermercado>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Supermercado obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Supermercado>builder().setItems(execute).setNextPageToken(cursorString).build();
	}
	
	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listPromocoes")
	public CollectionResponse<Produto> listPromoces(@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit, @Nullable @Named("nome") String nome, @Nullable @Named("unidade") String unidade) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<Supermercado> execute = null;
		List<Produto> executeProd = null;

		try {
			mgr = getEntityManager();
			Query query;
			
			if(nome!=null && unidade!=null) {
				query = mgr.createQuery("select p from Supermercado p where p.nome = :nome And p.unidade = :unidade",Supermercado.class);
				query.setParameter("nome", nome);
				query.setParameter("unidade", unidade);
			} else {
				query = mgr.createQuery("select from Supermercado as Supermercado");
			}
			
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<Supermercado>) query.getResultList();
			
			Long id = (long) -1;
			if(execute.size()==1) {
				id = execute.get(0).getId();
			} 
			
			if(id>0) {
				query = mgr.createQuery("select p from Produto p where p.supermercadoId = :id",Produto.class);
				query.setParameter("id", id);	
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
			
			
			executeProd = (List<Produto>) query.getResultList();
			
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Produto obj : executeProd)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Produto>builder().setItems(executeProd).setNextPageToken(cursorString).build();
	}
	
	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}


}



