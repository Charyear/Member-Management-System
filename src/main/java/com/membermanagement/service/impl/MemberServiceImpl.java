package com.membermanagement.service.impl;

import com.membermanagement.dao.MemberDAO;
import com.membermanagement.dao.impl.MemberDAOImpl;
import com.membermanagement.model.Member;
import com.membermanagement.service.MemberService;

import java.util.List;
import java.util.Map;

public class MemberServiceImpl implements MemberService {
    private MemberDAO memberDAO;

    public MemberServiceImpl() {
        this.memberDAO = new MemberDAOImpl();
    }

    @Override
    public boolean addMember(Member member) {
        return memberDAO.addMember(member);
    }

    @Override
    public boolean updateMember(Member member) {
        // 实现更新会员的方法
        // 1. 检查会员是否存在
        Member existingMember = memberDAO.getMemberById(member.getMemberId());
        if (existingMember == null) {
            return false;
        }
        
        // 2. 更新会员信息
        existingMember.setName(member.getName());
        existingMember.setPhone(member.getPhone());
        existingMember.setType(member.getType());
        existingMember.setConsumption(member.getConsumption());
        existingMember.setPoints(member.getPoints());
        
        // 3. 保存更新后的会员信息
        return memberDAO.updateMember(existingMember);
    }

    @Override
    public boolean deleteMember(String memberId) {
        return memberDAO.deleteMember(memberId);
    }

    @Override
    public Member getMember(String memberId) {
        return memberDAO.getMemberById(memberId);
    }

    @Override
    public List<Member> getAllMembers() {
        return memberDAO.getAllMembers();
    }

    @Override
    public List<Member> getMembersByType(String type) {
        return memberDAO.getMembersByType(type);
    }

    @Override
    public Member getMemberByPhone(String phone) {
        return memberDAO.getMemberByPhone(phone);
    }

    @Override
    public boolean updatePoints(String memberId, double spendAmount) {
        return memberDAO.updatePoints(memberId, spendAmount);
    }

    @Override
    public String generateMemberId() {
        // 生成6位随机会员ID
        String prefix = "M";
        String randomNumber = String.format("%05d", (int)(Math.random() * 100000));
        String memberId = prefix + randomNumber;

        // 检查生成的会员ID是否已存在
        while (getMember(memberId) != null) {
            randomNumber = String.format("%05d", (int)(Math.random() * 100000));
            memberId = prefix + randomNumber;
        }

        return memberId;
    }

    @Override
    public List<Member> searchMembers(String keyword) {
        return memberDAO.searchMembers(keyword);
    }

    @Override
    public Member getMemberById(String memberId) {
        return memberDAO.getMemberById(memberId);
    }

    @Override
    public List<Member> getMembersByPage(int pageNum, int pageSize) {
        return memberDAO.getMembersByPage(pageNum, pageSize);
    }
    
    @Override
    public int getTotalMembers() {
        return memberDAO.getTotalMembers();
    }

    @Override
    public Map<String, Integer> getMemberTypeDistribution() {
        return memberDAO.getMemberTypeDistribution();
    }

    @Override
    public double getTotalConsumption() {
        return memberDAO.getTotalConsumption();
    }

    @Override
    public int getTotalPoints() {
        return memberDAO.getTotalPoints();
    }

    @Override
    public List<Member> getMembersByPointsDesc(int pageNum, int pageSize) {
        return memberDAO.getMembersByPointsDesc(pageNum, pageSize);
    }

    @Override
    public List<Member> getMembersByTypeAndConsumption(String memberType, int pageNum, int pageSize) {
        return memberDAO.getMembersByTypeAndConsumption(memberType, pageNum, pageSize);
    }

    @Override
    public int getTotalMembersByType(String memberType) {
        return memberDAO.getTotalMembersByType(memberType);
    }
}
