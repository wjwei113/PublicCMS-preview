package com.publiccms.entities.cms;
// Generated 2016-7-31 0:08:13 by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sanluan.common.generator.annotation.GeneratorColumn;

/**
 * CmsVoteItemAttribute generated by hbm2java
 */
@Entity
@Table(name = "cms_vote_item_attribute")
public class CmsVoteItemAttribute implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @GeneratorColumn(title = "选项ID")
    private long voteItemId;
    @GeneratorColumn(title = "扩展数据")
    private String data;

    public CmsVoteItemAttribute() {
    }

    public CmsVoteItemAttribute(long voteItemId) {
        this.voteItemId = voteItemId;
    }

    public CmsVoteItemAttribute(long voteItemId, String data) {
        this.voteItemId = voteItemId;
        this.data = data;
    }

    @Id

    @Column(name = "vote_item_id", unique = true, nullable = false)
    public long getVoteItemId() {
        return this.voteItemId;
    }

    public void setVoteItemId(long voteItemId) {
        this.voteItemId = voteItemId;
    }

    @Column(name = "data")
    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
