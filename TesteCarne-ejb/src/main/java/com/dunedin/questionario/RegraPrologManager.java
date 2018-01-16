package com.dunedin.questionario;

import javax.ejb.Local;
import java.util.List;

@Local
public interface RegraPrologManager {
	
	public List<RegraProlog> retrieveAllRules();
	
}