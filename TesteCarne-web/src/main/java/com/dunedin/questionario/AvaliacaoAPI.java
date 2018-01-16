package com.dunedin.questionario;

import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path("/")
public class AvaliacaoAPI{
	
	@Inject AlternativaManager am;
	@Inject UsuarioManager um;
	@Inject PerguntaManager pm;
	@Inject RespostaManager rm;
	@Inject RegraPrologManager rpm;
	Avaliacao avaliacao;
	
	@Context
	private UriInfo uriInfo;
	
	@GET
	@Path("/teste")
	public String teste(){
		return uriInfo.getBaseUri().toString();
	}
	
	@POST
	@Produces("text/plain ; charset=UTF-8")
	@Path("/usuarios")
	public String login(@FormParam("nome")  String nome, @FormParam("email") String email){
	//public Response login(@FormParam("nome")  String nome, @FormParam("email") String email){
	
		String strJSON = "";

		long usuarioId = 0;

		if (nome.trim().length() > 0 && email.trim().length() > 0){
			
			//if (nome.trim().length() > 0){
				usuarioId = um.addUsuario(nome, email);
			//}
			
			if (usuarioId > 0){
				strJSON = "{\"usuarioid\": " + usuarioId + "}";
			}
			else if (usuarioId == 0){
				strJSON = "{\"erro\": \"Usuário já cadastrado\"}";
			}

		}
		else if (email.trim().length() > 0){

			Usuario usuario = um.findByEmail(email);

			if (usuario != null){
				strJSON = "{\"usuarioid\": " + usuario.getId() + "}";
			}
			else {
				strJSON = "{\"erro\": \"Usuário não cadastrado\"}";
			}

		}
		
		return strJSON;

	}

	@GET
	@Produces("text/plain ; charset=UTF-8")
	@Path("/perguntas")
	public String showProximaPergunta(@QueryParam("usuarioid")  long usuarioId){

		List<String> respostasProlog = rm.retrieveRespostasProlog(usuarioId);

		Usuario usuario = null;

		Pergunta proxPergunta = null;

		Gson gson = null;

		boolean risco = false;
		boolean baixa = false;

		String ret = "";

		try {

			if (respostasProlog.size() > 0){

				avaliacao = new Avaliacao(respostasProlog.toArray(new String[respostasProlog.size()]), rpm.retrieveAllRules());

				risco = avaliacao.assess();

			}

			if (risco){

				usuario = um.findById(usuarioId);

				baixa = rm.darBaixa(usuarioId);

				ret = "{\"conclusao\": \"" + usuario.getNome() + ", o consumo da sua carne apresenta riscos !\"}";

			}
			else { //Sem risco

				proxPergunta = pm.retrieveProximaPergunta(usuarioId);

				if (proxPergunta != null){

					gson = new Gson();

					ret = gson.toJson(proxPergunta);

				}
				else {

					usuario = um.findById(usuarioId);

					baixa = rm.darBaixa(usuarioId);

					ret = "{\"conclusao\": \"" + usuario.getNome() + ", o consumo da sua carne não apresenta riscos !\"}";

				}

			}

		}
		catch (Exception e){
			e.printStackTrace();
		}

		return ret;

	}

	@POST
	@Produces("text/plain")
	@Path("/perguntas/{perguntaid}/resposta")
	public String enviarResposta(@FormParam("usuarioid") int usuarioId, @PathParam("perguntaid") int perguntaId, @FormParam("respostaid") int respostaId){

		int ret = rm.addResposta(usuarioId, perguntaId, respostaId);

		return "{\"status\":" + ret + "}";

	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// BACKOFFICE
	//
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GET
	@Produces("text/plain ; charset=UTF-8")
	@Path("/backoffice/{email}/respostas")
	public String bkRetrieveRespostasPerEmail(@PathParam("email") String email){

		List<AuxResposta> lista = rm.bkRetrieveRespostasPerEmail(email);
		
		Gson gson = new Gson();

		return gson.toJson(lista);

	}
	
	@GET
	@Produces("text/plain ; charset=UTF-8")
	@Path("/backoffice/perguntas")
	public String bkRetrieveAllPerguntas(){

		List<Pergunta> lista = pm.findAll();
		
		String ret = "[";
		
		for (Pergunta p : lista) {
			ret += "{";
			ret += "\"pergunta_id\":"    + p.getId()        + ",";
			ret += "\"enunciado\":\""    + p.getEnunciado() + "\",";
			ret += "\"ordem\":"          + p.getOrdem()     + ",";
			ret += "\"visivel\":"        + p.getVisivel();
			ret += "},";
		}
		
		ret = ret.substring(0, ret.length() - 1);
		
		ret += "]";
		
		return ret;
		
	}
	
	@GET
	@Produces("text/plain ; charset=UTF-8")
	@Path("/backoffice/perguntas/{perguntaid}")
	public String bkRetrievePerguntas(@PathParam("perguntaid") int perguntaId){

		String ret = "";
		Pergunta p = pm.findPerId(perguntaId);
	
		ret += "[{";
		ret += "\"pergunta_id\":"    + p.getId()        + ",";
		ret += "\"enunciado\":\""    + p.getEnunciado() + "\",";
		ret += "\"ordem\":"          + p.getOrdem()     + ",";
		ret += "\"visivel\":"        + p.getVisivel();
		ret += "}]";

		return ret;
		
	}

	@GET
	@Produces("text/plain ; charset=UTF-8")
	@Path("/backoffice/perguntas/{perguntaid}/alternativas")
	public String bkRetrieveAlternativas(@PathParam("perguntaid") int perguntaId){

		List<Alternativa> la = am.findAllPerPerguntaId(perguntaId);
	
		String ret = "[";
		
		if (la.size() > 0) {
		
			for (Alternativa a : la) {
				ret += "{";
				ret += "\"pergunta_id\":"     + perguntaId        + ",";
				ret += "\"alternativa_id\":"  + a.getId()         + ",";
				ret += "\"descricao\":\""     + a.getDescricao()  + "\",";
				ret += "\"prolog\":\""        + a.getProlog()     + "\",";
				ret += "\"visivel\":"         + a.getVisivel();
				ret += "},";
			}
			
			ret = ret.substring(0, ret.length() - 1);
		
		}
		
		ret += "]";

		return ret;
		
	}
	
	@GET
	@Produces("text/plain ; charset=UTF-8")
	@Path("/backoffice/perguntas/{perguntaId}/alternativas/{alternativaId}")
	public String bkRetrieveAlternativa(@PathParam("perguntaId") int perguntaId, @PathParam("alternativaId") int alternativaId){

		Alternativa a = am.findAlternativaPerId(perguntaId, alternativaId);
	
		String ret = "[";
		
		ret += "{";
		ret += "\"pergunta_id\":"     + perguntaId        + ",";
		ret += "\"alternativa_id\":"  + a.getId()         + ",";
		ret += "\"descricao\":\""     + a.getDescricao()  + "\",";
		ret += "\"prolog\":\""        + a.getProlog()     + "\",";
		ret += "\"visivel\":"         + a.getVisivel();
		ret += "}";
		
		ret += "]";

		return ret;
		
	}
	
	@POST
	@Produces("text/plain")
	@Path("/backoffice/perguntas/")
	public String addPergunta(@FormParam("enunciado") String enunciado){
		
		int ret = 0;
		ret = pm.addPergunta(enunciado, 1);

		return "{\"status\":" + ret + "}";

	}
	
	@DELETE
	@Produces("text/plain")
	@Path("/backoffice/perguntas/{perguntaId}")
	public String delPergunta(@PathParam("perguntaId") int perguntaId){
		
		int ret = 0;
		Pergunta p = pm.findPerId(perguntaId);
		ret = pm.delPergunta(p);

		return "{\"status\":" + ret + "}";

	}
	
	@POST
	@Produces("text/plain")
	@Path("/backoffice/perguntas/{perguntaId}/alternativas")
	public String addAlternativa(@PathParam("perguntaId") int perguntaId, @FormParam("descricao") String descricao){
		
		int ret = 0;
		ret = am.addAlternativa(perguntaId, descricao);

		return "{\"status\":" + ret + "}";

	}
	
	@DELETE
	@Produces("text/plain")
	@Path("/backoffice/perguntas/{perguntaId}/alternativas/{alternativaId}")
	public String delAlternativa(@PathParam("perguntaId") int perguntaId, @PathParam("alternativaId") int alternativaId){
		
		int ret = 0;
		Alternativa a = am.findAlternativaPerId(perguntaId, alternativaId);
		ret = am.delAlternativa(a);

		return "{\"status\":" + ret + "}";

	}

}
