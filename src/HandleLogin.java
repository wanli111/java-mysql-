import java.sql.*;
import java.util.Scanner;

public class HandleLogin {
    Connection con;
    public HandleLogin(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        String url = "jdbc:mysql://localhost:3306/library?useSSL = true";
        try {
            con = DriverManager.getConnection(url,"root","" );
        } catch (SQLException e) {}
    }
    public Login Login(){
        PreparedStatement preSql;
        ResultSet rs;

        int flag = 1;
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();
        System.out.println("用户名");
        login.setReader_name(scanner.next());
        System.out.println("密码");
        login.setReader_password(scanner.next());
        String name = login.getReader_name();
        String password = login.getReader_password();
        String sql = "select reader_name,reader_password,reader_role,reader_num from reader where reader_name = ? and reader_password = ?";
        try {
            while (flag != 0){
                preSql = con.prepareStatement(sql);
                preSql.setString(1,name);
                preSql.setString(2,password);
                rs = preSql.executeQuery();
                if (rs.next()){
                    System.out.println("登录成功");
                    if(rs.getInt(3) == 1){
                        login.setReader_role(1);
                    }
                    login.setReader_num(rs.getInt(4));
                    login.setLoginSuccess(true);
                    break;
                }
                else {
                    switch (flag){
                        case 1:
                            flag++;
                            System.out.println("登录失败，还有两次输入用户名和密码的机会");
                            System.out.println("用户名");
                            login.setReader_name(scanner.next());
                            System.out.println("密码");
                            login.setReader_password(scanner.next());
                            name = login.getReader_name();
                            password = login.getReader_password();
                            break;
                        case 2:
                            flag++;
                            System.out.println("登录失败，还有一次输入用户名和密码的机会");
                            System.out.println("用户名");
                            login.setReader_name(scanner.next());
                            System.out.println("密码");
                            login.setReader_password(scanner.next());
                            name = login.getReader_name();
                            password = login.getReader_password();
                            break;
                        case 3:
                            flag = 1;
                            System.out.println("登录失败，请10秒后再操作");
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            System.out.println("用户名");
                            login.setReader_name(scanner.next());
                            System.out.println("密码");
                            login.setReader_password(scanner.next());
                            name = login.getReader_name();
                            password = login.getReader_password();
                            break;
                    }
                }
            }
            con.close();
        } catch (SQLException e) {}
        return login;
    }
}
