package com.anan.Soulmate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anan.Soulmate.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	User findByUsername(String username);
	List<User> findByName(String name);
}
