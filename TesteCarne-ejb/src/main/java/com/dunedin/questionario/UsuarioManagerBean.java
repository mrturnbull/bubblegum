package com.dunedin.questionario;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Stateless
public class UsuarioManagerBean implements UsuarioManager {

	@PersistenceContext(unitName="TesteCarneKinghost")
	private EntityManager em;
	
	public UsuarioManagerBean(){};

	//@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public long addUsuario(String nome, String email){

		long ret = 0; //Significa que o usu�rio n�o foi adicionado
		
		if (findByEmail(email) == null){
			
			Usuario usuario = new Usuario();
			usuario.setEmail(email);
			usuario.setNome(nome);

			em.persist(usuario);
			em.flush(); 				//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<????????

			ret = usuario.getId();

		}
		
		return ret;

	}

	public Usuario findByEmail(String email){
		
		Usuario u;
		
		try {
			u = (Usuario) em.createNamedQuery("Usuario.findByEmail").setParameter("email", email).getSingleResult();
			return u;
		}
		catch(NoResultException nre){
			return null;	
		}
	}

	public Usuario findById(long usuarioId){
		
		Usuario u;
		
		try {
			u = (Usuario) em.createNamedQuery("Usuario.findById").setParameter("id", usuarioId).getSingleResult();
			return u;
		}
		catch(NoResultException nre){
			return null;	
		}
	}

}
