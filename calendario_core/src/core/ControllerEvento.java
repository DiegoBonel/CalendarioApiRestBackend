
package core;

import db.ConexionMySQL;
import model.Evento;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;


public class ControllerEvento {
    public int insert(Evento e) throws SQLException{
        String query = "{call insertarEvento(?, ?, ?, ?, ?, " //Datos del Evento
                + "?)}"; //Datos de retorno
        
        int idEventoGenerado = -1;
        
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(query);
        
        cstmt.setString(1, e.getNombre());
        cstmt.setString(2, e.getFecha());
        cstmt.setString(3, e.getHoraInicio());
        cstmt.setString(4, e.getDescripcion());
        cstmt.setBoolean(5, e.isEstatus());
        cstmt.registerOutParameter(6, Types.INTEGER);
        cstmt.executeUpdate();

        //Recuperamos los ID's generados:
        idEventoGenerado = cstmt.getInt(6);

        e.setIdEvento(idEventoGenerado);
                
        cstmt.close();
        connMySQL.close();

        return idEventoGenerado;
    }
    
    public void update(Evento e) throws SQLException{
        String query = "{call actualizarEvento(?, ?, ?, ?, ?, ?)}"; //Datos del Evento
        
        
        ConexionMySQL connMySQL = new ConexionMySQL();

        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(query);
        
        cstmt.setInt(1, e.getIdEvento());
        cstmt.setString(2, e.getNombre());
        cstmt.setString(3, e.getFecha());
        cstmt.setString(4, e.getHoraInicio());
        cstmt.setString(5, e.getDescripcion());
        cstmt.setBoolean(6, e.isEstatus());
        
        
        cstmt.executeUpdate();

        cstmt.close();
        connMySQL.close();
    }
    
    public List<Evento> getAll(String fecha) throws Exception {
        String query = "SELECT * FROM evento where fecha like '%"+fecha+"%'"; 
        
        ConexionMySQL connMySQL = new ConexionMySQL();

        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(query);

        ResultSet rs = pstmt.executeQuery();
        
        List<Evento> eventos = new ArrayList<>();

        while (rs.next()) {
            eventos.add(fill(rs));
        }

        //Cerramos conexion
        rs.close();
        pstmt.close();
        connMySQL.close();

        return eventos;
    }
    
    public void eliminarEvento(String idEvento) throws SQLException{
        String query = "DELETE FROM evento WHERE idEvento = "+ idEvento;
         
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.execute();

        pstmt.close();
        connMySQL.close();
    }
    
    private Evento fill(ResultSet rs) throws Exception {
        Evento e = new Evento();

        //Le establecemos a persona los valores
        e.setIdEvento(rs.getInt("idEvento"));
        e.setNombre(rs.getString("nombre"));
        e.setFecha(rs.getString("fecha"));
        e.setHoraInicio(rs.getString("horaInicio"));
        e.setDescripcion(rs.getString("descripcion"));
        e.setEstatus(rs.getBoolean("estatus"));

        //Devolvemos Evento
        return e;
    }
}
