package com.membermanagement.servlet;

import com.membermanagement.model.Member;
import com.membermanagement.service.MemberService;
import com.membermanagement.service.impl.MemberServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class MemberServlet extends HttpServlet {
    private MemberService memberService;
    private static final Logger LOGGER = Logger.getLogger(MemberServlet.class.getName());

    @Override
    public void init() throws ServletException {
        memberService = new MemberServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        System.out.println("GET PathInfo: " + pathInfo);
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // 获取分页参数，每页显示9条记录
            int pageSize = 9;
            int pageNum = 1;
            try {
                String pageStr = request.getParameter("page");
                if (pageStr != null && !pageStr.trim().isEmpty()) {
                    pageNum = Integer.parseInt(pageStr);
                    if (pageNum < 1) pageNum = 1;
                }
            } catch (NumberFormatException e) {
                pageNum = 1;
            }
            
            // 获取搜索关键词
            String keyword = request.getParameter("keyword");
            List<Member> members;
            int totalMembers;
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                // 如果有搜索关键词，执行搜索
                members = memberService.searchMembers(keyword);
                totalMembers = members.size();
                request.setAttribute("keyword", keyword);
            } else {
                // 否则显示分页会员列表
                members = memberService.getMembersByPage(pageNum, pageSize);
                totalMembers = memberService.getTotalMembers();
            }
            
            // 计算总页数
            int totalPages = (int) Math.ceil((double) totalMembers / pageSize);
            
            // 获取会员类型分布
            Map<String, Integer> typeDistribution = memberService.getMemberTypeDistribution();
            
            // 获取总消费金额
            double totalConsumption = memberService.getTotalConsumption();
            // 获取总积分
            int totalPoints = memberService.getTotalPoints();
            
            request.setAttribute("members", members);
            request.setAttribute("currentPage", pageNum);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("totalMembers", totalMembers);
            request.setAttribute("memberTypeDistribution", typeDistribution);
            request.setAttribute("totalConsumption", totalConsumption);
            request.setAttribute("totalPoints", totalPoints);
            
            request.getRequestDispatcher("/WEB-INF/views/member/list.jsp").forward(request, response);
        } else if (pathInfo.equals("/add")) {
            // 显示添加会员表单
            request.getRequestDispatcher("/WEB-INF/views/member/add.jsp").forward(request, response);
        } else if (pathInfo.equals("/edit")) {
            // 显示编辑会员表单
            String memberId = request.getParameter("member_id");
            if (memberId != null && !memberId.trim().isEmpty()) {
                Member member = memberService.getMemberById(memberId);
                if (member != null) {
                    request.setAttribute("member", member);
                    request.getRequestDispatcher("/WEB-INF/views/member/edit.jsp").forward(request, response);
                    return;
                }
            }
            response.sendRedirect(request.getContextPath() + "/member/");
        } else if (pathInfo.equals("/delete")) {
            // 处理删除会员
            String memberId = request.getParameter("member_id");
            if (memberService.deleteMember(memberId)) {
                response.sendRedirect(request.getContextPath() + "/member/");
            } else {
                response.sendRedirect(request.getContextPath() + "/member/");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        LOGGER.info("处理POST请求: " + pathInfo);
        
        if (pathInfo != null && pathInfo.equals("/add")) {
            // 添加新会员
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String type = request.getParameter("type");
            
            Member newMember = new Member();
            newMember.setName(name);
            newMember.setPhone(phone);
            newMember.setType(type);
            newMember.setConsumption(0.0);
            newMember.setPoints(0);
            newMember.setMemberId(memberService.generateMemberId());
            
            if (memberService.addMember(newMember)) {
                response.sendRedirect(request.getContextPath() + "/member/");
            } else {
                request.setAttribute("error", "添加会员失败");
                request.getRequestDispatcher("/WEB-INF/views/member/add.jsp").forward(request, response);
            }
        } else if (pathInfo != null && pathInfo.equals("/edit")) {
            try {
                // 更新会员信息
                String memberId = request.getParameter("member_id");
                String name = request.getParameter("name");
                String phone = request.getParameter("phone");
                String type = request.getParameter("type");
                
                LOGGER.info("更新会员信息 - ID: " + memberId + ", 姓名: " + name + ", 电话: " + phone + ", 类型: " + type);
                
                // 获取现有会员信息
                Member existingMember = memberService.getMemberById(memberId);
                if (existingMember == null) {
                    LOGGER.warning("未找到会员: " + memberId);
                    request.setAttribute("error", "未找到指定会员");
                    request.getRequestDispatcher("/WEB-INF/views/member/edit.jsp").forward(request, response);
                    return;
                }
                
                // 更新会员信息，保留原有的消费金额和积分
                Member updatedMember = new Member();
                updatedMember.setMemberId(existingMember.getMemberId()); // 查询会员ID
                updatedMember.setName(name);
                updatedMember.setPhone(phone);
                updatedMember.setType(type);
                updatedMember.setConsumption(existingMember.getConsumption());
                updatedMember.setPoints(existingMember.getPoints());
                
                LOGGER.info("尝试更新会员: " + updatedMember);
                
                try {
                    if (memberService.updateMember(updatedMember)) {
                        LOGGER.info("会员更新成功: " + memberId);
                        response.sendRedirect(request.getContextPath() + "/member/");
                        return;
                    } else {
                        LOGGER.warning("会员更新失败: " + memberId);
                        request.setAttribute("error", "更新会员失败，请重试");
                        request.setAttribute("member", updatedMember);
                        request.getRequestDispatcher("/WEB-INF/views/member/edit.jsp").forward(request, response);
                    }
                } catch (RuntimeException e) {
                    LOGGER.severe("更新会员时发生数据库错误: " + e.getMessage());
                    request.setAttribute("error", "数据库操作失败：" + e.getMessage());
                    request.setAttribute("member", updatedMember);
                    request.getRequestDispatcher("/WEB-INF/views/member/edit.jsp").forward(request, response);
                }
            } catch (Exception e) {
                LOGGER.severe("更新会员时发生错误: " + e.getMessage());
                e.printStackTrace();
                request.setAttribute("error", "系统错误：" + e.getMessage());
                request.getRequestDispatcher("/WEB-INF/views/member/edit.jsp").forward(request, response);
            }
        } else if (pathInfo != null && pathInfo.equals("/updatePoints")) {
            try {
                // 更新积分
                String memberId = request.getParameter("member_id");
                double spendAmount = Double.parseDouble(request.getParameter("spendAmount"));
                
                if (memberService.updatePoints(memberId, spendAmount)) {
                    response.sendRedirect(request.getContextPath() + "/member/");
                } else {
                    response.sendRedirect(request.getContextPath() + "/member/");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/member/");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
