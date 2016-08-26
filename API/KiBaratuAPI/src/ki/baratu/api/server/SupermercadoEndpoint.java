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

@Api(name = "supermercadoendpoint", namespace = @ApiNamespace(ownerDomain = "que.o", ownerName = "que.o", packagePath = "tem.de.barato.aqui.server"))
public class SupermercadoEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listSupermercado")
	public CollectionResponse<Supermercado> listSupermercado(@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<Supermercado> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from Supermercado as Supermercado");
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
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getSupermercado")
	public Supermercado getSupermercado(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		Supermercado supermercado = null;
		try {
			supermercado = mgr.find(Supermercado.class, id);
		} finally {
			mgr.close();
		}
		return supermercado;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param supermercado the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertSupermercado")
	public Supermercado insertSupermercado(Supermercado supermercado) {
		EntityManager mgr = getEntityManager();
		try {
			if (containsSupermercado(supermercado)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(supermercado);
		} finally {
			mgr.close();
		}
		return supermercado;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param supermercado the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateSupermercado")
	public Supermercado updateSupermercado(Supermercado supermercado) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsSupermercado(supermercado)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(supermercado);
		} finally {
			mgr.close();
		}
		return supermercado;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeSupermercado")
	public void removeSupermercado(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			Supermercado supermercado = mgr.find(Supermercado.class, id);
			mgr.remove(supermercado);
		} finally {
			mgr.close();
		}
	}

	

//	@ApiMethod(name = "addSupermercadoProduto")
//	public void addSupermercadoProduto(@Named("id") Long id, Produto produto) {
//		EntityManager mgr = getEntityManager();
//		try {
//			Supermercado supermercado = mgr.find(Supermercado.class, id);
//			supermercado.addProduto(produto);
//			mgr.persist(supermercado);
//		} finally {
//			mgr.close();
//		}
//	}
//	
//
//	@ApiMethod(name = "delSupermercadoProduto")
//	public void delSupermercadoProduto(@Named("id") Long id, Produto produto) {
//		EntityManager mgr = getEntityManager();
//		try {
//			Supermercado supermercado = mgr.find(Supermercado.class, id);
//			supermercado.delProduto(produto);
//			mgr.persist(supermercado);
//		} finally {
//			mgr.close();
//		}
//	}
	
	
	private boolean containsSupermercado(Supermercado supermercado) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			Supermercado item = mgr.find(Supermercado.class, supermercado.getId());
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
