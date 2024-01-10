import java.util.*;
public class Main {
    public static void main(String[] args) {
        while(true){
            Scanner scanner = new Scanner(System.in);
            System.out.println("*************************");
            System.out.println("图书管理系统");
            System.out.println("1：注册账号");
            System.out.println("2：登录账号");
            System.out.println("0：退出系统");
            System.out.println("*************************");
            int choose = scanner.nextInt();
            switch (choose) {
                case 1:
                    HandleRegister handleRegister1 = new HandleRegister();
                    handleRegister1.addReader();
                    break;
                case 2:
                    HandleLogin handleLogin = new HandleLogin();
                    Login user = handleLogin.Login();
                    if (user.isLoginSuccess() && user.getReader_role() == 1) {
                        while (true) {
                            int flag = 1;
                            System.out.println("*************************");
                            System.out.println("欢迎进入图书管理系统");
                            System.out.println("1:增加图书");
                            System.out.println("2:删除图书");
                            System.out.println("3:修改图书");
                            System.out.println("4:查找图书");
                            System.out.println("5:增加用户");
                            System.out.println("6:删除用户");
                            System.out.println("7:修改用户");
                            System.out.println("8:查找用户");
                            System.out.println("9:查看被借阅的书的去向");
                            System.out.println("10:退出系统");
                            System.out.println("0:退出系统");
                            System.out.println("*************************");
                            choose = scanner.nextInt();
                            switch (choose) {
                                case 1:
                                    HandleBook handleBook1 = new HandleBook();
                                    handleBook1.addBook();
                                    break;
                                case 2:
                                    HandleBook handleBook2 = new HandleBook();
                                    handleBook2.deleteBook();
                                    break;
                                case 3:
                                    HandleBook handleBook3 = new HandleBook();
                                    handleBook3.updateBook();
                                    break;
                                case 4:
                                    HandleBook handleBook4 = new HandleBook();
                                    handleBook4.searchBook();
                                    break;
                                case 5:
                                    HandleRegister handleRegister2 = new HandleRegister();
                                    handleRegister2.addReader();
                                    break;
                                case 6:
                                    HandleRegister handleRegister3 = new HandleRegister();
                                    handleRegister3.deleteReader();
                                    break;
                                case 7:
                                    HandleRegister handleRegister4 = new HandleRegister();
                                    handleRegister4.updateReader(user);
                                    break;
                                case 8:
                                    HandleRegister handleRegister5 = new HandleRegister();
                                    handleRegister5.searchReader();
                                    break;
                                case 9:
                                    HandleBook handleBook = new HandleBook();
                                    handleBook.printBook();
                                    break;
                                case 10:
                                    flag = 0;
                                    break;
                                case 0:
                                    System.exit(0);
                                default:
                                    System.out.println("错误输入，请重新输入");
                            }
                            if (flag == 0) break;
                        }
                    }
                    if (user.isLoginSuccess() && user.getReader_role() == 0) {
                        while (true) {
                            System.out.println("*************************");
                            System.out.println("欢迎进入图书管理系统");
                            System.out.println("1:查找图书");
                            System.out.println("2:借阅图书");
                            System.out.println("3:归还图书");
                            System.out.println("4:修改个人信息");
                            System.out.println("5：查看未归还书籍");
                            System.out.println("9:返回登录");
                            System.out.println("0:退出系统");
                            System.out.println("*************************");
                            choose = scanner.nextInt();
                            int flag = 1;
                            switch (choose) {
                                case 1:
                                    HandleBook handleBook5 = new HandleBook();
                                    handleBook5.searchBook();
                                    break;
                                case 2:
                                    HandleBorrow handleBorrow = new HandleBorrow();
                                    handleBorrow.borrowBook(user);
                                    break;
                                case 3:
                                    HandleBorrow handleBorrow1 = new HandleBorrow();
                                    handleBorrow1.returnBook(user);
                                    break;
                                case 4:
                                    HandleRegister handleRegister7 = new HandleRegister();
                                    flag = handleRegister7.updateReader(user);
                                    break;
                                case 5:
                                    HandleBorrow handleBorrow2 = new HandleBorrow();
                                    handleBorrow2.searchUnreturnedBook(user);
                                    break;
                                case 9:
                                    flag = 0;
                                    break;
                                case 0:
                                    System.exit(0);
                                default:
                                    System.out.println("错误输入，请重新输入");
                            }
                            if (flag == 0) break;
                        }
                    }
                    break;
                case 0:
                    System.exit(0);
                    break;
                default:
                    System.out.println("错误输入，请重新输入");
            }
        }
    }
}