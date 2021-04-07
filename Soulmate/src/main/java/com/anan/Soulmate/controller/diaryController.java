package com.anan.Soulmate.controller;


import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.anan.Soulmate.config.auth.PrincipalDetails;
import com.anan.Soulmate.dto.ResponseDTO;
import com.anan.Soulmate.model.Album;
import com.anan.Soulmate.model.Diary;
import com.anan.Soulmate.model.Soulmate;
import com.anan.Soulmate.model.User;
import com.anan.Soulmate.repository.AlbumRepository;
import com.anan.Soulmate.repository.DiaryRepository;
import com.anan.Soulmate.repository.SoulmateRepository;
import com.anan.Soulmate.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class diaryController {
	
	private final UserRepository userRepository;
	private final AlbumRepository albumRepository;
	private final SoulmateRepository soulmateRepository;
	private final DiaryRepository diaryRepository;
	
	public void setDday(User principal, HttpServletRequest req) {
		
		Soulmate soulmate = soulmateRepository.findByUser1(principal);
		if(soulmate == null)
			soulmate = soulmateRepository.findByUser2(principal);
		if(soulmate == null) return;
		
		Date Ddate = soulmate.getDdate();
		if(Ddate != null && req.getAttribute("Dday") == null) {
			Date today = new Date();
			long diff = today.getTime() - Ddate.getTime(); 
			int Dday =  (int)(diff/(1000*60*60*24) + 1);
			
			req.setAttribute("Dday", Dday);
		}
	}
	
	public void setSoulmate(User principal, HttpServletRequest req) {
		
		boolean flag = false;
		Soulmate soulmate = soulmateRepository.findByUser1(principal);
		if(soulmate == null) {
			soulmate = soulmateRepository.findByUser2(principal);
			flag = true;
		}
		
		User user2 = null;
		if(soulmate != null) {
			if(flag)
				user2 = soulmate.getUser1();
			else
				user2 = soulmate.getUser2();
		}
		
		req.setAttribute("soulmate", soulmate);
		req.setAttribute("user2", user2);
	}
	
	@GetMapping("/user/diaryMain")
	public String diaryMain(@AuthenticationPrincipal PrincipalDetails principalDetails,
			HttpServletRequest req) {
		
		User principal = principalDetails.getUser();
		setDday(principal, req);
		setSoulmate(principal, req);
		
		return "user/diaryMain";
	}
	
	@GetMapping("/user/connectSoulmateForm")
	public String connectSoulmateForm() {
		return "/user/connectSoulmateForm";
	}
	
	@PostMapping("/user/connectSoulmateProc")
	@Transactional
	public @ResponseBody String connectSoulmateProc(User user,String Dday,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		User principal = principalDetails.getUser();
		User userEntity = userRepository.findByUsername(user.getUsername());
		ResponseDTO<String> resp = new ResponseDTO<>();

		if(user.getUsername().equals(principalDetails.getUsername())) {
			resp.setResponseCode(2);
			resp.setResponseMessage("본인과는 소울메이트가 될수 없습니다 .. ㅠㅠ");
		}else if(userEntity == null) {
			resp.setResponseCode(2);
			resp.setResponseMessage("아이디를 확인하세요");
		}else if(!userEntity.getName().equals(user.getName())) {
			resp.setResponseCode(2);
			resp.setResponseMessage("이름을 확인하세요");
		}else if(!userEntity.getEmail().equals(user.getEmail())) {
			resp.setResponseCode(2);
			resp.setResponseMessage("이메일을 확인하세요");
		}else if(soulmateRepository.findByUser1(userEntity) != null 
				|| soulmateRepository.findByUser2(userEntity) != null) {
			resp.setResponseCode(2);
			resp.setResponseMessage("해당 사용자는 이미 소울메이트가 있습니다");
		}else {
			resp.setResponseCode(1);
			resp.setResponseMessage("연결에 성공했습니다");
			
			Soulmate soulmate = new Soulmate();
			soulmate.setUser1(principal);
			soulmate.setUser2(userEntity);
			
			Date date = null;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(Dday);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			soulmate.setDdate(date);
			
			soulmateRepository.save(soulmate);
		}
		return new JSONObject(resp).toString();
	}
	
	@GetMapping("/user/album")
	public String album(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@PageableDefault(page=0, size=10,sort="createDate",direction = Sort.Direction.DESC) 
				Pageable pageable, HttpServletRequest req) {
		
		User principal = principalDetails.getUser();
		if(soulmateRepository.findByUser1(principal) == null 
				&& soulmateRepository.findByUser2(principal) == null) {
			return "redirect:/user/solo";
		}
		setDday(principal, req);
		setSoulmate(principal, req);
		
		Soulmate soulmate = soulmateRepository.findByUser1(principal);
		if(soulmate == null)
			soulmate = soulmateRepository.findByUser2(principal);
		Page<Album> albumPage = albumRepository.findBySoulmateId(soulmate.getId(), pageable);
		req.setAttribute("albumPage", albumPage);
		return "/user/album";
	}
	
	@PostMapping("/user/imgUpload")
	public String imgUpload(MultipartHttpServletRequest req,
			@AuthenticationPrincipal PrincipalDetails principalDetails, String title, String content) {
		User principal = principalDetails.getUser();
		Soulmate soulmate = soulmateRepository.findByUser1(principal);
		if(soulmate == null)
			soulmate = soulmateRepository.findByUser2(principal);
		
		Album album = new Album();
		album.setTitle(title);
		album.setContent(content);
		album.setSoulmate(soulmate);
		albumRepository.save(album);
		String root = "C:\\Soulmate\\album\\"+soulmate.getId()+"\\"+title+"\\";
		List<MultipartFile> fileList = req.getFiles("file");
		
		for(MultipartFile thisFile : fileList) {
			if(thisFile.getSize() == 0) 
				continue;
			String filename = thisFile.getOriginalFilename();
			String path = root+filename;
			File rootPath = new File(root);
			if(!rootPath.exists())
				rootPath.mkdirs();
			try {
				thisFile.transferTo(new File(path));
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "redirect:/user/album";
	}
	
	@GetMapping("/user/diary")
	public String diary(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@PageableDefault(page=0, size=2,sort="writeDate",direction = Sort.Direction.DESC) 
	Pageable pageable, HttpServletRequest req) {
		
		User principal = principalDetails.getUser();
		if(soulmateRepository.findByUser1(principal) == null 
				&& soulmateRepository.findByUser2(principal) == null) {
			return "redirect:/user/solo";
		}
		setDday(principal, req);
		setSoulmate(principal, req);
		
		Soulmate soulmate = soulmateRepository.findByUser1(principal);
		if(soulmate == null)
			soulmate = soulmateRepository.findByUser2(principal);
		Page<Diary> diaries = diaryRepository.findBySoulmate(soulmate, pageable);
		req.setAttribute("diaries", diaries.getContent());
		req.setAttribute("diaryPage", diaries);
		
		return "user/diary";
	}
	
	@GetMapping("/user/diaryForm")
	public String diaryForm(@AuthenticationPrincipal PrincipalDetails principalDetails,
			 HttpServletRequest req) {
		
		User principal = principalDetails.getUser();
		if(soulmateRepository.findByUser1(principal) == null 
				&& soulmateRepository.findByUser2(principal) == null) {
			return "redirect:/user/solo";
		}
		setDday(principal, req);
		setSoulmate(principal, req);
		
		return "user/diaryForm";
	}
	
	@PostMapping("/user/diaryProc")
	public String diaryProc(@AuthenticationPrincipal PrincipalDetails principalDetails,
			String title, String content) {
		
		User principal = principalDetails.getUser();
		Soulmate soulmate = soulmateRepository.findByUser1(principal);
		if(soulmate == null) 
			soulmate = soulmateRepository.findByUser2(principal);
		Diary diary = Diary.builder()
				.writer(principal)
				.soulmate(soulmate)
				.title(title)
				.content(content)
				.build();
		
		diaryRepository.save(diary);
				
		return "redirect:/user/diary";
	}
	
}
