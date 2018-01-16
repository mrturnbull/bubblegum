package com.dunedin.questionario;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Stateless
public class RespostaManagerBean implements RespostaManager {

	@PersistenceContext(unitName="TesteCarneKinghost")
	private EntityManager em;

	public RespostaManagerBean(){};

	public int addResposta(long usuarioId, int perguntaId, int respostaId){
		  
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  
		Resposta resposta = new Resposta();
		
		resposta.setUsuarioId(usuarioId);
		resposta.setPerguntaId(perguntaId);
		resposta.setRespostaId(respostaId);
		resposta.setDataInicio(sdf.format(cal.getTime()));
		//resposta.setDataFim("2017-09-16");
		
		em.persist(resposta);
		em.flush(); 				//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<????????
		
		return 1;
	
	}


	public List<String> retrieveRespostasProlog(long usuarioId){

	    String sql = "";
	
	    sql += " SELECT pa.prolog";
	    sql += " FROM PerguntaEnunciado pe";
	    sql += " INNER JOIN PerguntaAlternativa pa ON pe.PerguntaEnunciado_ID = pa.Pergunta_ID";
	    sql += " INNER JOIN Usuario_Resposta ur ON pa.Alternativa_Pergunta_ID = ur.Resposta_ID";
	    sql += " WHERE ur.Usuario_Id = :usuarioId";
	    sql += " AND pa.Alternativa_Pergunta_Id = (";
	    sql += "      SELECT ur2.Resposta_ID";
	    sql += " 	  FROM Usuario_Resposta ur2, PerguntaEnunciado pe2";
	    sql += " 	  WHERE ur2.Usuario_Id = :usuarioId AND ur2.Pergunta_Id = pe2.PerguntaEnunciado_ID AND pe2.PerguntaEnunciado_ID = pe.PerguntaEnunciado_ID";
	    sql += " 	    AND ur2.Data_Fim IS NULL";
	    sql += "	  LIMIT 1";	
	    sql += " )";
	    sql += " GROUP BY pa.prolog";
	    sql += " HAVING pa.prolog IS NOT NULL";
	
	    try {
	    	
		    	@SuppressWarnings("unchecked")
		    	List<String> listaProlog = (List<String>) em.createNativeQuery(sql).setParameter("usuarioId", usuarioId).getResultList();

		    	return listaProlog;

		}
		catch(NoResultException nre){
			return null;	
		}

	}


	public boolean darBaixa(long usuarioId){
		
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String sql = "";
		boolean ret = false;
		
		sql += " UPDATE Usuario_Resposta";
		sql += " SET Data_Fim = '" + sdf.format(cal.getTime()) + "'";
		sql += " WHERE Usuario_Id = " + usuarioId;
		sql += "   AND Data_Fim IS NULL";
		
		try {
			em.createNativeQuery(sql).executeUpdate();
			ret = true;
		}
		catch(Exception e) {
			ret = false;
		}
		
		return ret;
		
	}
	
	////////////////////////////////////////////////////////////////////////
	//
	// BACKOFFICE - 
	//
	//
	////////////////////////////////////////////////////////////////////////
	public List<AuxResposta> bkRetrieveRespostasPerEmail(String email){

	    String sql = "";
	
	    sql += " SELECT u.Usuario_Id AS UsuarioId, pe.Enunciado, pa.Descricao, ur.Data_Inicio AS DataInicio, ur.Data_Fim AS DataFim";
	    sql += " FROM Usuario_Resposta ur";
	    sql += " INNER JOIN Usuario u ON ur.Usuario_id = u.Usuario_id";
	    sql += " INNER JOIN PerguntaEnunciado pe ON ur.Pergunta_id = pe.PerguntaEnunciado_id";
	    sql += " INNER JOIN PerguntaAlternativa pa ON ur.Pergunta_id = pa.Pergunta_id AND ur.Resposta_id = pa.Alternativa_pergunta_id";
	    sql += " WHERE u.Email = :email";
	    sql += " ORDER BY ur.Data_Inicio, ur.Pergunta_ID";
	
	    try {
	    	
		    	@SuppressWarnings("unchecked")
		    	List<AuxResposta> lista = (List<AuxResposta>) em.createNativeQuery(sql).setParameter("email", email).getResultList();

		    	return lista;

		}
		catch(NoResultException nre){
			return null;	
		}

	}

}
