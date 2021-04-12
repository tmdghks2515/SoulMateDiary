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
import com.anan.Soulmate.model.Soulmate;
import com.anan.Soulmate.model.User;
import com.anan.Soulmate.repository.AlbumRepository;
import com.anan.Soulmate.repository.AnniversaryRepository;
import com.anan.Soulmate.repository.SoulmateRepository;
import com.anan.Soulmate.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;

@RestController
@RequiredArgsConstructor
public class diaryApiController {
	
	private final UserRepository userRepository;
	private final AlbumRepository albumRepository;
	private final SoulmateRepository soulmateRepository;
	private final AnniversaryRepository anniversaryRepository;
	
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
	public String getMonthAnni(int anniYear, int anniMonth) {
		
		System.out.println(anniYear+" "+anniMonth);
		List<Anniversary> anniList = anniversaryRepository.findByYearAndMonth(anniYear, anniMonth);
		JSONObject resp = new JSONObject();
		System.out.println(anniList);
		resp.put("result", anniList);
		
		return resp.toString();
	}
	
}
