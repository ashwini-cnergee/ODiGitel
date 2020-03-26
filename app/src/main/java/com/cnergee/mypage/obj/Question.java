package com.cnergee.mypage.obj;

import java.util.ArrayList;

public class Question {

	String Question_Id,Question_Text, Solution_Image,Solution_Text,AnswerType;
	ArrayList<Answer> alAnswers;
	
	
	
	public Question(String question_Id, String question_Text,
			String solution_Image, String solution_Text, String answerType) {
		
		Question_Id = question_Id;
		Question_Text = question_Text;
		Solution_Image = solution_Image;
		Solution_Text = solution_Text;
		AnswerType = answerType;
	}
	public String getQuestion_Id() {
		return Question_Id;
	}
	public void setQuestion_Id(String question_Id) {
		Question_Id = question_Id;
	}
	public String getQuestion_Text() {
		return Question_Text;
	}
	public void setQuestion_Text(String question_Text) {
		Question_Text = question_Text;
	}
	public String getSolution_Image() {
		return Solution_Image;
	}
	public void setSolution_Image(String solution_Image) {
		Solution_Image = solution_Image;
	}
	public String getSolution_Text() {
		return Solution_Text;
	}
	public void setSolution_Text(String solution_Text) {
		Solution_Text = solution_Text;
	}
	public String getAnswerType() {
		return AnswerType;
	}
	public void setAnswerType(String answerType) {
		AnswerType = answerType;
	}
	public ArrayList<Answer> getAlAnswers() {
		return alAnswers;
	}
	public void setAlAnswers(ArrayList<Answer> alAnswers) {
		this.alAnswers = alAnswers;
	}
	
	
}
