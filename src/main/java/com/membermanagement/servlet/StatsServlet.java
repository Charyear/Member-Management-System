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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/stats/*")
public class StatsServlet extends HttpServlet {
    private MemberService memberService;
    private static final Logger LOGGER = Logger.getLogger(StatsServlet.class.getName());

    @Override
    public void init() throws ServletException {
        memberService = new MemberServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int pageSize = 6;
            int pageNum = 1;
            String memberType = request.getParameter("memberType");
            if (memberType == null || memberType.trim().isEmpty()) {
                memberType = "金"; // 默认显示金卡会员
            }
            
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
            
            // 获取所有统计数据（不受会员类型筛选影响）
            int totalMembers = memberService.getTotalMembers();
            Map<String, Integer> typeDistribution = memberService.getMemberTypeDistribution();
            double totalConsumption = memberService.getTotalConsumption();
            int totalPoints = memberService.getTotalPoints();
            
            // 获取指定类型会员的消费排行
            List<Member> members = memberService.getMembersByTypeAndConsumption(memberType, pageNum, pageSize);
            int typeMembers = memberService.getTotalMembersByType(memberType);
            int totalPages = (int) Math.ceil((double) typeMembers / pageSize);
            
            // 设置请求属性
            request.setAttribute("members", members);
            request.setAttribute("currentPage", pageNum);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("selectedType", memberType);
            
            // 设置统计数据
            request.setAttribute("totalMembers", totalMembers);
            request.setAttribute("typeDistribution", typeDistribution);
            request.setAttribute("totalConsumption", totalConsumption);
            request.setAttribute("totalPoints", totalPoints);
            
            request.getRequestDispatcher("/WEB-INF/views/stats/report.jsp")
                   .forward(request, response);
                   
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "处理统计报表页面请求时发生错误", e);
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
}
