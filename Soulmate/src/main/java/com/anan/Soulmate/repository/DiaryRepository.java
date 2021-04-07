package com.anan.Soulmate.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.anan.Soulmate.model.Diary;
import com.anan.Soulmate.model.Soulmate;

public interface DiaryRepository extends CrudRepository<Diary, Long>{
	public Page<Diary> findBySoulmate(Soulmate soulmate, Pageable pageable); 
}
