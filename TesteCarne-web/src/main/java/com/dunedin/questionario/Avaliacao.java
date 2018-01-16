package com.dunedin.questionario;

import alice.tuprolog.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import javax.inject.Inject;

public class Avaliacao {

	//private String aRespostasProlog[] = null;
	private Struct aFatos[] = null;
	Var varX = null;
	List<Struct> lstrisco;
	List<RegraProlog> listaRegras;
	
	public Avaliacao(String aRespostasProlog[], List<RegraProlog> listaRegras){
	
		varX = new Var("X");
		this.listaRegras = listaRegras;
	
		//this.aRespostasProlog = aRespostasProlog;
		aFatos = new Struct[aRespostasProlog.length];
		System.out.println("***************************");
		for (int i=0; i < aRespostasProlog.length; i++){
			aFatos[i] = new Struct(aRespostasProlog[i], varX); //cor
			System.out.println(aRespostasProlog[i]);
		}
		
	}
	
	private List<Struct> loadRules(Struct st){
	 
		String[] cols = null;
		List<Struct> list = new ArrayList<Struct>();
		InputStream is = null;
	
		try {
			
			for (RegraProlog regra : listaRegras) {
				
				cols = regra.getPalavra().split(",");
		
				if (cols.length > 1){
					list.addAll(ANDFacts(st, cols));
				}
				else {
					list.addAll(ORFacts(st, cols));
				} 			
				    
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally {
	        if (is != null) {
	            try {
	                is.close();
	            } catch (Exception e) {
	                // ignore
	            }
	        }
	    }

		
		return list;
	
	}
	
	
	private List<Struct> ANDFacts(Struct st, String[] factNames){
	
		String op = ",";
		int i = 1;
		Struct stfirst = null;
		List<Struct> list = new ArrayList<Struct>();
		
		for (i = 1; i < factNames.length + 1; i++){
		
			if (i == factNames.length){
				op = ":-";
				list.add(new Struct(op, st, stfirst));
			}
			else {
				op = ",";
				stfirst = new Struct(op, new Struct(factNames[i], varX), new Struct(factNames[i - 1], varX));
			}
			
		}
		
		return list;
	
	}
	
	
	private List<Struct> ORFacts(Struct st, String[] factNames){
	
	
		String op = ":-";
		int i = 1;
		List<Struct> list = new ArrayList<Struct>();
		
		for (i = 1; i < factNames.length + 1; i++){
			list.add(new Struct(op, st, new Struct(factNames[i - 1], varX)));
		}
		
		return list;
	
	}
	
	public boolean assess() throws InvalidTheoryException, MalformedGoalException, NoSolutionException, NoMoreSolutionException {
		
		Struct clauseList = new Struct();
		
		Prolog engine = new Prolog();
		
		Struct strisco = new Struct("risco", varX);

		lstrisco = loadRules(strisco);
		
		for (Struct elrisco : lstrisco){
			clauseList = new Struct(elrisco, clauseList);
		}
		
		for (int i = aFatos.length - 1; i >= 0; i--){
			clauseList = new Struct(aFatos[i], clauseList);
		}
				
		Theory t = new Theory(clauseList);
		engine.addTheory(t);
		SolveInfo info = engine.solve("risco(X).");
		
		return info.isSuccess();
		
		
	}
}