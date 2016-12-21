
package webservices;

import com.google.gson.Gson;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.POST;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author leona
 */
@Path("/contato")
public class Contato {
    
    
    private static Retorno retorno = new Retorno();
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Contato
     */
    public Contato() {
     
    }
 
    
   
 
    @GET
    @Path("/download")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        Gson gson = new Gson();
        String contatoJSON = gson.toJson(retorno);
        return contatoJSON;
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.APPLICATION_JSON)
    public void postJson(@QueryParam("list") String lista) {
       Gson gson = new Gson();
       Retorno retornoFinal = gson.fromJson(lista, Retorno.class);
       retorno.contatos.clear();
       retorno.contatos.addAll(retornoFinal.contatos);
    }
}
