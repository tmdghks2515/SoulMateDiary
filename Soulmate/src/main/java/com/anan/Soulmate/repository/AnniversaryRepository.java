package com.anan.Soulmate.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.anan.Soulmate.model.Anniversary;
import com.anan.Soulmate.model.Soulmate;

public interface AnniversaryRepository extends CrudRepository<Anniversary, Long>{
	public List<Anniversary> findBySoulmate(Soulmate soulmate, Sort sort); 
	@Query(value="select * from anniversary where Year(anniDate) = :anniYear and "
			+ "Month(anniDate) = :anniMonth", nativeQuery = true)
	public List<Anniversary> findByYearAndMonth(int anniYear, int anniMonth);
}
