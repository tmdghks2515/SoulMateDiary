package com.anan.Soulmate.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.anan.Soulmate.model.Album;

public interface AlbumRepository extends JpaRepository<Album, Long>{
	public Page<Album> findBySoulmateId(long soulmateId, Pageable pageable);
	public Album findBySoulmateIdAndTitle(long soulmateId, String title);
	@Query(value="SELECT a.* FROM Album a where a.title like CONCAT('%',:title,'%') and a.soulmateId like :soulmateId ",
	nativeQuery = true)
	public List<Album> findAlbumWithPartOfTitle(@Param("title") String title, @Param("soulmateId") long soulmateId);
}
 