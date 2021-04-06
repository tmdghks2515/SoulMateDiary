package com.anan.Soulmate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anan.Soulmate.model.Soulmate;
import com.anan.Soulmate.model.User;

public interface SoulmateRepository extends JpaRepository<Soulmate, Long>{
	public Soulmate findByUser1(User user1);
	public Soulmate findByUser2(User user2);
}
