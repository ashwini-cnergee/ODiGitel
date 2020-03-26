package com.cnergee.mypage.obj;

public class Answer {

	String Ans_Text,Ans_Id, Ans_Ques_Id;
	

	public Answer(String ans_Text, String ans_Id, String ans_Ques_Id) {
	
		Ans_Text = ans_Text;
		Ans_Id = ans_Id;
		Ans_Ques_Id = ans_Ques_Id;
	}

	public String getAns_Text() {
		return Ans_Text;
	}

	public void setAns_Text(String ans_Text) {
		Ans_Text = ans_Text;
	}

	public String getAns_Id() {
		return Ans_Id;
	}

	public void setAns_Id(String ans_Id) {
		Ans_Id = ans_Id;
	}

	public String getAns_Ques_Id() {
		return Ans_Ques_Id;
	}

	public void setAns_Ques_Id(String ans_Ques_Id) {
		Ans_Ques_Id = ans_Ques_Id;
	}
	
	

}
