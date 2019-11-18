/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.fupunb.numerador_FUP.Classes;

//imports para enviar emial
import br.com.fupunb.numerador_FUP.Conexao_BD.Conecta_MySql;
import br.com.fupunb.numerador_FUP.TelasDeInteracao.Tela_Principal;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author marcio
 */
public class Servidor {

    private int id_serv;
    private String nome;
    private String matriculaFUB;
    private String email;
    private String senha;
    private int status_Senha;

    /**
     * @return the id_serv
     */
    public int getId_serv() {
        return id_serv;
    }

    /**
     * @param id_serv the id_serv to set
     */
    public void setId_serv(int id_serv) {
        this.id_serv = id_serv;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the matriculaFUB
     */
    public String getMatriculaFUB() {
        return matriculaFUB;
    }

    /**
     * @param matriculaFUB the matriculaFUB to set
     */
    public void setMatriculaFUB(String matriculaFUB) {
        this.matriculaFUB = matriculaFUB;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @param senha the senha to set
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * @return the status_Senha
     */
    public int getStatus_Senha() {
        return status_Senha;
    }

    /**
     * @param status_Senha the status_Senha to set
     */
    public void setStatus_Senha(int status_Senha) {
        this.status_Senha = status_Senha;
    }

    //MÉTODOS SEM RETORNO
    public void buscarDadosServidor(String matricula, JTextField jTF_NOME, JTextField jTF_EMAIL) throws SQLException {

        //Servidor buscarMatricula = new Servidor();
        //  buscarMatricula.
        setMatriculaFUB(matricula);
        Connection conexao = null;
        try {
            conexao = Conecta_MySql.conectarDB();
            PreparedStatement pstmtFazerBusca = conexao.prepareStatement("SELECT NOME, EMAIL FROM servidor WHERE MATRICULAFUB = ?");
            pstmtFazerBusca.setString(1, getMatriculaFUB());
            ResultSet rs = pstmtFazerBusca.executeQuery();
            while (rs.next()) {
                jTF_NOME.setText(rs.getString("NOME"));
                jTF_EMAIL.setText(rs.getString("EMAIL"));
            }
        } catch (IOException ex) {
            System.out.println("falha na conexão: " + ex);
        } finally {
            if (conexao != null) {
                conexao.close();
                System.out.println("banco desconectado");
            }
        }

    }

    public void cadastrarSenha(String jPasswordF_ESCOLHER_SENHA, String matriculaFUB) throws ClassNotFoundException, SQLException, Exception {

        //recebendo parametros
        setMatriculaFUB(matriculaFUB);
        setSenha(jPasswordF_ESCOLHER_SENHA);
        setStatus_Senha(1);
        //atualizando banco de dados   
        Connection conexao = Conecta_MySql.conectarDB();
        try {
            PreparedStatement executaCadastro = conexao.prepareStatement("UPDATE servidor SET SENHA = ?, STATUSSENHA = ? WHERE MATRICULAFUB = ?");
            executaCadastro.setString(1, getSenha());
            executaCadastro.setInt(2, getStatus_Senha());
            executaCadastro.setString(3, getMatriculaFUB());
            executaCadastro.executeUpdate();
        } finally {
            if (conexao != null) {
                conexao.close();
                System.out.println("Banco Desconectado!");
            }
        }

    }

    //ENVIAR O E-MAIL COM A SENHA
    public void enviarSenhaPorEmail(String emailServidor, String senhaServidor, String nomedoServidor) {

        Properties props = new Properties();
        /**
         * Parâmetros de conexão com servidor Gmail
         */
        //props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.live.com");
        props.put("mail.smtp.socketFactory.port", "587");
        //props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String email = "numerador_docs@hotmail.com";
                String senha = "gpfup2018";
                return new PasswordAuthentication(email, senha);
            }
        });

        /**
         * Ativa Debug para sessão
         */
        session.setDebug(true);

        try {
            String remetente = "numerador_docs@hotmail.com";
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remetente)); //Remetente

            Address[] toUser = InternetAddress //Destinatário(s)
                    .parse(emailServidor);

            message.setRecipients(Message.RecipientType.TO, toUser);
            message.setSubject("não-responda-Numerador-Documentos");//Assunto
            message.setText(nomedoServidor + ",\nEsta é a senha que o(a) Sr(a). cadastrou para acessar o Numerador de Documentos da FUP:  " + senhaServidor);
            /**
             * Método para enviar a mensagem criada
             */
            Transport.send(message);

            System.out.println("Feito!!!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    //MÉTODOS COM RETORNO
    public boolean verificarStatusSenha(int status) {
        return status == 1;
    }

    public boolean verificarAcessoUsuario(String matricula, String senha) throws IOException, SQLException {

        setMatriculaFUB(matricula);
        setSenha(senha);
        String buscarInformacoesLogin = "SELECT SENHA FROM servidor WHERE MATRICULAFUB = ?";
        String conferirSenha = "";
        Connection conexao = Conecta_MySql.conectarDB();
        try {
            PreparedStatement verificarLogin = conexao.prepareStatement(buscarInformacoesLogin);
            verificarLogin.setString(1, matricula);
            ResultSet rs = verificarLogin.executeQuery();
            while (rs.next()) {

                conferirSenha = rs.getString("SENHA");
            }
        } finally {
            if (conexao != null) {
                conexao.close();
                System.out.println("Banco desconectado");
            }
        }

        return (!"".equalsIgnoreCase(conferirSenha)) && (getSenha().equalsIgnoreCase(conferirSenha));

    }

    public boolean verificarServidorExisteCadastro(String matriculaFUB) throws IOException, SQLException {
        setMatriculaFUB(matriculaFUB);
        boolean matricula = false;
        Connection conexao = Conecta_MySql.conectarDB();
        try {
            PreparedStatement cadastrarServidor = conexao.prepareStatement("SELECT MATRICULAFUB FROM servidor WHERE MATRICULAFUB = ?");
            cadastrarServidor.setString(1, getMatriculaFUB());
            ResultSet rs = cadastrarServidor.executeQuery();
            while (rs.next()) {
                if (rs.getString("MATRICULAFUB").equals(getMatriculaFUB())) {

                    matricula = true;

                } else {
                    matricula = false;
                }
            }
        } finally {
            if (conexao != null) {
                conexao.close();
                System.out.println("Banco Desconectado!");
            }
        }

        return matricula;
    }

    public void cadastroServidor(String nome, String matriculaFUB, String email) throws IOException, SQLException {
        setNome(nome);
        setMatriculaFUB(matriculaFUB);
        setEmail(email);
        setStatus_Senha(0);
        Connection conexao = null;
        try {

            if (verificarServidorExisteCadastro(getMatriculaFUB())) {

                int resposta = JOptionPane.showConfirmDialog(null, "ESSA MATRIUCLA/SERVIDOR JÁ CONSTA NO CADASTRO.\nDESEJA "
                        + "SUBSTITUIR OS DADOS JÁ EXISTENTES?");
                switch (resposta) {

                    case 0:
                        conexao = Conecta_MySql.conectarDB();
                        PreparedStatement alterarCadastroServidor = conexao.prepareStatement("UPDATE servidor SET NOME = ?, EMAIL = ? WHERE MATRICULAFUB = ?");
                        alterarCadastroServidor.setString(1, getNome());
                        alterarCadastroServidor.setString(2, getEmail());
                        alterarCadastroServidor.setString(3, getMatriculaFUB());
                        alterarCadastroServidor.executeUpdate();
                        JOptionPane.showMessageDialog(null, "CADASTRO ALTERADO COM SUCESSO");
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "O CADASTRO NÃO FOI ALTERADO");
                        break;
                    case 2:
                        break;
                }

            } else {
                conexao = Conecta_MySql.conectarDB();
                PreparedStatement cadastrarServidor = conexao.prepareStatement("INSERT INTO servidor(NOME,MATRICULAFUB,EMAIL,STATUSSENHA) VALUES (?,?,?,?)");
                cadastrarServidor.setString(1, getNome());
                cadastrarServidor.setString(2, getMatriculaFUB());
                cadastrarServidor.setString(3, getEmail());
                cadastrarServidor.setInt(4, getStatus_Senha());
                cadastrarServidor.executeUpdate();
                JOptionPane.showMessageDialog(null, "USUÁRIO CADASTRADO COM SUCESSO!");
            }
        } finally {
            if (conexao != null) {
                conexao.close();
                System.out.println("Banco Desconectado!");
            }
        }

    }

}
