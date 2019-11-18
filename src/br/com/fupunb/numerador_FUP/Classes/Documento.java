/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.fupunb.numerador_FUP.Classes;

import br.com.fupunb.numerador_FUP.Conexao_BD.Conecta_MySql;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

/**
 *
 * @author marcio
 */
public class Documento {

    private int fk_serv_doc;
    private int numero;
    private int tipo;
    private String nomeDOC;
    private String assunto;
    private String remetente;
    private String destinatario;
    private String dataDeEmissao;
    private String anoAtual;
    private String numeroFormatado;

    /**
     * @return the fk_serv_doc
     */
    public int getFk_serv_doc() {
        return fk_serv_doc;
    }

    /**
     * @param fk_serv_doc the fk_serv_doc to set
     */
    public void setFk_serv_doc(int fk_serv_doc) {
        this.fk_serv_doc = fk_serv_doc;
    }

    /**
     * @return the numero
     */
    public int getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }

    /**
     * @return the tipo
     */
    public int getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the nomeDOC
     */
    public String getNomeDOC() {
        return nomeDOC;
    }

    /**
     * @param nomeDOC the nomeDOC to set
     */
    public void setNomeDOC(String nomeDOC) {
        this.nomeDOC = nomeDOC;
    }

    /**
     * @return the assunto
     */
    public String getAssunto() {
        return assunto;
    }

    /**
     * @param assunto the assunto to set
     */
    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    /**
     * @return the remetente
     */
    public String getRemetente() {
        return remetente;
    }

    /**
     * @param remetente the remetente to set
     */
    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    /**
     * @return the destinatario
     */
    public String getDestinatario() {
        return destinatario;
    }

    /**
     * @param destinatario the destinatario to set
     */
    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    /**
     * @return the dataDeEmissao
     */
    public String getDataDeEmissao() {
        return dataDeEmissao;
    }

    /**
     * @param dataDeEmissao the dataDeEmissao to set
     */
    public void setDataDeEmissao(String dataDeEmissao) {
        this.dataDeEmissao = dataDeEmissao;
    }

    /**
     * @return the anoAtual
     */
    public String getAnoAtual() {
        return anoAtual;
    }

    /**
     * @param anoAtual the anoAtual to set
     */
    public void setAnoAtual(String anoAtual) {
        this.anoAtual = anoAtual;
    }

    /**
     * @return the numeroFormatado
     */
    public String getNumeroFormatado() {
        return numeroFormatado;
    }

    /**
     * @param numeroFormatado the numeroFormatado to set
     */
    public void setNumeroFormatado(String numeroFormatado) {
        this.numeroFormatado = numeroFormatado;
    }

    //MÉTODOS COM RETORNO
    public String nomearTipo(int nomeTipo) {

        setTipo(nomeTipo);
        switch (getTipo()) {
            case 1:
                setNomeDOC("CARTA");
                break;
            case 2:
                setNomeDOC("MEMORANDO");
                break;
            case 3:
                setNomeDOC("OFÍCIO");
                break;
            default:
                return null;
        }

        return getNomeDOC();

    }

    public int numeroSemFormatar(int numeroAnterior) {
        return numeroAnterior + 1;
    }

    public int buscarNumeroAnteriorBancoDados(String NOME_DOC) throws IOException, SQLException {

        //buscar no banco de dados na coluna ULTIMO_NUMERO_GERADO
        //cada tipo de documento terá um ultimo número para ser procurado separadamente
        int ultimoNumeroGerado = 0;
        setNomeDOC(NOME_DOC);
        String buscarInformacoesULTIMO_NUMERO_REGISTRADO = "SELECT ULTIMO_NUMERO_REGISTRADO FROM documento WHERE NOME_DOC = ?";
        Connection conexao = Conecta_MySql.conectarDB();
        try {
            PreparedStatement verificarULTIMO_NUMERO_REGISTRADO = conexao.prepareStatement(buscarInformacoesULTIMO_NUMERO_REGISTRADO);
            verificarULTIMO_NUMERO_REGISTRADO.setString(1, getNomeDOC());
            ResultSet rs = verificarULTIMO_NUMERO_REGISTRADO.executeQuery();
            while (rs.next()) {

                ultimoNumeroGerado = rs.getInt("ULTIMO_NUMERO_REGISTRADO");
            }
        } finally {
            if (conexao != null) {
                conexao.close();
            }
        }

        return ultimoNumeroGerado;
    }

    public String numerarDoc(int numeroAnterior) {

        int numeroDoc = numeroAnterior + 1;
        String numeroGerado;
        if (numeroDoc <= 9) {
            numeroGerado = ("0" + "0" + "0" + numeroDoc);
        } else if (numeroDoc <= 99) {
            numeroGerado = ("0" + "0" + numeroDoc);
        } else if (numeroDoc <= 999) {
            numeroGerado = ("0" + numeroDoc);
        } else if (numeroDoc <= 9999) {
            numeroGerado = ("" + numeroDoc);
        } else {
            numeroGerado = "CAPACIDADE MÁXIMA DE DOCUMENTOS JÁ EMITIDA!\n REINICIE A NUMERAÇÃO!";
        }
        Date data = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy");
        return numeroGerado + " / " + formato.format(data);

    }

    public String datarEmissao() {

        //campoData.setEditable(false);
        Date dataSistema = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        String campoData = (formato.format(dataSistema));
        return campoData;
    }

    public String anoVigente() {

        Date anoVigente = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy");
        String ano = (formato.format(anoVigente));
        return ano;

    }

    public boolean consultarAnoVigente() throws IOException, SQLException {

        String anoVigenteTabelaBancoDados = "";
        setAnoAtual(anoVigente());
        String sqlConsulta = "SELECT ANO_VIGENTE FROM documento";
        Connection conexao = Conecta_MySql.conectarDB();
        try {
            PreparedStatement verificarAnoVigente = conexao.prepareStatement(sqlConsulta);
            ResultSet rs = verificarAnoVigente.executeQuery();
            while (rs.next()) {

                anoVigenteTabelaBancoDados = rs.getString("ANO_VIGENTE");
            }
        } finally {
            if (conexao != null) {
                conexao.close();
                System.out.println("conexao finalizada");
            }
        }
        return anoVigenteTabelaBancoDados.equals(getAnoAtual());

    }

    //MÉTODOS SEM RETORNO
    public void enviarEmailComNumeroGerado(String emailUsuario, String NomeUsuario, String tipoDocumento, String numeroGerado, String assunto, String remetenteUser, String destinatario) {

        Properties props = new Properties();
        /**
         * Parâmetros de conexão com servidor Gmail
         */
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true"); 

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String email = "numeradordedocsfup@gmail.com";
                String senha = "gpfup2018";
                return new PasswordAuthentication(email, senha);
            }
        });

        /**
         * Ativa Debug para sessão
         */
        session.setDebug(true);

        try {
            String remetenteEmail = "numeradordedocsfup@gmail.com";
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remetenteEmail)); //Remetente

            Address[] toUser = InternetAddress //Destinatário(s)
                    .parse(emailUsuario);

            message.setRecipients(Message.RecipientType.TO, toUser);
            message.setSubject("não-responda-Numerador-Documentos-oficiais");//Assunto
            message.setText("UM NÚMERO DE DOCUMENTO FOI GERADO:"
                    + "\nNOME DO USUÁRIO: " + NomeUsuario + "\nTIPO DE DOCUMENTO: " + tipoDocumento + ""
                    + "\nNUMERO GERADO: " + numeroGerado + "\nASSUNTO: " + assunto + "\nREMETENTE: " + remetenteUser + ""
                    + "\nDESTINATÁRIO: " + destinatario);
            /**
             * Método para enviar a mensagem criada
             */
            Transport.send(message, toUser);

            System.out.println("Feito!!!");
            JOptionPane.showMessageDialog(null, "FOI ENVIADO UM E-MAIL COM O NÚMERO GERADO PARA " + emailUsuario);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
           
        }
    }

    public void inserirSolicitacaoBancoDados(int fk_id_serv, int ultimoNumeroRegistrado,
            String numeroFormatado, String nomeDoc, String assunto,
            String remetente, String destinatario, String dataEmissao,
            String anoVigente) throws Exception {

        //recebendo parametros
        setFk_serv_doc(fk_id_serv);
        setNumero(ultimoNumeroRegistrado);
        setNumeroFormatado(numeroFormatado);
        setNomeDOC(nomeDoc);
        setAssunto(assunto);
        setRemetente(remetente);
        setDestinatario(destinatario);
        setDataDeEmissao(dataEmissao);
        setAnoAtual(anoVigente);
        // int id = 4;
        //variáveis
        String inserindoDadosBanco = "INSERT INTO documento (id_serv, NUMERO_FORMATADO, NOME_DOC, ASSUNTO, REMETENTE,"
                + "DESTINATARIO, DATA_DE_EMISSAO, ULTIMO_NUMERO_REGISTRADO, ANO_VIGENTE)"
                + "VALUES (?,?,?,?,?,?,?,?,?)";
        //inserindo no banco de dados
        Connection conexao = Conecta_MySql.conectarDB();
        try {
            PreparedStatement inserirDados = conexao.prepareStatement(inserindoDadosBanco);
            // inserirDados.setInt(1, id);
            inserirDados.setInt(1, getFk_serv_doc());
            inserirDados.setString(2, getNumeroFormatado());
            inserirDados.setString(3, getNomeDOC());
            inserirDados.setString(4, getAssunto());
            inserirDados.setString(5, getRemetente());
            inserirDados.setString(6, getDestinatario());
            inserirDados.setString(7, getDataDeEmissao());
            inserirDados.setInt(8, getNumero());
            inserirDados.setString(9, getAnoAtual());
            inserirDados.executeUpdate();
            System.out.println("INSERÇÃO REALIZADA COM SUCESSO!");

        } catch (SQLException e) {
            //throw new Exception("FALHA NA CONEXAO COM A BASE DE DADOS!");
            System.out.println(e);
        } finally {
            if (conexao != null) {
                conexao.close();
                System.out.println("conexao fechada!");
            }
        }

    }

    public void zerarNumeracao(String nomeDocumento, String assunto, int ultimoNumeroRegistrado) throws SQLException, IOException {

        //definindo valores
        setAnoAtual(anoVigente());
        setNomeDOC(nomeDocumento);
        setAssunto(assunto);
        setNumero(ultimoNumeroRegistrado);
        String sqlAlterarNumeracao = "INSERT INTO documento (NOME_DOC, ASSUNTO, ULTIMO_NUMERO_REGISTRADO, ANO_VIGENTE) VALUES "
                + "(?,?,?,?)";
        //Estabelcendo conexao
        Connection conexao = Conecta_MySql.conectarDB();
        try {
            PreparedStatement zerarNumerador = conexao.prepareStatement(sqlAlterarNumeracao);
            zerarNumerador.setString(1, getNomeDOC());
            zerarNumerador.setString(2, getAssunto());
            zerarNumerador.setInt(3, getNumero());
            zerarNumerador.setString(4, getAnoAtual());
            zerarNumerador.execute();
            System.out.println("o numerador foi atualizado com sucesso!");
        } finally {
            if (conexao != null) {
                conexao.close();
            }
        }

    }

}
