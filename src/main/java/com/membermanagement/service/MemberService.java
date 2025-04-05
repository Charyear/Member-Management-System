package com.membermanagement.service;

import com.membermanagement.model.Member;
import java.util.List;
import java.util.Map;

public interface MemberService {
    // 添加会员
    boolean addMember(Member member);
    
    // 删除会员
    boolean deleteMember(String memberId);
    
    // 更新会员信息
    boolean updateMember(Member member);
    
    // 获取单个会员
    Member getMember(String memberId);

    Member getMemberById(String memberId);
    
    // 获取所有会员
    List<Member> getAllMembers();
    
    // 分页获取会员
    List<Member> getMembersByPage(int pageNum, int pageSize);
    
    // 获取会员总数
    int getTotalMembers();
    
    // 根据类型获取会员
    List<Member> getMembersByType(String type);
    
    // 根据手机号查询会员
    Member getMemberByPhone(String phoneNumber);
    
    // 更新会员积分
    boolean updatePoints(String memberId, double spendAmount);
    
    // 生成6位会员ID
    String generateMemberId();
    
    // 根据关键词搜索会员
    List<Member> searchMembers(String keyword);
    
    // 获取会员类型分布
    Map<String, Integer> getMemberTypeDistribution();
    
    double getTotalConsumption();
    int getTotalPoints();

    /**
     * 获取按积分降序排列的会员列表
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 会员列表
     */
    List<Member> getMembersByPointsDesc(int pageNum, int pageSize);

    /**
     * 获取指定类型会员的消费排行
     * @param memberType 会员类型（金/银）
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 会员列表
     */
    List<Member> getMembersByTypeAndConsumption(String memberType, int pageNum, int pageSize);

    /**
     * 获取指定类型会员的总数
     * @param memberType 会员类型（金/银）
     * @return 会员总数
     */
    int getTotalMembersByType(String memberType);
}
