package dao.impl;

import dao.interfaces.ClienteDAO;
import model.Cliente;
import model.ClientePremium;
import model.ClienteStandar;
import util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public Cliente getClientePorEmail(String email) throws SQLException {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareCall("{CALL sp_getClienteByEmail(?)}")) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Leemos el tipo (ENUM: 'ESTANDAR' o 'PREMIUM')
                String tipo = rs.getString("tipo");

                if ("PREMIUM".equalsIgnoreCase(tipo)) {
                    return new ClientePremium(
                            rs.getString("email"),
                            rs.getString("nombre"),
                            rs.getString("domicilio"),
                            rs.getString("nif"),
                            rs.getDouble("descuento_envio"),
                            rs.getInt("cuota_anual")
                    );
                } else {
                    return new ClienteStandar(
                            rs.getString("email"),
                            rs.getString("nombre"),
                            rs.getString("domicilio"),
                            rs.getString("nif"),
                            rs.getDouble("descuento_envio")
                    );
                }
            }

            // Si no se encontró nada
            System.out.println("No hay ningún cliente registrado con este email");
            return null;

        } catch (Exception e) {
            throw e; // repropagamos el error
        }
    }



    @Override
    public List<Cliente> getTodosLosClientes() throws SQLException {
        List<Cliente> lista = new ArrayList<>();

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareCall("{CALL sp_getAllClientes()}")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                boolean premium = rs.getBoolean("premium");
                if (premium) {
                    lista.add(new ClientePremium(
                            rs.getString("email"),
                            rs.getString("nombre"),
                            rs.getString("domicilio"),
                            rs.getString("nif"),
                            rs.getDouble("descuento_envio"),
                            rs.getInt("cuota_anual")
                    ));
                } else {
                    lista.add(new ClienteStandar(
                            rs.getString("email"),
                            rs.getString("nombre"),
                            rs.getString("domicilio"),
                            rs.getString("nif"),
                            rs.getDouble("descuento_envio")
                    ));
                }
            }

        }

        return lista;
    }

    @Override
    public List<Cliente> getClientesEstandar() throws SQLException {
        List<Cliente> lista = new ArrayList<>();

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareCall("{CALL sp_getclientesEstandar()}")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new ClienteStandar(
                        rs.getString("email"),
                        rs.getString("nombre"),
                        rs.getString("domicilio"),
                        rs.getString("nif"),
                        rs.getDouble("descuento_envio")
                ));
            }
        }
        return lista;
    }

    @Override
    public List<Cliente> getClientesPremium() throws SQLException {
        List<Cliente> lista = new ArrayList<>();

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareCall("{CALL sp_getClientesPremium()}")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new ClientePremium(
                        rs.getString("email"),
                        rs.getString("nombre"),
                        rs.getString("domicilio"),
                        rs.getString("nif"),
                        rs.getDouble("descuento_envio"),
                        rs.getInt("cuota_anual")
                ));
            }
        }
        return lista;
    }

    @Override
    public void anadirCliente(Cliente cliente) throws SQLException {
        Connection conn = null; //se inicia fuera para el try y finally

        try{
            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false); //iniciar la transacción
            try(CallableStatement cs = conn.prepareCall("{CALL sp_addCliente(?,?,?,?,?,?,?)}")) {
                cs.setString(1, cliente.getEmail());
                cs.setString(2, cliente.getNombre());
                cs.setString(3, cliente.getDomicilio());
                cs.setString(4, cliente.getNIF());

                if (cliente instanceof ClientePremium) {
                    cs.setString(5, "PREMIUM");
                    cs.setDouble(6, ((ClientePremium) cliente).getDescuentoEnvio());
                    cs.setDouble(7, ((ClientePremium) cliente).getCuotaAnual());

                } else {
                    cs.setString(5, "ESTANDAR");
                    cs.setNull(6, Types.DECIMAL);
                    cs.setNull(7, Types.DECIMAL);
                }

                cs.executeUpdate();
            }
                conn.commit();// confirma la transacción si todo va bien

        } catch (SQLException e) {
            if (conn != null){
                try {
                    conn.rollback(); // se deshace la transacicón si algo falla
                }catch (SQLException ex ) {
                    System.err.println("Error al hacer rollback: "+ ex.getMessage());
                }
            }
            throw e; // lanza erro al controlador
        }finally {
                if (conn != null){
                    try{
                        conn.close();
                    }catch(SQLException e){
                        System.err.println("Error al cerrar la conexión: " + e.getMessage());
                    }
                }
            }

    }
}
