package com.example.usuario.venderapp.DataBase;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

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
    private final String INSTANCE_NAME = "medusa_webqa";
    private final String URL = "jdbc:jtds:sqlserver://"+SERVIDOR+":"
            +PUERTO+";instanceName="+INSTANCE_NAME+";databaseName="
            +DB+";user="+USER+";password="+PASSWORD;

    private boolean active = false;
    Handler pro_handler;
    int progress;

    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    CallableStatement cs=null;

    public ResultSet consulta(String query){
        try {
            rs = st.executeQuery(query);
        } catch (Exception ex) {
            Logger lgr = Logger.getLogger(MyConnection.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            rs=null;
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
        }
        return resultado;
    }
    public boolean getActive(){
        return active;
    }

    public MyConnection(){

                try{
                    //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    Class.forName("net.sourceforge.jtds.jdbc.Driver");
                    con = DriverManager.getConnection(URL);
                    st = con.createStatement();
                    active=true;
                }catch(Exception e){
                    System.out.println(e.toString());
                    e.printStackTrace();
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



    public void actualizarDatos(Context context, String user,ProgressDialog progress) {

        ResultSet rs=null;

        String q_proyectos=  "Select distinct codigo_proyecto,proyecto_libres from dbo.vw_user_proy where usuario='&PV_USER&' and"+
                " codigo_urbanizacion='2';";
        String q_lotes= "Select codigo_lote,codigo_proyecto,manzana,lote,estado_construccion,area_terreno,plazo_entrada,plazo_entrega,vender_como "+
                "from dbo.vw_lotes_app where codigo_urbanizacion='2'";
        String q_modelos = "Select * from dbo.vw_modelos_en_lot_app m "+
                " inner join dbo.vw_lotes_app l on l.codigo_lote = m.codigo_lote AND l.vender_como='&PV_VENDER_COMO&'"+
                " where m.codigo_lote='&PV_LOTE&' and m.modelo'&PV_SOLAR&'";

        try{

            rs=consulta(q_proyectos.replace("&PV_USER&", user));

            DbProyecto proy;
            proy=new DbProyecto(context);
            while (rs.next()) {
                //insertar(String id,String id_curso,String nombre_curso,String titulo,String contenido,Date fecha,String num_msgs)
                proy.insertar(rs.getString(1),rs.getString(2));
            }
            rs.close();
            rs=null;
            DbLote lote=new DbLote(context);
            DbModelo modelo=new DbModelo(context);
            rs=consulta(q_lotes);
            while (rs.next()) {
                lote.insertar(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9));
            }
            rs.close();
            rs=null;
            try {
                Cursor dato = lote.consultarLotePorProy(null);
                int lotes=dato.getCount();
                double progreso=100.0/lotes;
                double incremento=0;
                if (dato.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        //System.out.println(q_modelos.replace("&PV_LOTE&", dato.getString(0)).replace("&PV_VENDER_COMO&", dato.getString(7)));
                        if (dato.getString(7).equals("1"))
                            rs=consulta(q_modelos.replace("&PV_LOTE&", dato.getString(0)).replace("&PV_VENDER_COMO&", dato.getString(7)).replace("'&PV_SOLAR&'", "<> 'SOLAR'"));

                        else
                            rs=consulta(q_modelos.replace("&PV_LOTE&", dato.getString(0)).replace("&PV_VENDER_COMO&", dato.getString(7)).replace("'&PV_SOLAR&'", "= 'SOLAR'"));
                        while (rs.next()) {

                            //db.insert(NOMBRE_TABLA,null,generarContentValues(modelo,id_lote,area,pisos,cuota_ent,cuota_ini,tasa,plazo1,
                            //plazo2,plazo3,precio,img_fach,img_pb,img_pa1,img_pa2));
                            modelo.insertar(rs.getString(2),rs.getString(1)+ dato.getString(7),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),
                                    rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13)
                                    ,rs.getString(14),rs.getString(15));

                        }
                        incremento+=progreso;
                        if(incremento>=1.0&&incremento<1.5){
                            progress.incrementProgressBy(1);
                            incremento-=1.0;
                        }
                        else if(incremento>=1.5){
                            progress.incrementProgressBy(2);
                            incremento=-1.5;
                        }
                        rs.close();
                        rs=null;

                    } while(dato.moveToNext());
                    progress.incrementProgressBy(100-progress.getProgress());
                }

            }catch (Exception e){
                System.out.println(e.toString());
            }



        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {

                if(con!=null)
                    con.close();
                if(rs!=null)
                    rs.close();
            } catch (SQLException ex) {
                //Logger.getLogger(consulta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
