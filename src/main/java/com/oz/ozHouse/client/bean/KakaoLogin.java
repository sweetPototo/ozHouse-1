package com.oz.ozHouse.client.bean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.*;

import com.oz.ozHouse.client.service.MemberMapper;
import com.oz.ozHouse.dto.MemberDTO;

@Service
public class KakaoLogin {
	@Autowired
	private MemberMapper memberMapper;

	public String getAccessToken (String authorize_code) {
		String access_Token = "";
		String refresh_Token = "";
		String reqURL = "https://kauth.kakao.com/oauth/token";
		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			StringBuilder sb = new StringBuilder();
			sb.append("grant_type=authorization_code");
			sb.append("&client_id=e5b283df9616f7c21f3e15db5f9b0df2"); // �듅�씤 �궎
			sb.append("&redirect_uri=http://localhost:8080/ozHouse/kakao_login.do"); // url
			sb.append("&code=" + authorize_code);
			bw.write(sb.toString());
			bw.flush();
			int responseCode = conn.getResponseCode();
			System.out.println("responseCode : " + responseCode);
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			String result = "";
			while ((line = br.readLine()) != null) {
				result += line;
			}
			System.out.println("response body : " + result);
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(result);
			access_Token = element.getAsJsonObject().get("access_token").getAsString();
			refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();
			System.out.println("access_token : " + access_Token);
			System.out.println("refresh_token : " + refresh_Token);
			br.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return access_Token;
	}
	
	// 카카占쏙옙 占싸깍옙占쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙
	public MemberDTO getUserInfo(String access_Token) {
		 //    占쏙옙청占싹댐옙 클占쏙옙占싱억옙트占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쌕몌옙 占쏙옙 占쌍기에 HashMap타占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙
		HashMap<String, String> userInfo = new HashMap<String, String>();
		String reqURL = "https://kapi.kakao.com/v2/user/me";
		
		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "Bearer " + access_Token);
			int responseCode = conn.getResponseCode();
			System.out.println("responseCode : " + responseCode);
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			String result = "";
			while ((line = br.readLine()) != null) {
				result += line;
			}
			System.out.println("response body : " + result);
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(result);
			JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();

			JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
			String nickname = properties.getAsJsonObject().get("nickname").toString();
			String email = kakao_account.getAsJsonObject().get("email").getAsString();
			String profile_image = properties.getAsJsonObject().get("profile_image").getAsString();
			userInfo.put("member_email", email);
			userInfo.put("member_nickname", nickname);		
			userInfo.put("member_image", profile_image);
		} catch (IOException e) {
			e.printStackTrace();
		}
			// catch �븘�옒 肄붾뱶 異붽�.
			MemberDTO dto = memberMapper.findkakao(userInfo);
			
			if(dto==null) {
			// result媛� null�씠硫� �젙蹂닿� ���옣�씠 �븞�릺�엳�뒗嫄곕�濡� �젙蹂대�� ���옣.
				dto = new MemberDTO(); 
				dto.setMember_email(userInfo.get("member_email"));
				dto.setMember_nickname(userInfo.get("member_nickname"));
				dto.setMember_image(userInfo.get("member_image"));
				return dto;
			} else {
				return dto;
				// �젙蹂닿� �씠誘� �엳湲� �븣臾몄뿉 result瑜� 由ы꽩�븿.
			}
	        
		}
}
