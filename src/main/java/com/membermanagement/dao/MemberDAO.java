package com.membermanagement.dao;

import com.membermanagement.model.Member;
import java.util.List;
import java.util.Map;

public interface MemberDAO {
    // 添加新会员
    boolean addMember(Member member);
    
    // 根据ID删除会员
    boolean deleteMember(String memberId);
    
    // 更新会员信息
    boolean updateMember(Member member);
    
    // 根据ID查询会员
    Member getMemberById(String memberId);
    
    // 获取所有会员列表
    List<Member> getAllMembers();
    
    // 根据会员类型查询会员
    List<Member> getMembersByType(String memberType);
    
    // 根据手机号查询会员
    Member getMemberByPhone(String phoneNumber);
    
    // 更新会员积分
    boolean updatePoints(String memberId, double spendAmount);
    
    // 根据姓名或会员ID搜索会员
    List<Member> searchMembers(String keyword);
    
    // 分页获取会员列表
    List<Member> getMembersByPage(int pageNum, int pageSize);
    
    // 获取会员总数
    int getTotalMembers();
    
    // 获取会员类型分布
    Map<String, Integer> getMemberTypeDistribution();
    
    /**
     * 获取所有会员的总消费金额
     * @return 总消费金额
     */
    double getTotalConsumption();
    
    /**
     * 获取所有会员的总积分
     * @return 总积分
     */
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
