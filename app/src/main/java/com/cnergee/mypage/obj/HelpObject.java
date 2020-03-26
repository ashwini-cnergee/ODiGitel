package com.cnergee.mypage.obj;

public class HelpObject {


String text_name="";
int drawable_resource;
int color;
String file_name="";


public HelpObject(String text_name, int drawable_resource, int color,
		String file_name) {
	super();
	this.text_name = text_name;
	this.drawable_resource = drawable_resource;
	this.color = color;
	this.file_name = file_name;
}
public String getText_name() {
	return text_name;
}
public void setText_name(String text_name) {
	this.text_name = text_name;
}
public int getDrawable_resource() {
	return drawable_resource;
}
public void setDrawable_resource(int drawable_resource) {
	this.drawable_resource = drawable_resource;
}
public int getColor() {
	return color;
}
public void setColor(int color) {
	this.color = color;
}
public String getFile_name() {
	return file_name;
}
public void setFile_name(String file_name) {
	this.file_name = file_name;
}


}
