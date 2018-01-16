package com.dunedin.questionario;

import java.io.Serializable;

public class AlternativaPK implements Serializable {
	
	private int id;
	private int perguntaId;
	
	public AlternativaPK() {}
	
	public boolean equals(Object other) {
		
		if (other instanceof AlternativaPK) {
			
			final AlternativaPK otherAlternativaPK = (AlternativaPK) other;
			
			return (otherAlternativaPK.id == id && otherAlternativaPK.perguntaId == perguntaId);
			
			
		}
		
		return false;
		
	}
	
	public int hashCode() {
		return super.hashCode();
	}
	

}
