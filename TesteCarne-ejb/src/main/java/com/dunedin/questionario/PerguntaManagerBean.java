package com.dunedin.questionario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.QueryHint;


@Stateless
public class PerguntaManagerBean implements PerguntaManager {

	Pergunta pergunta = null;
	
	@PersistenceContext(unitName="TesteCarneKinghost")
	private EntityManager em;

	public PerguntaManagerBean(){};
	
	public int delPergunta(Pergunta pergunta){
		  
		  em.remove(em.merge(pergunta));
		  em.flush(); 				//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<????????
		
		  return 1;

	}
	
	public int addPergunta(String enunciado, int isVisivel){
	  
	  Pergunta pergunta = new Pergunta();
	
	  pergunta.setEnunciado(enunciado);
	  pergunta.setTipo(0);
	  pergunta.setVisivel(isVisivel);
	
	  em.persist(pergunta);
	  em.flush(); 				//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<????????
	
	  return 1;

	}

	public Pergunta retrieveProximaPergunta(long usuarioId){

	    String sql = "";
	
	    sql += " SELECT pe.PerguntaENUNCIADO_ID, pe.Enunciado, pa.Alternativa_Pergunta_ID, pa.Descricao AS AlternativaDescricao";
	    sql += " FROM PerguntaEnunciado pe";
	    sql += " INNER JOIN PerguntaAlternativa pa ON pe.PerguntaEnunciado_ID = pa.Pergunta_ID";
	    sql += " WHERE pe.PerguntaEnunciado_ID = (";
	    sql += "    SELECT pe.PerguntaEnunciado_ID";
	    sql += " 	  FROM PerguntaEnunciado pe";
	    sql += " 	  INNER JOIN PerguntaAlternativa pa ON pe.PerguntaEnunciado_ID = pa.Pergunta_ID";
	    sql += " 	  LEFT OUTER JOIN Usuario_Resposta ur ON pa.Pergunta_ID = ur.Pergunta_ID AND ur.Usuario_Id = :usuarioId";
	    sql += " 	  WHERE ur.Resposta_ID IS NULL AND ur.Data_Fim IS NULL";
	    sql += "    	AND pe.Visivel = 1";
	    sql += " 	  ORDER BY pe.Ordem";
	    sql += " 	  LIMIT 1";
	    sql += " )";
	    sql += " ORDER BY pa.Descricao";
	    
	    //System.out.println("Usuarioid = " + usuarioId);
	    
	    try{
	    	
	    		//em.clear();
	    		pergunta = null;
	    	
		    	@SuppressWarnings("unchecked")
		    	List<Object[]> listaP = (List<Object[]>) em.createNativeQuery(sql).setParameter("usuarioId", usuarioId).getResultList();
		    	
		    	//System.out.println("ListaP size: " + listaP.size());
		    	
		    	if (listaP.size() > 0) {
		    	
			    	List<Alternativa> listaAlternativas = new ArrayList<Alternativa>();
			    	
			    	pergunta = new Pergunta();
			    	
			    for (Object[] p : listaP){
			          
			        pergunta.setId((int) p[0]);
			        pergunta.setEnunciado((String) p[1]);
			        
			        Alternativa alternativa = new Alternativa();
			        alternativa.setId((int) p[2]);
			        alternativa.setDescricao((String) p[3]);
			        
			        listaAlternativas.add(alternativa);
			
			    }
			    
			    pergunta.setAlternativas(listaAlternativas);
			    
			    //em.clear();
		    
		    	}
		    
		    return pergunta;

		}
		catch(NoResultException nre){
			return null;	
		}
		
	}
	
	public List<Pergunta> findAll(){
		
		List<Pergunta> u;
		
		try {
			u = (List<Pergunta>) em.createNamedQuery("PerguntaEnunciado.findAll").getResultList();
			return u;
		}
		catch(NoResultException nre){
			return null;	
		}
	}
	
	public Pergunta findPerId(int perguntaId){
		
		Pergunta u;
		
		try {
			u = (Pergunta) em.createNamedQuery("PerguntaEnunciado.findPerId").setParameter("id", perguntaId).getSingleResult();
			return u;
		}
		catch(NoResultException nre){
			nre.printStackTrace();
			return null;	
		}
	}


}
