package gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.ServerSocket;
import java.sql.*;

public class Login extends JFrame {
    JPanel p1 = new JPanel();
    JPanel p2 = new JPanel();
    JPanel p3 = new JPanel();
    Label lbUsername = new Label("Username");
    Label lbPassword = new Label("Password");
    static TextField tfUsername = new TextField(25);
    static TextField tfPassword = new TextField(25);
    static Button btnLoginServer = new Button("Login Server");
    static Button btnLoginClient = new Button("Login Client");
    static Button btnRegisterClient = new Button("Register Client");
    static int server_count = 0;
    static int client_count = 0;

    static final String JDBC_DRIVER="com.mysql.cj.jdbc.Driver";
    static final String DB_URL="jdbc:mysql://101.132.138.214:3306/LAN?serverTimezone=GMT&useSSL=false";
    // ���ݿ��û���������
    static final String USER = "lan";
    static final String PASS = "212194";
    static boolean server_status = false;
    static boolean client_status = false;
    static boolean register_status = false;

    public Login() {
        this.add(p1,"North");
        p1.setPreferredSize(new Dimension(400, 65));
        Border borUser = BorderFactory.createTitledBorder("�û���");
        p1.setBorder(borUser);
        p1.add(lbUsername); p1.add(tfUsername);
        this.add(p2,"Center");
        Border borPass = BorderFactory.createTitledBorder("����");
        p2.setBorder(borPass);
        p2.add(lbPassword); p2.add(tfPassword);
        p2.setPreferredSize(new Dimension(400, 65));
        this.add(p3,"South");
        Border borLogin = BorderFactory.createTitledBorder("��¼/ע��");
        p3.setBorder(borLogin);
        p3.setPreferredSize(new Dimension(400, 65));
        p3.add(btnLoginServer); p3.add(btnLoginClient); p3.add(btnRegisterClient);
        btnLoginServer.addActionListener(new login_server_Listener());
        btnLoginClient.addActionListener(new login_client_Listener());
        btnRegisterClient.addActionListener(new register_client_Listener());
        /*WindowAdapter window = new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        };
        this.addWindowListener(window);*/
    }

    //��������¼����
    static class login_server_Listener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            String username = tfUsername.getText();
            String password = tfPassword.getText();
            try {
                // ע�� JDBC ������
                Class.forName("com.mysql.cj.jdbc.Driver");
                // ��һ������
                conn = DriverManager.getConnection(DB_URL,USER,PASS);
                // ִ�� SQL ��ѯ
                stmt = conn.createStatement();
                String sql;
                sql = "SELECT * FROM ServerUser;";
                rs = stmt.executeQuery(sql);
                // չ����������ݿ�
                while(rs.next()) {
                    // ͨ���ֶμ���
                    String Username = rs.getString("username");
                    String Password = rs.getString("password");
                    if(Username.equals(username) && Password.equals(password)){
                        server_status = true;
                    }
                }
                rs.close();
                stmt.close();
                conn.close();
            }catch(Exception se){
                System.out.println(se);
            }

            if(server_status) {
                if(server_count==1){
                    JOptionPane.showMessageDialog(null, "ֻ������һ��Server��");
                }else{
                    Server frameServer = new Server();
                    frameServer.setTitle("Server");
                    frameServer.setSize(600, 460);
                    frameServer.setLocation(360, 300);
                    frameServer.setVisible(true);
                    frameServer.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    server_count++;
                    server_status = false;
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "��¼ʧ�ܣ�");
            }
        }
    }

    //�ͻ��˵�¼����
    static class login_client_Listener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            String username = tfUsername.getText();
            String password = tfPassword.getText();
            try {
                // ע�� JDBC ������
                Class.forName("com.mysql.cj.jdbc.Driver");
                // ��һ������
                conn = DriverManager.getConnection(DB_URL,USER,PASS);
                // ִ�� SQL ��ѯ
                stmt = conn.createStatement();
                String sql;
                sql = "SELECT * FROM ClientUser;";
                rs = stmt.executeQuery(sql);
                // չ����������ݿ�
                while(rs.next()) {
                    // ͨ���ֶμ���
                    String Username = rs.getString("username");
                    String Password = rs.getString("password");
                    if(Username.equals(username) && Password.equals(password)){
                        client_status = true;
                    }
                }
                rs.close();
                stmt.close();
                conn.close();
            }catch(Exception se){
                System.out.println(se);
            }

            if(client_status) {
                if(client_count==10){
                    JOptionPane.showMessageDialog(null, "���֧��10��Client��");
                }else{
                    Client frameClient = new Client();
                    frameClient.setTitle(username);
                    frameClient.setSize(600, 460);
                    frameClient.setLocation(960, 300);
                    frameClient.setVisible(true);
                    frameClient.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    client_count++;
                    client_status = false;
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "��¼ʧ�ܣ�");
            }
        }
    }

    //�ͻ���ע�����
    static class register_client_Listener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Connection conn_out = null;
            Statement stmt_out = null;
            ResultSet rs = null;
            String username = tfUsername.getText();
            String password = tfPassword.getText();
            try {
                // ע�� JDBC ������
                Class.forName("com.mysql.cj.jdbc.Driver");
                // ��һ������
                conn_out = DriverManager.getConnection(DB_URL,USER,PASS);
                // ִ�� SQL ��ѯ
                stmt_out = conn_out.createStatement();
                String sql;
                sql = "SELECT * FROM ClientUser;";
                rs = stmt_out.executeQuery(sql);
                // չ����������ݿ�
                while(rs.next()) {
                    // ͨ���ֶμ���
                    String Username = rs.getString("username");
                    //String Password = rs.getString("password");
                    if(Username.equals(username)){
                        JOptionPane.showMessageDialog(null, "�û����Ѵ��ڣ�");
                        //register_status = false;
                    }
                    else if(password.equals("")) {
                        JOptionPane.showMessageDialog(null, "���벻��Ϊ�գ�");
                    }
                    else if(!Username.equals(username) && !password.equals("")){
                        register_status = true;
                    }
                }
                rs.close();
                stmt_out.close();
                conn_out.close();
            }catch(Exception e1){
                System.out.println(e1);
            }

            if(register_status) {
                Connection conn_in = null;
                Statement stmt_in = null;
                try{
                    // ע�� JDBC ������
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    // ��һ������
                    conn_in = DriverManager.getConnection(DB_URL,USER,PASS);
                    // ִ�� SQL ��ѯ
                    stmt_in = conn_in.createStatement();
                    String sql;
                    sql = "INSERT INTO ClientUser (username,password) VALUES ('" + username + "','" + password + "');";
                    stmt_in.execute(sql);
                    // ��ɺ�ر�
                    stmt_in.close();
                    conn_in.close();
                    JOptionPane.showMessageDialog(null, "ע��ɹ���");
                    register_status = false;
                }catch(Exception e2){
                    System.out.println(e2);
                }
            }
        }
    }

    public static void main(String[] args) {
        Login frameLogin = new Login();
        frameLogin.setTitle("Login");
        frameLogin.setSize(400, 235);
        frameLogin.setLocation(760, 300);
        frameLogin.setVisible(true);
        //frameLogin.setAlwaysOnTop(true);
        frameLogin.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
