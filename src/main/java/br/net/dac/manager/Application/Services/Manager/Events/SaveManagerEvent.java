package br.net.dac.manager.Application.Services.Manager.Events;

public class SaveManagerEvent {
    private String name;
    private String email;
    private String cpf;
    private String phone;
    
    public SaveManagerEvent(String name, String email, String cpf, String phone) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.phone = phone;
    }
    
    public SaveManagerEvent() {
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
    

}
