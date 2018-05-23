package com.demo.springboot.util.model;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.demo.springboot.enums.Active;
import com.demo.springboot.enums.Deleted;


@MappedSuperclass
public class AbstractModel <ID extends Serializable> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
    private ID id;
	
	@Enumerated(EnumType.ORDINAL)
	private Deleted deleted = Deleted.NO;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate;
	//OneToOne OneWay mapping.
	private Long lastUpdater;
	
	@Enumerated(EnumType.ORDINAL)
	private Active active = Active.YES;
    
	public Active getActive() {
		return active;
	}
	
	public void setActive(Active active) {
		this.active = active;
	}
	
	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}
	
	public void setDeleted(Deleted deleted) {
		this.deleted = deleted;
	}
	
	public Deleted getDeleted() {
		return deleted;
	}
		
	public Date getLastUpdate() {
		return lastUpdate;
	}
	
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	public Long getLastUpdater() {
		return lastUpdater;
	}
	
	public void setLastUpdater(Long lastUpdater) {
		this.lastUpdater = lastUpdater;
	}
}
