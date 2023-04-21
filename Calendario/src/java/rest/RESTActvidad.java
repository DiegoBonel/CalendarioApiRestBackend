
package rest;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import core.ControllerEvento;
import model.Evento;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("evento")
public class RESTActvidad {
    @POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosEvento") @DefaultValue("") String datosEvento) {
        String out = null;
        Gson gson = new Gson();
        Evento evento = null;
        ControllerEvento cu = new ControllerEvento();
        System.out.println(datosEvento);

        try {
            evento = gson.fromJson(datosEvento, Evento.class);
            if (evento.getIdEvento()== 0) {
                cu.insert(evento);
            } else {
                cu.update(evento);
            }
            
            out = Integer.toString(evento.getIdEvento());
        } catch (JsonParseException jpe) {
            jpe.printStackTrace();
            out = "{\"exception\":\"Error en los datos introducidos o en el formato.\"}";
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }

    @POST
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@FormParam("fecha") @DefaultValue("") String fecha) {
        String out = null;
        ControllerEvento ce = null;
        List<Evento> eventos = null;
        try {
            ce = new ControllerEvento();
            eventos = ce.getAll(fecha);
            out = new Gson().toJson(eventos);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @POST
    @Path("eliminarEvento")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarEvento(@FormParam("idEvento") @DefaultValue("") String idEvento){
        String out = null;
        ControllerEvento ce = null;
        try{
            ce = new ControllerEvento();
            ce.eliminarEvento(idEvento);
            out = "{\"correcto\":\"Eliminacion correcta.\"}";
        } catch(Exception e){
            e.printStackTrace();
            out = "{\"exception\":\"Error interno del servidor.\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
}
