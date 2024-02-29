package com.oz.ozHouse.dto;

public class BlogDTO {

	private int blog_num;
	private String member_id;
	private String blog_subject;
	private String blog_content;
	private String blog_image;
	private String blog_date;
	private int blog_like;
	private String blog_room_type;
	private int readcount;
	

	public String getBlog_room_type() {
		return blog_room_type;
	}
	public void setBlog_room_type(String blog_room_type) {
		this.blog_room_type = blog_room_type;
	}
	public int getBlog_num() {
		return blog_num;
	}
	public void setBlog_num(int blog_num) {
		this.blog_num = blog_num;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getBlog_subject() {
		return blog_subject;
	}
	public void setBlog_subject(String blog_subject) {
		this.blog_subject = blog_subject;
	}
	public String getBlog_content() {
		return blog_content;
	}
	public void setBlog_content(String blog_content) {
		this.blog_content = blog_content;
	}
	public String getBlog_image() {
		return blog_image;
	}
	public void setBlog_image(String blog_image) {
		this.blog_image = blog_image;
	}
	public String getBlog_date() {
		return blog_date;
	}
	public void setBlog_date(String blog_date) {
		this.blog_date = blog_date;
	}
	public int getBlog_like() {
		return blog_like;
	}
	public void setBlog_like(int blog_like) {
		this.blog_like = blog_like;
	}
	public int getReadcount() {
		return readcount;
	}
	public void setReadcount(int readcount) {
		this.readcount = readcount;
	}
	
}
