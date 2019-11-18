/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.fupunb.numerador_FUP.Conexao_BD;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author marcio
 */
public class Conecta_MySql {

    Connection conexao;
   
    //BANCO LOCALHOST
    /*  public static Connection conectarDB() throws IOException, SQLException {
        InputStream is = Conecta_MySql.class.getClassLoader().getResourceAsStream("application.properties");
        if (is == null) {
            throw new FileNotFoundException("O ARQUIVO DE CONEXÃO NÃO FOI LOCALIZADO!");
        }
        Properties props = new Properties();
        props.load(is);
        Connection conexao = DriverManager.getConnection(props.getProperty("urlConexaoLocal"), props.getProperty("usuarioConexaoLocal"),
                props.getProperty("senhaConexaoLocal"));
        System.out.println("Banco Conectado");
        return conexao;

    }*/
    
    //BANCO AJAX
  public static Connection conectarDB() throws IOException, SQLException {
        InputStream is = Conecta_MySql.class.getClassLoader().getResourceAsStream("application.properties");
        if (is == null) {
            throw new FileNotFoundException("O ARQUIVO DE CONEXÃO NÃO FOI LOCALIZADO!");
        }
        Properties props = new Properties();
        props.load(is);
        Connection conexao = DriverManager.getConnection(props.getProperty("ConexaoMYSQL"), props.getProperty("usuarioConexaoMYSQL"),
                props.getProperty("senhaConexaoMYSQL"));
        System.out.println("Banco Conectado");
        return conexao;

    }
     
    //BANCO PASTA EM REDE
    /*  public static Connection conectarDB() throws IOException, SQLException {
        InputStream is = Conecta_MySql.class.getClassLoader().getResourceAsStream("application.properties");
        if (is == null) {
            throw new FileNotFoundException("O ARQUIVO DE CONEXÃO NÃO FOI LOCALIZADO!");
        }
        Properties props = new Properties();
        props.load(is);
        Connection conexao = DriverManager.getConnection(props.getProperty("urlConexao"), props.getProperty("usuarioConexao"),
                props.getProperty("senhaConexao"));
        System.out.println("Banco Conectado");
        return conexao;

    }*/
     
     

}//fim clase Conecta_MySql
