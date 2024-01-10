import java.sql.PreparedStatement;
import  java.sql.*;
import java.util.Scanner;

public class HandleRegister {
    Connection con;
    public HandleRegister(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {}
        String url = "jdbc:mysql://localhost:3306/library?useSSL = true";
        try {
            con = DriverManager.getConnection(url,"root","root" );
        } catch (SQLException e) {}
    }
    public void addReader(){
        PreparedStatement preSql;
        int flag = 0;
        ResultSet rs;
        Statement Sql;
        Register register = new Register();
        Scanner scanner = new Scanner(System.in);
        System.out.println("用户名（以字母开头，可以包含数字、字母、_，6-15位）：");
        String name_regex = "[a-zA-Z][a-zA-Z0-9_]{5,14}";
        String name = scanner.next();
        while (true){
            if (name.matches(name_regex)){
                    try {
                        Sql = con.createStatement();
                        while (true) {
                            rs = Sql.executeQuery("select reader_name from reader");
                            while (rs.next()){
                                String reader_name = rs.getString(1);
                                if(reader_name.equals(name)){
                                    System.out.println("该用户名已存在，请输入新的用户名");
                                    name = scanner.next();
                                    flag = 1;
                                    break;
                                }
                            }
                            if (flag == 1) {
                                break;
                            }
                            register.setReader_name(name);
                            break;
                        }
                    } catch (SQLException e) {}
                    if (flag == 0){
                        register.setReader_name(name);
                        break;
                    }
                    flag = 0;
            }
            else {
                System.out.println("请输入正确的用户名格式：");
                name = scanner.next();
            }
        }
        System.out.println("密码（必须包含大写字母、小写字母 、数字、 特殊字符四种里至少三种，且至少8位）");
        String password_regex = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z0-9]+$)(?![a-z\\W_]+$)(?![0-9\\W_]+$)[a-zA-Z0-9\\W_]{8,16}$";
        String  password = scanner.next();
        while (true){
            if (password.matches(password_regex)){
                System.out.println("请再输入一次密码");
                String re = scanner.next();
                if (!re.equals(password)) {
                    System.out.println("两次密码输入不一致，请重新输入");
                    password = scanner.next();
                    continue;
                }
                register.setReader_password(password);
                break;
            }
            else {
                System.out.println("请输入正确的密码格式：");
                password = scanner.next();
            }
        }
        System.out.println("身份证");
        String id_regex = "([1-6][1-9]|50)\\d{4}(18|19|20)\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]";
        String id = scanner.next();
        while(true){
            if (id.matches(id_regex)){
                try {
                    Sql = con.createStatement();
                    while (true) {
                        rs = Sql.executeQuery("select reader_idnum from reader");
                        while (rs.next()){
                            String reader_idnum = rs.getString(1);
                            if(reader_idnum.equals(id)){
                                System.out.println("该身份证已存在，请输入新的身份证");
                                id = scanner.next();
                                flag = 1;
                                break;
                            }
                        }
                        if (flag == 1) {
                            break;
                        }
                        register.setReader_idnum(id);
                        break;
                    }
                } catch (SQLException e) {}
                if (flag == 0){
                    break;
                }
                flag = 0;
            }
            else{
                System.out.println("请输入正确的身份证格式：");
                id = scanner.next();
            }
        }
        System.out.println("手机号");
        String phone_regex = "(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}";
        String phone = scanner.next();
        while(true){
            if (phone.matches(phone_regex)){
                try {
                    Sql = con.createStatement();
                    while (true) {
                        rs = Sql.executeQuery("select reader_phone from reader");
                        while (rs.next()){
                            String reader_phone = rs.getString(1);
                            if(reader_phone.equals(phone)){
                                System.out.println("该手机号已存在，请输入新的手机号");
                                phone = scanner.next();
                                flag = 1;
                                break;
                            }
                        }
                        if (flag == 1) {
                            break;
                        }
                        register.setReader_phone(phone);
                        break;
                    }
                } catch (SQLException e) {}
                if (flag == 0){
                    break;
                }
                    flag = 0;
            }
            else {
                System.out.println("请输入正确的手机号格式：");
                phone = scanner.next();
            }
        }
        String sql = "insert into reader (reader_name,reader_password,reader_idnum,reader_phone) values(?,?,?,?)";
        int ok = 0;
        try {
            preSql = con.prepareStatement(sql);
            preSql.setString(1,register.getReader_name());
            preSql.setString(2,register.getReader_password());
            preSql.setString(3,register.getReader_idnum());
            preSql.setString(4,register.getReader_phone());
            ok = preSql.executeUpdate();
            con.close();
        } catch (SQLException e) {}
        if (ok != 0){
            System.out.println("注册成功");
        }
    }
    public  void deleteReader(){
        PreparedStatement preSql;
        ResultSet rs;
        Scanner scanner = new Scanner(System.in);
        String sql1 = "select reader_num from reader where reader_num = ?";
        String sql3 = "select reader_count from count where reader_num = ?";
        System.out.println("用户编号：");
        int num = scanner.nextInt();
        try {
            preSql = con.prepareStatement(sql1);
            preSql.setInt(1,num);
            rs = preSql.executeQuery();
            if(!rs.next()){
                System.out.println("不存在该用户");
                con.close();
                return;
            }
            preSql = con.prepareStatement(sql3);
            preSql.setInt(1,num);
            rs = preSql.executeQuery();
            while(rs.next()){
                if(rs.getInt(1) > 0){
                    System.out.println("该用户有借阅的图书，请在归还后删除");
                    con.close();
                    return;
                }
            }
        } catch (SQLException e) {}
        String sql2 = "delete from reader where reader_num = ?";
        int ok = 0;
        try {
            preSql = con.prepareStatement(sql2);
            preSql.setInt(1,num);
            ok = preSql.executeUpdate();
            con.close();
        } catch (SQLException e) {}
        if (ok != 0 ){
            System.out.println("删除成功");
        }
    }
    public int updateReader(Login login){
        PreparedStatement preSql;
        ResultSet rs;
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入要修改的信息（1：用户名 2：密码 3：手机号 4：哥们点错了>_<)：");
        int choose = scanner.nextInt();
        switch (choose){
            case 1:
                System.out.println("旧用户名：");
                String past_name = scanner.next();
                try {
                    String sql4 = "select reader_name from reader where reader_num = ?";
                    preSql = con.prepareStatement(sql4);
                    preSql.setInt(1,login.getReader_num());
                    rs = preSql.executeQuery();
                    rs.next();
                    if (!rs.getString(1).equals(past_name)){
                        System.out.println("用户名都记错啦，扎古扎古");
                        break;
                    }
                    System.out.println("密码：");
                    String password1 = scanner.next();
                    String sql6 = "select reader_password from reader where reader_num = ?";
                    preSql = con.prepareStatement(sql6);
                    preSql.setInt(1,login.getReader_num());
                    rs = preSql.executeQuery();
                    rs.next();
                    if(!rs.getString(1).equals(password1)){
                        System.out.println("密码都记不住还想改手机号？？？？");
                        con.close();
                        return 1;
                    }
                    String new_name;
                    while(true) {
                        System.out.println("新用户名（以字母开头，可以包含数字、字母、_，6-15位）：");
                        String name_regex = "[a-zA-Z][a-zA-Z0-9_]{5,14}";
                        new_name = scanner.next();
                        while (!new_name.matches(name_regex)) {
                            System.out.println("再打一遍！给我照着格式打！不然就打你！");
                            new_name = scanner.next();
                        }
                        String sql1 = "select reader_name from reader where reader_name = ?";
                        preSql = con.prepareStatement(sql1);
                        preSql.setString(1, new_name);
                        rs = preSql.executeQuery();
                        if (rs.next()) {
                            System.out.println("该用户名已存在,请重新输入！");
                            continue;
                        }
                        break;
                    }
                    String sql5 = "update reader set reader_name = ? where reader_num = ?";
                    preSql = con.prepareStatement(sql5);
                    preSql.setString(1,new_name);
                    preSql.setInt(2,login.getReader_num());
                    preSql.executeUpdate();
                    System.out.println("更新成功，请重新登录");
                    con.close();
                    return 0;
                } catch (SQLException e) {}
            case 2:
                System.out.println("旧密码：");
                String past_password = scanner.next();
                try {
                    String sql4 = "select reader_password from reader where reader_num = ?";
                    preSql = con.prepareStatement(sql4);
                    preSql.setInt(1,login.getReader_num());
                    rs = preSql.executeQuery();
                    rs.next();
                    if (!rs.getString(1).equals(past_password)){
                        System.out.println("密码都记错啦，扎古扎古");
                        break;
                    }
                    String new_password;
                    do {
                        System.out.println("新密码（必须包含大写字母、小写字母 、数字、 特殊字符四种里至少三种，且至少8位）");
                        String password_regex = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z0-9]+$)(?![a-z\\W_]+$)(?![0-9\\W_]+$)[a-zA-Z0-9\\W_]{8,16}$";
                        new_password = scanner.next();
                        while (!new_password.matches(password_regex)) {
                            System.out.println("再打一遍！给我照着格式打！不然就打你！");
                            new_password = scanner.next();
                        }
                        System.out.println("再输入一次新密码");
                        String confirmed_password = scanner.next();
                        if (!new_password.equals(confirmed_password)){
                            System.out.println("两次密码输入不一致，请重新输入");
                            continue;
                        }
                        break;
                    }while (true);
                    String sql5 = "update reader set reader_password = ? where reader_num = ?";
                    preSql = con.prepareStatement(sql5);
                    preSql.setString(1,new_password);
                    preSql.setInt(2,login.getReader_num());
                    preSql.executeUpdate();
                    System.out.println("更新成功，请重新登录");
                    con.close();
                    return 0;
                } catch (SQLException e) {}
                break;
            case 3:
                System.out.println("旧手机号：");
                String past_phone = scanner.next();
                try {
                    String sql4 = "select reader_phone from reader where reader_num = ?";
                    preSql = con.prepareStatement(sql4);
                    preSql.setInt(1,login.getReader_num());
                    rs = preSql.executeQuery();
                    rs.next();
                    if (!rs.getString(1).equals(past_phone)){
                        System.out.println("手机号都记错啦，扎古扎古");
                        break;
                    }
                    System.out.println("密码：");
                    String password1 = scanner.next();
                    String sql6 = "select reader_password from reader where reader_num = ?";
                    preSql = con.prepareStatement(sql6);
                    preSql.setInt(1,login.getReader_num());
                    rs = preSql.executeQuery();
                    rs.next();
                    if(!rs.getString(1).equals(password1)){
                        System.out.println("密码都记不住还想改手机号？？？？");
                        con.close();
                        return 1;
                    }
                    String new_phone;
                    while(true) {
                        System.out.println("新手机号：");
                        String phone_regex = "(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}";
                        new_phone = scanner.next();
                        while (!new_phone.matches(phone_regex)) {
                            System.out.println("再打一遍！给我照着格式打！不然就打你！");
                            new_phone = scanner.next();
                        }
                        String sql1 = "select reader_phone from reader where reader_phone = ?";
                        preSql = con.prepareStatement(sql1);
                        preSql.setString(1, new_phone);
                        rs = preSql.executeQuery();
                        if (rs.next()) {
                            System.out.println("该手机号已存在,请重新输入！");
                            continue;
                        }
                        break;
                    }
                    String sql5 = "update reader set reader_phone = ? where reader_num = ?";
                    preSql = con.prepareStatement(sql5);
                    preSql.setString(1,new_phone);
                    preSql.setInt(2,login.getReader_num());
                    preSql.executeUpdate();
                    con.close();
                } catch (SQLException e) {}
            case 4:
                return 1;
            default:
                System.out.println("他可不是乱打的哦");
                return 1;
        }
        return 1;
    }
    public void searchReader(){
        PreparedStatement preSql;
        ResultSet rs = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("用户编号：");
        int num = scanner.nextInt();
        try {
            String sql = "select * from reader where reader_num = ?";
            preSql = con.prepareStatement(sql);
            preSql.setInt(1,num);
            rs = preSql.executeQuery();
            if (rs.next()){
                String name = rs.getString(2);
                String password = rs.getString(3);
                int count = rs.getInt(4);
                String id = rs.getString(5);
                String phone = rs.getString(6);
                int role = rs.getInt(7);
                System.out.print("编号\t");
                System.out.print("姓名\t\t");
                System.out.print("密码\t\t  ");
                System.out.print("借阅量\t\t");
                System.out.print("身份证号\t\t\t");
                System.out.print("手机号\t\t");
                System.out.print("   角色\n");
                System.out.printf("%d\t",num);
                System.out.printf("%s\t",name);
                System.out.printf("%s\t",password);
                System.out.printf("%d\t",count);
                System.out.printf("%s\t",id);
                System.out.printf("%s\t\t",phone);
                System.out.printf("%d\n",role);
                String sql1 = "SELECT DISTINCT borrow.book_id,book_category.book_name,book_category.book_ISBN from borrow,book_each,book_category where borrow.reader_num = ? and borrow.book_id = book_each.book_id and book_each.book_ISBN = book_category.book_ISBN and borrow.state = 0";
                preSql = con.prepareStatement(sql1);
                preSql.setInt(1, num);
                rs = preSql.executeQuery();
                if (rs.next()) {
                    int book_id = rs.getInt(1);
                    name = rs.getString(2);
                    String ISBN = rs.getString(3);
                    System.out.print("该用户当前已借阅书目:\n");
                    System.out.print("book_id\t");
                    System.out.print("book_name\t");
                    System.out.print("book_ISBN\n");
                    System.out.printf("%d\t", book_id);
                    System.out.printf("%s\t", name);
                    System.out.printf("%s\n", ISBN);
                    while (rs.next()) {
                        book_id = rs.getInt(1);
                        name = rs.getString(2);
                        ISBN = rs.getString(3);
                        System.out.printf("%d\t", book_id);
                        System.out.printf("%s\t", name);
                        System.out.printf("%s\n", ISBN);
                    }
                }
            }
            else{
                System.out.println("不存在该用户");
                con.close();
                return;
            }
            con.close();
        } catch (SQLException e) {}
    }
}
