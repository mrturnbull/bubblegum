package com.dunedin.questionario;

import javax.ejb.Local;
import java.util.List;

@Local
public interface PerguntaManager{

	public Pergunta retrieveProximaPergunta(long usuarioId);
	public List<Pergunta> findAll();
	public Pergunta findPerId(int perguntaId);
	public int addPergunta(String enunciado, int isVisivel);
	public int delPergunta(Pergunta pergunta);

}
