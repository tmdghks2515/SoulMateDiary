package com.anan.Soulmate.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.management.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anan.Soulmate.config.auth.PrincipalDetails;
import com.anan.Soulmate.dto.ResponseDTO;
import com.anan.Soulmate.model.Album;
import com.anan.Soulmate.model.Anniversary;
import com.anan.Soulmate.model.MainCarousel;
import com.anan.Soulmate.model.Schedule;
import com.anan.Soulmate.model.Soulmate;
import com.anan.Soulmate.model.User;
import com.anan.Soulmate.repository.AlbumRepository;
import com.anan.Soulmate.repository.AnniversaryRepository;
import com.anan.Soulmate.repository.CarouselRepository;
import com.anan.Soulmate.repository.ScheduleRepository;
import com.anan.Soulmate.repository.SoulmateRepository;
import com.anan.Soulmate.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;

@RestController
@RequiredArgsConstructor
public class diaryApiController {
	
	private final AlbumRepository albumRepository;
	private final SoulmateRepository soulmateRepository;
	private final AnniversaryRepository anniversaryRepository;
	private final ScheduleRepository scheduleRepository;
	private final CarouselRepository carouselRepository;
	
	@RequestMapping("/user/showImg")
	public int showimg(String soulmateId, String title, Integer no, HttpServletResponse res) {
		if(no == null)
			no = 0;
		String path = "C:\\Soulmate\\album\\" + soulmateId + "\\" + title;
		File folder = new File(path);
		File[] fLi = folder.listFiles();
		File file = fLi[no];
		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(res.getOutputStream());
			byte[] buffer = new byte[1024 * 1024 * 50];
			while(true){
				int size = fis.read(buffer);//읽어온 바이트수
				if(size == -1) break;//더이상 읽을 내용이 없다
				bos.write(buffer,0,size);
				bos.flush();
		}
			fis.close();
			bos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		res.setContentType(MediaType.TEXT_HTML_VALUE);
		return fLi.length;
	}

	@GetMapping("/user/showImgs")
	@Transactional
	public String showImgs(@AuthenticationPrincipal PrincipalDetails principalDetails,Long soulmateId, String title, HttpServletResponse res) {
		
		JSONObject result = new JSONObject();
		Album album = albumRepository.findBySoulmateIdAndTitle(soulmateId, title);
		
		String path = "C:\\Soulmate\\album\\" + soulmateId + "\\" + title;
		File folder = new File(path);
		File[] fLi = folder.listFiles();
		
		int len = fLi.length;
		String content = album.getContent();
		String date = album.getCreateDate().toString().substring(0, 10);
		result.put("len", len);
		result.put("content", content);
		result.put("date", date);
		
		return result.toString();
	}
	
	@GetMapping("/user/doSearch")
	public String doSearch(@AuthenticationPrincipal PrincipalDetails principalDetails, 
			String title) {
		User principal = principalDetails.getUser();
		Soulmate soulmate = soulmateRepository.findByUser1(principal);
		if(soulmate == null)
			soulmate = soulmateRepository.findByUser2(principal);
		
		List<Album> albumList = albumRepository.findAlbumWithPartOfTitle(title, soulmate.getId());
		System.out.println(albumList.size());
		JSONObject resp = new JSONObject();
		JSONArray ja = new JSONArray();
		for(int i=0; i<albumList.size(); i++) {
			JSONObject temp = new JSONObject();
			Album albumTemp = albumList.get(i);
			temp.put("title", albumTemp.getTitle());
			temp.put("content", albumTemp.getContent());
			temp.put("createDate", albumTemp.getCreateDate());
			temp.put("soulmateId", albumTemp.getSoulmate());
			ja.add(albumTemp);
		}
		resp.put("result", ja);
		return resp.toString();
	}
	
	@GetMapping("/user/solo")
	public String solo() {
		return "<script>alert('연결된 소울메이트가 없습니다');"
				+"history.back();</script>";
	}
	
	@PostMapping("/user/addAnniversary")
	public String addAnniversary(Anniversary anni, String date, HttpServletRequest req,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		Date anniDate = null;
		try {
			anniDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		
		User principal = principalDetails.getUser();
		Soulmate soulmate = soulmateRepository.findByUser1(principal);
		if(soulmate == null)
			soulmateRepository.findByUser2(principal);
		
		anni.setAnniDate(anniDate);
		anni.setSoulmate(soulmate);
		
		anniversaryRepository.save(anni);
		
		return null;
	}
	
	@Transactional
	@PostMapping("/user/saveStory")
	public String saveStory(Long id, String story) {
		
		Anniversary anni = anniversaryRepository.findById(id).orElseThrow(() -> 
				new IllegalArgumentException("해당 기념일은 없습니다"));
		anni.setStory(story);
		anniversaryRepository.save(anni);
		
		ResponseDTO<String> respDto = new ResponseDTO<String>(200, "저장성공");
		JSONObject resp = new JSONObject(respDto);
		return resp.toString();
	}
	
	@GetMapping("/user/getMonthAnni")
	public String getMonthAnni(int anniYear, int anniMonth,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		// principal 의 soulmate 불러오기
		User principal = principalDetails.getUser();
		Soulmate soulmate = soulmateRepository.findByUser1(principal);
		if(soulmate == null)
			soulmate = soulmateRepository.findByUser1(principal);
		
		// 해당 soulmate 의 anniversary entity 리스트 JsonObject에 담아주기
		List<Anniversary> anniList = anniversaryRepository.findByYearAndMonth(anniYear, anniMonth, soulmate.getId());
		JSONObject resp = new JSONObject();
		resp.put("result", anniList);
		
		// 해당 soulmate 의  schedule entity 리스트 JsonObject에 담아주기
		List<Schedule> scheduleList = scheduleRepository.findByYearAndMonth(anniYear, anniMonth, soulmate.getId());
		resp.put("scheduleList", scheduleList);
		
		return resp.toString();
	}
	
	@GetMapping("/getCarousels")
	public String getCarousels() {
		
		JSONObject resp = new JSONObject();
		List<MainCarousel> carouselList = (List<MainCarousel>) carouselRepository.findAll();
		if(carouselList == null || carouselList.size() == 0) {
			for(int i=1; i<=3; i++) {
				MainCarousel MC = MainCarousel.builder().page(i).build();
				carouselList.add(MC);
			}
			carouselRepository.saveAll(carouselList);
			carouselList = (List<MainCarousel>) carouselRepository.findAll();
		}
		resp.put("result", carouselList);
		
		return resp.toString();
	}
	
	@PostMapping("/admin/updateCarousel")
	public String updateCarousel(String title, String content, int page) {
		
		// 해당 page의 캐러셀 entity를 불러와서 title, content 변경후 저장.
		MainCarousel carousel = carouselRepository.findById(page).orElseThrow(() ->
			new IllegalArgumentException("해당 캐러셀 없음"));
		carousel.setTitle(title);
		carousel.setContent(content);
		System.out.println(carousel);
		System.out.println("/////////////////////////");
		carouselRepository.save(carousel);
		
		ResponseDTO<String> res = new ResponseDTO<>();
		res.setResponseCode(200);
		res.setResponseMessage("변경에 성공했습니다.");
		JSONObject resp = new JSONObject(res);
		
		return resp.toString();
	}
	
	@GetMapping("/carouselImg")
	public String carouselImg(int page, HttpServletResponse res) {
		String path = "C:\\Soulmate\\main\\";
		File folder = new File(path);
		File[] fList = folder.listFiles();
		File file = fList[page-1];
		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(res.getOutputStream());
			byte[] buffer = new byte[1024 * 1024 * 50];
			while(true){
				int size = fis.read(buffer);//읽어온 바이트수
				if(size == -1) break;//더이상 읽을 내용이 없다
				bos.write(buffer,0,size);
				bos.flush();
		}
			fis.close();
			bos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		res.setContentType(MediaType.TEXT_HTML_VALUE);
		
		return null;
	}
	
}
