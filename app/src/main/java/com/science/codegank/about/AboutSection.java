package com.science.codegank.about;

import com.science.baserecyclerviewadapter.entity.SectionEntity;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/30
 */

public class AboutSection extends SectionEntity<About> {
    public AboutSection(boolean isHeader, boolean isFooter, About data) {
        super(isHeader, isFooter, data);
    }

    public AboutSection(About data) {
        super(data);
    }
}
