package pl.jakubtworek.PasswordManager.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jakubtworek.PasswordManager.entity.Password;

public interface PasswordDAO extends JpaRepository<Password, Integer> {
}