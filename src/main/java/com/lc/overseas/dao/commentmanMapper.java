package com.lc.overseas.dao;

import com.lc.overseas.pojo.commentman;

import java.util.List;
import java.util.Map;

public interface commentmanMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table commentman
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table commentman
     *
     * @mbggenerated
     */
    int insert(commentman record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table commentman
     *
     * @mbggenerated
     */
    int insertSelective(commentman record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table commentman
     *
     * @mbggenerated
     */
    commentman selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table commentman
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(commentman record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table commentman
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(commentman record);

    List<commentman> getAllCommentMan();

    List<commentman> getCommentMan(Map<String,Object> map);

    int updateHmd(commentman record);
}