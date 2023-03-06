package org.acme.hibernate.orm.panache;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.util.List;

/**
 * @author mshaikh
 */
@Path("/fruits")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class FruitResource {

	@GET
	public Uni<List<Fruit>> getAll() {
		return Fruit.listAll(Sort.by("name"));
	}

	@GET
	@Path("/{id}")
	public Uni<Fruit> getSingle(Long id) {
		return Fruit.findById(id);
	}

	@POST
	public Uni<Response> create(Fruit fruit) {

		if (fruit == null || fruit.id == null) {
			log.error("Id was invalidly set on request.");
			throw new WebApplicationException("Id was invalidly set on request.", 422);
		}
		return Panache.<Fruit>withTransaction(fruit::persist).onItem()
				.transform(inserted -> Response.created(URI.create("/fruits/" + inserted.id)).build());
	}
	
	@PUT
	@Path("{id}")
	public Uni<Response> update(Long id, Fruit fruit) {
		if (fruit == null || fruit.name == null) {
			log.error("Fruit name was not set in the request.");
			throw new WebApplicationException("Fruit name was not set in the request.", 422);
		}
		return Panache.withTransaction(() -> Fruit.<Fruit> findById(id).onItem().ifNotNull().invoke(entity -> entity.name = fruit.name))
				.onItem().ifNotNull().transform(ent -> Response.ok(ent).build()).onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND).build());
				
	}

}
