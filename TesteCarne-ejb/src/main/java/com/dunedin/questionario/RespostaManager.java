package com.dunedin.questionario;

import java.util.List;
import javax.ejb.Local;

@Local
public interface RespostaManager{

	public int addResposta(long usuarioId, int perguntaId, int respostaId);
	public List<String> retrieveRespostasProlog(long usuarioId);
	public List<AuxResposta> bkRetrieveRespostasPerEmail(String email);
	public boolean darBaixa(long usuarioId);

}
