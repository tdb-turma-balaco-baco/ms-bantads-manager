package br.ufpr.tads.msbantadsmanager.core.domain.model;

import br.ufpr.tads.msbantadsmanager.core.domain.vo.CPF;
import br.ufpr.tads.msbantadsmanager.core.domain.vo.ManagerId;

public class Manager {
  private ManagerId id;
  private CPF cpf;
  private String email;
  private String name;
  private String phoneNumber;
  private int accounts;

  public Manager(
      ManagerId id, CPF cpf, String email, String name, String phoneNumber, int accounts) {
    this.id = id;
    this.cpf = cpf;
    this.email = email;
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.accounts = accounts;
  }

  public static Manager create(
      CPF cpf, String email, String name, String phoneNumber) {
    return new Manager(new ManagerId(), cpf, email, name, phoneNumber, 0);
  }

  public ManagerId getId() {
    return id;
  }

  public CPF getCpf() {
    return cpf;
  }

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public int getAccounts() {
    return accounts;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public void setAccounts(int accounts) {
    this.accounts = accounts;
  }
}
