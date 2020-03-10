/**
 * 
 */
package net.fluance.app.spring.data.jpa.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.Version;


@SuppressWarnings("serial")
@MappedSuperclass
public class BasicJPABaseEntity extends JPABaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long id;

	@Version
    @Column(name = "version")
    protected Integer version;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
	
}
