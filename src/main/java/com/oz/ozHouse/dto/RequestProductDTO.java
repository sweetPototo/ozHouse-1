package com.oz.ozHouse.dto;

public class RequestProductDTO {
	private int product_num;
	private String product_name;
	private int category_num;
	private String product_image;
	private String product_image_pro;
	private int product_quantity;
	private int product_price;
	private String product_modifier;
	private int product_point;
	private int product_delivery_charge;
	private int product_discount_rate;
	private int product_discount_price;
	private int product_assembly_cost;
	private String product_image_change;
	private String product_image_pro_change;
	private int mer_num;
	private String category_name;
	private String encodedImage;
	
	public String getEncodedImage() {
		return encodedImage;
	}
	public void setEncodedImage(String encodedImage) {
		this.encodedImage = encodedImage;
	}
	
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public int getMer_num() {
		return mer_num;
	}
	public void setMer_num(int mer_num) {
		this.mer_num = mer_num;
	}
	public String getProduct_image_change() {
		return product_image_change;
	}
	public void setProduct_image_change(String product_image_change) {
		this.product_image_change = product_image_change;
	}
	
	public String getProduct_image_pro_change() {
		return product_image_pro_change;
	}
	public void setProduct_image_pro_change(String product_image_pro_change) {
		this.product_image_pro_change = product_image_pro_change;
	}
	public int getProduct_assembly_cost() {
		return product_assembly_cost;
	}
	public void setProduct_assembly_cost(int product_assembly_cost) {
		this.product_assembly_cost = product_assembly_cost;
	}
	public int getProduct_num() {
		return product_num;
	}
	public void setProduct_num(int product_num) {
		this.product_num = product_num;
	}
	public int getProduct_delivery_charge() {
		return product_delivery_charge;
	}
	public void setProduct_delivery_charge(int product_delivery_charge) {
		this.product_delivery_charge = product_delivery_charge;
	}
	public int getProduct_discount_rate() {
		return product_discount_rate;
	}
	public void setProduct_discount_rate(int product_discount_rate) {
		this.product_discount_rate = product_discount_rate;
	}
	public int getProduct_discount_price() {
		return product_discount_price;
	}
	public void setProduct_discount_price(int product_discount_price) {
		this.product_discount_price = product_discount_price;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public int getCategory_num() {
		return category_num;
	}
	public void setCategory_num(int category_num) {
		this.category_num = category_num;
	}
	public String getProduct_image() {
		return product_image;
	}
	public void setProduct_image(String product_image) {
		this.product_image = product_image;
	}
	public String getProduct_image_pro() {
		return product_image_pro;
	}
	public void setProduct_image_pro(String product_image_pro) {
		this.product_image_pro = product_image_pro;
	}
	public int getProduct_quantity() {
		return product_quantity;
	}
	public void setProduct_quantity(int product_quantity) {
		this.product_quantity = product_quantity;
	}
	public int getProduct_price() {
		return product_price;
	}
	public void setProduct_price(int product_price) {
		this.product_price = product_price;
	}
	public String getProduct_modifier() {
		return product_modifier;
	}
	public void setProduct_modifier(String product_modifier) {
		this.product_modifier = product_modifier;
	}
	public int getProduct_point() {
		return product_point;
	}
	public void setProduct_point(int product_point) {
		this.product_point = product_point;
	}
}
