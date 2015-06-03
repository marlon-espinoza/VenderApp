package com.example.usuario.venderapp.DataBase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by USUARIO on 28-abr-15.
 */
public class MyConnection {
    private final String SERVIDOR="192.168.1.163";
    private final String PUERTO="20015";
    private final String DB="MedusaEv";
    private final String USER ="u_aminweb";
    private final String PASSWORD = "U_Admin2014Web";
    private final String URL = "jdbc:jtds:sqlserver://"+SERVIDOR+":"+PUERTO+";instanceName=medusa_webqa"+";databaseName="+DB
            +";user="+USER+";password="+PASSWORD;

    private boolean active = false;

    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cs=null;

    public ResultSet consulta(String query){
        try {
            rs = st.executeQuery(query);

            //System.out.println(rs.getString(1));
            //System.out.println(st.execute(query));

/*
                if (rs.next()) {
                    System.out.println(rs.getString(1));
                }
*/
        } catch (Exception ex) {
            Logger lgr = Logger.getLogger(MyConnection.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            rs=null;
            System.out.println("murio aqui por "+ex.toString());
        }
        return rs;
    }
    public int login(String user,String pass){
        int resultado=0;
        try{
            cs=con.prepareCall("{? = call dbo.UsuarioTablet(?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);

            cs.setString(2,user);
            cs.setString(3,pass);
            cs.setInt(4,resultado);
            cs.execute();
            System.out.println("Return:  "+resultado);
            System.out.println("Return2:  "+cs.getInt(1));
            resultado=cs.getInt(1);
            cs.close();
        }catch (Exception ex) {
            Logger lgr = Logger.getLogger(MyConnection.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            try {
                cs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            cs=null;
            System.out.println("murio aqui por "+ex.toString());
        }
        return resultado;
    }
    public boolean getActive(){
        return active;
    }

    public MyConnection(){

                try{
                    System.out.println("aqui llego");
                    System.out.println(URL);
                    //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    Class.forName("net.sourceforge.jtds.jdbc.Driver");
                    System.out.println("ojo");
                    con = DriverManager.getConnection(URL);
                    System.out.println("aqui aquedo");
                    st = con.createStatement();
                    active=true;


                }catch(Exception e){
                    System.out.println("inicio error");
                    System.out.println(e.toString());
                    e.printStackTrace();
                    System.out.println("fin error");
                    active=false;
                    try {
                        if (rs != null) {
                            rs.close();
                        }
                        if (st != null) {
                            st.close();
                        }
                        if (con != null) {
                            con.close();
                        }

                    } catch (SQLException ex) {

                        active=false;
                        Logger lgr = Logger.getLogger(MyConnection.class.getName());
                        lgr.log(Level.WARNING, ex.getMessage(), ex);
                    }
                }
            }






    public void close(){
        active=false;
        try {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }

        } catch (Exception ex) {
            System.out.println(ex.toString());

            active=false;
            Logger lgr = Logger.getLogger(MyConnection.class.getName());
            lgr.log(Level.WARNING, ex.getMessage(), ex);
        }

    }

}
