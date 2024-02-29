package com.oz.ozHouse.dto;

public class CommentDTO {

	private int comment_num;
	private int blog_num;
	private String member_id;
	private String comment_content;
	private String comment_date;
	private int comment_re_step;
	private int comment_re_level;
	
	public int getComment_num() {
		return comment_num;
	}
	public void setComment_num(int comment_num) {
		this.comment_num = comment_num;
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
	public String getComment_content() {
		return comment_content;
	}
	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}
	public String getComment_date() {
		return comment_date;
	}
	public void setComment_date(String comment_date) {
		this.comment_date = comment_date;
	}
	public int getComment_re_step() {
		return comment_re_step;
	}
	public void setComment_re_step(int comment_re_step) {
		this.comment_re_step = comment_re_step;
	}
	public int getComment_re_level() {
		return comment_re_level;
	}
	public void setComment_re_level(int comment_re_level) {
		this.comment_re_level = comment_re_level;
	}
	
}
