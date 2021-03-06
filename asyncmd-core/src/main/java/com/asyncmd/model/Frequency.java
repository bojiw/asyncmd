
package com.asyncmd.model;

import com.asyncmd.exception.AsynExCode;
import com.asyncmd.exception.AsynException;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * 调度频率
 * @author wangwendi
 * @version $Id: Frequency.java, v 0.1 2019年07月15日 wangwendi Exp $
 */
public class Frequency {
    /**
     * 单位秒
     */
    public static final String second = "s";
    /**
     * 单位分
     */
    public static final String minute = "m";
    /**
     * 单位小时
     */
    public static final String hour = "h";

    /**
     * 调度频率
     */
    private int frequency;
    /**
     * 单位
     */
    private String unit;

    public Frequency(String frequency){
        if (StringUtils.isEmpty(frequency)){
            throw new AsynException(AsynExCode.EXECUTER_FREQUENCY_ILLEGAL);
        }
        if (setValue(frequency, second)){
            return;
        }
        if (setValue(frequency,minute)){
            return;
        }
        if (setValue(frequency,hour)){
            return;
        }
        throw new AsynException(AsynExCode.EXECUTER_FREQUENCY_ILLEGAL);


    }
    private boolean setValue(String frequency,String unit){
        String substring = frequency.substring(0, frequency.contains(unit) ? frequency.indexOf(unit) : 0);
        if (!StringUtils.isEmpty(substring)){
            try {
                this.frequency = Integer.valueOf(substring);
            }catch (Exception e){
                throw new AsynException(AsynExCode.EXECUTER_FREQUENCY_ILLEGAL);
            }
            this.unit = unit;
            return true;
        }
        return false;
    }
    public Date getNextTime(Date oldNextTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oldNextTime);
        add(calendar);
        return calendar.getTime();

    }

    private void add(Calendar calendar){
        if (second.equals(unit)){
            calendar.add(Calendar.SECOND,frequency);
            return;
        }
        if (minute.equals(unit)){
            calendar.add(Calendar.MINUTE,frequency);
            return;
        }
        if (hour.equals(unit)){
            calendar.add(Calendar.HOUR,frequency);
        }
        return;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}