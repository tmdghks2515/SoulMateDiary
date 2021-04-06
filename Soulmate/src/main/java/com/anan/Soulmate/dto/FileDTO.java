package com.anan.Soulmate.dto;

import java.io.File;

import lombok.Data;
@Data
public class FileDTO {
	private String path;// 파일 전체경로
	private String fileName;

	public FileDTO(File file) {
		super();
		this.path = file.getAbsolutePath();
		this.fileName = file.getName();
	}
}
