package com.membermanagement.servlet;

import com.membermanagement.model.Member;
import com.membermanagement.service.MemberService;
import com.membermanagement.service.impl.MemberServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/points/*")
public class PointsServlet extends HttpServlet {
    private MemberService memberService;

    @Override
    public void init() throws ServletException {
        memberService = new MemberServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int pageSize = 7;  // 修改为6条每页，确保内容不会超出视图
            int pageNum = 1;
            
            String pageStr = request.getParameter("page");
            if (pageStr != null && !pageStr.trim().isEmpty()) {
                try {
                    pageNum = Integer.parseInt(pageStr);
                    if (pageNum < 1) {
                        pageNum = 1;
                    }
                } catch (NumberFormatException e) {
                    // 使用默认页码 1
                }
            }
            
            // 获取按积分降序排序的会员列表
            List<Member> members = memberService.getMembersByPointsDesc(pageNum, pageSize);
            int totalMembers = memberService.getTotalMembers();
            int totalPages = (int) Math.ceil((double) totalMembers / pageSize);
            
            request.setAttribute("members", members);
            request.setAttribute("currentPage", pageNum);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("totalMembers", totalMembers);
            
            request.getRequestDispatcher("/WEB-INF/views/points/manage.jsp")
                   .forward(request, response);
                   
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "获取积分数据时发生错误");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String memberId = request.getParameter("memberId");
        double spendAmount = Double.parseDouble(request.getParameter("spendAmount"));
        
        if (memberService.updatePoints(memberId, spendAmount)) {
            response.sendRedirect(request.getContextPath() + "/points/");
        } else {
            request.setAttribute("error", "更新积分失败");
            doGet(request, response);
        }
    }
}
