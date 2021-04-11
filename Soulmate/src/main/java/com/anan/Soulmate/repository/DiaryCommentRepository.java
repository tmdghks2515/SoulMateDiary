package com.anan.Soulmate.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.anan.Soulmate.model.Diary;
import com.anan.Soulmate.model.DiaryComment;

public interface DiaryCommentRepository extends CrudRepository<DiaryComment, Long>{
	public Page<DiaryComment> findByDiary(Diary diary, Pageable pageable);
}
