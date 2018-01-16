package com.dunedin.questionario;

import javax.ejb.Local;
import java.util.List;

@Local
public interface AlternativaManager{

	public List<Alternativa> findAllPerPerguntaId(int perguntaId);
	public Alternativa findAlternativaPerId(int perguntaId, int alternativaId);
	public int addAlternativa(int perguntaId, String descricao);
	public int delAlternativa(Alternativa alternativa);
	public int getMaxAlternativaId(int perguntaId);

}

