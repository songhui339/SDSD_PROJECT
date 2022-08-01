package com.sdsd.mvc.groupboard.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.sdsd.mvc.common.util.FileRename;
import com.sdsd.mvc.groupboard.model.service.GroupBoardService;
import com.sdsd.mvc.groupboard.model.vo.GroupBoard;
import com.sdsd.mvc.indiboard.model.vo.IndiBoard;
import com.sdsd.mvc.member.model.vo.Member;

import oracle.net.aso.g;

@WebServlet("/groupboard/groupwrite")
public class GroupBoardWriteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GroupBoardWriteServlet() {
    }

    // 모임 플로깅 인증 게시글 작성 서블릿
    
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession(false);
    	Member loginMember = (session == null) ? null : (Member) session.getAttribute("loginMember");
    	
    	if (loginMember != null) {    		
    		request.getRequestDispatcher("/views/groupboard/").forward(request, response);    		
    	} else {
    		request.setAttribute("msg", "로그인 후 사용할 수 있습니다.");
    		request.setAttribute("location", "/views/login.jsp");
    		request.getRequestDispatcher("/views/common/msg.jsp").forward(request, response);
    	} 
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int result = 0;
    	GroupBoard groupBoard = null;
    	// 파일이 저장될 경로
    	String path = getServletContext().getRealPath("/resources/upload/board");
    	
    	// 파일의 최대 사이즈 지정 (10MB)
    	int maxSize = 10485760;
    	
    	// 인코딩 설정
    	String encoding = "UTF-8";
    	
    	MultipartRequest mr = new MultipartRequest(request, path, maxSize, encoding, new FileRename());
    	
    	// 폼 파라미터로 넘어온 값들 (파일에 대한 정보 X)
    	String writer = mr.getParameter("nickName");
    	String content = mr.getParameter("content");
//    	String title = mr.getParameter("title");
    	
    	// 플로깅 모임명을 어떻게 할지 고민 중
//    	String groupName = mr.getParameter("groupName");
    	
    	String originalFileName = mr.getOriginalFileName("upfile");
    	
    	System.out.println(writer);
    	// 파일에 대한 정보를 가져올 때
    	
       	HttpSession session = request.getSession(false);
    	Member loginMember = (session == null) ? null : (Member) session.getAttribute("loginMember");
    	
    	if (loginMember != null) {
    		groupBoard = new GroupBoard();
    		
    		groupBoard.setWriterNo(loginMember.getNo());
    		groupBoard.setWriterName(writer);
    		groupBoard.setBorContent(content);
    		groupBoard.setBorFile(originalFileName);
//    		groupBoard.setBorTitle(title); // 게시글 title
//    		groupBoard.setGroupName(groupName); // 플로깅 모임명 아직 미정
    		
    		result = new GroupBoardService().save(groupBoard);
    		
    	}
	}

}
