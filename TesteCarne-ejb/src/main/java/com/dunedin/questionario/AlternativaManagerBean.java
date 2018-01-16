package com.dunedin.questionario;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.QueryHint;
import javax.persistence.StoredProcedureQuery;


@Stateless
public class AlternativaManagerBean implements AlternativaManager {
	
	@PersistenceContext(unitName="TesteCarneKinghost")
	private EntityManager em;

	public AlternativaManagerBean(){};
	
	public List<Alternativa> findAllPerPerguntaId(int perguntaId){
		
		List<Alternativa> u;
		
		try {
			u = (List<Alternativa>) em.createNamedQuery("Alternativa.findAllPerPerguntaId").setParameter("perguntaId", perguntaId).getResultList();
			//u = (List<Alternativa>) em.createNamedQuery("Alternativa.findAllPerPerguntaId").getResultList();
			return u;
		}
		catch(NoResultException nre){
			return null;	
		}
	}
	
	public Alternativa findAlternativaPerId(int perguntaId, int alternativaId){
		
		Alternativa u;
		
		try {
			u = (Alternativa) em.createNamedQuery("Alternativa.findAlternativaPerId").setParameter("perguntaId", perguntaId).setParameter("alternativaId", alternativaId).getSingleResult();
			//u = (List<Alternativa>) em.createNamedQuery("Alternativa.findAllPerPerguntaId").getResultList();
			return u;
		}
		catch(NoResultException nre){
			return null;	
		}
	}
	
	public int getMaxAlternativaId(int perguntaId) {
		
		try {
		
			StoredProcedureQuery query =
			        em.createStoredProcedureQuery("GETMAXALTERNATIVAID")
			        .registerStoredProcedureParameter(
			            "perguntaid", Integer.class, ParameterMode.IN)
			        .setParameter("perguntaid", perguntaId);
			
			//query.execute();
			Object ret = query.getSingleResult();
			return (ret != null) ? (int) ret : 0;
		}
		catch(NoResultException nre){
			nre.printStackTrace();
			return 0;	
		}
		
		
	}
	
	public int addAlternativa(int perguntaId, String descricao){
		  
	  Alternativa alternativa = new Alternativa();
	
	  alternativa.setPerguntaId(perguntaId);
	  alternativa.setId(getMaxAlternativaId(perguntaId) + 1);
	  alternativa.setDescricao(descricao);
	  alternativa.setProlog(null);
	  alternativa.setVisivel(1);
	
	  em.persist(alternativa);
	  em.flush(); 				//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<????????
	 
	  return 1;
		
	}
	
	public int delAlternativa(Alternativa alternativa){
		  
	  em.remove(em.merge(alternativa));
	  em.flush(); 				//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<????????
	
	  return 1;

	}


}

