package pl.jakubtworek.PasswordManager.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import pl.jakubtworek.PasswordManager.entity.Category;
import pl.jakubtworek.PasswordManager.entity.Password;

public interface CategoryDAO extends JpaRepository<Category, Integer> {
}
