package com.sdsd.mvc.ceboard.model.vo;

import java.util.Date;

import com.sdsd.mvc.ploboard.model.vo.ploboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CEboard {
	private int borId;
	
	private int borWriterNo;
	
	private String borWriterName;
	
	private String borTitle;
	
	private String borContent;
	
	private int borNumber;
	
	private Date createDate;
	
	private Date correctDate;
	
	private int readCount;
	
	private String borStatus;
	
	private String originalFile;
	
	private String renamedFile;
	
}