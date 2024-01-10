import java.sql.*;
import java.util.Scanner;

public class HandleBorrow {
    Connection con;
    public HandleBorrow(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {}
        String url = "jdbc:mysql://localhost:3306/library?useSSL = true";
        try {
            con = DriverManager.getConnection(url,"root","" );
        } catch (SQLException e) {}
    }
    public void borrowBook(Login login) {
        boolean flag1 = false;
        boolean flag2 = false;
        PreparedStatement preSql;
        ResultSet rs = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入要借阅的图书名:");
        String find_name = scanner.next();
        String sql1 = "select * from book_category where book_name like ?";
        try {
            preSql = con.prepareStatement(sql1);
            preSql.setString(1, "%" + find_name + "%");
            rs = preSql.executeQuery();
            if (rs.next()) {
                String ISBN = rs.getString(1);
                String name = rs.getString(2);
                int inventory = rs.getInt(4);
                System.out.print("ISBN\t\t");
                System.out.print("name\t");
                System.out.print("inventory\n");
                System.out.printf("%s\t", ISBN);
                System.out.printf("%s\t\t", name);
                System.out.printf("%d\n", inventory);
                while (rs.next()) {
                    ISBN = rs.getString(1);
                    name = rs.getString(2);
                    inventory = rs.getInt(4);
                    System.out.printf("%s\t", ISBN);
                    System.out.printf("%s\t\t", name);
                    System.out.printf("%d\n", inventory);
                }
                System.out.println("请输入要借阅的图书的ISBN:");
                String find_ISBN = scanner.next();
                rs = preSql.executeQuery();
                while (rs.next()) {
                    ISBN = rs.getString(1);
                    if (ISBN.equals(find_ISBN)) {
                        flag1 = true;
                        break;
                    }
                }
                if (!flag1) {
                    System.out.println("错误的ISBN");
                    con.close();
                    return;
                }
                String sql2 = "select reader_count from reader where reader_name = ?";
                String sql3 = "select book_id,book_position from book_each where book_ISBN = ? and book_position = 1";
                preSql = con.prepareStatement(sql2);
                preSql.setString(1, login.getReader_name());
                rs = preSql.executeQuery();
                if (rs.next()) {
                    if (rs.getInt(1) == 5) {
                        System.out.println("已达到5本借书上限，请及时归还图书后再借阅");
                        con.close();
                        return;
                    }
                }
                preSql = con.prepareStatement(sql3);
                preSql.setString(1, find_ISBN);
                rs = preSql.executeQuery();
                while (rs.next()) {
                    if (rs.getInt(2) == 1) {
                        int id = rs.getInt(1);
                        flag2 = true;
                        String sql4 = "update book_category set book_inventory = book_inventory - 1 where book_ISBN = ?";
                        String sql5 = "update reader set reader_count = reader_count + 1 where reader_name = ?";
                        String sql6 = "update book_each set book_position = 0 where book_id = ?";
                        String sql7 = "insert into borrow (book_id,reader_num) values (?,?)";
                        String sql8 = "SELECT DISTINCT book_category.book_ISBN from borrow,book_each,book_category where borrow.reader_num = ? and borrow.book_id = book_each.book_id and borrow.state = 0";
                        preSql = con.prepareStatement(sql8);
                        preSql.setInt(1,login.getReader_num());
                        rs = preSql.executeQuery();
                        while (rs.next()) {
                            boolean flag3 = false;
                            if (find_ISBN.equals(rs.getString(1))) {
                                System.out.println("已借阅相同图书，是否确认再借阅一本（y/n）");
                                while (true) {
                                    String choose = scanner.next();
                                    switch (choose) {
                                        case "y":
                                            flag3 = true;
                                            break;
                                        case "n":
                                            return;
                                        default:
                                            System.out.println("错误输入，请重新输入");
                                            break;
                                    }
                                    if (flag3) break;
                                }
                                break;
                            }
                        }
                        preSql = con.prepareStatement(sql4);
                        preSql.setString(1, find_ISBN);
                        preSql.executeUpdate();
                        preSql = con.prepareStatement(sql5);
                        preSql.setString(1, login.getReader_name());
                        preSql.executeUpdate();
                        preSql = con.prepareStatement(sql6);
                        preSql.setInt(1, id);
                        preSql.executeUpdate();
                        preSql = con.prepareStatement(sql7);
                        preSql.setInt(1, id);
                        preSql.setInt(2, login.getReader_num());
                        preSql.executeUpdate();
                        break;
                        }
                    }
                    if (!flag2) {
                        System.out.println("该书已被借完，下次再来借吧");
                        con.close();
                        return;
                    }
                    System.out.println("借阅成功");
                    con.close();
            }
            else{
                System.out.println("不存在该书");
                con.close();
            }
        } catch (SQLException e) {
        }
    }
    public void returnBook(Login login){
        boolean flag = false;
        PreparedStatement preSql =null;
        ResultSet rs = null;
        Scanner scanner = new Scanner(System.in);
        String sql1 = "SELECT DISTINCT borrow.book_id,book_category.book_name,book_category.book_ISBN from borrow,book_each,reader,book_category where borrow.reader_num = ? and borrow.book_id = book_each.book_id and book_each.book_ISBN = book_category.book_ISBN and borrow.state = 0";
        try {
            preSql = con.prepareStatement(sql1);
            preSql.setInt(1,login.getReader_num());
            rs = preSql.executeQuery();
            if (rs.next()){
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String ISBN = rs.getString(3);
                System.out.print("当前已借阅书目:\n");
                System.out.print("book_id\t");
                System.out.print("book_name\t");
                System.out.print("book_ISBN\n");
                System.out.printf("%d\t",id);
                System.out.printf("%s\t",name);
                System.out.printf("%s\n",ISBN);
                while(rs.next()){
                    id = rs.getInt(1);
                    name = rs.getString(2);
                    ISBN = rs.getString(3);
                    System.out.printf("%d\t",id);
                    System.out.printf("%s\t",name);
                    System.out.printf("%s\n",ISBN);
                }
            }
            System.out.println("请输入要归还的书目的编号（book_id）:");
            int book_id = scanner.nextInt();
            rs = preSql.executeQuery();
            while(rs.next()){
                int id = rs.getInt(1);
                if (id == book_id){
                    flag = true;
                    break;
                }
            }
            if(!flag){
                System.out.println("错误的图书编号");
                con.close();
                return;
            }
            String sql2 = "select book_ISBN from book_each where book_id = ? ";
            String sql3 = "update book_category set book_inventory = book_inventory + 1 where book_ISBN = ?";
            String sql4 = "update reader set reader_count = reader_count - 1 where reader_name = ?";
            String sql5 = "update book_each set book_position = 1 where book_id = ?";
            String sql6 = "update borrow set state = 1 where book_id = ?";
            preSql = con.prepareStatement(sql2);
            preSql.setInt(1,book_id);
            rs = preSql.executeQuery();
            rs.next();
            String book_ISBN = rs.getString(1);
            preSql = con.prepareStatement(sql3);
            preSql.setString(1,book_ISBN);
            preSql.executeUpdate();
            preSql = con.prepareStatement(sql4);
            preSql.setString(1,login.getReader_name());
            preSql.executeUpdate();
            preSql = con.prepareStatement(sql5);
            preSql.setInt(1,book_id);
            preSql.executeUpdate();
            preSql = con.prepareStatement(sql6);
            preSql.setInt(1,book_id);
            preSql.executeUpdate();
            System.out.println("归还成功");
            con.close();
        } catch (SQLException e) {}
    }
    public void searchUnreturnedBook(Login login) {
        PreparedStatement preSql = null;
        ResultSet rs = null;
        String sql1 = "SELECT DISTINCT borrow.book_id,book_category.book_name,book_category.book_ISBN from borrow,book_each,reader,book_category where borrow.reader_num = ? and borrow.book_id = book_each.book_id and book_each.book_ISBN = book_category.book_ISBN and borrow.state = 0";
        try {
            preSql = con.prepareStatement(sql1);
            preSql.setInt(1, login.getReader_num());
            rs = preSql.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String ISBN = rs.getString(3);
                System.out.print("当前已借阅书目:\n");
                System.out.print("book_id\t");
                System.out.print("book_name\t");
                System.out.print("book_ISBN\n");
                System.out.printf("%d\t", id);
                System.out.printf("%s\t", name);
                System.out.printf("%s\n", ISBN);
                while (rs.next()) {
                    id = rs.getInt(1);
                    name = rs.getString(2);
                    ISBN = rs.getString(3);
                    System.out.printf("%d\t", id);
                    System.out.printf("%s\t", name);
                    System.out.printf("%s\n", ISBN);
                }
            }
        } catch (SQLException e) {}
    }
}
