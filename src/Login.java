public class Login {
    boolean loginSuccess = false;
    String reader_name;
    String reader_password;
    int reader_role = 0;
    int reader_num;

    public int getReader_num() {
        return reader_num;
    }

    public void setReader_num(int reader_num) {
        this.reader_num = reader_num;
    }

    public int getReader_role() {
        return reader_role;
    }

    public void setReader_role(int reader_role) {
        this.reader_role = reader_role;
    }

    public boolean isLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    public String getReader_name() {
        return reader_name;
    }

    public void setReader_name(String reader_name) {
        this.reader_name = reader_name;
    }

    public String getReader_password() {
        return reader_password;
    }

    public void setReader_password(String reader_password) {
        this.reader_password = reader_password;
    }
}
