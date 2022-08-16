package pl.jakubtworek.PasswordManager.service;

import pl.jakubtworek.PasswordManager.entity.Password;

import java.util.List;

public interface PasswordService {
    public List<Password> findAll();

    public Password findById(int theId);

    public void save(Password thePassword);

    public void deleteById(int theId);
}
