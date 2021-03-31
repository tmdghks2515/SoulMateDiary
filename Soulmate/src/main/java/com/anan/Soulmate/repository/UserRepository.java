package com.anan.Soulmate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anan.Soulmate.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	User findByUsername(String username);
}
