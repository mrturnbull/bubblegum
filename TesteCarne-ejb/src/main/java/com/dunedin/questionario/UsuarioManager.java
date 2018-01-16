package com.dunedin.questionario;

import javax.ejb.Local;

@Local
public interface UsuarioManager{

	public long addUsuario(String nome, String email);
	public Usuario findById(long usuarioId);
	public Usuario findByEmail(String email);

}
