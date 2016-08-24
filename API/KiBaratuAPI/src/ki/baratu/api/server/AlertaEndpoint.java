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

@Api(name = "alertaendpoint", namespace = @ApiNamespace(ownerDomain = "baratu.ki", ownerName = "baratu.ki", packagePath = "api.server"))
public class AlertaEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listAlerta")
	public CollectionResponse<Alerta> listAlerta(@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<Alerta> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from Alerta as Alerta");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<Alerta>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Alerta obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Alerta>builder().setItems(execute).setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getAlerta")
	public Alerta getAlerta(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		Alerta alerta = null;
		try {
			alerta = mgr.find(Alerta.class, id);
		} finally {
			mgr.close();
		}
		return alerta;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param alerta the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertAlerta")
	public Alerta insertAlerta(Alerta alerta) {
		EntityManager mgr = getEntityManager();
		try {
			if (containsAlerta(alerta)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(alerta);
		} finally {
			mgr.close();
		}
		return alerta;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param alerta the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateAlerta")
	public Alerta updateAlerta(Alerta alerta) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsAlerta(alerta)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(alerta);
		} finally {
			mgr.close();
		}
		return alerta;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeAlerta")
	public void removeAlerta(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			Alerta alerta = mgr.find(Alerta.class, id);
			mgr.remove(alerta);
		} finally {
			mgr.close();
		}
	}

	private boolean containsAlerta(Alerta alerta) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			Alerta item = mgr.find(Alerta.class, alerta.getId());
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
