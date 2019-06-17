package com.tour.common.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class MemberSessionAspect {

	/*
	 * @Pointcut("execution(public * com.tour.controller.MemberController..*(..))")
	 * public void checkAccount() { //ȸ�� �������� �ƴ��� ���� Ȯ�� }
	 * 
	 * @Pointcut("execution(public * com.tour.controller.RestReviewController..*(..))"
	 * ) public void checkRestAccount() { //ȸ�� �������� �ƴ��� ���� Ȯ�� }
	 */

	//����üũ�� ���ϴ� URL�迭
	String[] exceptList = {
		"/member/login",
		"/client/index",
		"/review/delete",
		"/review/list",
		"/review/detail"
	};
	
	//
	@Around("checkAccount()")
	public String loginCheck(ProceedingJoinPoint joinPoint) throws Throwable{
		HttpServletRequest request = null;
		String viewName = null; 
		Object[] objArray =  joinPoint.getArgs();
		String requestURL = null;
		int count = 0; //���ܸ�ܿ� �����ϴ� URL�� ��� ī��Ʈ ����
		for(Object obj:objArray) {
			if(obj instanceof HttpServletRequest) {
				request = (HttpServletRequest)obj;
				requestURL=request.getRequestURL().toString();
				System.out.println("���� ��û�� URL�� "+requestURL+"������ URL : "+requestURL.endsWith("/member/login"));
			
				for(int i=0; i<exceptList.length; i++) {
					if(requestURL.endsWith(exceptList[i])) { //���ܸ�� �߰�
						count++;
					}
				}
			}
		}
		if(request != null && count < 1) { //request��ü�� ���� �޼��常 �����ϱ�
			
			if(request.getSession().getAttribute("member") == null) { //member������ �������� �ʴ´ٸ�,
				System.out.println("������ �������� ����");
				viewName = "client/login/error";
			}else {//member ������ �ִٸ� ������� �״��!!
				viewName = (String)joinPoint.proceed();
				String methodName = joinPoint.getSignature().getName();
				System.out.println("�α��� �ʿ� // ���� �����Ϸ��� �ߴ� �޼��� : "+methodName);
			}
			
		}else {
			viewName = (String)joinPoint.proceed();
			String methodName = joinPoint.getSignature().getName();
			System.out.println("�α��� �� �ʿ� // ���� �����Ϸ��� �ߴ� �޼��� : "+methodName);
		}
		count = 0;
		return viewName;
	}
}
