import java.sql.*;
import java.util.Scanner;
    public class HandleBook {
        Connection con;
        public HandleBook(){
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
        public void addBook(){
            PreparedStatement preSql;
            ResultSet rs;
            Scanner scanner = new Scanner(System.in);
            System.out.println("ISBN:");
            String ISBN = scanner.next();
            System.out.println("书名");
            String name = scanner.next();
            System.out.println("采购量");
            int purchase = scanner.nextInt();
            int ok = 0;
            String sql1 = "insert into book_category values(?,?,?,?)";
            String sql2 = "insert into book_each (book_ISBN) values(?)";
            try {
                preSql = con.prepareStatement(sql1);
                preSql.setString(1,ISBN);
                preSql.setString(2,name);
                preSql.setInt(3,purchase);
                preSql.setInt(4,purchase);
                ok = preSql.executeUpdate();
                preSql = con.prepareStatement(sql2);
                preSql.setString(1,ISBN);
                for (int i = 0; i < purchase; i++){
                    preSql.executeUpdate();
                }
                if (ok != 0) {
                    System.out.println("新增成功");
                }
                con.close();
            } catch (SQLException e) {}
        }
        public void deleteBook(){
            ResultSet rs;
            PreparedStatement preSql;
            Scanner scanner = new Scanner(System.in);
            String sql3 = "select book_ISBN from book_category where book_ISBN = ?";
            String sql4 = "select book_position from book_each where book_ISBN = ?";
            System.out.println("ISBN:");
            String ISBN = scanner.next();
            try {
                preSql = con.prepareStatement(sql3);
                preSql.setString(1,ISBN);
                rs = preSql.executeQuery();
                if(!rs.next()){
                    System.out.println("不存在该书");
                    con.close();
                    return;
                }
                preSql = con.prepareStatement(sql4);
                preSql.setString(1,ISBN);
                rs = preSql.executeQuery();
                while(rs.next()){
                    if (rs.getInt(1) == 0){
                        System.out.println("该书正被外借，无法删除");
                        con.close();
                        return;
                    }
                }
            } catch (SQLException e) {}
            String sql1 = "delete from book_category where book_ISBN = ?";
            String sql2 = "delete from book_each where book_ISBN = ?";
            int ok1 = 0;
            int ok2 = 0;
            try {
                preSql = con.prepareStatement(sql1);
                preSql.setString(1,ISBN);
                ok1 = preSql.executeUpdate();
                preSql = con.prepareStatement(sql2);
                preSql.setString(1,ISBN);
                ok2 = preSql.executeUpdate();
                con.close();
            } catch (SQLException e) {}
            if (ok1 != 0 && ok2 != 0){
                System.out.println("删除成功");
            }
        }
        public void updateBook(){
            ResultSet rs;
            PreparedStatement preSql;
            Scanner scanner = new Scanner(System.in);
            String sql4 = "select book_ISBN from book_category where book_ISBN = ?";
            System.out.println("查找的ISBN:");
            String search_ISBN = scanner.next();
            try {
                preSql = con.prepareStatement(sql4);
                preSql.setString(1,search_ISBN);
                rs = preSql.executeQuery();
                if(!rs.next()){
                    System.out.println("不存在该书");
                    con.close();
                    return;
                }
            } catch (SQLException e) {}
            System.out.println("更新的ISBN:");
            String update_ISBN = scanner.next();
            System.out.println("更新的书名");
            String name = scanner.next();
            System.out.println("新增采购量");
            int purchase = scanner.nextInt();
            String sql1 = "update book_category set book_ISBN = ?,book_name = ?, book_purchase = book_purchase + ?, book_inventory = book_inventory + ?  where book_ISBN = ?";
            String sql2 = "update book_each set book_ISBN = ? where book_ISBN = ?";
            String sql3 = "insert into book_each (book_ISBN) values(?)";
            int ok = 0;
            try {
                preSql = con.prepareStatement(sql1);
                preSql.setString(1,update_ISBN);
                preSql.setString(2,name);
                preSql.setInt(3,purchase);
                preSql.setInt(4,purchase);
                preSql.setString(5,search_ISBN);
                ok = preSql.executeUpdate();
                preSql = con.prepareStatement(sql2);
                preSql.setString(1,update_ISBN);
                preSql.setString(2,search_ISBN);
                preSql.executeUpdate();
                if (purchase > 0){
                    preSql = con.prepareStatement(sql3);
                    preSql.setString(1,update_ISBN);
                    for (int i = 0; i < purchase; i++){
                         preSql.executeUpdate();
                    }
                }
                if(ok != 0){
                    System.out.println("修改成功");
                }
                con.close();
            } catch (SQLException e) {}
        }
        public void searchBook() {
            ResultSet rs;
            PreparedStatement preSql = null;
            String sql;
            Statement sqlstatement;
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入书名：");
            String find_name = scanner.next();
            sql = "select * from book_category where book_name like ?" ;
            try {
                preSql = con.prepareStatement(sql);
                preSql.setString(1,"%" + find_name + "%");
                rs = preSql.executeQuery();
                if (rs.next()){
                    String ISBN = rs.getString(1);
                    String name = rs.getString(2);
                    int purchase = rs.getInt(3);
                    int inventory = rs.getInt(4);
                    System.out.print("ISBN\t\t");
                    System.out.print("name\t");
                    System.out.print("purchase\t");
                    System.out.print("inventory\n");
                    System.out.printf("%s\t\t",ISBN);
                    System.out.printf("%s\t\t",name);
                    System.out.printf("%d\t\t\t\t",purchase);
                    System.out.printf("%d\n",inventory);
                    while(rs.next()){
                        ISBN = rs.getString(1);
                        name = rs.getString(2);
                        purchase = rs.getInt(3);
                        inventory = rs.getInt(4);
                        System.out.printf("%s\t",ISBN);
                        System.out.printf("%s\t\t",name);
                        System.out.printf("%d\t\t\t\t",purchase);
                        System.out.printf("%d\n",inventory);
                    }
                }
                else{
                    System.out.println("不存在该书");
                    con.close();
                    return;
                }
                con.close();
            } catch (SQLException e) {}
        }
        public void printBook(){
            PreparedStatement preSql = null;
            ResultSet rs = null;
            String sql1 = "SELECT DISTINCT borrow.book_id,book_category.book_name,book_category.book_ISBN,reader.reader_num,reader.reader_name from borrow,book_each,reader,book_category where borrow.reader_num = reader.reader_num and borrow.book_id = book_each.book_id and book_each.book_ISBN = book_category.book_ISBN and borrow.state = 0";
            try {
                preSql = con.prepareStatement(sql1);
                rs = preSql.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    String book_name = rs.getString(2);
                    String ISBN = rs.getString(3);
                    int num = rs.getInt(4);
                    String reader_name = rs.getString(5);
                    System.out.print("book_id\t");
                    System.out.print("book_name\t");
                    System.out.print("book_ISBN\t");
                    System.out.print("reader_num\t");
                    System.out.print("reader_name\n");
                    System.out.printf("%d\t", id);
                    System.out.printf("%s\t", book_name);
                    System.out.printf("%s\t", ISBN);
                    System.out.printf("%d\t", num);
                    System.out.printf("%s\n", reader_name);
                    while (rs.next()) {
                        id = rs.getInt(1);
                        book_name = rs.getString(2);
                        ISBN = rs.getString(3);
                        num = rs.getInt(4);
                        reader_name = rs.getString(5);
                        System.out.printf("%d\t", id);
                        System.out.printf("%s\t", book_name);
                        System.out.printf("%s\t", ISBN);
                        System.out.printf("%d\t", num);
                        System.out.printf("%s\n", reader_name);
                    }
                }
            } catch (SQLException e) {}
        }
    }