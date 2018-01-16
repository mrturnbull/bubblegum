package com.dunedin.questionario;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class RegraPrologManagerBean implements RegraPrologManager {

	@PersistenceContext(unitName="TesteCarneKinghost")
	private EntityManager em;
	
	public RegraPrologManagerBean(){};

	public List<RegraProlog> retrieveAllRules(){
		
		try {
			//@SuppressWarnings("unchecked")
			return em.createQuery("SELECT rp FROM RegraProlog rp").getResultList();
			
		}
		catch(NoResultException nre){
			return null;	
		}
	}

	

}

