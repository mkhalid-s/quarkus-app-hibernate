package org.acme.hibernate.orm.panache;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

/**
 * 
 */

@Entity
@Cacheable
public class Fruit extends PanacheEntity {
	
	@Column(length = 50, unique = true)
	public String name;

}
