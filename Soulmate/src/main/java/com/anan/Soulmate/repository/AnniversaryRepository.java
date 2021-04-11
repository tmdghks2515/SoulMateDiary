package com.anan.Soulmate.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.anan.Soulmate.model.Anniversary;
import com.anan.Soulmate.model.Soulmate;

public interface AnniversaryRepository extends CrudRepository<Anniversary, Long>{
	public List<Anniversary> findBySoulmate(Soulmate soulmate, Sort sort); 
}
