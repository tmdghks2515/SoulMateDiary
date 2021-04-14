package com.anan.Soulmate.controller;


import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.anan.Soulmate.config.auth.PrincipalDetails;
import com.anan.Soulmate.dto.ResponseDTO;
import com.anan.Soulmate.model.Album;
import com.anan.Soulmate.model.Anniversary;
import com.anan.Soulmate.model.Diary;
import com.anan.Soulmate.model.DiaryComment;
import com.anan.Soulmate.model.MainCarousel;
import com.anan.Soulmate.model.Schedule;
import com.anan.Soulmate.model.Soulmate;
import com.anan.Soulmate.model.User;
import com.anan.Soulmate.repository.AlbumRepository;
import com.anan.Soulmate.repository.AnniversaryRepository;
import com.anan.Soulmate.repository.CarouselRepository;
import com.anan.Soulmate.repository.DiaryCommentRepository;
import com.anan.Soulmate.repository.DiaryRepository;
import com.anan.Soulmate.repository.ScheduleRepository;
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
	private final DiaryCommentRepository diaryCommentRepository;
	private final AnniversaryRepository anniversaryRepository;
	private final ScheduleRepository scheduleRepository;
	private final CarouselRepository carouselRepository;
	
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
	
	public void setDefaultAnniversaries(Soulmate soulmate, String Dday) {
		
		Date Ddate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Ddate = sdf.parse(Dday);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(Ddate);
		List<Anniversary> defaultAnniList = new ArrayList<>();
		
		 // 1일
		Anniversary anni1 = Anniversary.builder().anniDate(Ddate).anniName("1일").soulmate(soulmate).build();
		defaultAnniList.add(anni1);
		
		// 50일
		c.add(Calendar.DATE, 49);
		Date D50 = c.getTime();
		Anniversary anni50 = Anniversary.builder().anniDate(D50).anniName("50일").soulmate(soulmate).build();
		defaultAnniList.add(anni50);
		c.add(Calendar.DATE, -49);
		
		// 100일 , 200일 ... 1000일
		for(int i=1 ; i<=10 ; i++) {
			c.add(Calendar.DATE, i*100-1);
			Date date100 = c.getTime();
			Anniversary anni100 = Anniversary.builder().anniDate(date100).anniName(i*100+"일").soulmate(soulmate).build();
			defaultAnniList.add(anni100);
			c.add(Calendar.DATE, -i*100+1);
		}
		
		// 1주년, 2주년 ... 5주년
		for(int i=1 ; i<=5 ; i++) {
			c.set(c.get(Calendar.YEAR)+i, c.get(Calendar.MONTH), c.get(Calendar.DATE));
			Date dateY = c.getTime();
			Anniversary anniY = Anniversary.builder().anniDate(dateY).anniName(i+"주년").soulmate(soulmate).build();
			defaultAnniList.add(anniY);
			c.set(c.get(Calendar.YEAR)-i, c.get(Calendar.MONTH), c.get(Calendar.DATE));
		}
		
		anniversaryRepository.saveAll(defaultAnniList);
		
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
			
			Soulmate soulmateEntity = soulmateRepository.findByUser1(principal);
			if(soulmateEntity == null)
				soulmateRepository.findByUser2(principal);
			setDefaultAnniversaries(soulmateEntity, Dday);
			
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
			@PageableDefault(page=0, size=10,sort="writeDate",direction = Sort.Direction.DESC) 
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
	
	@GetMapping("/user/diaryDetail")
	public String diaryDetail(Long id, HttpServletRequest req,
			@AuthenticationPrincipal PrincipalDetails principalDetails,
			@PageableDefault(page=0, size=5,sort="writeDate",direction = Sort.Direction.DESC) 
			Pageable pageable) {
		
		User principal = principalDetails.getUser();
		if(soulmateRepository.findByUser1(principal) == null 
				&& soulmateRepository.findByUser2(principal) == null) {
			return "redirect:/user/solo";
		}
		setDday(principal, req);
		setSoulmate(principal, req);
		
		Diary diary = diaryRepository.findById(id).orElseThrow(() -> 
			new IllegalArgumentException("해당 일기는 존재하지 않습니다"));
		req.setAttribute("diary", diary);
		
		Page<DiaryComment> cmtListPage = diaryCommentRepository.findByDiary(diary, pageable);
		req.setAttribute("cmtList", cmtListPage.getContent());
		req.setAttribute("cmtListPage", cmtListPage);
		
		return "user/diaryDetail";
	}
	
	@PostMapping("/user/writeComment")
	public String writeComment(Long id, String content, 
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User principal = principalDetails.getUser();
		Diary diary = diaryRepository.findById(id).orElseThrow(() -> 
				new IllegalArgumentException("해당 일기는 존재하지 않습니다"));
		
		DiaryComment comment = DiaryComment.builder()
				.content(content)
				.writer(principal)
				.diary(diary)
				.build();
		
		diaryCommentRepository.save(comment);
		
		return "redirect:/user/diaryDetail?id="+id;
	}
	
	@GetMapping("/user/anniversary")
	public String anniversary(HttpServletRequest req,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		
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
		
		List<Anniversary> anniList =  anniversaryRepository.findBySoulmate(soulmate, Sort.by(Direction.ASC, "anniDate"));
		req.setAttribute("anniList", anniList);
		
		return "user/anniversary";
	}
	
	@GetMapping("/user/calendar")
	public String calendar(HttpServletRequest req,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		User principal = principalDetails.getUser();
		if(soulmateRepository.findByUser1(principal) == null 
				&& soulmateRepository.findByUser2(principal) == null) {
			return "redirect:/user/solo";
		}
		setDday(principal, req);
		setSoulmate(principal, req);
		
		return "user/calendar";
	}
	
     // 스케듈 등록하는 메서드
	@PostMapping("/user/saveSchedule")
	public String saveSchedule(String date, String time, String name,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		// principal 의 Soulmate 객체 가져오기
		User principal = principalDetails.getUser();
		Soulmate soulmate = soulmateRepository.findByUser1(principal);
		if(soulmate == null)
			soulmate = soulmateRepository.findByUser2(principal);
		
		// 받아온 데이터 Date 타입으로 파싱
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String str = date+" "+time;
		Date scheduleTime = null;
		try {
			scheduleTime= sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		// Schedule 객체 생성
		Schedule schedule = Schedule.builder()
				.scheduleName(name)
				.scheduleTime(scheduleTime)
				.soulmate(soulmate)
				.build();
		
		// DB에 Schedule 객체 저장
		scheduleRepository.save(schedule);
		
		return "redirect:/user/calendar";
	}
	
	@PostMapping("/admin/changeCarouselImg")
	public String changeCarouselImg(int page, MultipartHttpServletRequest req) {
		
		MultipartFile file = req.getFile("file");
		String path = "C:\\Soulmate\\main\\"+page;
		try {
			file.transferTo(new File(path));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "redirect:/";
	}
	
}
