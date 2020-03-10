/**
 * 
 */
package net.fluance.app.data.model;

import java.io.Serializable;

public interface BaseEntity extends Serializable {

	Object getId();
	Integer getVersion();
	
}
