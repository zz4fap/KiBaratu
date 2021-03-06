package ki.baratu.api.server;

import ki.baratu.api.EMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JPACursorHelper;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.logging.Logger;


@Api(name = "produtoendpoint", namespace = @ApiNamespace(ownerDomain = "baratu.ki", ownerName = "baratu.ki", packagePath = "api.server"))
public class ProdutoEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listProduto")
	public CollectionResponse<Produto> listProduto(@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<Produto> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from Produto as Produto");
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

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getProduto")
	public Produto getProduto(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		Produto produto = null;
		try {
			produto = mgr.find(Produto.class, id);
		} finally {
			mgr.close();
		}
		return produto;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param produto the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertProduto")
	public Produto insertProduto(Produto produto) {
		EntityManager mgr = getEntityManager();
		try {
			if (containsProduto(produto)) {
				throw new EntityExistsException("Object already exists");
			}
			Logger log = Logger.getLogger(ProdutoEndpoint.class.getName()); 
			log.info("Inserindo produto\n" + produto);

			mgr.persist(produto);
		} finally {
			mgr.close();
		}
		return produto;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param produto the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateProduto")
	public Produto updateProduto(Produto produto) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsProduto(produto)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(produto);
		} finally {
			mgr.close();
		}
		return produto;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeProduto")
	public void removeProduto(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			Produto produto = mgr.find(Produto.class, id);
			mgr.remove(produto);
		} finally {
			mgr.close();
		}
	}

	private boolean containsProduto(Produto produto) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
	        // If no ID was set, the entity doesn't exist yet.
	        if(produto.getId() == null)
	            return false;
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
