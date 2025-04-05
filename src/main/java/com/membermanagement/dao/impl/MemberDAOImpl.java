package com.membermanagement.dao.impl;

import com.membermanagement.dao.MemberDAO;
import com.membermanagement.model.Member;
import com.membermanagement.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

public class MemberDAOImpl implements MemberDAO {
    
    private static final Logger LOGGER = Logger.getLogger(MemberDAOImpl.class.getName());

    @Override
    public boolean addMember(Member member) {
        String sql = "INSERT INTO members (member_id, name, phone, type, consumption, points) VALUES (?, ?, ?, ?, ?, ?)";
        return executeUpdate(sql, 
            member.getMemberId(),
            member.getName(),
            member.getPhone(),
            member.getType(),
            member.getConsumption(),
            member.getPoints()
        );
    }
    
    @Override
    public boolean deleteMember(String memberId) {
        String sql = "DELETE FROM members WHERE member_id = ?";
        return executeUpdate(sql, memberId);
    }
    
    @Override
    public boolean updateMember(Member member) {
        // 验证会员类型
        if (!"金".equals(member.getType()) && !"银".equals(member.getType())) {
            LOGGER.warning("无效的会员类型: " + member.getType() + ", 会员ID: " + member.getMemberId());
            return false;
        }

        // 首先检查会员是否存在
        Member existingMember = getMemberById(member.getMemberId());
        if (existingMember == null) {
            LOGGER.warning("未找到要更新的会员ID: " + member.getMemberId());
            return false;
        }

        String sql = "UPDATE members SET name = ?, phone = ?, type = ?, consumption = ?, points = ? WHERE member_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // 验证并设置参数
            if (member.getName() == null || member.getName().trim().isEmpty()) {
                LOGGER.warning("会员姓名不能为空: " + member.getMemberId());
                return false;
            }
            if (member.getPhone() == null || !member.getPhone().matches("\\d{11}")) {
                LOGGER.warning("无效的手机号码: " + member.getPhone() + ", 会员ID: " + member.getMemberId());
                return false;
            }
            
            pstmt.setString(1, member.getName().trim());
            pstmt.setString(2, member.getPhone().trim());
            pstmt.setString(3, member.getType());
            pstmt.setDouble(4, Math.max(0, member.getConsumption())); // 确保消费金额非负
            pstmt.setInt(5, Math.max(0, member.getPoints())); // 确保积分非负
            pstmt.setString(6, member.getMemberId());
            
            LOGGER.info("执行SQL: " + sql);
            LOGGER.info("SQL参数: [" + 
                       "name=" + member.getName() + ", " +
                       "phone=" + member.getPhone() + ", " +
                       "type=" + member.getType() + ", " +
                       "consumption=" + member.getConsumption() + ", " +
                       "points=" + member.getPoints() + ", " +
                       "member_id=" + member.getMemberId() + "]");
            
            int rowsAffected = pstmt.executeUpdate();
            boolean success = rowsAffected > 0;
            
            if (success) {
                LOGGER.info("成功更新会员: " + member.getMemberId());
            } else {
                LOGGER.warning("更新会员失败，影响行数为0: " + member.getMemberId());
            }
            
            return success;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "更新会员时发生SQL错误: " + e.getMessage(), e);
            throw new RuntimeException("更新会员信息时发生数据库错误: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Member getMemberById(String memberId) {
        String sql = "SELECT * FROM members WHERE member_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, memberId);
            LOGGER.info("执行SQL: " + sql + " [member_id=" + memberId + "]");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Member member = new Member();
                    member.setMemberId(rs.getString("member_id"));
                    member.setName(rs.getString("name"));
                    member.setPhone(rs.getString("phone"));
                    member.setType(rs.getString("type"));
                    member.setConsumption(rs.getDouble("consumption"));
                    member.setPoints(rs.getInt("points"));
                    LOGGER.info("找到会员: " + member.getMemberId());
                    return member;
                } else {
                    LOGGER.warning("未找到会员ID: " + memberId);
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "查询会员时发生SQL错误: " + e.getMessage(), e);
            throw new RuntimeException("查询会员信息时发生数据库错误: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Member> getAllMembers() {
        String sql = "SELECT * FROM members";
        return executeQuery(sql, rs -> {
            List<Member> members = new ArrayList<>();
            while (rs.next()) {
                members.add(extractMemberFromResultSet(rs));
            }
            return members;
        });
    }
    
    @Override
    public List<Member> getMembersByType(String memberType) {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE type = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, memberType);
            LOGGER.info("Executing query: " + sql);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                members.add(extractMemberFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving members by type", e);
        }
        return members;
    }
    
    @Override
    public Member getMemberByPhone(String phoneNumber) {
        String sql = "SELECT * FROM members WHERE phone = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, phoneNumber);
            LOGGER.info("Executing query: " + sql);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractMemberFromResultSet(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving member by phone", e);
        }
        return null;
    }
    
    @Override
    public boolean updatePoints(String memberId, double spendAmount) {
        String sql = "UPDATE members SET consumption = consumption + ?, points = points + ? WHERE member_id = ?";
        int newPoints = (int)(spendAmount * 10);
        return executeUpdate(sql, spendAmount, newPoints, memberId);
    }
    
    @Override
    public List<Member> searchMembers(String keyword) {
        String sql = "SELECT * FROM members WHERE name LIKE ? OR member_id LIKE ?";
        String searchParam = "%" + keyword + "%";
        return executeQuery(sql, rs -> {
            List<Member> members = new ArrayList<>();
            while (rs.next()) {
                members.add(extractMemberFromResultSet(rs));
            }
            return members;
        }, searchParam, searchParam);
    }
    
    @Override
    public List<Member> getMembersByPage(int pageNum, int pageSize) {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members LIMIT ? OFFSET ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            int offset = (pageNum - 1) * pageSize;
            pstmt.setInt(1, pageSize);
            pstmt.setInt(2, offset);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Member member = new Member();
                    member.setMemberId(rs.getString("member_id"));
                    member.setName(rs.getString("name"));
                    member.setPhone(rs.getString("phone"));
                    member.setType(rs.getString("type"));
                    member.setConsumption(rs.getDouble("consumption"));
                    member.setPoints(rs.getInt("points"));
                    members.add(member);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "分页获取会员列表失败", e);
        }
        
        return members;
    }
    
    @Override
    public List<Member> getMembersByPointsDesc(int pageNum, int pageSize) {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members ORDER BY points DESC LIMIT ? OFFSET ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            int offset = (pageNum - 1) * pageSize;
            pstmt.setInt(1, pageSize);
            pstmt.setInt(2, offset);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    members.add(extractMemberFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "获取会员列表（按积分排序）失败", e);
        }
        
        return members;
    }
    
    @Override
    public int getTotalMembers() {
        String sql = "SELECT COUNT(*) FROM members";
        return executeQuery(sql, rs -> rs.next() ? rs.getInt(1) : 0);
    }
    
    @Override
    public Map<String, Integer> getMemberTypeDistribution() {
        String sql = "SELECT type, COUNT(*) as count FROM members GROUP BY type";
        return executeQuery(sql, rs -> {
            Map<String, Integer> distribution = new HashMap<>();
            while (rs.next()) {
                distribution.put(rs.getString("type"), rs.getInt("count"));
            }
            return distribution;
        });
    }
    
    @Override
    public double getTotalConsumption() {
        String sql = "SELECT SUM(consumption) as total FROM members";
        return executeQuery(sql, rs -> rs.next() ? rs.getDouble("total") : 0.0);
    }

    @Override
    public int getTotalPoints() {
        String sql = "SELECT SUM(points) as total FROM members";
        return executeQuery(sql, rs -> rs.next() ? rs.getInt("total") : 0);
    }

    @Override
    public List<Member> getMembersByTypeAndConsumption(String memberType, int pageNum, int pageSize) {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE type = ? ORDER BY consumption DESC LIMIT ? OFFSET ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            int offset = (pageNum - 1) * pageSize;
            pstmt.setString(1, memberType);
            pstmt.setInt(2, pageSize);
            pstmt.setInt(3, offset);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    members.add(extractMemberFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "获取会员消费排行失败", e);
        }
        
        return members;
    }

    @Override
    public int getTotalMembersByType(String memberType) {
        String sql = "SELECT COUNT(*) FROM members WHERE type = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, memberType);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "获取会员类型总数失败", e);
        }
        return 0;
    }

    /**
     * 执行查询并返回单个结果
     */
    private <T> T executeQuery(String sql, ResultSetExtractor<T> extractor, Object... params) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // 设置参数
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return extractor.extract(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "执行查询失败: " + sql, e);
            return null;
        }
    }
    
    /**
     * 执行更新操作
     */
    private boolean executeUpdate(String sql, Object... params) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // 设置参数
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "执行更新失败: " + sql, e);
            return false;
        }
    }
    
    @FunctionalInterface
    private interface ResultSetExtractor<T> {
        T extract(ResultSet rs) throws SQLException;
    }
    
    private Member extractMemberFromResultSet(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setMemberId(rs.getString("member_id"));
        member.setName(rs.getString("name"));
        member.setPhone(rs.getString("phone"));
        member.setType(rs.getString("type"));
        member.setConsumption(rs.getDouble("consumption"));
        member.setPoints(rs.getInt("points"));
        return member;
    }
}
