package com.ju.wyhouse.utils;

import com.ju.wyhouse.R;

/**
 * Author:gaoju
 * Date:2020/2/6 17:35
 * Path:com.ju.wyhouse.utils
 * Desc:数据类工具
 */
public class DataUtil {


    //墙背景颜色
    private static int[] colorBgWallArray = {
            R.color.wall_color_one,
            R.color.wall_color_two,
            R.color.wall_color_three,
            R.color.wall_color_four,
            R.color.wall_color_five,
            R.color.wall_color_six,
            R.color.wall_color_seven,
            R.color.wall_color_eight,
            R.color.wall_color_nine,
            R.color.wall_color_ten,
            R.color.wall_color_eleven,
            R.color.wall_color_twelve,
            R.color.wall_color_thirteen,
            R.color.wall_color_fourteen
    };

    //墙头像颜色
    private static int[] colorCirWallArray = {
            R.drawable.wall_noname_cir_one,
            R.drawable.wall_noname_cir_two,
            R.drawable.wall_noname_cir_three,
            R.drawable.wall_noname_cir_four,
            R.drawable.wall_noname_cir_five,
            R.drawable.wall_noname_cir_six,
            R.drawable.wall_noname_cir_seven,
            R.drawable.wall_noname_cir_eight,
            R.drawable.wall_noname_cir_nine,
            R.drawable.wall_noname_cir_ten,
            R.drawable.wall_noname_cir_eleven,
            R.drawable.wall_noname_cir_twelve,
            R.drawable.wall_noname_cir_thirteen,
            R.drawable.wall_noname_cir_fourteen,
    };

    //课程表颜色
    private static int[] colorCourseArray = {R.color.course_one,
            R.color.course_two,
            R.color.course_three,
            R.color.course_four,
            R.color.course_five,
            R.color.course_six,
            R.color.course_seven
    };

    /**
     * 获取墙背景颜色
     *
     * @return
     */
    public static int[] getColorBgWallArray() {
        return colorBgWallArray;
    }


    /**
     * 获取墙头像颜色
     *
     * @return
     */
    public static int[] getColorCirWallArray() {
        return colorCirWallArray;
    }


    /**
     * 获取课程表颜色
     * @return
     */
    public static int[] getColorCourseArray() {
        return colorCourseArray;
    }
}
