package br.ufpr.tads.manager;

public class ManagerResult {
    private Long id;
    private String name;
    private String email;
    private String cpf;
    private String phone;
    private int totalAccounts;

    public ManagerResult(Long id, String name, String email, String cpf, String phone, int totalAccounts) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.phone = phone;
        this.totalAccounts = totalAccounts;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public int getTotalAccounts() {
        return totalAccounts;
    }
    public void setTotalAccounts(int totalAccounts) {
        this.totalAccounts = totalAccounts;
    }
    
}
