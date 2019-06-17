package com.tour.bulletin.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tour.exception.DataNotFoundException;
import com.tour.exception.UpdateException;
import com.tour.model.bulletin.domain.BulletinMember;
import com.tour.model.bulletin.service.BulletinMemberService;

@Controller
public class BulletinMemberController {
	@Autowired
	@Qualifier("bulletinMemberServiceImpl")
	private BulletinMemberService memberService;

	// =========================== 회원가입 Page 관련 Method 정호진 (190509 - 정호진
	   // )====================================
	   // 아이디,이메일 중복 체크
	   @RequestMapping(value = "/members/check", method = RequestMethod.GET)
	   @ResponseBody
	   public String check(BulletinMember member) {
	      System.out.println("Member=" + member);

	      BulletinMember result = memberService.check(member);
	      String checkResult ="0";
	      
	      if (result == null) {
	         checkResult="0";
	      }else {
	         checkResult="1";
	      }
	      return checkResult;

	   }
	
	// 로그인 체크
	@RequestMapping(value = "/member/login", method = RequestMethod.POST)
	public String login(BulletinMember member, HttpServletRequest request) {
		System.out.println("로그인 요청한 아이디 : " + member.getId());
		System.out.println("로그인 요청한 비밀번호 : " + member.getPass());
		BulletinMember obj = memberService.loginCheck(member);
		// 세션에 담기
		request.getSession().setAttribute("member", obj);

		return "redirect:/chimper/chimper/index.jsp";
	}

	// 메인 페이지 요청
	@RequestMapping(value = "/member/main", method = RequestMethod.GET)
	public String requestMain(HttpServletRequest request) {
		return "redirect:/board/list";
	}

	// 회원가입
	@RequestMapping(value = "/members/insert", method = RequestMethod.POST)
	public String insert(BulletinMember member) {
		memberService.insert(member);
		System.out.println("회원가입 Controller   insert통과");

		return "redirect:/chimper/chimper/index.jsp";
	}

	// =========================== 관리자 회원관리 Page 관련 Method (190501 - 박현호
	// )====================================
	// 회원 한명 가져오기
	@RequestMapping(value = "/member", method = RequestMethod.GET)
	@ResponseBody
	public String select2(@RequestParam("member_id") int member_id) {
		System.out.println("MemberController : select2() 호출!");
		System.out.println("MemberController : select2() : 화면에 띄울 회원의 고유값 : " + member_id);
		BulletinMember member = memberService.select(member_id);
		System.out.println("MemberController : select2() : 가져온 member 객체 : " + member);
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"member_id\":\"" + member.getMember_id() + "\",");
		sb.append("\"id\":\"" + member.getId() + "\",");
		sb.append("\"pass\":\"" + member.getPass() + "\",");
		sb.append("\"member_name\":\"" + member.getMember_name() + "\",");
		sb.append("\"email\":\"" + member.getEmail() + "\",");
		sb.append("\"member_level_id\":\"" + member.getMemberLevel().getMember_level_id() + "\"");
		sb.append("}");

		return sb.toString();
	}

	// 회원 전체 목록 가져오기
	@RequestMapping(value = "/members", method = RequestMethod.GET)
	public ModelAndView selectAll() {
		System.out.println("MemberController : selectAll() 호출!");
		List<BulletinMember> memberList = memberService.selectAllAdmin();
		ModelAndView mav = new ModelAndView();
		mav.addObject("memberList", memberList);
		mav.setViewName("chimper/chimper/admin/memberManage");

		return mav;
	}

	// 관리자 회원정보 수정!!
	@RequestMapping(value = "/editMember", method = RequestMethod.POST)
	public String updateMember(BulletinMember member) {
		int result = memberService.update(member);

		return "redirect:/members";
	}
	// ========================================================================================

	// =========================== 마이페이지 관련 Method (190501 -
	// 박현호)====================================
	@RequestMapping(value = "/editMyInfo", method = RequestMethod.POST)
	public String updateMyInfo(BulletinMember member, HttpServletRequest request) {
		int result = memberService.update(member);
		BulletinMember editedMember = memberService.select(member.getMember_id());
		request.getSession().setAttribute("member", editedMember);

		return "redirect:/myBoards?member_id=" + member.getMember_id();
	}
	// ========================================================================================

	// =========================== 계정 찾기 관련 Method (190519 -
	// 박현호)====================================
	// 아이디 찾기 Method
	@RequestMapping(value = "/findAccount/findID", method = RequestMethod.GET)
	@ResponseBody
	public String getID(BulletinMember member) {
		System.out.println("BulletinMemberController : getID() 호출!!");
		// System.out.println("BulletinMemberController : getID() : member_name :
		// "+member.getMember_name());
		// System.out.println("BulletinMemberController : getID() : email :
		// "+member.getEmail());

		String id = memberService.getID(member);
		System.out.println("BulletinMemberController : getID() : 찾은 id : " + id);

		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"id\":\"" + id + "\"");
		sb.append("}");

		System.out.println("BulletinMemberController : getID() : 계정 ID 찾기요청에 보낼 JSON : " + sb.toString());
		return sb.toString();
	}

	// 비밀번호 초기화 Method
	@RequestMapping(value = "/findAccount/resetPass", method = RequestMethod.POST)
	@ResponseBody
	public String resetPass(BulletinMember member) {
		System.out.println("BulletinMemberController : resetPass() 호출!!");
		System.out.println("BulletinMemberController : resetPass() : id : " + member.getId());
		System.out.println("BulletinMemberController : resetPass() : email : " + member.getEmail());
		int result = memberService.resetPass(member);

		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"resultCode\":\"" + result + "\"");
		sb.append("}");

		System.out.println("BulletinMemberController : resetPass() : 비밀번호 초기화 요청에 보낼 JSON : " + sb.toString());
		return sb.toString();
	}
	// 예외처리
	// ================================================================
	// 로그인 실패
	@ExceptionHandler(DataNotFoundException.class)
	public ModelAndView loginFail(DataNotFoundException e) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("error", e);
		mav.setViewName("error/errorPage");
		return mav;
	}

	// 회원정보 수정 실패
	@ExceptionHandler(UpdateException.class)
	public ModelAndView loginFail(UpdateException e) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("error", e);
		mav.setViewName("error/errorPage");
		return mav;
	}
	// ================================================================
}
